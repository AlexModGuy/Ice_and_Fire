package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetOutOfWater;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIWander;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.message.MessageSirenSong;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateAmphibious;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySiren extends EntityMob implements IAnimatedEntity, IVillagerFear {

    public static final int SEARCH_RANGE = 32;
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "siren"));
    public static final Predicate<Entity> SIREN_PREY = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return (p_apply_1_ instanceof EntityPlayer && !((EntityPlayer) p_apply_1_).isCreative() && !((EntityPlayer) p_apply_1_).isSpectator()) || p_apply_1_ instanceof EntityVillager || p_apply_1_ instanceof IHearsSiren;
        }
    };
    private static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.createKey(EntitySiren.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> AGGRESSIVE = EntityDataManager.createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SING_POSE = EntityDataManager.createKey(EntitySiren.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SINGING = EntityDataManager.createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWIMMING = EntityDataManager.createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CHARMED = EntityDataManager.createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySiren.class, DataSerializers.BYTE);
    public static Animation ANIMATION_BITE = Animation.create(20);
    public static Animation ANIMATION_PULL = Animation.create(20);
    @SideOnly(Side.CLIENT)
    public ChainBuffer tail_buffer;
    public float singProgress;
    public float swimProgress;
    public int singCooldown;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isSinging;
    private boolean isSwimming;
    private boolean isLandNavigator;
    private int ticksAgressive;

    public EntitySiren(World worldIn) {
        super(worldIn);
        this.setSize(1.6F, 0.9F);
        this.switchNavigator(true);
        this.stepHeight = 2;
        this.tasks.addTask(0, new SirenAIFindWaterTarget(this));
        this.tasks.addTask(1, new AquaticAIGetInWater(this, 1.0D));
        this.tasks.addTask(1, new AquaticAIGetOutOfWater(this, 1.0D));
        this.tasks.addTask(2, new SirenAIWander(this, 1));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return EntitySiren.this.isAgressive() && !(entity.isCreative() || entity.isSpectator());
            }
        }));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true, false, new Predicate<EntityVillager>() {
            @Override
            public boolean apply(@Nullable EntityVillager entity) {
                return EntitySiren.this.isAgressive();
            }
        }));
        if (FMLCommonHandler.instance().getSide().isClient()) {
            tail_buffer = new ChainBuffer();
        }
    }

    public static boolean isWearingEarplugs(EntityLivingBase entity) {
        ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return helmet.getItem() == IafItemRegistry.earplugs || helmet != ItemStack.EMPTY && helmet.getItem().getTranslationKey().contains("earmuff");
    }

    public static boolean isDrawnToSong(Entity entity) {
        return entity instanceof EntityPlayer && !((EntityPlayer) entity).isCreative() || entity instanceof EntityVillager || entity instanceof IHearsSiren;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 8;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    public float getBlockPathWeight(BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER ? 10F : super.getBlockPathWeight(pos);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRNG().nextInt(2) == 0) {
            if (this.getAnimation() != ANIMATION_PULL) {
                this.setAnimation(ANIMATION_PULL);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        } else {
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(IafSoundRegistry.NAGA_ATTACK, 1, 1);
            }
        }
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) this.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public float getPathPriority(PathNodeType nodeType) {
        return nodeType == PathNodeType.WATER ? 0F : super.getPathPriority(nodeType);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateAmphibious(this, world);
            this.isLandNavigator = true;
        } else {
            this.moveHelper = new EntitySiren.SwimmingMoveHelper();
            this.navigator = new PathNavigateSwimmer(this, world);
            this.isLandNavigator = false;
        }
    }

    private boolean isPathOnHighGround() {
        if (this.navigator != null && this.navigator.getPath() != null && this.navigator.getPath().getFinalPathPoint() != null) {
            BlockPos target = new BlockPos(this.navigator.getPath().getFinalPathPoint().x, this.navigator.getPath().getFinalPathPoint().y, this.navigator.getPath().getFinalPathPoint().z);
            BlockPos siren = new BlockPos(this);
            return world.isAirBlock(siren.up()) && world.isAirBlock(target.up()) && target.getY() >= siren.getY();
        }
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return super.isInWater() || this.isInsideOfMaterial(Material.WATER) || this.isInsideOfMaterial(Material.CORAL);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (singCooldown > 0) {
            singCooldown--;
            this.setSinging(false);
        }
        if (!world.isRemote && this.getAttackTarget() == null && !this.isAgressive()) {
            this.setSinging(true);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 5) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if (this.getAnimation() == ANIMATION_PULL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 12D && this.getAnimationTick() == 5) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            this.getAttackTarget().motionX += (Math.signum(this.posX - this.getAttackTarget().posX) * 0.5D - this.getAttackTarget().motionX) * 0.100000000372529 * 5;
            this.getAttackTarget().motionY += (Math.signum(this.posY - this.getAttackTarget().posY + 1) * 0.5D - this.getAttackTarget().motionY) * 0.100000000372529 * 5;
            this.getAttackTarget().motionZ += (Math.signum(this.posZ - this.getAttackTarget().posZ) * 0.5D - this.getAttackTarget().motionZ) * 0.100000000372529 * 5;
            float angle = (float) (Math.atan2(this.getAttackTarget().motionZ, this.getAttackTarget().motionX) * 180.0D / Math.PI) - 90.0F;
            //entity.moveForward = 0.5F;
            double d0 = this.posX - this.getAttackTarget().posX;
            double d2 = this.posZ - this.getAttackTarget().posZ;
            double d1 = this.posY - 1 - this.getAttackTarget().posY;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            this.getAttackTarget().rotationPitch = ServerEvents.updateRotation(this.getAttackTarget().rotationPitch, f1, 30F);
            this.getAttackTarget().rotationYaw = ServerEvents.updateRotation(this.getAttackTarget().rotationYaw, f, 30F);
        }
        if (world.isRemote) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 2.5F, this);
        }
        if (this.isAgressive()) {
            ticksAgressive++;
        } else {
            ticksAgressive = 0;
        }
        if (ticksAgressive > 300 && this.isAgressive() && this.getAttackTarget() == null && !world.isRemote) {
            this.setAggressive(false);
            this.ticksAgressive = 0;
            this.setSinging(false);
        }

        if (this.isInWater() && !this.isSwimming()) {
            this.setSwimming(true);
        }
        if (!this.isInWater() && this.isSwimming()) {
            this.setSwimming(false);
        }
        boolean pathOnHighGround = this.isPathOnHighGround() || this.getAttackTarget() != null && !this.getAttackTarget().isInWater() && !this.getAttackTarget().isOverWater();
        if (this.getAttackTarget() == null || !this.getAttackTarget().isInWater() && !this.getAttackTarget().isOverWater()) {
            if (pathOnHighGround && this.isInWater()) {
                jump();
                doWaterSplashEffect();
            }
        }
        if ((this.isInWater() && !pathOnHighGround) && this.isLandNavigator) {
            switchNavigator(false);
        }
        if ((!this.isInWater() || pathOnHighGround) && !this.isLandNavigator) {
            switchNavigator(true);
        }
        if (this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).isCreative()) {
            this.setAttackTarget(null);
            this.setAggressive(false);
        }
        if (this.getAttackTarget() != null && !this.isAgressive()) {
            this.setAggressive(true);
        }
        boolean singing = isActuallySinging() && !this.isAgressive() && !this.isInWater() && onGround;
        if (singing && singProgress < 20.0F) {
            singProgress += 1F;
        } else if (!singing && singProgress > 0.0F) {
            singProgress -= 1F;
        }
        boolean swimming = isSwimming();
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 1F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        if (!world.isRemote && !EntityGorgon.isStoneMob(this) && this.isActuallySinging()) {
            checkForPrey();
            updateLure();
        }
        if (!world.isRemote && EntityGorgon.isStoneMob(this) && this.isSinging()) {
            this.setSinging(false);
        }
        if (isActuallySinging() && !this.isInWater()) {
            if (this.getRNG().nextInt(3) == 0) {
                this.renderYawOffset = 0;
                renderYawOffset = rotationYaw;
                if (this.world.isRemote) {
                    float radius = -0.9F;
                    float angle = (0.01745329251F * this.renderYawOffset) - 3F;
                    double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                    double extraY = 1.2F;
                    double extraZ = radius * MathHelper.cos(angle);
                    IceAndFire.PROXY.spawnParticle("siren_music", this.posX + extraX + this.rand.nextFloat() - 0.5, this.posY + extraY + this.rand.nextFloat() - 0.5, this.posZ + extraZ + this.rand.nextFloat() - 0.5, 0, 0, 0);
                }
            }
        }
        if (this.isActuallySinging() && !this.isInWater() && this.ticksExisted % 200 == 0) {
            this.playSound(IafSoundRegistry.SIREN_SONG, 2, 1);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void checkForPrey() {
        this.setSinging(true);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase) {
            this.triggerOtherSirens((EntityLivingBase) source.getTrueSource());
        }
        return super.attackEntityFrom(source, amount);
    }

    public void triggerOtherSirens(EntityLivingBase aggressor) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(12, 12, 12));
        for (Entity entity : entities) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).setAttackTarget(aggressor);
                ((EntitySiren) entity).setAggressive(true);
                ((EntitySiren) entity).setSinging(false);

            }
        }
    }

    public void updateLure() {
        if (this.ticksExisted % 20 == 0) {
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(50, 12, 50), SIREN_PREY);
            for (EntityLivingBase entity : entities) {
                SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(entity, SirenEntityProperties.class);
                if (!isWearingEarplugs(entity) && sirenProps != null && (!sirenProps.isCharmed || sirenProps.getSiren(world) == null)) {
                    sirenProps.isCharmed = true;
                    sirenProps.sirenID = this.getEntityId();
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("HairColor", this.getHairColor());
        tag.setBoolean("Aggressive", this.isAgressive());
        tag.setInteger("SingingPose", this.getSingingPose());
        tag.setBoolean("Singing", this.isSinging());
        tag.setBoolean("Swimming", this.isSwimming());
        tag.setBoolean("Passive", this.isCharmed());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setHairColor(tag.getInteger("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInteger("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));
        this.setCharmed(tag.getBoolean("Passive"));

    }

    public boolean isSinging() {
        if (world.isRemote) {
            return this.isSinging = this.dataManager.get(SINGING).booleanValue();
        }
        return isSinging;
    }

    public void setSinging(boolean singing) {
        if (singCooldown > 0) {
            singing = false;
        }
        this.dataManager.set(SINGING, singing);
        if (!world.isRemote) {
            this.isSinging = singing;
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageSirenSong(this.getEntityId(), singing));
        }
    }

    public boolean wantsToSing() {
        return this.isSinging() && this.isInWater() && !this.isAgressive();
    }

    public boolean isActuallySinging() {
        return isSinging() && !wantsToSing();
    }

    public boolean isSwimming() {
        if (world.isRemote) {
            return this.isSwimming = this.dataManager.get(SWIMMING).booleanValue();
        }
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        this.dataManager.set(SWIMMING, swimming);
        if (!world.isRemote) {
            this.isSwimming = swimming;
        }
    }

    public void setAggressive(boolean aggressive) {
        this.dataManager.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.dataManager.get(AGGRESSIVE).booleanValue();
    }

    public boolean isCharmed() {
        return this.dataManager.get(CHARMED).booleanValue();
    }

    public void setCharmed(boolean aggressive) {
        this.dataManager.set(CHARMED, aggressive);
    }

    public int getHairColor() {
        return this.dataManager.get(HAIR_COLOR).intValue();
    }

    public void setHairColor(int hairColor) {
        this.dataManager.set(HAIR_COLOR, hairColor);
    }

    public int getSingingPose() {
        return this.dataManager.get(SING_POSE).intValue();
    }

    public void setSingingPose(int pose) {
        this.dataManager.set(SING_POSE, MathHelper.clamp(pose, 0, 2));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.sirenMaxHealth);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAIR_COLOR, Integer.valueOf(0));
        this.dataManager.register(SING_POSE, Integer.valueOf(0));
        this.dataManager.register(AGGRESSIVE, Boolean.valueOf(false));
        this.dataManager.register(SINGING, Boolean.valueOf(false));
        this.dataManager.register(SWIMMING, Boolean.valueOf(false));
        this.dataManager.register(CHARMED, Boolean.valueOf(false));
        this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setHairColor(this.getRNG().nextInt(3));
        this.setSingingPose(this.getRNG().nextInt(3));
        return livingdata;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_BITE, ANIMATION_PULL};
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_IDLE : IafSoundRegistry.MERMAID_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isAgressive() ? IafSoundRegistry.NAGA_HURT : IafSoundRegistry.MERMAID_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return this.isAgressive() ? IafSoundRegistry.NAGA_DIE : IafSoundRegistry.MERMAID_DIE;
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (this.isServerWorld()) {
            float f4;
            float f5;
            if (this.isInWater()) {
                this.moveRelative(strafe, forward, vertical, 0.1F);
                f4 = 0.8F;
                float d0 = (float) EnchantmentHelper.getDepthStriderModifier(this);
                if (d0 > 3.0F) {
                    d0 = 3.0F;
                }
                if (!this.onGround) {
                    d0 *= 0.5F;
                }
                if (d0 > 0.0F) {
                    f4 += (0.54600006F - f4) * d0 / 3.0F;
                }
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= f4;
                this.motionX *= 0.900000011920929D;
                this.motionY *= 0.900000011920929D;
                this.motionY *= f4;
                this.motionZ *= 0.900000011920929D;
                this.motionZ *= f4;
            } else {
                super.travel(strafe, forward, vertical);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean shouldFear() {
        return isAgressive();
    }

    class SwimmingMoveHelper extends EntityMoveHelper {
        private EntitySiren siren = EntitySiren.this;

        public SwimmingMoveHelper() {
            super(EntitySiren.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.siren.getNavigator().noPath() && !this.siren.isBeingRidden()) {
                double distanceX = this.posX - this.siren.posX;
                double distanceY = this.posY - this.siren.posY;
                double distanceZ = this.posZ - this.siren.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.siren.rotationYaw = this.limitAngle(this.siren.rotationYaw, angle, 30.0F);
                this.siren.setAIMoveSpeed(1F);
                this.siren.motionY += (double) this.siren.getAIMoveSpeed() * distanceY * 0.1D;
                if (distance < (double) Math.max(1.0F, this.entity.width)) {
                    float f = this.siren.rotationYaw * 0.017453292F;
                    this.siren.motionX -= MathHelper.sin(f) * 0.35F;
                    this.siren.motionZ += MathHelper.cos(f) * 0.35F;
                }
            } else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                if (this.entity.onGround) {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            } else {
                this.siren.setAIMoveSpeed(0.0F);
            }
        }
    }
}
