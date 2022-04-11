package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
import java.util.*;
import java.util.function.Predicate;

public class ItemCockatriceScepter extends Item {

    private final Random rand = new Random();
    private int specialWeaponDmg;

    public ItemCockatriceScepter() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxDamage(700));
        this.setRegistryName(IceAndFire.MODID, "cockatrice_scepter");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.cockatrice_scepter.desc_0").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.cockatrice_scepter.desc_1").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity livingEntity, int timeLeft) {
        if (specialWeaponDmg > 0) {
            stack.damageItem(specialWeaponDmg, livingEntity, (player) -> {
                player.sendBreakAnimation(livingEntity.getActiveHand());
            });
            specialWeaponDmg = 0;
        }
        MiscProperties.getTargeting(livingEntity).forEach(target -> {
            MiscProperties.removeTargetedBy(livingEntity, target);
        });
        MiscProperties.removeTargets(livingEntity);
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
        if (player instanceof PlayerEntity) {
            double dist = 32;
            Vector3d playerEyePosition = player.getEyePosition(1.0F);
            Vector3d playerLook = player.getLook(1.0F);
            Vector3d Vector3d2 = playerEyePosition.add(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist);
            Entity pointedEntity = null;
            List<Entity> nearbyEntities = player.world.getEntitiesInAABBexcluding(player, player.getBoundingBox().expand(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist).grow(1.0D, 1.0D, 1.0D), new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) {
                    boolean blindness = entity instanceof LivingEntity && ((LivingEntity) entity).isPotionActive(Effects.BLINDNESS) || (entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) entity).canBeTurnedToStone());
                    return entity != null && entity.canBeCollidedWith() && !blindness && (entity instanceof PlayerEntity || (entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity)));
                }
            });
            double d2 = dist;
            for (Entity nearbyEntity : nearbyEntities) {
                AxisAlignedBB axisalignedbb = nearbyEntity.getBoundingBox().grow(nearbyEntity.getCollisionBorderSize());
                Optional<Vector3d> optional = axisalignedbb.rayTrace(playerEyePosition, Vector3d2);

                if (axisalignedbb.contains(playerEyePosition)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = nearbyEntity;
                        d2 = 0.0D;
                    }
                } else if (optional.isPresent()) {
                    double d3 = playerEyePosition.distanceTo(optional.get());

                    if (d3 < d2 || d2 == 0.0D) {
                        if (nearbyEntity.getLowestRidingEntity() == player.getLowestRidingEntity() && !player.canRiderInteract()) {
                            if (d2 == 0.0D) {
                                pointedEntity = nearbyEntity;
                            }
                        } else {
                            pointedEntity = nearbyEntity;
                            d2 = d3;
                        }
                    }
                }

            }
            if (pointedEntity instanceof LivingEntity) {
                if (!pointedEntity.isAlive())
                    return;
                MiscProperties.addScepterTargetData(player, (LivingEntity) pointedEntity);
            }
            attackTargets(player);
        }
    }

    private void attackTargets(LivingEntity caster) {
        List<LivingEntity> targets = MiscProperties.getTargeting(caster);
        for (LivingEntity target : targets) {
            if (!EntityGorgon.isEntityLookingAt(caster, target, 0.2F) || !caster.isAlive()) {
                MiscProperties.removeTargetedBy(caster, target);
                MiscProperties.removeTarget(caster, target);
            }
            target.addPotionEffect(new EffectInstance(Effects.WITHER, 40, 2));
            if (caster.ticksExisted % 20 == 0) {
                specialWeaponDmg++;
                target.attackEntityFrom(DamageSource.WITHER, 2);
            }
            drawParticleBeam(caster, target);
            if (!target.isAlive()) {
                MiscProperties.removeTarget(caster, target);
            }

        }
    }

    private void drawParticleBeam(LivingEntity origin, LivingEntity target) {
        double d5 = 80F;
        double d0 = target.getPosX() - origin.getPosX();
        double d1 = target.getPosY() + (double) (target.getHeight() * 0.5F)
                - (origin.getPosY() + (double) origin.getEyeHeight() * 0.5D);
        double d2 = target.getPosZ() - origin.getPosZ();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 = d0 / d3;
        d1 = d1 / d3;
        d2 = d2 / d3;
        double d4 = this.rand.nextDouble();
        while (d4 < d3) {
            d4 += 1.0D;
            origin.world.addParticle(ParticleTypes.ENTITY_EFFECT, origin.getPosX() + d0 * d4, origin.getPosY() + d1 * d4 + (double) origin.getEyeHeight() * 0.5D, origin.getPosZ() + d2 * d4, 0.0D, 0.0D, 0.0D);
        }
    }

}
