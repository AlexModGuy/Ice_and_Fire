package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nullable;

public class EntityMyrmexWorker extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final ResourceLocation DESERT_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "myrmex_worker_desert"));
    public static final ResourceLocation JUNGLE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "myrmex_worker_jungle"));
    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_worker.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_worker.png");
    public boolean keepSearching = true;

    public EntityMyrmexWorker(World worldIn) {
        super(worldIn);
        this.setSize(0.99F, 0.95F);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 3;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
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
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 1));
            }
        }
        if (!this.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            if (this.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemMyrmexEgg) {
                boolean isJungle = this.getHeldItem(EnumHand.MAIN_HAND).getItem() == IafItemRegistry.myrmex_jungle_egg;
                int metadata = this.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
                EntityMyrmexEgg egg = new EntityMyrmexEgg(world);
                egg.copyLocationAndAnglesFrom(this);
                egg.setJungle(isJungle);
                egg.setMyrmexCaste(metadata);
                if (!world.isRemote) {
                    world.spawnEntity(egg);
                }
                egg.startRiding(this);
                this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
        if (!this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof EntityMyrmexBase && ((EntityMyrmexBase) entity).getGrowthStage() >= 2) {
                    entity.dismountRidingEntity();
                }
            }
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new MyrmexAITradePlayer(this));
        this.tasks.addTask(0, new MyrmexAILookAtTradePlayer(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new MyrmexAIStoreBabies(this, 1.0D));
        this.tasks.addTask(3, new MyrmexAIStoreItems(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAIReEnterHive(this, 1.0D));
        this.tasks.addTask(4, new MyrmexAILeaveHive(this, 1.0D));
        this.tasks.addTask(6, new MyrmexAIForage(this));
        this.tasks.addTask(7, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.tasks.addTask(8, new MyrmexAIWander(this, 1D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new MyrmexAIDefendHive(this));
        this.targetTasks.addTask(2, new MyrmexAIForageForItems(this));
        this.targetTasks.addTask(3, new MyrmexAIPickupBabies(this));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new MyrmexAIAttackPlayers(this));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, true, new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving entity) {
                return entity != null && !IMob.VISIBLE_MOB_SELECTOR.apply(entity) && !EntityMyrmexBase.haveSameHive(EntityMyrmexWorker.this, entity) && DragonUtils.isAlive(entity);
            }
        }));

    }

    public boolean shouldWander() {
        return super.shouldWander() && this.canSeeSky();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.myrmexBaseAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 0.6F;
    }

    public boolean shouldLeaveHive() {
        return !holdingSomething();
    }

    public boolean shouldEnterHive() {
        return holdingSomething();
    }

    public boolean shouldMoveThroughHive() {
        return !holdingSomething();
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


    public boolean holdingSomething() {
        return this.getHeldEntity() != null || !this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() || this.getAttackTarget() != null;
    }

    public boolean holdingBaby() {
        return this.getHeldEntity() != null && (this.getHeldEntity() instanceof EntityMyrmexBase || this.getHeldEntity() instanceof EntityMyrmexEgg);
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            float radius = 1.05F;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            passenger.setPosition(this.posX + extraX, this.posY + 0.25F, this.posZ + extraZ);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (amount >= 1.0D && !this.world.isRemote && this.getRNG().nextInt(3) == 0 && this.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY && !properties.isStone) {
            this.entityDropItem(this.getHeldItem(EnumHand.MAIN_HAND), 0);
            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (amount >= 1.0D && !this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                entity.dismountRidingEntity();
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    public VillagerRegistry.VillagerProfession getProfessionForge() {
        return this.isJungle() ? IafVillagerRegistry.INSTANCE.jungleMyrmexWorker : IafVillagerRegistry.INSTANCE.desertMyrmexWorker;
    }

    public Entity getHeldEntity() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void onPickupItem(EntityItem itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (item == IafItemRegistry.myrmex_jungle_resin && this.isJungle() || item == IafItemRegistry.myrmex_desert_resin && !this.isJungle()) {

            EntityPlayer owner = null;
            try {
                owner = this.world.getPlayerEntityByName(itemEntity.getThrower());
            } catch (Exception e) {
                IceAndFire.logger.warn("Myrmex picked up resin that wasn't thrown!");
            }
            if (owner != null && this.getHive() != null) {
                this.getHive().modifyPlayerReputation(owner.getUniqueID(), 5);
                this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1, 1);
                if (!world.isRemote) {
                    world.spawnEntity(new EntityXPOrb(world, owner.posX, owner.posY, owner.posZ, 1 + rand.nextInt(3)));
                }
            }
        }
    }
}
