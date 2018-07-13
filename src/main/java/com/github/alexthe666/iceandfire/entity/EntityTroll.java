package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTroll extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WEAPON = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    public static final Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);
    public static final Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    public static final Animation ANIMATION_ROAR = Animation.create(25);
    public float stoneProgress;

    public EntityTroll(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
    }

    @Override
    public boolean isAIDisabled() {
        return EntityGorgon.isStoneMob(this) || super.isAIDisabled();
    }


    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this);
        return this.getRNG().nextInt(IceAndFire.CONFIG.trollSpawnCheckChance + 1) == 0 && !this.world.canSeeSky(pos) && pos.getY() <= 50 && super.getCanSpawnHere();
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new TrollAIFleeSun(this, 1.0D));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
        ((PathNavigateGround) this.getNavigator()).setAvoidSun(true);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.trollAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.trollMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(9.0D);

    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRNG().nextBoolean()) {
            this.setAnimation(ANIMATION_STRIKE_VERTICAL);

        } else {
            this.setAnimation(ANIMATION_STRIKE_HORIZONTAL);
        }
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(WEAPON, Integer.valueOf(0));
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public void setType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    public EnumTroll getType() {
        return EnumTroll.values()[getVariant()];
    }

    private int getWeapon() {
        return this.dataManager.get(WEAPON).intValue();
    }

    private void setWeapon(int variant) {
        this.dataManager.set(WEAPON, variant);
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    public EnumTroll.Weapon getWeaponType() {
        return EnumTroll.Weapon.values()[getWeapon()];
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("Weapon", this.getWeapon());
        compound.setFloat("StoneProgress", stoneProgress);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setWeapon(compound.getInteger("Weapon"));
        this.stoneProgress = compound.getFloat("StoneProgress");
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setType(EnumTroll.getBiomeType(world.getBiome(this.getPosition())));
        this.setWeaponType(EnumTroll.getWeaponForType(this.getType()));
        return livingdata;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source.damageType.contains("arrow")) {
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.deathTime == 20 && !this.world.isRemote) {
            switch(this.getType()){
                case MOUNTAIN:
                    if(this.getRNG().nextInt(3) == 0) {
                        dropItemAt(new ItemStack(Items.EMERALD, this.getRNG().nextInt(1), 0), this.posX, this.posY, this.posZ);
                    }
                    break;
                case FROST:
                    if(this.getRNG().nextInt(3) == 0) {
                        dropItemAt(new ItemStack(ModItems.sapphireGem, this.getRNG().nextInt(1), 0), this.posX, this.posY, this.posZ);
                    }
                    dropItemAt(new ItemStack(Items.SNOWBALL, this.getRNG().nextInt(3) + 1), this.posX, this.posY, this.posZ);
                    break;
                case FOREST:
                    dropItemAt(new ItemStack(Blocks.BROWN_MUSHROOM, this.getRNG().nextInt(3), 0), this.posX, this.posY, this.posZ);
                    dropItemAt(new ItemStack(Blocks.RED_MUSHROOM, this.getRNG().nextInt(3), 0), this.posX, this.posY, this.posZ);
                    break;
            }
            dropItemAt(new ItemStack(this.getType().leather, 1 + this.getRNG().nextInt(3), 0), this.posX, this.posY, this.posZ);
            dropItemAt(new ItemStack(ModItems.troll_tusk, this.getRNG().nextInt(2), 0), this.posX, this.posY, this.posZ);
            if(this.getRNG().nextInt(3) == 0){
                ItemStack weaponStack = new ItemStack(this.getWeaponType().item, 1, 0);
                weaponStack.attemptDamageItem(this.getRNG().nextInt(250), this.getRNG(), null);
                dropItemAt(weaponStack, this.posX, this.posY, this.posZ);
            }else{
                ItemStack brokenDrop = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 0);
                ItemStack brokenDrop2 = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 0);
                if(this.getWeaponType() == EnumTroll.Weapon.AXE){
                    brokenDrop = new ItemStack(Items.STICK, this.getRNG().nextInt(2) + 1, 0);
                    brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.getRNG().nextInt(2) + 1, 0);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.COLUMN){
                    brokenDrop = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 2);
                    brokenDrop2 = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 2);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.COLUMN_FOREST){
                    brokenDrop = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 1);
                    brokenDrop2 = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 2);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.COLUMN_FROST){
                    brokenDrop = new ItemStack(Blocks.STONEBRICK, this.getRNG().nextInt(2) + 1, 0);
                    brokenDrop2 = new ItemStack(Items.SNOWBALL, this.getRNG().nextInt(4) + 1);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.HAMMER){
                    brokenDrop = new ItemStack(Items.BONE, this.getRNG().nextInt(2) + 1, 0);
                    brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.getRNG().nextInt(2) + 1, 0);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.TRUNK){
                    brokenDrop = new ItemStack(Blocks.LOG, this.getRNG().nextInt(2) + 1, 0);
                    brokenDrop2 = new ItemStack(Blocks.LOG, this.getRNG().nextInt(2) + 1, 0);
                }
                if(this.getWeaponType() == EnumTroll.Weapon.TRUNK_FROST){
                    brokenDrop = new ItemStack(Blocks.LOG, this.getRNG().nextInt(4) + 1, 1);
                    brokenDrop2 = new ItemStack(Items.SNOWBALL, this.getRNG().nextInt(4) + 1);
                }
                dropItemAt(brokenDrop, this.posX, this.posY, this.posZ);
                dropItemAt(brokenDrop2, this.posX, this.posY, this.posZ);

            }
        }
    }

    @Nullable
    private EntityItem dropItemAt(ItemStack stack, double x, double y, double z) {
        if(stack.getCount() > 0){
            EntityItem entityitem = new EntityItem(this.world, x, y, z, stack);
            entityitem.setDefaultPickupDelay();
            if (captureDrops)
                this.capturedDrops.add(entityitem);
            else
                this.world.spawnEntity(entityitem);
            return entityitem;
        }
        return null;

    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        boolean stone = EntityGorgon.isStoneMob(this);
        if (stone && stoneProgress < 20.0F) {
            stoneProgress += 2F;
        } else if (!stone && stoneProgress > 0.0F) {
            stoneProgress -= 2F;
        }
        if(!stone && this.getAnimation() == NO_ANIMATION && this.getAttackTarget() != null && this.getRNG().nextInt(100) == 0){
            this.setAnimation(ANIMATION_ROAR);
        }
        if(this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5){
            this.playSound(ModSounds.TROLL_ROAR, 1, 1);
        }
        if (!stone && this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 30, 1, false, false));
        }
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);
            if (f > 0.5F && this.world.canSeeSky(blockpos)) {
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
                if (properties != null && !properties.isStone) {
                    properties.isStone = true;
                    this.motionX = 0;
                    this.motionY = 0;
                    this.motionZ = 0;
                    this.setAnimation(NO_ANIMATION);
                    this.playSound(ModSounds.GORGON_TURN_STONE, 1, 1);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
            float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
            float weaponY = (float) (posY + (0.2F));
            IBlockState state = world.getBlockState(new BlockPos(weaponX, weaponY - 1, weaponZ));
            for (int i = 0; i < 20; i++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                if (state.getMaterial().isSolid() && world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, weaponX + (this.getRNG().nextFloat() - 0.5F), weaponY + (this.getRNG().nextFloat() - 0.5F), weaponZ + (this.getRNG().nextFloat() - 0.5F), motionX, motionY, motionZ, new int[]{Block.getIdFromBlock(state.getBlock())});
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            float f1 = 0.5F;
            float f2 = this.getAttackTarget().moveForward;
            float f3 = 0.6F;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            this.getAttackTarget().motionX = f5;
            this.getAttackTarget().motionZ = f6;
            this.getAttackTarget().motionY = 0.4F;
        }
        if (this.getNavigator().noPath() && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 3 && this.getDistanceSq(this.getAttackTarget()) < 30 && this.world.getGameRules().getBoolean("mobGriefing")) {
            this.faceEntity(this.getAttackTarget(), 30, 30);
            if (this.getAnimation() == NO_ANIMATION && this.rand.nextInt(15) == 0) {
                this.setAnimation(ANIMATION_STRIKE_VERTICAL);
            }
            if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
                float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
                float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
                float weaponY = (float) (posY + (this.getEyeHeight() / 2));
                IBlockState state = world.getBlockState(new BlockPos(weaponX, weaponY, weaponZ));
                BlockBreakExplosion explosion = new BlockBreakExplosion(world, this, weaponX, weaponY, weaponZ, 1F + this.getRNG().nextFloat());
                explosion.doExplosionA();
                explosion.doExplosionB(true);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);

            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);

        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void playLivingSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playLivingSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
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

    @Nullable
    protected SoundEvent getAmbientSound() {
        return ModSounds.TROLL_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.TROLL_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return ModSounds.TROLL_DIE;
    }


    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL, ANIMATION_SPEAK, ANIMATION_ROAR};
    }
}
