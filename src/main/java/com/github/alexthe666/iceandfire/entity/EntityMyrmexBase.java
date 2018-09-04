package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
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
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class EntityMyrmexBase extends EntityTameable implements IAnimatedEntity {

    private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityMyrmexBase.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> GROWTH_STAGE = EntityDataManager.<Integer>createKey(EntityMyrmexBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> VARIANT = EntityDataManager.<Boolean>createKey(EntityMyrmexBase.class, DataSerializers.BOOLEAN);
    private int animationTick;
    private Animation currentAnimation;
    MyrmexHive hive;
    private int randomTickDivider;
    private boolean isLookingForHome;
    protected int growthTicks = 1;
    private static final ResourceLocation TEXTURE_DESERT_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_larva.png");
    private static final ResourceLocation TEXTURE_DESERT_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_pupa.png");
    private static final ResourceLocation TEXTURE_JUNGLE_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_larva.png");
    private static final ResourceLocation TEXTURE_JUNGLE_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_pupa.png");
    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);
    public EntityMyrmexBase(World worldIn) {
        super(worldIn);
    }

    public boolean isAIDisabled(){
        return this.getGrowthStage() < 2 || super.isAIDisabled();
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if(this.getGrowthStage() < 2){
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
        return new PathNavigateClimber(this, worldIn);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
        this.dataManager.register(GROWTH_STAGE, Integer.valueOf(2));
        this.dataManager.register(VARIANT, Boolean.valueOf(false));
    }

    public void onUpdate() {
        super.onUpdate();

        this.setScaleForAge(false);
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
        if(this.getGrowthStage() < 2){
            growthTicks++;
            if(growthTicks == 24000){
                this.setGrowthStage(this.getGrowthStage() + 1);
                growthTicks = 0;
            }
        }
        if(!this.world.isRemote  && this.getGrowthStage() < 2 && this.getRNG().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION){
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
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

    public boolean isOnLadder(){
        return isBesideClimbableBlock();
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

    protected void updateAITasks() {
        super.updateAITasks();
        if (--this.randomTickDivider <= 0) {
            BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.hive = MyrmexWorldData.get(world).getNearestVillage(blockpos, 32);

            if (this.hive == null) {
                this.detachHome();
            } else {
                BlockPos blockpos1 = this.hive.getCenter();
                this.setHomePosAndDistance(blockpos1, this.hive.getVillageRadius());

                if (this.isLookingForHome) {
                    this.isLookingForHome = false;
                    this.hive.setDefaultPlayerReputation(5);
                }
            }
        }
    }

    public void setLookingForHome() {
        this.isLookingForHome = true;
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
        if(this.getGrowthStage() == 0){
            return isJungle() ? TEXTURE_JUNGLE_LARVA : TEXTURE_DESERT_LARVA;
        } else if(this.getGrowthStage() == 1){
            return isJungle() ? TEXTURE_JUNGLE_PUPA : TEXTURE_DESERT_PUPA;
        }else{
            return getAdultTexture();
        }
    }

    public MyrmexHive getHive() {
        return hive;
    }

    public void setHive(MyrmexHive newHive) {
        hive = newHive;
    }

    public static boolean haveSameHive(EntityMyrmexBase myrmex, Entity entity){
        if(entity instanceof EntityMyrmexBase){
            if(myrmex.isJungle() == ((EntityMyrmexBase)entity).isJungle()){
                return  myrmex.hive == ((EntityMyrmexBase)entity).hive;
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
        return blockState.getMaterial() == Material.LEAVES || blockState.getMaterial() == Material.PLANTS;
    }

    public boolean isOnResin(){
        IBlockState state = world.getBlockState(new BlockPos(this).down());
        return state.getBlock() instanceof BlockMyrmexResin;
    }
}
