package com.github.alexthe666.iceandfire.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityGhostSword extends AbstractArrowEntity {

    public EntityGhostSword(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setBaseDamage(9F);
    }

    public EntityGhostSword(EntityType<? extends AbstractArrowEntity> type, World worldIn, double x, double y, double z,
                            float r, float g, float b) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(9F);
    }

    public EntityGhostSword(EntityType<? extends AbstractArrowEntity> type, World worldIn, LivingEntity shooter,
        double dmg) {
        super(type, shooter, worldIn);
        this.setBaseDamage(dmg);
    }

    public EntityGhostSword(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.GHOST_SWORD.get(), worldIn);
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
        super.tick();
        noPhysics = true;
        float sqrt = MathHelper.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        if ((sqrt < 0.1F) && this.tickCount > 200) {
            this.remove();
        }
        double d0 = 0;
        double d1 = 0.0D;
        double d2 = 0.01D;
        double x = this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
        double y = this.getY() + this.random.nextFloat() * this.getBbHeight() - this.getBbHeight();
        double z = this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
        float f = (this.getBbWidth() + this.getBbHeight() + this.getBbWidth()) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            this.level.addParticle(ParticleTypes.SNEEZE, x, y + 0.5D, z, d0, d1, d2);
        }
        Vector3d vector3d = this.getDeltaMovement();
        float f3 = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (180F / (float) Math.PI));
        this.xRot = (float) (MathHelper.atan2(vector3d.y, f3) * (180F / (float) Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
        Vector3d vector3d2 = this.position();
        Vector3d vector3d3 = vector3d2.add(vector3d);
        RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            vector3d3 = raytraceresult.getLocation();
        }
        while (!this.removed) {
            EntityRayTraceResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canHarmPlayer((PlayerEntity) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                if(raytraceresult.getType() != RayTraceResult.Type.BLOCK){
                    this.onHit(raytraceresult);

                }
                this.hasImpulse = true;
            }

            if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                break;
            }

            raytraceresult = null;
        }
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

    private IntOpenHashSet piercedEntities;
    private List<Entity> hitEntities;
    private int knockbackStrength;

    @Override
    public void setKnockback(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        float f = (float) this.getDeltaMovement().length();
        int i = MathHelper.ceil(Math.max(f * this.getBaseDamage(), 0.0D));
        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercedEntities.add(entity.getId());
        }

        if (this.isCritArrow()) {
            i += this.random.nextInt(i / 2 + 2);
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.MAGIC;

        if (entity1 != null) {
            if (entity1 instanceof LivingEntity) {
                damagesource = DamageSource.arrow(this, entity1);
                damagesource.setMagic();
                ((LivingEntity) entity1).setLastHurtMob(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        if (entity.hurt(damagesource, i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;

                if (this.knockbackStrength > 0) {
                    Vector3d vec3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize()
                        .scale(this.knockbackStrength * 0.6D);
                    if (vec3d.lengthSqr() > 0.0D) {
                        livingentity.push(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) entity1).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingentity);
                }

            }

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            //this.ticksInAir = 0;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.remove();
            }
        }

    }
}
