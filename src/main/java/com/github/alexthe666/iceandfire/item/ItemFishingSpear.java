package com.github.alexthe666.iceandfire.item;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFishingSpear extends Item {

    public ItemFishingSpear() {
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire.fishing_spear");
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setRegistryName(IceAndFire.MODID, "fishing_spear");
        GameRegistry.register(this);
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode;
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i < 20) {
                return;
            }
            double d0 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * 1.0D;
            double d1 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * 1.0D + (double) entityplayer.getEyeHeight();
            double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * 1.0D;
            float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * 1.0F;
            float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * 1.0F;
            Vec3d vec3d = new Vec3d(d0, d1, d2);
            float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            Vec3d vec3d1 = vec3d.addVector((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
            RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);
            if (raytraceresult == null) {
                return;
            }
            if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                return;
            } else {
                Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
                boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
                if (!flag1) {
                    return;
                }
                if (!entityplayer.capabilities.isCreativeMode) {
                    stack.damageItem(1, entityplayer);
                }
                entityplayer.addStat(StatList.getObjectUseStats(this));
                if (!worldIn.isRemote) {
                    EntityItem item = new EntityItem(worldIn, raytraceresult.getBlockPos().getX() + 0.5D, raytraceresult.getBlockPos().getY() + 1.5D, raytraceresult.getBlockPos().getZ() + 0.5D, new ItemStack(Items.FISH, 1, itemRand.nextInt(4)));
                    worldIn.spawnEntity(item);
                }
                worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.1F);
            }

            entityplayer.addStat(StatList.getObjectUseStats(this));
        }

    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer entityplayer, EnumHand hand) {
        double d0 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * 1.0D;
        double d1 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * 1.0D + (double) entityplayer.getEyeHeight();
        double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * 1.0D;
        float f = 1.0F;
        float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * 1.0F;
        float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * 1.0F;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        entityplayer.setActiveHand(hand);
        Vec3d vec3d1 = vec3d.addVector((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
        RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);
        if (raytraceresult == null) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else {
            Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
            boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
            if (!flag1) {
                return new ActionResult(EnumActionResult.PASS, itemStackIn);
            }
            entityplayer.addStat(StatList.getObjectUseStats(this));
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
    }
}
