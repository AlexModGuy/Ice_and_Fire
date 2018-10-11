package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class EntityMyrmexBase extends EntityTameable implements IAnimatedEntity {

    private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityMyrmexBase.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> GROWTH_STAGE = EntityDataManager.<Integer>createKey(EntityMyrmexBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> VARIANT = EntityDataManager.<Boolean>createKey(EntityMyrmexBase.class, DataSerializers.BOOLEAN);
    private int animationTick;
    private Animation currentAnimation;
    MyrmexHive hive;
    public boolean isEnteringHive = false;
    public boolean isBeingGuarded = false;
    protected int growthTicks = 1;
    private static final ResourceLocation TEXTURE_DESERT_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_larva.png");
    private static final ResourceLocation TEXTURE_DESERT_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_pupa.png");
    private static final ResourceLocation TEXTURE_JUNGLE_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_larva.png");
    private static final ResourceLocation TEXTURE_JUNGLE_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_pupa.png");
    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);

    public EntityMyrmexBase(World worldIn) {
        super(worldIn);
    }

    public boolean canMove() {
        return this.getGrowthStage() > 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if(dmg == DamageSource.IN_WALL && this.getGrowthStage() < 2){
            return false;
        }
        if (this.getGrowthStage() < 2) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }
        return super.attackEntityFrom(dmg, i);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
    }

    public float getBlockPathWeight(BlockPos pos) {
        return 0.0F;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return super.createNavigator(worldIn);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
        this.dataManager.register(GROWTH_STAGE, Integer.valueOf(2));
        this.dataManager.register(VARIANT, Boolean.valueOf(false));
    }

    public void onUpdate() {
        super.onUpdate();
        if(this.getGrowthStage() < 2 && this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityMyrmexBase){
            float yaw = this.getRidingEntity().rotationYaw;
            this.rotationYaw = yaw;
            this.rotationYawHead = yaw;
            this.renderYawOffset = 0;
            this.prevRenderYawOffset = 0;
        }
        this.setScaleForAge(false);
        if (!this.world.isRemote) {
            if(this.getNavigator().getPath() != null && this.getNavigator().getPath().getFinalPathPoint() != null && this.getNavigator().getPath().getFinalPathPoint().y < this.posY || this.getNavigator().noPath()){
                this.setBesideClimbableBlock(false);
            }else{
                this.setBesideClimbableBlock(this.collidedHorizontally);
            }
        }
        if (this.getGrowthStage() < 2) {
            growthTicks++;
            if (growthTicks == 24000) {
                this.setGrowthStage(this.getGrowthStage() + 1);
                growthTicks = 0;
            }
        }
        if (!this.world.isRemote && this.getGrowthStage() < 2 && this.getRNG().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }

        if (this.getAttackTarget() != null && !(this.getAttackTarget() instanceof EntityPlayer) && this.getNavigator().noPath()) {
            this.setAttackTarget(null);
        }

        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("GrowthStage", this.getGrowthStage());
        tag.setInteger("GrowthTicks", growthTicks);
        tag.setBoolean("Variant", this.isJungle());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setGrowthStage(tag.getInteger("GrowthStage"));
        this.growthTicks = tag.getInteger("GrowthTicks");
        this.setJungleVariant(tag.getBoolean("Variant"));
    }

    public void setGrowthStage(int stage) {
        this.dataManager.set(GROWTH_STAGE, stage);
    }

    public int getGrowthStage() {
        return this.dataManager.get(GROWTH_STAGE).intValue();
    }

    public boolean isJungle() {
        return this.dataManager.get(VARIANT).booleanValue();
    }

    public void setJungleVariant(boolean isJungle) {
        this.dataManager.set(VARIANT, isJungle);
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isBesideClimbableBlock() {
        return (((Byte) this.dataManager.get(CLIMBING)).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = ((Byte) this.dataManager.get(CLIMBING)).byteValue();

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    public boolean isOnLadder() {
        return false;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
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
        return new Animation[]{ANIMATION_PUPA_WIGGLE};
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);

        if (this.hive != null && livingBase != null) {
            this.hive.addOrRenewAgressor(livingBase);

            if (livingBase instanceof EntityPlayer) {
                int i = -1;

                if (this.isChild()) {
                    i = -3;
                }

                this.hive.modifyPlayerReputation(livingBase.getUniqueID(), i);

                if (this.isEntityAlive()) {
                    this.world.setEntityState(this, (byte) 13);
                }
            }
        }
    }

    public void onDeath(DamageSource cause) {
        if (this.hive != null) {
            Entity entity = cause.getTrueSource();
            if (entity != null) {
                this.hive.modifyPlayerReputation(entity.getUniqueID(), -2);
            }
        }
        super.onDeath(cause);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.hive = MyrmexWorldData.get(world).getNearestVillage(this.getPosition(), 400);
        return livingdata;
    }

    public abstract boolean shouldLeaveHive();

    public abstract boolean shouldEnterHive();

    @Override
    public void setScaleForAge(boolean baby) {
        this.setScale(this.getGrowthStage() == 0 ? 0.5F : this.getGrowthStage() == 1 ? 0.75F : 1F);
    }

    public abstract ResourceLocation getAdultTexture();

    public abstract float getModelScale();

    public ResourceLocation getTexture() {
        if (this.getGrowthStage() == 0) {
            return isJungle() ? TEXTURE_JUNGLE_LARVA : TEXTURE_DESERT_LARVA;
        } else if (this.getGrowthStage() == 1) {
            return isJungle() ? TEXTURE_JUNGLE_PUPA : TEXTURE_DESERT_PUPA;
        } else {
            return getAdultTexture();
        }
    }

    public MyrmexHive getHive() {
        return hive;
    }

    public void setHive(MyrmexHive newHive) {
        hive = newHive;
    }

    public static boolean haveSameHive(EntityMyrmexBase myrmex, Entity entity) {
        if (entity instanceof EntityMyrmexBase) {
            if(myrmex.hive != null && ((EntityMyrmexBase) entity).hive != null){
                if (myrmex.isJungle() == ((EntityMyrmexBase) entity).isJungle()) {
                    return myrmex.hive.getCenter() == ((EntityMyrmexBase) entity).hive.getCenter();
                }
            }

        }
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {
        if (!haveSameHive(this, entityIn)) {
            entityIn.applyEntityCollision(this);
        }
    }

    public boolean canSeeSky() {
        return world.canBlockSeeSky(new BlockPos(this));
    }

    public static boolean isEdibleBlock(IBlockState blockState) {
        Block block = blockState.getBlock();
        return blockState.getMaterial() == Material.LEAVES || blockState.getMaterial() == Material.PLANTS || blockState.getMaterial() == Material.VINE || blockState.getMaterial() == Material.CACTUS || block instanceof BlockBush || block instanceof BlockCactus;
    }

    public boolean isOnResin() {
        IBlockState state = world.getBlockState(new BlockPos(this).down());
        IBlockState state2 = world.getBlockState(new BlockPos(this).down(2));
        return state.getBlock() instanceof BlockMyrmexResin || state2.getBlock() instanceof BlockMyrmexResin;
    }


    public boolean isInNursery() {
        if (hive != null && hive.getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty()) {
            return false;
        }
        BlockPos nursery = hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRNG(), this.getPosition());
        return this.getDistanceSqToCenter(nursery) < 45;
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (!this.canMove()) {
            strafe = 0;
            forward = 0;
            super.travel(strafe, forward, vertical);
            return;
        }
        super.travel(strafe, forward, vertical);
    }
}
