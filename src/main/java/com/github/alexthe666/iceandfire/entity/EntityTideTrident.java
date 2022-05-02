package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityTideTrident extends TridentEntity {

    private static final int ADDITIONALPIERCING = 2;
    private int entitiesHit = 0;

    public EntityTideTrident(EntityType<? extends TridentEntity> type, World worldIn) {
        super(type, worldIn);
        thrownStack = new ItemStack(IafItemRegistry.TIDE_TRIDENT);
    }

    public EntityTideTrident(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        this(IafEntityRegistry.TIDE_TRIDENT.get(), worldIn);
        this.setPosition(thrower.getPosX(), thrower.getPosYEye() - 0.1F, thrower.getPosZ());
        this.setShooter(thrower);
        thrownStack = thrownStackIn;
        this.dataManager.set(LOYALTY_LEVEL, (byte) EnchantmentHelper.getLoyaltyModifier(thrownStackIn));
        this.dataManager.set(field_226571_aq_, thrownStackIn.hasEffect());
        int piercingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, thrownStackIn);
        this.setPierceLevel((byte) piercingLevel);
    }

    public EntityTideTrident(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.TIDE_TRIDENT.get(), worldIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingentity.getCreatureAttribute());
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource = DamageSource.causeTridentDamage(this, entity1 == null ? this : entity1);
        entitiesHit++;
        if (entitiesHit >= getMaxPiercing())
            this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity1, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity) entity1, livingentity1);
                }

                this.arrowHit(livingentity1);
            }
        }

        float f1 = 1.0F;
        if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.getPosition();
            if (this.world.canSeeSky(blockpos)) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.world);
                lightningboltentity.moveForced(Vector3d.copyCentered(blockpos));
                lightningboltentity.setCaster(entity1 instanceof ServerPlayerEntity ? (ServerPlayerEntity) entity1 : null);
                this.world.addEntity(lightningboltentity);
                soundevent = SoundEvents.ITEM_TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    private int getMaxPiercing() {
        return ADDITIONALPIERCING + getPierceLevel();
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.getItem() == Items.TRIDENT ? new ItemStack(IafItemRegistry.TIDE_TRIDENT) : this.thrownStack.copy();
    }

}