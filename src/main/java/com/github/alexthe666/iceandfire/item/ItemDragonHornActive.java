package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornActive extends Item {

    public ItemDragonHornActive(String name) {
        this.maxStackSize = 1;
        this.setTranslationKey("iceandfire." + name);
        this.setRegistryName(IceAndFire.MODID, name);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemDragonHornActive ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
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
            Vec3d vec3d1 = vec3d.add((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
            RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);
            if (raytraceresult == null) {
                return;
            }
            if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                return;
            } else {
                BlockPos pos = raytraceresult.getBlockPos();
                worldIn.playSound(entityplayer, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3, 0.75F);
                if (this == IafItemRegistry.dragon_horn_fire) {
                    EntityFireDragon dragon = new EntityFireDragon(worldIn);
                    if (stack.getTagCompound() != null) {
                        dragon.readFromNBT(stack.getTagCompound());
                    }
                    dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                    dragon.getNavigator().clearPath();
                    stack.getTagCompound().setBoolean("Released", true);
                    if (!worldIn.isRemote) {
                        worldIn.spawnEntity(dragon);
                    }
                    stack.shrink(1);
                    ItemStack hornItem = new ItemStack(IafItemRegistry.dragon_horn);
                    if (!entityplayer.inventory.addItemStackToInventory(hornItem)) {
                        entityplayer.dropItem(hornItem, false);
                    }
                }
                if (this == IafItemRegistry.dragon_horn_ice) {
                    EntityIceDragon dragon = new EntityIceDragon(worldIn);
                    dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    if (stack.getTagCompound() != null) {
                        dragon.readEntityFromNBT(stack.getTagCompound());
                    }
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                    dragon.getNavigator().clearPath();
                    stack.getTagCompound().setBoolean("Released", true);
                    if (!worldIn.isRemote) {
                        worldIn.spawnEntity(dragon);
                    }
                    stack.shrink(1);
                    ItemStack hornItem = new ItemStack(IafItemRegistry.dragon_horn);
                    if (!entityplayer.inventory.addItemStackToInventory(hornItem)) {
                        entityplayer.dropItem(hornItem, false);
                    }
                }
                entityplayer.addStat(StatList.getObjectUseStats(this));
            }
        }

    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer entityplayer, EnumHand hand) {
        ItemStack itemStackIn = entityplayer.getHeldItem(hand);
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
        Vec3d vec3d1 = vec3d.add((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
        RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            String fire = new TextComponentTranslation("entity.firedragon.name").getUnformattedText();
            String ice = new TextComponentTranslation("entity.icedragon.name").getUnformattedText();
            tooltip.add("" + (this == IafItemRegistry.dragon_horn_fire ? fire : ice));
            String name = stack.getTagCompound().getString("CustomName").isEmpty() ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + stack.getTagCompound().getString("CustomName");
            tooltip.add("" + name);
            String gender = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((stack.getTagCompound().getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female"));
            tooltip.add("" + gender);
            int stagenumber = stack.getTagCompound().getInteger("AgeTicks") / 24000;
            int stage1 = 0;
            {
                if (stagenumber >= 100) {
                    stage1 = 5;
                } else if (stagenumber >= 75) {
                    stage1 = 4;
                } else if (stagenumber >= 50) {
                    stage1 = 3;
                } else if (stagenumber >= 25) {
                    stage1 = 2;
                } else {
                    stage1 = 1;
                }
            }
            String stage = StatCollector.translateToLocal("dragon.stage") + stage1 + " " + StatCollector.translateToLocal("dragon.days.front") + stagenumber + " " + StatCollector.translateToLocal("dragon.days.back");
            tooltip.add("" + stage);
        }
    }
}
