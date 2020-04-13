package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityMyrmexSwarmer extends EntityMyrmexRoyal {

    private static final DataParameter<Optional<UUID>> SUMMONER_ID = EntityDataManager.createKey(EntityMyrmexSwarmer.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> TICKS_ALIVE = EntityDataManager.createKey(EntityMyrmexSwarmer.class, DataSerializers.VARINT);

    public EntityMyrmexSwarmer(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityMyrmexRoyal.FlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, world);
        switchNavigator(false);
        this.setSize(0.5F, 0.5F);
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 0;
    }

    protected void switchNavigator(boolean onLand) {
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.myrmexBaseAttackStrength - 1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
    }


    protected double attackDistance() {
        return 13;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new MyrmexAIFollowSummoner(this, 1.0D, 10.0F, 5.0F));
        this.tasks.addTask(2, new AIFlyAtTarget());
        this.tasks.addTask(3, new AIFlyRandom());
        this.tasks.addTask(4, new EntityAIAttackMeleeNoCooldown(this, 1.0D, true));
        this.tasks.addTask(5, new MyrmexAIWander(this, 1D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new MyrmexAISummonerHurtByTarget(this));
        this.targetTasks.addTask(3, new MyrmexAISummonerHurtTarget(this));
    }

    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof EntityMyrmexSwarmer) {
            super.collideWithEntity(entityIn);
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SUMMONER_ID, Optional.absent());
        this.dataManager.register(TICKS_ALIVE, 0);
    }

    @Nullable
    public EntityLivingBase getSummoner() {
        try {
            UUID uuid = this.getSummonerUUID();
            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if(entityIn == null){
            return false;
        }
        if (this.getSummonerUUID() == null || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() == null) {
            return false;
        }
        if(entityIn instanceof EntityTameable){
            UUID ownerID = ((EntityTameable) entityIn).getOwnerId();
            return ownerID != null && ownerID.equals(this.getSummonerUUID());
        }
        return entityIn.getUniqueID().equals(this.getSummonerUUID()) || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() != null && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID().equals(this.getSummonerUUID());
    }

    public void setSummonerID(@Nullable UUID uuid) {
        this.dataManager.set(SUMMONER_ID, Optional.fromNullable(uuid));
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getSummonerUUID() == null) {
            compound.setString("SummonerUUID", "");
        } else {
            compound.setString("SummonerUUID", this.getSummonerUUID().toString());
        }
        compound.setInteger("SummonTicks", this.getTicksAlive());

    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String s = "";
        if (compound.hasKey("SummonerUUID", 8)) {
            s = compound.getString("SummonerUUID");
        }
        if (!s.isEmpty()) {
            try {
                this.setSummonerID(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
        this.setTicksAlive(compound.getInteger("SummonTicks"));
    }

    public void setSummonedBy(EntityPlayer player) {
        this.setSummonerID(player.getUniqueID());
    }

    @Nullable
    public UUID getSummonerUUID() {
        return (UUID) ((Optional) this.dataManager.get(SUMMONER_ID)).orNull();
    }

    public int getTicksAlive() {
        return this.dataManager.get(TICKS_ALIVE).intValue();
    }

    public void setTicksAlive(int ticks) {
        this.dataManager.set(TICKS_ALIVE, ticks);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        setFlying(true);
        boolean flying = this.isFlying() && !this.onGround;
        setTicksAlive(getTicksAlive() + 1);
        if (flying) {
            this.motionY -= 0.08D;
            if (this.moveHelper.getY() > this.posY) {
                this.motionY += 0.08D;
            }
        }
        if (this.onGround) {
            this.onGround = false;
            this.motionY += 0.2F;
        }
        if (this.getAttackTarget() != null) {
            this.moveHelper.setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().getEntityBoundingBox().minY, this.getAttackTarget().posZ, 1.0F);
            if (this.getAttackBounds().intersects(this.getAttackTarget().getEntityBoundingBox())) {
                this.setAnimation(rand.nextBoolean() ? ANIMATION_BITE : ANIMATION_STING);
            }
            if (this.getAttackTarget().isDead) {
                this.moveHelper.action = EntityMoveHelper.Action.WAIT;
            }
        }
        if (this.getTicksAlive() > 1800) {
            this.onKillCommand();
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < attackDistance()) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < attackDistance()) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 70, 1));
            }
        }
    }

    public int getGrowthStage() {
        return 2;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public float getModelScale() {
        return 0.25F;
    }

    public boolean shouldHaveNormalAI() {
        return false;
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    public boolean isBreedingSeason() {
        return false;
    }

    public boolean shouldAttackEntity(EntityLivingBase attacker, EntityLivingBase entitylivingbase) {
        return !isOnSameTeam(attacker);
    }
}
