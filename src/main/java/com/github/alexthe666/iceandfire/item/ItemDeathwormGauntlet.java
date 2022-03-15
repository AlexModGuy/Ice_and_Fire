package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
        super(IceAndFire.PROXY.setupISTER(new Item.Properties().maxDamage(500).group(IceAndFire.TAB_ITEMS)));
        this.setRegistryName(IceAndFire.MODID, "deathworm_gauntlet_" + color);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (stack.getTag() != null) {
            if (deathwormReceded || deathwormLaunched) {
                return;
            } else {
                if (player instanceof PlayerEntity) {
                    if (stack.getTag().getInt("HolderID") != player.getEntityId()) {
                        stack.getTag().putInt("HolderID", player.getEntityId());
                    }
                    if (((PlayerEntity) player).getCooldownTracker().getCooldown(this, 0.0F) == 0) {
                        ((PlayerEntity) player).getCooldownTracker().setCooldown(this, 10);
                        player.playSound(IafSoundRegistry.DEATHWORM_ATTACK, 1F, 1F);
                        deathwormReceded = false;
                        deathwormLaunched = true;
                    }
                }
            }
        }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity LivingEntity, int timeLeft) {
        if (specialDamage > 0) {
            stack.damageItem(specialDamage, LivingEntity, (player) -> {
                player.sendBreakAnimation(LivingEntity.getActiveHand());
            });
            specialDamage = 0;
        }
        if (stack.getTag().getInt("HolderID") != -1) {
            stack.getTag().putInt("HolderID", -1);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.isItemEqual(newStack);
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
                    Vector3d Vector3d = player.getLook(1.0F).normalize();
                    double range = 5;
                    for (MobEntity LivingEntity : world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(player.getPosX() - range, player.getPosY() - range, player.getPosZ() - range, player.getPosX() + range, player.getPosY() + range, player.getPosZ() + range))) {
                        Vector3d Vector3d1 = new Vector3d(LivingEntity.getPosX() - player.getPosX(), LivingEntity.getPosY() - player.getPosY(), LivingEntity.getPosZ() - player.getPosZ());
                        double d0 = Vector3d1.length();
                        Vector3d1 = Vector3d1.normalize();
                        double d1 = Vector3d.dotProduct(Vector3d1);
                        boolean canSee = d1 > 1.0D - 0.5D / d0 && player.canEntityBeSeen(LivingEntity);
                        if (canSee) {
                            specialDamage++;
                            LivingEntity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) entity), 3F);
                            LivingEntity.applyKnockback(0.5F, LivingEntity.getPosX() - player.getPosX(), LivingEntity.getPosZ() - player.getPosZ());
                        }
                    }
                }
            }
            MiscProperties.setLungeTicks((LivingEntity) entity, tempLungeTicks);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.deathworm_gauntlet.desc_0").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.deathworm_gauntlet.desc_1").mergeStyle(TextFormatting.GRAY));
    }
}
