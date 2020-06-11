package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormLand;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormSand;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import javax.annotation.Nullable;

public class EntityDeathWorm extends TameableEntity implements ISyncMount, IBlacklistedFromStatues, IAnimatedEntity, IVillagerFear, IAnimalFear, IPhasesThroughBlock, IGroundMount {

    public static final ResourceLocation TAN_LOOT = new ResourceLocation("iceandfire", "deathworm_tan");
    public static final ResourceLocation WHITE_LOOT = new ResourceLocation("iceandfire", "deathworm_white");
    public static final ResourceLocation RED_LOOT = new ResourceLocation("iceandfire", "deathworm_red");
    public static final ResourceLocation TAN_GIANT_LOOT = new ResourceLocation("iceandfire", "deathworm_tan_giant");
    public static final ResourceLocation WHITE_GIANT_LOOT = new ResourceLocation("iceandfire", "deathworm_white_giant");
    public static final ResourceLocation RED_GIANT_LOOT = new ResourceLocation("iceandfire", "deathworm_red_giant");
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> WORM_AGE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.BLOCK_POS);
    public static Animation ANIMATION_BITE = Animation.create(10);
    @OnlyIn(Dist.CLIENT)
    public ChainBuffer tail_buffer;
    private int animationTick;
    private boolean willExplode = false;
    private int ticksTillExplosion = 60;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[6];
    private boolean isSandNavigator;
    private float prevScale = 0.0F;
    private LookController lookHelper;
    private int growthCounter = 0;

    public EntityDeathWorm(EntityType type, World worldIn) {
        super(type, worldIn);
        this.lookHelper = new IAFLookHelper(this);
        this.ignoreFrustumCheck = true;
        this.stepHeight = 1;
        if (worldIn.isRemote) {
            tail_buffer = new ChainBuffer();
        }
        this.switchNavigator(false);
        this.goalSelector.addGoal(0, new EntityGroundAIRide<>(this));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(3, new DeathWormAIFindSandTarget(this, 10));
        this.goalSelector.addGoal(4, new DeathWormAIGetInSand(this, 1.0D));
        this.goalSelector.addGoal(5, new DeathWormAIWander(this, 1));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new DeathwormAITargetItems(this, false, false));
        this.targetSelector.addGoal(5, new DeathWormAITarget(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity input) {
                if (EntityDeathWorm.this.isTamed()) {
                    return input instanceof MonsterEntity;
                } else {
                    return (IafConfig.deathWormAttackMonsters ? input instanceof LivingEntity : (input instanceof AnimalEntity || input instanceof PlayerEntity)) && DragonUtils.isAlive(input) && !(input instanceof EntityDragonBase && ((EntityDragonBase) input).isModelDead()) && !EntityDeathWorm.this.isOwner(input);
                }
            }
        }));
        initSegments(1);
    }

    public LookController getLookController() {
        return this.lookHelper;
    }


    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.getPosX());
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(this.getPosZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        return BlockTags.SAND.contains(this.world.getBlockState(blockpos.down()).getBlock()) && this.getRNG().nextInt(1 + IafConfig.deathWormSpawnCheckChance) == 0 && this.world.getLight(blockpos) > 8;
    }

    public void onUpdateParts() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.tick();
            }
        }
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return this.getRenderScale() > 3 ? 20 : 10;
    }

    public void initSegments(float scale) {
        segments = new EntityMutlipartPart[11];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new EntityMutlipartPart(this, (-0.8F - (i * 0.8F)) * scale, 0, 0, 0.7F * scale, 0.7F * scale, 1);
        }
    }

    private void clearSegments() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.onKillCommand();
                entity.remove();
            }
        }
    }

    public void setExplosive(boolean explosive) {
        this.willExplode = true;
        this.ticksTillExplosion = 60;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            this.playSound(this.getRenderScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
        }
        if (this.getRNG().nextInt(3) == 0 && this.getRenderScale() > 1 && this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ()))) {
                BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, this, entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), this.getRenderScale());
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
        }
        return false;
    }

    public void onDeath(DamageSource cause) {
        clearSegments();
        super.onDeath(cause);
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        switch (this.getVariant()) {
            case 0:
                return this.getRenderScale() > 3 ? TAN_GIANT_LOOT : TAN_LOOT;
            case 1:
                return this.getRenderScale() > 3 ? WHITE_GIANT_LOOT : WHITE_LOOT;
            case 2:
                return this.getRenderScale() > 3 ? RED_GIANT_LOOT : RED_LOOT;
        }
        return null;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(1F));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.dataManager.register(WORM_AGE, Integer.valueOf(10));
        this.dataManager.register(HOME, BlockPos.ZERO);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("GrowthCounter", this.growthCounter);
        compound.putFloat("Scale", this.getScale());
        compound.putInt("WormAge", this.getWormAge());
        compound.putLong("WormHome", this.getWormHome().toLong());
        compound.putBoolean("WillExplode", this.willExplode);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
        this.growthCounter = compound.getInt("GrowthCounter");
        this.setDeathWormScale(compound.getFloat("Scale"));
        this.setWormAge(compound.getInt("WormAge"));
        this.setWormHome(BlockPos.fromLong(compound.getLong("WormHome")));
        this.willExplode = compound.getBoolean("WillExplode");
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = dataManager.get(CONTROL_STATE).byteValue();
        if (newState) {
            dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getControlState() {
        return Byte.valueOf(dataManager.get(CONTROL_STATE));
    }

    public void setControlState(byte state) {
        dataManager.set(CONTROL_STATE, Byte.valueOf(state));
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public BlockPos getWormHome() {
        return this.dataManager.get(HOME);
    }

    public void setWormHome(BlockPos home) {
        if (home instanceof BlockPos) {
            this.dataManager.set(HOME, home);
        }
    }

    public int getWormAge() {
        return Math.max(1, Integer.valueOf(dataManager.get(WORM_AGE).intValue()));
    }

    public void setWormAge(int age) {
        this.dataManager.set(WORM_AGE, Integer.valueOf(age));
    }

    public float getScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    public float getRenderScale() {
        return Math.min(this.getScale() * (this.getWormAge() / 5F), 7F);
    }

    public void setDeathWormScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
        this.updateAttributes();
        clearSegments();
        if (!this.world.isRemote) {
            initSegments(scale * (this.getWormAge() / 5F));
            IceAndFire.sendMSGToAll(new MessageDeathWormHitbox(this.getEntityId(), scale * (this.getWormAge() / 5F)));
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRNG().nextInt(3));
        float size = 0.25F + (float) (Math.random() * 0.35F);
        this.setDeathWormScale(this.getRNG().nextInt(20) == 0 ? size * 4 : size);
        if (isSandBelow() && this.getRenderScale() != 1) {
            this.getMotion().add(0, -0.5D, 0);
        }
        return spawnDataIn;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IafConfig.deathWormAttackStrength);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IafConfig.deathWormMaxHealth);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IafConfig.deathWormTargetSearchLength));
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            this.rotationYaw = passenger.rotationYaw;
            float radius = -0.5F * this.getRenderScale();
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPosition(this.getPosX() + extraX, this.getPosY() + this.getEyeHeight() - 0.55F, this.getPosZ() + extraZ);
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) passenger;
                return player;
            }
        }
        return null;
    }

    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        if (this.getWormAge() > 4 && player.getRidingEntity() == null&& player.getHeldItemMainhand().getItem() == Items.FISHING_ROD && player.getHeldItemOffhand().getItem() == Items.FISHING_ROD && !this.world.isRemote) {
            player.startRiding(this);
            return true;
        }
        return super.processInteract(player, hand);
    }

    private void switchNavigator(boolean inSand) {
        if (inSand) {
            this.moveController = new EntityDeathWorm.SandMoveHelper();
            this.navigator = new PathNavigateDeathWormSand(this, world);
            this.isSandNavigator = true;
        } else {
            this.moveController = new MovementController(this);
            this.navigator = new PathNavigateDeathWormLand(this, world);
            this.isSandNavigator = false;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK) {
            return false;
        }
        if (this.isBeingRidden() && source.getTrueSource() != null && this.getControllingPassenger() != null && source.getTrueSource() == this.getControllingPassenger()) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean checkBlockCollision(AxisAlignedBB bb) {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.Mutable blockpos$pooledmutableblockpos = new BlockPos.Mutable();

        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    BlockState BlockState1 = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (BlockState1.getMaterial() != Material.AIR && BlockState1.getMaterial() != Material.SAND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        BlockPos.Mutable blockpos$pooledmutableblockpos = new BlockPos.Mutable();

        for (int i = 0; i < 8; ++i) {
            int j = MathHelper.floor(this.getPosY() + (double) (((float) ((i >> 0) % 2) - 0.5F) * 0.1F) + (double) this.getEyeHeight());
            int k = MathHelper.floor(this.getPosX() + (double) (((float) ((i >> 1) % 2) - 0.5F) * this.getWidth() * 0.8F));
            int l = MathHelper.floor(this.getPosZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * this.getWidth() * 0.8F));

            if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
                blockpos$pooledmutableblockpos.setPos(k, j, l);

                if (this.world.getBlockState(blockpos$pooledmutableblockpos).causesSuffocation(world, blockpos$pooledmutableblockpos) && this.world.getBlockState(blockpos$pooledmutableblockpos).getMaterial() != Material.SAND) {
                    return true;
                }
            }
        }
        return false;
    }


    protected void pushOutOfBlocks(double x, double y, double z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double) blockpos.getX();
        double d1 = y - (double) blockpos.getY();
        double d2 = z - (double) blockpos.getZ();

        if (!this.world.checkBlockCollision(this.getBoundingBox())) {
            return;
        } else {
            Direction dir = Direction.UP;
            double d3 = Double.MAX_VALUE;

            if (!isBlockFullCube(blockpos.west()) && d0 < d3) {
                d3 = d0;
                dir = Direction.WEST;
            }

            if (!isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
                d3 = 1.0D - d0;
                dir = Direction.EAST;
            }

            if (!isBlockFullCube(blockpos.north()) && d2 < d3) {
                d3 = d2;
                dir = Direction.NORTH;
            }

            if (!isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
                d3 = 1.0D - d2;
                dir = Direction.SOUTH;
            }

            if (!isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
                d3 = 1.0D - d1;
                dir = Direction.UP;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float) dir.getAxisDirection().getOffset();

            if (dir.getAxis() == Direction.Axis.X) {
                this.setMotion(this.getMotion().mul(f1 * f, 0.75D, 0.75D));
            } else if (dir.getAxis() == Direction.Axis.Y) {
                this.setMotion(this.getMotion().mul(0.75D, f1 * f, 0.75D));
            } else if (dir.getAxis() == Direction.Axis.Z) {
                this.setMotion(this.getMotion().mul(0.75D, 0.75D, f1 * f));
            }
        }
    }

    private boolean isBlockFullCube(BlockPos pos) {
        return world.getBlockState(pos).getMaterial() != Material.SAND && world.getBlockState(pos).isSolid();
    }

    private void updateAttributes() {
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.2D, 0.15D * this.getRenderScale()));
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.max(1, IafConfig.deathWormAttackStrength * this.getRenderScale()));
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(6, IafConfig.deathWormMaxHealth * this.getRenderScale()));
        this.setHealth((float) this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
    }

    public void onKillEntity(LivingEntity LivingEntityIn) {
        if (this.isTamed()) {
            this.heal(14);
        }
    }

    public void livingTick() {
        super.livingTick();
        if (world.getDifficulty() == Difficulty.PEACEFUL && this.getAttackTarget() instanceof PlayerEntity) {
            this.setAttackTarget(null);
        }
        if (this.willExplode) {
            if (this.ticksTillExplosion == 0) {
                boolean b = !MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, this.getPosX(), this.getPosY(), this.getPosZ()));
                if(b){
                    world.createExplosion(null, this.getPosX(), this.getPosY(), this.getPosZ(), 2.5F * this.getRenderScale(), false, Explosion.Mode.DESTROY);
                }
            } else {
                this.ticksTillExplosion--;
            }
        }
        if (this.ticksExisted == 1) {
            initSegments(this.getRenderScale());
        }
        if (growthCounter > 1000 && this.getWormAge() < 5) {
            growthCounter = 0;
            this.setWormAge(Math.min(5, this.getWormAge() + 1));
            this.clearSegments();
            this.heal(15);
            this.setDeathWormScale(this.getScale());
            if (world.isRemote) {
                for (int i = 0; i < 10 * this.getRenderScale(); i++) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getSurface((int) Math.floor(this.getPosX()), (int) Math.floor(this.getPosY()), (int) Math.floor(this.getPosZ())) + 0.5F, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    for (int j = 0; j < segments.length; j++) {
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, segments[j].getPosX() + (double) (this.rand.nextFloat() * segments[j].getWidth() * 2.0F) - (double) segments[j].getWidth(), this.getSurface((int) Math.floor(segments[j].getPosX()), (int) Math.floor(segments[j].getPosY()), (int) Math.floor(segments[j].getPosZ())) + 0.5F, segments[j].getPosZ() + (double) (this.rand.nextFloat() * segments[j].getWidth() * 2.0F) - (double) segments[j].getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    }
                }
            }
        }
        if (this.getWormAge() < 5) {
            growthCounter++;
        }
        if (this.getControllingPassenger() != null) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.setMotion(this.getMotion().add(0, 2, 0));
                //this.noClip = true;
            } else {
                this.noClip = false;
            }

        }
        if (this.getControllingPassenger() != null && this.getAttackTarget() != null) {
            this.getNavigator().clearPath();
            this.setAttackTarget(null);
        }
        if (this.getAttackTarget() == null) {
            this.rotationPitch = 0;
        } else {
            this.faceEntity(this.getAttackTarget(), 10.0F, 10.0F);
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist >= 4.0D * getRenderScale() && dist <= 16.0D * getRenderScale() && (this.isInSand() || this.onGround)) {
                double d0 = this.getAttackTarget().getPosX() - this.getPosX();
                double d1 = this.getAttackTarget().getPosZ() - this.getPosZ();
                float leap = MathHelper.sqrt(d0 * d0 + d1 * d1);
                if ((double) leap >= 1.0E-4D) {
                    this.setMotion(this.getMotion().add(d0 / (double) leap * 0.800000011920929D, 0.5F, d1 / (double) leap * 0.800000011920929D));
                }
                this.setAnimation(ANIMATION_BITE);
            }
            if (dist < Math.min(4, 4D * getRenderScale()) && this.getAnimation() == ANIMATION_BITE) {
                float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), f);
                this.setMotion(this.getMotion().add(0, -0.4F, 0));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(MathHelper.floor(this.getPosX()), 0, MathHelper.floor(this.getPosZ()));

        if (this.world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.getPosY() + (double) this.getEyeHeight()));
            if (world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.SAND || this.isEntityInsideOpaqueBlock()) {
                blockpos$mutableblockpos.setY(world.getHeight(Heightmap.Type.WORLD_SURFACE, MathHelper.floor(this.getPosX()), MathHelper.floor(this.getPosZ())));
            }
            return this.world.getLightSubtracted(blockpos$mutableblockpos, 0);
        } else {
            return 0;
        }
    }

    public int getSurface(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        while (!world.isAirBlock(pos)) {
            pos = pos.up();
        }
        return pos.getY();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.getRenderScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_IDLE : IafSoundRegistry.DEATHWORM_IDLE;
    }


    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.getRenderScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_HURT : IafSoundRegistry.DEATHWORM_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return this.getRenderScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_DIE : IafSoundRegistry.DEATHWORM_DIE;
    }

    @Override
    public void tick() {
        super.tick();
        onUpdateParts();
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof PlayerEntity) {
            LivingEntity target = DragonUtils.riderLookingAtEntity(this, (PlayerEntity) this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(this.getRenderScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
                if (this.getRNG().nextInt(3) == 0 && this.getRenderScale() > 1) {
                    float radius = 1.5F * this.getRenderScale();
                    float angle = (0.01745329251F * this.renderYawOffset);
                    double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                    double extraZ = radius * MathHelper.cos(angle);
                    BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, this, this.getPosX() + extraX, this.getPosY() - this.getEyeHeight(), this.getPosZ() + extraZ, this.getRenderScale() * 0.75F);
                    explosion.doExplosionA();
                    explosion.doExplosionB(true);
                }
            }
            if (target != null) {
                target.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (!this.isInSand()) {
            this.noClip = false;
        } else {
            BlockPos pos = new BlockPos(this.getPosX(), this.getSurface((int) Math.floor(this.getPosX()), (int) Math.floor(this.getPosY()), (int) Math.floor(this.getPosZ())), this.getPosZ()).down();
            BlockState state = world.getBlockState(pos);
            if (state.isOpaqueCube(world, pos )) {
                if (world.isRemote) {
                    this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getSurface((int) Math.floor(this.getPosX()), (int) Math.floor(this.getPosY()), (int) Math.floor(this.getPosZ())) + 0.5F, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    for (int i = 0; i < segments.length; i++) {
                        this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), segments[i].prevPosX + (double) (this.rand.nextFloat() * segments[i].getWidth() * 2.0F) - (double) segments[i].getWidth(), this.getSurface((int) Math.floor(segments[i].prevPosX), (int) Math.floor(segments[i].prevPosY), (int) Math.floor(segments[i].prevPosZ)) + 0.5F, segments[i].prevPosZ + (double) (this.rand.nextFloat() * segments[i].getWidth() * 2.0F) - (double) segments[i].getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    }
                }
            }
            if (this.ticksExisted % 10 == 0) {
                this.playSound(SoundEvents.BLOCK_SAND_BREAK, 1, 0.5F);
            }
        }
        if (this.up() && this.onGround) {
            this.jump();
        }
        if (isInSand() && !this.isSandNavigator) {
            switchNavigator(true);
        }
        if (!isInSandStrict() && this.isSandNavigator) {
            switchNavigator(false);
        }
        if (world.isRemote) {
            tail_buffer.calculateChainSwingBuffer(90, 20, 5F, this);
        }
        if (this.getControllingPassenger() != null) {
            this.noClip = false;
            this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());
            if (isSandBelow()) {
                int i = MathHelper.floor(this.getPosX());
                int j = MathHelper.floor(this.getPosY() - 1);
                int k = MathHelper.floor(this.getPosZ());
                BlockPos blockpos = new BlockPos(i, j, k);
                BlockState BlockState = this.world.getBlockState(blockpos);

                if (world.isRemote) {
                    world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, BlockState), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getSurface((int) Math.floor(this.getPosX()), (int) Math.floor(this.getPosY()), (int) Math.floor(this.getPosZ())) + 0.5F, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    for (int e = 0; e < segments.length; e++) {
                        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, BlockState), segments[e].getPosX() + (double) (this.rand.nextFloat() * segments[e].getWidth() * 2.0F) - (double) segments[e].getWidth(), this.getSurface((int) Math.floor(segments[e].getPosX()), (int) Math.floor(segments[e].getPosY()), (int) Math.floor(segments[e].getPosZ())) + 0.5F, segments[e].getPosZ() + (double) (this.rand.nextFloat() * segments[e].getWidth() * 2.0F) - (double) segments[e].getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    }
                }
            }
        }
        if (world.isRemote) {
            this.updateClientControls();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getInstance();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            attack(IafKeybindRegistry.dragon_strike.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, getPosX(), getPosY(), getPosZ()));
            }
        }
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, getPosX(), getPosY(), getPosZ()));
            }
        }
    }

    public boolean up() {
        return (dataManager.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    public boolean dismount() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    public void up(boolean up) {
        setStateField(0, up);
    }

    public void dismount(boolean dismount) {
        setStateField(1, dismount);
    }

    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    public boolean isSandBelow() {
        int i = MathHelper.floor(this.getPosX());
        int j = MathHelper.floor(this.getPosY() - 1);
        int k = MathHelper.floor(this.getPosZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState BlockState = this.world.getBlockState(blockpos);
        return BlockState.getMaterial() == Material.SAND;
    }

    public boolean isInSand() {
        return this.getControllingPassenger() == null && this.world.isMaterialInBB(this.getBoundingBox().grow(0.25D, 0.25D, 0.25D), Material.SAND);
    }

    public boolean isInSandStrict() {
        return this.getControllingPassenger() == null && this.world.isMaterialInBB(this.getBoundingBox(), Material.SAND);
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
        return new Animation[]{ANIMATION_BITE};
    }

    public Entity[] getWormParts() {
        return segments;
    }

    public int getHorizontalFaceSpeed() {
        return 10;
    }

    @Override
    public boolean canPassengerSteer() {
        return false;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }


    @Override
    public void travel(Vec3d vec) {
        float f4;
        if (this.isBeingRidden() && this.canBeSteered()) {
            LivingEntity controller = (LivingEntity) this.getControllingPassenger();
            if (controller != null) {
                if (this.getAttackTarget() != null) {
                    this.setAttackTarget(null);
                    this.getNavigator().clearPath();
                }
                super.travel(vec);
                return;
            }
        }
        if (this.isServerWorld()) {
            float f5;
            if (this.isInSandStrict()) {
                this.moveRelative(0.1F, vec);
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
                this.move(MoverType.SELF, vec);
                this.setMotion(this.getMotion().mul(f4 * 0.9, f4 * 0.9, f4 * 0.9));
            } else {
                super.travel(vec);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.getPosX() - this.prevPosX;
        double deltaZ = this.getPosZ() - this.prevPosZ;
        double deltaY = this.getPosY() - this.prevPosY;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;

    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean canPhaseThroughBlock(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.SAND;
    }

    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, BlockState blockStateIn, float p_174816_5_) {
        float hardness = blockStateIn.getBlockHardness(worldIn, pos);
        return hardness != -1.0F && hardness < 1.5F;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isRidingPlayer(PlayerEntity player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Nullable
    public PlayerEntity getRidingPlayer() {
        if (this.getControllingPassenger() instanceof PlayerEntity) {
            return (PlayerEntity) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getRideSpeedModifier() {
        return 1;
    }

    public class SandMoveHelper extends MovementController {
        private EntityDeathWorm worm = EntityDeathWorm.this;

        public SandMoveHelper() {
            super(EntityDeathWorm.this);
        }

        @Override
        public void tick() {
            if (this.action == Action.MOVE_TO && !this.worm.getNavigator().noPath() && !this.worm.isBeingRidden()) {
                double distanceX = this.posX - this.worm.getPosX();
                double distanceY = this.posY - this.worm.getPosY();
                double distanceZ = this.posZ - this.worm.getPosZ();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.worm.rotationYaw = this.limitAngle(this.worm.rotationYaw, angle, 30.0F);
                this.worm.setAIMoveSpeed(1F);
                double motionY = (double) this.worm.getAIMoveSpeed() * distanceY * 0.1D;
                double motionX = 0;
                double motionZ = 0;
                if (distance < (double) Math.max(1.0F, this.worm.getWidth())) {
                    float f = this.worm.rotationYaw * 0.017453292F;
                    motionX -= MathHelper.sin(f) * 0.35F;
                    motionZ += MathHelper.cos(f) * 0.35F;
                }
                this.worm.setMotion(this.worm.getMotion().add(motionX, motionY, motionZ));
            } else if (this.action == Action.JUMPING) {
                this.worm.setAIMoveSpeed((float) (this.speed * this.worm.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
            } else {
                this.worm.setAIMoveSpeed(0.0F);
            }
        }
    }
}
