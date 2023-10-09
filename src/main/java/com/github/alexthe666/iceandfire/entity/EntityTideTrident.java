package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityTideTrident extends ThrownTrident {

    private static final int ADDITIONALPIERCING = 2;
    private int entitiesHit = 0;

    public EntityTideTrident(EntityType<? extends ThrownTrident> type, Level worldIn) {
        super(type, worldIn);
        tridentItem = new ItemStack(IafItemRegistry.TIDE_TRIDENT.get());
    }

    public EntityTideTrident(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        this(IafEntityRegistry.TIDE_TRIDENT.get(), worldIn);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1F, thrower.getZ());
        this.setOwner(thrower);
        tridentItem = thrownStackIn;
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(thrownStackIn));
        this.entityData.set(ID_FOIL, thrownStackIn.hasFoil());
        int piercingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, thrownStackIn);
        this.setPierceLevel((byte) piercingLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity;
            f += EnchantmentHelper.getDamageBonus(this.tridentItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = level().damageSources().trident(this, entity1 == null ? this : entity1);
        entitiesHit++;
        if (entitiesHit >= getMaxPiercing())
            this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        float f1 = 1.0F;
        if (this.level()instanceof ServerLevel && this.level().isThundering() && EnchantmentHelper.hasChanneling(this.tridentItem)) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level());
                lightningboltentity.moveTo(Vec3.atCenterOf(blockpos));
                lightningboltentity.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                this.level().addFreshEntity(lightningboltentity);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    private int getMaxPiercing() {
        return ADDITIONALPIERCING + getPierceLevel();
    }

}