package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.BlockWall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChainTie extends EntityHanging {

    public EntityChainTie(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 0.9F);
        this.facingDirection = EnumFacing.NORTH;
    }

    public EntityChainTie(World worldIn, BlockPos hangingPositionIn) {
        super(worldIn, hangingPositionIn);
        this.setPosition((double) hangingPositionIn.getX() + 0.5D, (double) hangingPositionIn.getY(), (double) hangingPositionIn.getZ() + 0.5D);
        this.setSize(0.8F, 0.9F);
        this.forceSpawn = true;
    }

    public static EntityChainTie createKnot(World worldIn, BlockPos fence) {
        EntityChainTie entityleashknot = new EntityChainTie(worldIn, fence);
        worldIn.spawnEntity(entityleashknot);
        entityleashknot.playPlaceSound();
        return entityleashknot;
    }

    @Nullable
    public static EntityChainTie getKnotForPosition(World worldIn, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (EntityChainTie entityleashknot : worldIn.getEntitiesWithinAABB(EntityChainTie.class, new AxisAlignedBB((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D))) {
            if (entityleashknot != null && entityleashknot.getHangingPosition() != null && entityleashknot.getHangingPosition().equals(pos)) {
                return entityleashknot;
            }
        }

        return null;
    }

    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        if (this.isAddedToWorld() && !this.world.isRemote)
            this.world.updateEntityWithOptionalForce(this, false); // Forge - Process chunk registration after moving.
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - (double) f, y, z - (double) f, x + (double) f, y + (double) f1, z + (double) f));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer) {
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    public int getWidthPixels() {
        return 0;
    }

    public int getHeightPixels() {
        return 0;
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        BlockPos blockpos = this.getHangingPosition();
        if(blockpos != null){
            compound.setInteger("TileX", blockpos.getX());
            compound.setInteger("TileY", blockpos.getY());
            compound.setInteger("TileZ", blockpos.getZ());
        }

    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        this.hangingPosition = new BlockPos(compound.getInteger("TileX"), compound.getInteger("TileY"), compound.getInteger("TileZ"));
    }

    public float getEyeHeight() {
        return 0F;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024.0D;
    }

    public void onBroken(@Nullable Entity brokenEntity) {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    public void setDead() {
        this.isDead = true;
        double d0 = 30D;
        List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
        for (EntityLiving entityliving : list) {
            ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entityliving, ChainEntityProperties.class);
            if (chainProperties != null && chainProperties.isChained() && chainProperties.isConnectedToEntity(entityliving, this)) {
                chainProperties.removeChain(entityliving, this);
                EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double) 1, this.posZ, new ItemStack(IafItemRegistry.chain));
                entityitem.setDefaultPickupDelay();
                this.world.spawnEntity(entityitem);
            }
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.world.isRemote) {
            return true;
        } else {
            boolean flag = false;
            double d0 = 30D;
            List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));

            for (EntityLiving entityliving : list) {
                ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entityliving, ChainEntityProperties.class);
                if (chainProperties != null && chainProperties.isChained() && chainProperties.isConnectedToEntity(entityliving, player)) {
                    chainProperties.addChain(entityliving, this);
                    chainProperties.removeChain(entityliving, player);
                    flag = true;
                }
            }

            if (!flag) {
                this.setDead();

                if (player.capabilities.isCreativeMode) {
                    for (EntityLiving entityliving1 : list) {
                        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entityliving1, ChainEntityProperties.class);
                        if (chainProperties.isChained() && chainProperties.isConnectedToEntity(entityliving1, this)) {
                            chainProperties.removeChain(entityliving1, this);
                            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double) 1, this.posZ, new ItemStack(IafItemRegistry.chain));
                            entityitem.setDefaultPickupDelay();
                            this.world.spawnEntity(entityitem);
                        }
                    }
                }
            }

            return true;
        }
    }

    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockWall;
    }

    public void playPlaceSound() {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}
