package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornActive extends Item {

    public ItemDragonHornActive(String name) {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        this.setRegistryName(IceAndFire.MODID, name);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, World worldIn, LivingEntity entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemDragonHornActive ? (stack.getUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, World worldIn, LivingEntity entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity LivingEntity, int timeLeft) {
        if (LivingEntity instanceof PlayerEntity) {
            PlayerEntity PlayerEntity = (PlayerEntity) LivingEntity;
            boolean flag = PlayerEntity.isCreative();
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i < 20) {
                return;
            }
            double d0 = PlayerEntity.prevPosX + (PlayerEntity.getPosX() - PlayerEntity.prevPosX) * 1.0D;
            double d1 = PlayerEntity.prevPosY + (PlayerEntity.getPosY() -  PlayerEntity.prevPosY) * 1.0D + (double) PlayerEntity.getEyeHeight();
            double d2 = PlayerEntity.prevPosZ + (PlayerEntity.getPosZ() - PlayerEntity.prevPosZ) * 1.0D;
            float f1 = PlayerEntity.prevRotationPitch + (PlayerEntity.rotationPitch - PlayerEntity.prevRotationPitch) * 1.0F;
            float f2 = PlayerEntity.prevRotationYaw + (PlayerEntity.rotationYaw - PlayerEntity.prevRotationYaw) * 1.0F;
            Vec3d vec3d = new Vec3d(d0, d1, d2);
            float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            Vec3d vec3d1 = vec3d.add((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
            RayTraceResult raytraceresult = worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, LivingEntity));
            if (raytraceresult == null) {
                return;
            }
            if (raytraceresult.hitInfo != RayTraceResult.Type.BLOCK) {
                return;
            } else {
                BlockPos pos = new BlockPos(raytraceresult.getHitVec());
                worldIn.playSound(PlayerEntity, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3, 0.75F);
                if (this == IafItemRegistry.DRAGON_HORN_FIRE) {
                    EntityFireDragon dragon = new EntityFireDragon(worldIn);
                    if (stack.getTag() != null) {
                        dragon.read(stack.getTag());
                    }
                    dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                    dragon.getNavigator().clearPath();
                    stack.getTag().putBoolean("Released", true);
                    if (!worldIn.isRemote) {
                        worldIn.addEntity(dragon);
                    }
                    stack.shrink(1);
                    ItemStack hornItem = new ItemStack(IafItemRegistry.DRAGON_HORN);
                    if (!PlayerEntity.inventory.addItemStackToInventory(hornItem)) {
                        PlayerEntity.dropItem(hornItem, false);
                    }
                }
                if (this == IafItemRegistry.DRAGON_HORN_ICE) {
                    EntityIceDragon dragon = new EntityIceDragon(worldIn);
                    dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    if (stack.getTag() != null) {
                        dragon.readEntityFromNBT(stack.getTag());
                    }
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                    dragon.getNavigator().clearPath();
                    stack.getTag().putBoolean("Released", true);
                    if (!worldIn.isRemote) {
                        worldIn.addEntity(dragon);
                    }
                    stack.shrink(1);
                    ItemStack hornItem = new ItemStack(IafItemRegistry.DRAGON_HORN);
                    if (!PlayerEntity.inventory.addItemStackToInventory(hornItem)) {
                        PlayerEntity.dropItem(hornItem, false);
                    }
                }
            }
        }

    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity PlayerEntity, Hand hand) {
        ItemStack itemStackIn = PlayerEntity.getHeldItem(hand);
        double d0 = PlayerEntity.prevPosX + (PlayerEntity.getPosX() - PlayerEntity.prevPosX) * 1.0D;
        double d1 = PlayerEntity.prevPosY + (PlayerEntity.getPosY() - PlayerEntity.prevPosY) * 1.0D + (double) PlayerEntity.getEyeHeight();
        double d2 = PlayerEntity.prevPosZ + (PlayerEntity.getPosZ() - PlayerEntity.prevPosZ) * 1.0D;
        float f = 1.0F;
        float f1 = PlayerEntity.prevRotationPitch + (PlayerEntity.rotationPitch - PlayerEntity.prevRotationPitch) * 1.0F;
        float f2 = PlayerEntity.prevRotationYaw + (PlayerEntity.rotationYaw - PlayerEntity.prevRotationYaw) * 1.0F;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        PlayerEntity.setActiveHand(hand);
        Vec3d vec3d1 = vec3d.add((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            ITextComponent fire = new TranslationTextComponent("entity.firedragon.name").applyTextStyle(TextFormatting.GRAY);
            ITextComponent ice = new TranslationTextComponent("entity.icedragon.name").applyTextStyle(TextFormatting.GRAY);
            tooltip.add((this == IafItemRegistry.DRAGON_HORN_FIRE ? fire : ice));
            String name = stack.getTag().getString("CustomName").isEmpty() ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + stack.getTag().getString("CustomName");
            tooltip.add(new StringTextComponent(name));
            String gender = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((stack.getTag().getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female"));
            tooltip.add(new StringTextComponent(gender));
            int stagenumber = stack.getTag().getInt("AgeTicks") / 24000;
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
            tooltip.add(new StringTextComponent(stage));
        }
    }
}
