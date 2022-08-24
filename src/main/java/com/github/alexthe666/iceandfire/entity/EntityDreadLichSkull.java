package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityDreadLichSkull extends AbstractArrowEntity {


    public EntityDreadLichSkull(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setBaseDamage(6F);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrowEntity> type, World worldIn, double x, double y,
        double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(6F);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrowEntity> type, World worldIn, LivingEntity shooter,
        double x, double y, double z) {
        super(type, shooter, worldIn);
        this.setBaseDamage(6);
    }

    public EntityDreadLichSkull(EntityType<? extends AbstractArrowEntity> type, World worldIn, LivingEntity shooter,
        double dmg) {
        super(type, shooter, worldIn);
        this.setBaseDamage(dmg);
    }

    public EntityDreadLichSkull(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.DREAD_LICH_SKULL.get(), worldIn);
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        float sqrt = MathHelper.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        boolean flag = true;
        Entity shootingEntity = this.getOwner();
        if (shootingEntity != null && shootingEntity instanceof MobEntity && ((MobEntity) shootingEntity).getTarget() != null) {
            LivingEntity target = ((MobEntity) shootingEntity).getTarget();
            double minusX = target.getX() - this.getX();
            double minusY = target.getY() - this.getY();
            double minusZ = target.getZ() - this.getZ();
            double speed = 0.15D;
            this.setDeltaMovement(this.getDeltaMovement().add(minusX * speed * 0.1D, minusY * speed * 0.1D, minusZ * speed * 0.1D));
        }
        if (shootingEntity instanceof PlayerEntity) {
            LivingEntity target = ((PlayerEntity) shootingEntity).getKillCredit();
            if (target == null || !target.isAlive()) {
                double d0 = 10;
                List<Entity> list = level.getEntities(shootingEntity, (new AxisAlignedBB(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0D, this.getY() + 1.0D, this.getZ() + 1.0D)).inflate(d0, 10.0D, d0), EntityPredicates.ENTITY_STILL_ALIVE);
                LivingEntity closest = null;
                if (!list.isEmpty()) {
                    for(Entity e : list) {
                        if (e instanceof LivingEntity && !e.getUUID().equals(shootingEntity.getUUID()) && e instanceof IMob) {
                            if (closest == null || closest.distanceTo(shootingEntity) > e.distanceTo(shootingEntity)) {
                                closest = (LivingEntity) e;
                            }
                        }
                    }
                }
                target = closest;
            }
            if (target != null && target.isAlive()) {
                double minusX = target.getX() - this.getX();
                double minusY = target.getY() + target.getEyeHeight() - this.getY();
                double minusZ = target.getZ() - this.getZ();
                double speed = 0.25D * Math.min(this.distanceTo(target), 10D) / 10D;
                this.setDeltaMovement(this.getDeltaMovement().add((Math.signum(minusX) * 0.5D - this.getDeltaMovement().x) * 0.10000000149011612D, (Math.signum(minusY) * 0.5D - this.getDeltaMovement().y) * 0.10000000149011612D, (Math.signum(minusZ) * 0.5D - this.getDeltaMovement().z) * 0.10000000149011612D));
                this.yRot = (float) (MathHelper.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * (180D / Math.PI));
                this.xRot = (float) (MathHelper.atan2(this.getDeltaMovement().y, sqrt) * (180D / Math.PI));
                flag = false;
            }
        }
        if ((sqrt < 0.1F || this.horizontalCollision || this.verticalCollision || this.inGround) && this.tickCount > 5 && flag) {
            this.remove();
        }
        double d0 = 0;
        double d1 = 0.01D;
        double d2 = 0D;
        double x = this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
        double y = this.getY() + this.random.nextFloat() * this.getBbHeight() - this.getBbHeight();
        double z = this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
        float f = (this.getBbWidth() + this.getBbHeight() + this.getBbWidth()) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, x, y + 0.5D, z, d0, d1, d2);
        }
        super.tick();
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = getX() - toX;
        double d1 = getY() - toY;
        double d2 = getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ARROW_HIT && soundIn != SoundEvents.ARROW_HIT_PLAYER) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), soundIn, this.getSoundSource(), volume, pitch);
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult raytraceResultIn) {
        if (raytraceResultIn.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = raytraceResultIn.getEntity();
            Entity shootingEntity = this.getOwner();
            if (entity != null) {
                if (shootingEntity != null && entity.isAlliedTo(shootingEntity)) {
                    return;
                }
            }
        }
        super.onHitEntity(raytraceResultIn);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        Entity shootingEntity = this.getOwner();
        if (living != null && (shootingEntity == null || !living.is(shootingEntity))) {
            if (living instanceof PlayerEntity) {
                this.damageShield((PlayerEntity) living, (float) this.getBaseDamage());
            }
        }
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getUseItem().getItem().isShield(player.getUseItem(), player)) {
            ItemStack copyBeforeUse = player.getUseItem().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getUseItem().hurtAndBreak(i, player, (playerSheild) -> {
                playerSheild.broadcastBreakEvent(playerSheild.getUsedItemHand());
            });

            if (player.getUseItem().isEmpty()) {
                Hand Hand = player.getUsedItemHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == net.minecraft.util.Hand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                player.stopUsingItem();
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            }
        }
    }

    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
