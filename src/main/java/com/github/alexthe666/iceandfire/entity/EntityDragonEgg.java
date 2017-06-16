package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.BlockEggInIce;
import com.github.alexthe666.iceandfire.core.ModAchievements;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDragonEgg extends EntityLiving {

    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.<Integer>createKey(EntityDragonEgg.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.<Integer>createKey(EntityDragonEgg.class, DataSerializers.VARINT);

    public EntityDragonEgg(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.setSize(0.45F, 0.55F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("Color", (byte) this.getType().ordinal());
        tag.setByte("DragonAge", (byte) this.getDragonAge());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setType(EnumDragonEgg.values()[tag.getInteger("Color")]);
        this.setDragonAge(tag.getByte("DragonAge"));

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(DRAGON_TYPE, 0);
        this.getDataManager().register(DRAGON_AGE, 0);
    }

    public EnumDragonEgg getType() {
        return EnumDragonEgg.values()[this.getDataManager().get(DRAGON_TYPE)];
    }

    public void setType(EnumDragonEgg newtype) {
        this.getDataManager().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource i) {
        return i.getTrueSource() != null;
    }

    public int getDragonAge() {
        return this.getDataManager().get(DRAGON_AGE);
    }

    public void setDragonAge(int i) {
        this.getDataManager().set(DRAGON_AGE, i);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        BlockPos pos = new BlockPos(this);
        if (world.getBlockState(pos).getMaterial() == Material.FIRE && getType().isFire) {
            this.setDragonAge(this.getDragonAge() + 1);
        }
        if (world.getBlockState(pos).getMaterial() == Material.WATER && !getType().isFire && this.getRNG().nextInt(500) == 0) {
            world.setBlockState(pos, ModBlocks.eggInIce.getDefaultState());
            this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvents.BLOCK_GLASS_BREAK, this.getSoundCategory(), 2.5F, 1.0F, false);
            if (world.getBlockState(pos).getBlock() instanceof BlockEggInIce) {
                ((TileEntityEggInIce) world.getTileEntity(pos)).type = this.getType();
                this.setDead();
            }
        }
        if (this.getDragonAge() > 20 * 60) {
            if (world.getBlockState(pos).getMaterial() == Material.FIRE && getType().isFire && world.getClosestPlayerToEntity(this, 5) != null) {
                world.setBlockToAir(pos);
                EntityFireDragon dragon = new EntityFireDragon(world);
                dragon.setVariant(getType().ordinal());
                dragon.setGender(rand.nextBoolean());
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                if (!world.isRemote) {
                    world.spawnEntity(dragon);
                }
                dragon.setTamed(true);
                dragon.setOwnerId(world.getClosestPlayerToEntity(this, 5).getUniqueID());
                world.getClosestPlayerToEntity(this, 5).addStat(ModAchievements.dragonHatch, 1);
            }
            this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, this.getSoundCategory(), 2.5F, 1.0F, false);
            this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, ModSounds.dragon_hatch, this.getSoundCategory(), 2.5F, 1.0F, false);
            this.setDead();
        }
    }

    public String getTexture() {
        String i = getType().isFire ? "firedragon/" : "icedragon/";
        return "iceandfire:textures/models/" + i + "egg_" + getType().name().toLowerCase() + ".png";

    }

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public SoundEvent getHurtSound() {
        return null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2) {
        if (!world.isRemote && !var1.canHarmInCreative()) {
            this.dropItem(this.getItem().getItem(), 1);
        }
        this.setDead();
        return super.attackEntityFrom(var1, var2);
    }

    private ItemStack getItem() {
        switch (getType().ordinal()) {
            default:
                return new ItemStack(ModItems.dragonegg_red);
            case 1:
                return new ItemStack(ModItems.dragonegg_green);
            case 2:
                return new ItemStack(ModItems.dragonegg_bronze);
            case 3:
                return new ItemStack(ModItems.dragonegg_gray);
            case 4:
                return new ItemStack(ModItems.dragonegg_blue);
            case 5:
                return new ItemStack(ModItems.dragonegg_white);
            case 6:
                return new ItemStack(ModItems.dragonegg_sapphire);
            case 7:
                return new ItemStack(ModItems.dragonegg_silver);

        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }
}
