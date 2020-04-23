package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nullable;

public class EntityMyrmexQueen extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Animation ANIMATION_EGG = Animation.create(20);
    public static final Animation ANIMATION_DIGNEST = Animation.create(45);
    public static final ResourceLocation DESERT_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "myrmex_queen_desert"));
    public static final ResourceLocation JUNGLE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "myrmex_queen_jungle"));
    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_queen.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_queen.png");
    private static final DataParameter<Boolean> HASMADEHOME = EntityDataManager.createKey(EntityMyrmexQueen.class, DataSerializers.BOOLEAN);
    private int eggTicks = 0;

    public EntityMyrmexQueen(World worldIn) {
        super(worldIn);
        this.setSize(2.9F, 1.86F);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 20;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HASMADEHOME, Boolean.valueOf(true));
    }

    public void setCustomNameTag(String name) {
        if (!this.getCustomNameTag().equals(name)) {
            if (this.getHive() != null) {
                this.getHive().colonyName = name;
            }
        }
        super.setCustomNameTag(name);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("EggTicks", eggTicks);
        tag.setBoolean("MadeHome", this.hasMadeHome());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.eggTicks = tag.getInteger("EggTicks");
        this.setMadeHome(tag.getBoolean("MadeHome"));
    }

    public boolean hasMadeHome() {
        return this.dataManager.get(HASMADEHOME).booleanValue();
    }

    public void setMadeHome(boolean madeHome) {
        this.dataManager.set(HASMADEHOME, madeHome);
    }

    public VillagerRegistry.VillagerProfession getProfessionForge() {
        return this.isJungle() ? IafVillagerRegistry.INSTANCE.jungleMyrmexQueen : IafVillagerRegistry.INSTANCE.desertMyrmexQueen;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAnimation() == ANIMATION_DIGNEST) {
            spawnGroundEffects(3);
        }
        if (this.getHive() != null) {
            this.getHive().tick(0, world);
        }

        if (hasMadeHome() && this.getGrowthStage() >= 2 && !this.canSeeSky()) {
            eggTicks++;
        } else if (this.canSeeSky()) {
            this.setAnimation(ANIMATION_DIGNEST);
            if (this.getAnimationTick() == 42) {
                int down = Math.max(15, this.getPosition().getY() - 20 + this.getRNG().nextInt(10));
                BlockPos genPos = new BlockPos(this.posX, down, this.posZ);
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, genPos.getX(), genPos.getY(), genPos.getZ()))){
                    WorldGenMyrmexHive hiveGen = new WorldGenMyrmexHive(true, this.isJungle());
                    hiveGen.generate(world, this.getRNG(), genPos);
                    this.setMadeHome(true);
                    this.setLocationAndAngles(genPos.getX(), down, genPos.getZ(), 0, 0);
                    this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 30));
                    this.setHive(hiveGen.hive);
                    for (int i = 0; i < 3; i++) {
                        EntityMyrmexWorker worker = new EntityMyrmexWorker(world);
                        worker.copyLocationAndAnglesFrom(this);
                        worker.setHive(this.getHive());
                        worker.setJungleVariant(this.isJungle());
                        if (!world.isRemote) {
                            world.spawnEntity(worker);
                        }
                    }
                    return;
                }
            }
        }
        if (!world.isRemote && eggTicks > IceAndFire.CONFIG.myrmexPregnantTicks && this.getHive() == null || !world.isRemote && this.getHive() != null && this.getHive().repopulate() && eggTicks > IceAndFire.CONFIG.myrmexPregnantTicks) {
            float radius = -5.25F;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            BlockPos eggPos = new BlockPos(this.posX + extraX, this.posY + 0.75F, this.posZ + extraZ);
            if (world.isAirBlock(eggPos)) {
                this.setAnimation(ANIMATION_EGG);
                if (this.getAnimationTick() == 10) {
                    EntityMyrmexEgg egg = new EntityMyrmexEgg(this.world);
                    egg.setJungle(this.isJungle());
                    int caste = getRandomCaste(world, this.getRNG(), getHive() == null || getHive().reproduces);
                    egg.setMyrmexCaste(caste);
                    egg.setLocationAndAngles(this.posX + extraX, this.posY + 0.75F, this.posZ + extraZ, 0, 0);
                    if (getHive() != null) {
                        egg.hiveUUID = this.getHive().hiveUUID;
                    }
                    if (!world.isRemote) {
                        world.spawnEntity(egg);
                    }
                    eggTicks = 0;
                }

            }


        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getAttackTarget().getEntityBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            if (this.getAttackBounds().intersects(this.getAttackTarget().getEntityBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
                this.getAttackTarget().isAirBorne = true;
                float f = MathHelper.sqrt(0.5 * 0.5 + 0.5 * 0.5);
                this.getAttackTarget().motionX /= 2.0D;
                this.getAttackTarget().motionZ /= 2.0D;
                this.getAttackTarget().motionX -= 0.5 / (double) f * 4;
                this.getAttackTarget().motionZ -= 0.5 / (double) f * 4;

                if (this.getAttackTarget().onGround) {
                    this.getAttackTarget().motionY /= 2.0D;
                    this.getAttackTarget().motionY += 4;

                    if (this.getAttackTarget().motionY > 0.4000000059604645D) {
                        this.getAttackTarget().motionY = 0.4000000059604645D;
                    }
                }
            }
        }

    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source) || this.getAnimation() == ANIMATION_DIGNEST;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new MyrmexAITradePlayer(this));
        this.tasks.addTask(0, new MyrmexAILookAtTradePlayer(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(3, new MyrmexAIReEnterHive(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAIWanderHiveCenter(this, 1.0D));
        this.tasks.addTask(5, new MyrmexQueenAIWander(this, 1D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new MyrmexAIDefendHive(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new MyrmexAIAttackPlayers(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, true, new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && !IMob.VISIBLE_MOB_SELECTOR.apply(entity) && !EntityMyrmexBase.haveSameHive(EntityMyrmexQueen.this, entity) && DragonUtils.isAlive(entity);
            }
        }));

    }

    public void fall(float distance, float damageMultiplier) {
    }

    public boolean shouldMoveThroughHive() {
        return false;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.myrmexBaseAttackStrength * 3.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 1.75F;
    }

    @Override
    public int getCasteImportance() {
        return 3;
    }

    public boolean shouldLeaveHive() {
        return false;
    }

    public boolean shouldEnterHive() {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            if (!this.world.isRemote && this.getRNG().nextInt(3) == 0 && this.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY) {
                this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
                this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            if (!this.getPassengers().isEmpty()) {
                for (Entity entity : this.getPassengers()) {
                    entity.dismountRidingEntity();
                }
            }
            return true;
        }
        return false;
    }

    public boolean canMove() {
        return super.canMove() && this.hasMadeHome();
    }

    public void spawnGroundEffects(float size) {
        for (int i = 0; i < size * 3; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = size * rand.nextFloat();
                float angle = (0.01745329251F * this.renderYawOffset) * 3.14F * rand.nextFloat();
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * MathHelper.cos(angle));

                IBlockState iblockstate = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX + extraX), MathHelper.floor(this.posY + extraY) - 1, MathHelper.floor(this.posZ + extraZ)));
                if (iblockstate.getMaterial() != Material.AIR) {
                    if (world.isRemote) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.posX + extraX, this.posY + extraY, this.posZ + extraZ, motionX, motionY, motionZ, Block.getStateId(iblockstate));
                    }
                }
            }
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING, ANIMATION_EGG, ANIMATION_DIGNEST};
    }
}
