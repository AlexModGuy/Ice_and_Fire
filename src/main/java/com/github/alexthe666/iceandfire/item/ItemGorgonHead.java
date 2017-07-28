package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemGorgonHead extends Item {

    public ItemGorgonHead() {
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire.gorgon_head");
        this.maxStackSize = 1;
        this.setRegistryName(IceAndFire.MODID, "gorgon_head");
        GameRegistry.register(this);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if(stack.getMetadata() == 1){
            float dist = 64;
            Vec3d vec3 = entityLiving.getPositionVector();
            Vec3d vec3a = entityLiving.getLook(1.0F);
            Vec3d vec3b = vec3.addVector(vec3a.x * dist, vec3a.y * dist, vec3a.z * dist);
            RayTraceResult result = worldIn.rayTraceBlocks(vec3, vec3b);
            EntityLiving target = null;
            List<Entity> list = worldIn.getEntitiesInAABBexcluding(entityLiving, entityLiving.getEntityBoundingBox().expand(vec3b.x, vec3b.y, vec3b.z).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                public boolean apply(@Nullable Entity entity) {
                    return entity != null && entity.canBeCollidedWith();
                }
            }));

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
                if (entity1 instanceof EntityLiving) {
                    target = (EntityLiving)entity1;
                }
            }

            if(result != null && target != null && target instanceof EntityLiving && !(target instanceof EntityStoneStatue)){
                Random rand = new Random();
                    EntityStoneStatue stoneStatue = new EntityStoneStatue(worldIn, target);
                    for(int i = 0; i < 8; i++){
                        double d2 = rand.nextGaussian() * 0.02D;
                        double d0 = rand.nextGaussian() * 0.02D;
                        double d1 = rand.nextGaussian() * 0.02D;
                        if (worldIn.isRemote) {
                            worldIn.spawnParticle(EnumParticleTypes.BLOCK_DUST, target.posX + (double) (rand.nextFloat() * target.width * 2.0F) - (double) target.width, target.posY + (double) (rand.nextFloat() * target.height), target.posZ + (double) (rand.nextFloat() * target.width * 2.0F) - (double) target.width, d2, d0, d1, new int[]{Block.getIdFromBlock(Blocks.COBBLESTONE)});
                        }
                    }
                worldIn.playSound(target.posX, target.posY, target.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1F, 1F, false);
                stoneStatue.setPositionAndRotation(target.posX, target.posY, target.posZ, ((EntityLiving) target).rotationYaw, target.rotationPitch);
                if(worldIn.isRemote){
                    worldIn.spawnEntity(stoneStatue);
                }
                stoneStatue.setModel(target);
                target.setDead();
                if(!(entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).isCreative())){
                    stack.shrink(1);
                }
            }
        }
        stack.setItemDamage(0);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        int i = this.getMaxItemUseDuration(stack) - count;
        if(i > 20 && stack.getMetadata() == 0) {
            stack.setItemDamage(1);
        }
    }
}
