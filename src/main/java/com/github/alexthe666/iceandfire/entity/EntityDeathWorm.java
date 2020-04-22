package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormLand;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormSand;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityDeathWorm extends EntityTameable implements ISyncMount, IBlacklistedFromStatues, IMultipartEntity, IAnimatedEntity, IVillagerFear, IAnimalFear, IPhasesThroughBlock, IGroundMount {

    public static final ResourceLocation TAN_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_tan"));
    public static final ResourceLocation WHITE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_white"));
    public static final ResourceLocation RED_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_red"));
    public static final ResourceLocation TAN_GIANT_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_tan_giant"));
    public static final ResourceLocation WHITE_GIANT_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_white_giant"));
    public static final ResourceLocation RED_GIANT_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "deathworm_red_giant"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> WORM_AGE = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityDeathWorm.class, DataSerializers.BLOCK_POS);
    public static Animation ANIMATION_BITE = Animation.create(10);
    @SideOnly(Side.CLIENT)
    public ChainBuffer tail_buffer;
    private int animationTick;
    private boolean willExplode = false;
    private int ticksTillExplosion = 60;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[6];
    private boolean isSandNavigator;
    private float prevScale = 0.0F;
    private EntityLookHelper lookHelper;
    private int growthCounter = 0;

    public EntityDeathWorm(World worldIn) {
        super(worldIn);
        this.lookHelper = new IAFLookHelper(this);
        this.ignoreFrustumCheck = true;
        this.stepHeight = 1;
        this.setSize(0.8F, 0.8F);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            tail_buffer = new ChainBuffer();
        }
        this.spawnableBlock = Blocks.SAND;
        this.switchNavigator(false);
        this.tasks.addTask(0, new EntityGroundAIRide<>(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.5D, true));
        this.tasks.addTask(3, new DeathWormAIFindSandTarget(this, 10));
        this.tasks.addTask(4, new DeathWormAIGetInSand(this, 1.0D));
        this.tasks.addTask(5, new DeathWormAIWander(this, 1));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new DeathwormAITargetItems(this, false, false));
        this.targetTasks.addTask(5, new DeathWormAITarget(this, EntityLivingBase.class, false, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase input) {
                if (EntityDeathWorm.this.isTamed()) {
                    return input instanceof EntityMob;
                } else {
                    return (IceAndFire.CONFIG.deathWormAttackMonsters ? input instanceof EntityLivingBase : (input instanceof EntityAnimal || input instanceof EntityPlayer)) && DragonUtils.isAlive(input) && !(input instanceof EntityDragonBase && ((EntityDragonBase) input).isModelDead()) && !EntityDeathWorm.this.isOwner(input);
                }
            }
        }));
        initSegments(1);
    }

    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }


    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return this.world.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.getRNG().nextInt(1 + IceAndFire.CONFIG.deathWormSpawnCheckChance) == 0 && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

    public void onUpdateParts() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.onUpdate();
            }
        }
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return this.getScaleForAge() > 3 ? 20 : 10;
    }

    public void initSegments(float scale) {
        this.setScaleForAge(false);
        segments = new EntityMutlipartPart[11];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new EntityMutlipartPart(this, (-0.8F - (i * 0.8F)) * scale, 0, 0, 0.7F * scale, 0.7F * scale, 1);
        }
    }

    private void clearSegments() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.onKillCommand();
                world.removeEntityDangerously(entity);
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
            this.playSound(this.getScaleForAge() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
        }
        if (this.getRNG().nextInt(3) == 0 && this.getScaleForAge() > 1 && this.world.getGameRules().getBoolean("mobGriefing")) {
            if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, entityIn.posX, entityIn.posY, entityIn.posZ))) {
                BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, this, entityIn.posX, entityIn.posY, entityIn.posZ, this.getScaleForAge());
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
        }
        return false;
    }

    public void onDeath(DamageSource cause) {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        clearSegments();
        if (!this.dead) {
            Entity entity = cause.getTrueSource();
            EntityLivingBase entitylivingbase = this.getAttackingEntity();

            if (this.scoreValue >= 0 && entitylivingbase != null) {
                entitylivingbase.awardKillScore(this, this.scoreValue, cause);
            }

            if (entity != null) {
                entity.onKillEntity(this);
            }

            this.dead = true;
            this.getCombatTracker().reset();

            if (!this.world.isRemote) {
                int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(this, entity, cause);

                captureDrops = true;
                capturedDrops.clear();

                if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
                    boolean flag = this.recentlyHit > 0;
                    this.dropLoot(flag, i, cause);
                }

                captureDrops = false;

                if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(this, cause, capturedDrops, i, recentlyHit > 0)) {
                    for (EntityItem item : capturedDrops) {
                        world.spawnEntity(item);
                        item.posY = this.getSurface((int) item.posX, (int) item.posY, (int) item.posZ);
                    }
                }
            }

            this.world.setEntityState(this, (byte) 3);
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Nullable
    private EntityItem dropItemAt(ItemStack stack, double x, double y, double z) {
        EntityItem entityitem = new EntityItem(this.world, x, y, z, stack);
        entityitem.setDefaultPickupDelay();
        if (captureDrops)
            this.capturedDrops.add(entityitem);
        else
            this.world.spawnEntity(entityitem);
        return entityitem;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        switch (this.getVariant()) {
            case 0:
                return this.getScaleForAge() > 3 ? TAN_GIANT_LOOT : TAN_LOOT;
            case 1:
                return this.getScaleForAge() > 3 ? WHITE_GIANT_LOOT : WHITE_LOOT;
            case 2:
                return this.getScaleForAge() > 3 ? RED_GIANT_LOOT : RED_LOOT;
        }
        return null;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(1F));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.dataManager.register(WORM_AGE, Integer.valueOf(10));
        this.dataManager.register(HOME, BlockPos.ORIGIN);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("GrowthCounter", this.growthCounter);
        compound.setFloat("Scale", this.getScale());
        compound.setInteger("WormAge", this.getWormAge());
        compound.setLong("WormHome", this.getWormHome().toLong());
        compound.setBoolean("WillExplode", this.willExplode);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.growthCounter = compound.getInteger("GrowthCounter");
        this.setDeathWormScale(compound.getFloat("Scale"));
        this.setWormAge(compound.getInteger("WormAge"));
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

    public float getScaleForAge() {
        return this.getScale() * (this.getWormAge() / 5F);
    }

    @Override
    public void setScaleForAge(boolean baby) {
        this.setScale(Math.min(this.getScaleForAge(), 7F));
    }

    public void setDeathWormScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
        this.updateAttributes();
        clearSegments();
        if (!this.world.isRemote) {
            initSegments(scale * (this.getWormAge() / 5F));
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDeathWormHitbox(this.getEntityId(), scale * (this.getWormAge() / 5F)));
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(3));
        float size = 0.25F + (float) (Math.random() * 0.35F);
        this.setDeathWormScale(this.getRNG().nextInt(20) == 0 ? size * 4 : size);
        if (isSandBelow() && this.getScaleForAge() != 1) {
            this.motionY = -0.5F;
        }
        return livingdata;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.deathWormAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.deathWormMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.deathWormTargetSearchLength));
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            this.rotationYaw = passenger.rotationYaw;
            float radius = -0.5F * this.getScaleForAge();
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPosition(this.posX + extraX, this.posY + this.getEyeHeight() - 0.55F, this.posZ + extraZ);
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) passenger;
                return player;
            }
        }
        return null;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        if (this.getWormAge() > 4 && !player.isRiding() && player.getHeldItemMainhand().getItem() == Items.FISHING_ROD && player.getHeldItemOffhand().getItem() == Items.FISHING_ROD && !this.world.isRemote) {
            player.startRiding(this);
            return true;
        }
        return super.processInteract(player, hand);
    }

    private void switchNavigator(boolean inSand) {
        if (inSand) {
            this.moveHelper = new EntityDeathWorm.SandMoveHelper();
            this.navigator = new PathNavigateDeathWormSand(this, world);
            this.isSandNavigator = true;
        } else {
            this.moveHelper = new EntityMoveHelper(this);
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
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    IBlockState iblockstate1 = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (iblockstate1.getMaterial() != Material.AIR && iblockstate1.getMaterial() != Material.SAND) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int i = 0; i < 8; ++i) {
            int j = MathHelper.floor(this.posY + (double) (((float) ((i >> 0) % 2) - 0.5F) * 0.1F) + (double) this.getEyeHeight());
            int k = MathHelper.floor(this.posX + (double) (((float) ((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
            int l = MathHelper.floor(this.posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F));

            if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
                blockpos$pooledmutableblockpos.setPos(k, j, l);

                if (this.world.getBlockState(blockpos$pooledmutableblockpos).causesSuffocation() && this.world.getBlockState(blockpos$pooledmutableblockpos).getMaterial() != Material.SAND) {
                    blockpos$pooledmutableblockpos.release();
                    return true;
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }


    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double) blockpos.getX();
        double d1 = y - (double) blockpos.getY();
        double d2 = z - (double) blockpos.getZ();

        if (!this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
            return false;
        } else {
            EnumFacing enumfacing = EnumFacing.UP;
            double d3 = Double.MAX_VALUE;

            if (!isBlockFullCube(blockpos.west()) && d0 < d3) {
                d3 = d0;
                enumfacing = EnumFacing.WEST;
            }

            if (!isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
                d3 = 1.0D - d0;
                enumfacing = EnumFacing.EAST;
            }

            if (!isBlockFullCube(blockpos.north()) && d2 < d3) {
                d3 = d2;
                enumfacing = EnumFacing.NORTH;
            }

            if (!isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
                d3 = 1.0D - d2;
                enumfacing = EnumFacing.SOUTH;
            }

            if (!isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
                d3 = 1.0D - d1;
                enumfacing = EnumFacing.UP;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float) enumfacing.getAxisDirection().getOffset();

            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                this.motionX = f1 * f;
                this.motionY *= 0.75D;
                this.motionZ *= 0.75D;
            } else if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
                this.motionX *= 0.75D;
                this.motionY = f1 * f;
                this.motionZ *= 0.75D;
            } else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
                this.motionX *= 0.75D;
                this.motionY *= 0.75D;
                this.motionZ = f1 * f;
            }
            return true;
        }
    }

    private boolean isBlockFullCube(BlockPos pos) {
        AxisAlignedBB axisalignedbb = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
        return world.getBlockState(pos).getMaterial() != Material.SAND && axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    private void updateAttributes() {
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.2D, 0.15D * this.getScaleForAge()));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.max(1, IceAndFire.CONFIG.deathWormAttackStrength * this.getScaleForAge()));
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(6, IceAndFire.CONFIG.deathWormMaxHealth * this.getScaleForAge()));
        this.setHealth((float) this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        if (this.isTamed()) {
            this.heal(14);
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer) {
            this.setAttackTarget(null);
        }
        if (this.willExplode) {
            if (this.ticksTillExplosion == 0) {
                boolean b = !MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, posX, posY, posZ));
                world.newExplosion(null, this.posX, this.posY, this.posZ, 2.5F * this.getScaleForAge(), false, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) && b);
            } else {
                this.ticksTillExplosion--;
            }
        }
        if (this.ticksExisted == 1) {
            initSegments(this.getScaleForAge());
        }
        if (growthCounter > 1000 && this.getWormAge() < 5) {
            growthCounter = 0;
            this.setWormAge(Math.min(5, this.getWormAge() + 1));
            this.clearSegments();
            this.heal(15);
            this.setDeathWormScale(this.getScale());
            if (world.isRemote) {
                for (int i = 0; i < 10 * this.getScaleForAge(); i++) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getSurface((int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ)) + 0.5F, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    for (int j = 0; j < segments.length; j++) {
                        this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, segments[j].posX + (double) (this.rand.nextFloat() * segments[j].width * 2.0F) - (double) segments[j].width, this.getSurface((int) Math.floor(segments[j].posX), (int) Math.floor(segments[j].posY), (int) Math.floor(segments[j].posZ)) + 0.5F, segments[j].posZ + (double) (this.rand.nextFloat() * segments[j].width * 2.0F) - (double) segments[j].width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    }
                }
            }
        }
        if (this.getWormAge() < 5) {
            growthCounter++;
        }
        if (this.getControllingPassenger() != null) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.motionY = 2;
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
            if (dist >= 4.0D * getScaleForAge() && dist <= 16.0D * getScaleForAge() && (this.isInSand() || this.onGround)) {
                double d0 = this.getAttackTarget().posX - this.posX;
                double d1 = this.getAttackTarget().posZ - this.posZ;
                float leap = MathHelper.sqrt(d0 * d0 + d1 * d1);
                if ((double) leap >= 1.0E-4D) {
                    this.motionX += d0 / (double) leap * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                    this.motionZ += d1 / (double) leap * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                }
                this.motionY = 0.5F;
                this.setAnimation(ANIMATION_BITE);
            }
            if (dist < Math.min(4, 4D * getScaleForAge()) && this.getAnimation() == ANIMATION_BITE) {
                float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), f);
                this.motionY /= 2.0D;
                this.motionY -= 0.4;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

        if (this.world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + (double) this.getEyeHeight()));
            if (world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.SAND || this.isEntityInsideOpaqueBlock()) {
                blockpos$mutableblockpos.setY(world.getHeight(MathHelper.floor(this.posX), MathHelper.floor(this.posZ)));
            }
            return this.world.getCombinedLight(blockpos$mutableblockpos, 0);
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
        return this.getScaleForAge() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_IDLE : IafSoundRegistry.DEATHWORM_IDLE;
    }


    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.getScaleForAge() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_HURT : IafSoundRegistry.DEATHWORM_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return this.getScaleForAge() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_DIE : IafSoundRegistry.DEATHWORM_DIE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        onUpdateParts();
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer) {
            EntityLivingBase target = DragonUtils.riderLookingAtEntity(this, (EntityPlayer) this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(this.getScaleForAge() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
                if (this.getRNG().nextInt(3) == 0 && this.getScaleForAge() > 1) {
                    float radius = 1.5F * this.getScaleForAge();
                    float angle = (0.01745329251F * this.renderYawOffset);
                    double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                    double extraZ = radius * MathHelper.cos(angle);
                    BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, this, this.posX + extraX, this.posY - this.getEyeHeight(), this.posZ + extraZ, this.getScaleForAge() * 0.75F);
                    explosion.doExplosionA();
                    explosion.doExplosionB(true);
                }
            }
            if (target != null) {
                target.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (!this.isInSand()) {
            this.noClip = false;
        } else {
            IBlockState state = world.getBlockState(new BlockPos(this.posX, this.getSurface((int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ)), this.posZ).down());
            int blockId = Block.getStateId(state);
            if (state.isOpaqueCube()) {
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getSurface((int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ)) + 0.5F, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, blockId);
                    for (int i = 0; i < segments.length; i++) {
                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, segments[i].prevPosX + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.getSurface((int) Math.floor(segments[i].prevPosX), (int) Math.floor(segments[i].prevPosY), (int) Math.floor(segments[i].prevPosZ)) + 0.5F, segments[i].prevPosZ + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, blockId);
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
            this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            if (isSandBelow()) {
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getSurface((int) Math.floor(this.posX), (int) Math.floor(this.posY), (int) Math.floor(this.posZ)) + 0.5F, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(Blocks.SAND));
                    for (int i = 0; i < segments.length; i++) {
                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, segments[i].posX + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.getSurface((int) Math.floor(segments[i].posX), (int) Math.floor(segments[i].posY), (int) Math.floor(segments[i].posZ)) + 0.5F, segments[i].posZ + (double) (this.rand.nextFloat() * segments[i].width * 2.0F) - (double) segments[i].width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(Blocks.SAND));
                    }
                }
            }
        }
        if (world.isRemote) {
            this.updateClientControls();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @SideOnly(Side.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            attack(IafKeybindRegistry.dragon_strike.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
            }
        }
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
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
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 1);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getMaterial() == Material.SAND;
    }

    public boolean isInSand() {
        return this.getControllingPassenger() == null && this.world.isMaterialInBB(this.getEntityBoundingBox().grow(0.25D, 0.25D, 0.25D), Material.SAND);
    }

    public boolean isInSandStrict() {
        return this.getControllingPassenger() == null && this.world.isMaterialInBB(this.getEntityBoundingBox(), Material.SAND);
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
    public void travel(float strafe, float vertical, float forward) {
        float f4;
        if (this.isBeingRidden() && this.canBeSteered()) {
            EntityLivingBase controller = (EntityLivingBase) this.getControllingPassenger();
            if (controller != null) {
                if (this.getAttackTarget() != null) {
                    this.setAttackTarget(null);
                    this.getNavigator().clearPath();
                }
                super.travel(strafe, vertical, forward);
                return;
            }
        }
        if (this.isServerWorld()) {
            float f5;
            if (this.isInSandStrict()) {
                this.moveRelative(strafe, vertical, forward, 0.1F);
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
                super.travel(strafe, vertical, forward);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        double deltaY = this.posY - this.prevPosY;
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
    public boolean canPhaseThroughBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.SAND;
    }

    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
        float hardness = blockStateIn.getBlockHardness(worldIn, pos);
        return hardness != -1.0F && hardness < 1.5F;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public boolean isRidingPlayer(EntityPlayer player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Nullable
    public EntityPlayer getRidingPlayer() {
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            return (EntityPlayer) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getRideSpeedModifier() {
        return 1;
    }

    public class SandMoveHelper extends EntityMoveHelper {
        private EntityDeathWorm worm = EntityDeathWorm.this;

        public SandMoveHelper() {
            super(EntityDeathWorm.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.worm.getNavigator().noPath() && !this.worm.isBeingRidden()) {
                double distanceX = this.posX - this.worm.posX;
                double distanceY = this.posY - this.worm.posY;
                double distanceZ = this.posZ - this.worm.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.worm.rotationYaw = this.limitAngle(this.worm.rotationYaw, angle, 30.0F);
                this.worm.setAIMoveSpeed(1F);
                this.worm.motionY += (double) this.worm.getAIMoveSpeed() * distanceY * 0.1D;
                if (distance < (double) Math.max(1.0F, this.entity.width)) {
                    float f = this.worm.rotationYaw * 0.017453292F;
                    this.worm.motionX -= MathHelper.sin(f) * 0.35F;
                    this.worm.motionZ += MathHelper.cos(f) * 0.35F;
                }
            } else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            } else {
                this.worm.setAIMoveSpeed(0.0F);
            }
        }
    }
}
