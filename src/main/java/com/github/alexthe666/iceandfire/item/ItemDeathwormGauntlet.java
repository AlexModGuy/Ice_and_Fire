package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDeathwormGauntlet extends Item implements ICustomRendered {

    private boolean deathwormReceded = true;
    private boolean deathwormLaunched = false;
    private int specialDamage = 0;

    public ItemDeathwormGauntlet(String color) {
        super(IceAndFire.PROXY.setupISTER(new Item.Properties().durability(500).tab(IceAndFire.TAB_ITEMS)));
        this.setRegistryName(IceAndFire.MODID, "deathworm_gauntlet_" + color);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        playerIn.startUsingItem(hand);
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (stack.getTag() != null) {
            if (deathwormReceded || deathwormLaunched) {
                return;
            } else {
                if (player instanceof PlayerEntity) {
                    if (stack.getTag().getInt("HolderID") != player.getId()) {
                        stack.getTag().putInt("HolderID", player.getId());
                    }
                    if (((PlayerEntity) player).getCooldowns().getCooldownPercent(this, 0.0F) == 0) {
                        ((PlayerEntity) player).getCooldowns().addCooldown(this, 10);
                        player.playSound(IafSoundRegistry.DEATHWORM_ATTACK, 1F, 1F);
                        deathwormReceded = false;
                        deathwormLaunched = true;
                    }
                }
            }
        }
    }

    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity LivingEntity, int timeLeft) {
        if (specialDamage > 0) {
            stack.hurtAndBreak(specialDamage, LivingEntity, (player) -> {
                player.broadcastBreakEvent(LivingEntity.getUsedItemHand());
            });
            specialDamage = 0;
        }
        if (stack.getTag().getInt("HolderID") != -1) {
            stack.getTag().putInt("HolderID", -1);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.sameItem(newStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        boolean hitMob = false;
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        } else {
            if (!(entity instanceof LivingEntity))
                return;
            int tempLungeTicks = MiscProperties.getLungeTicks((LivingEntity) entity);
            if (deathwormReceded) {
                if (tempLungeTicks > 0) {
                    tempLungeTicks = tempLungeTicks - 4;
                }
                if (tempLungeTicks <= 0) {
                    tempLungeTicks = 0;
                    deathwormReceded = false;
                    deathwormLaunched = false;
                }
            } else if (deathwormLaunched) {
                tempLungeTicks = 4 + tempLungeTicks;
                if (tempLungeTicks > 20 && !deathwormReceded) {
                    deathwormReceded = true;
                }
            }

            if (MiscProperties.getLungeTicks((LivingEntity) entity) == 20) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    Vector3d Vector3d = player.getViewVector(1.0F).normalize();
                    double range = 5;
                    for (LivingEntity livingEntity : world.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range))) {
                        //Let's not pull/hit ourselves
                        if (livingEntity == entity)
                            continue;
                        Vector3d Vector3d1 = new Vector3d(livingEntity.getX() - player.getX(), livingEntity.getY() - player.getY(), livingEntity.getZ() - player.getZ());
                        double d0 = Vector3d1.length();
                        Vector3d1 = Vector3d1.normalize();
                        double d1 = Vector3d.dot(Vector3d1);
                        boolean canSee = d1 > 1.0D - 0.5D / d0 && player.canSee(livingEntity);
                        if (canSee) {
                            specialDamage++;
                            livingEntity.hurt(DamageSource.playerAttack((PlayerEntity) entity), 3F);
                            livingEntity.knockback(0.5F, livingEntity.getX() - player.getX(), livingEntity.getZ() - player.getZ());
                        }
                    }
                }
            }
            MiscProperties.setLungeTicks((LivingEntity) entity, tempLungeTicks);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.deathworm_gauntlet.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.deathworm_gauntlet.desc_1").withStyle(TextFormatting.GRAY));
    }
}
