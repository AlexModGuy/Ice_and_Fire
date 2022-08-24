package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;


public class EntityStoneStatue extends LivingEntity implements IBlacklistedFromStatues {

    public boolean smallArms;
    private static final DataParameter<String> TRAPPED_ENTITY_TYPE = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.STRING);
    private static final DataParameter<CompoundNBT> TRAPPED_ENTITY_DATA = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.COMPOUND_TAG);
    private static final DataParameter<Float> TRAPPED_ENTITY_WIDTH = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TRAPPED_ENTITY_HEIGHT = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> TRAPPED_ENTITY_SCALE = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> CRACK_AMOUNT = EntityDataManager.defineId(EntityStoneStatue.class, DataSerializers.INT);
    private EntitySize stoneStatueSize = EntitySize.fixed(0.5F, 0.5F);

    public EntityStoneStatue(EntityType<? extends LivingEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 20)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.0D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRAPPED_ENTITY_TYPE, "minecraft:pig");
        this.entityData.define(TRAPPED_ENTITY_DATA, new CompoundNBT());
        this.entityData.define(TRAPPED_ENTITY_WIDTH, 0.5F);
        this.entityData.define(TRAPPED_ENTITY_HEIGHT, 0.5F);
        this.entityData.define(TRAPPED_ENTITY_SCALE, 1F);
        this.entityData.define(CRACK_AMOUNT, 0);
    }

    public EntityType getTrappedEntityType() {
        String str = getTrappedEntityTypeString();
        return EntityType.byString(str).orElse(EntityType.PIG);
    }


    public String getTrappedEntityTypeString() {
        return this.entityData.get(TRAPPED_ENTITY_TYPE);
    }

    public void setTrappedEntityTypeString(String string) {
        this.entityData.set(TRAPPED_ENTITY_TYPE, string);
    }

    public CompoundNBT getTrappedTag() {
        return this.entityData.get(TRAPPED_ENTITY_DATA);
    }

    public void setTrappedTag(CompoundNBT tag) {
        this.entityData.set(TRAPPED_ENTITY_DATA, tag);
    }

    public float getTrappedWidth() {
        return this.entityData.get(TRAPPED_ENTITY_WIDTH);
    }

    public void setTrappedEntityWidth(float size) {
        this.entityData.set(TRAPPED_ENTITY_WIDTH, size);
    }

    public float getTrappedHeight() {
        return this.entityData.get(TRAPPED_ENTITY_HEIGHT);
    }

    public void setTrappedHeight(float size) {
        this.entityData.set(TRAPPED_ENTITY_HEIGHT, size);
    }

    public float getTrappedScale() {
        return this.entityData.get(TRAPPED_ENTITY_SCALE);
    }

    public void setTrappedScale(float size) {
        this.entityData.set(TRAPPED_ENTITY_SCALE, size);
    }

    public static EntityStoneStatue buildStatueEntity(LivingEntity parent){
        EntityStoneStatue statue = IafEntityRegistry.STONE_STATUE.get().create(parent.level);
        CompoundNBT entityTag = new CompoundNBT();
        try {
            if (!(parent instanceof PlayerEntity)) {
                parent.saveWithoutId(entityTag);
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.debug("Encountered issue creating stone statue from {}", parent);
        }
        statue.setTrappedTag(entityTag);
        statue.setTrappedEntityTypeString(ForgeRegistries.ENTITIES.getKey(parent.getType()).toString());
        statue.setTrappedEntityWidth(parent.getBbWidth());
        statue.setTrappedHeight(parent.getBbHeight());
        statue.setTrappedScale(parent.getScale());

        return statue;
    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getCollisionBoundingBox();
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CrackAmount", this.getCrackAmount());
        tag.putFloat("StatueWidth", this.getTrappedWidth());
        tag.putFloat("StatueHeight", this.getTrappedHeight());
        tag.putFloat("StatueScale", this.getTrappedScale());
        tag.putString("StatueEntityType", this.getTrappedEntityTypeString());
        tag.put("StatueEntityTag", this.getTrappedTag());
    }

    @Override
    public float getScale() {
        return this.getTrappedScale();
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.setCrackAmount(tag.getByte("CrackAmount"));
        this.setTrappedEntityWidth(tag.getFloat("StatueWidth"));
        this.setTrappedHeight(tag.getFloat("StatueHeight"));
        this.setTrappedScale(tag.getFloat("StatueScale"));
        this.setTrappedEntityTypeString(tag.getString("StatueEntityType"));
        if (tag.contains("StatueEntityTag")) {
            this.setTrappedTag(tag.getCompound("StatueEntityTag"));

        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source == DamageSource.OUT_OF_WORLD;
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return stoneStatueSize;
    }

    @Override
    public void tick() {
        super.tick();
        this.yRot = this.yBodyRot;
        this.yHeadRot = this.yRot;
        if (Math.abs(this.getBbWidth() - getTrappedWidth()) > 0.01 || Math.abs(this.getBbHeight() - getTrappedHeight()) > 0.01) {
            double prevX = this.getX();
            double prevZ = this.getZ();
            this.stoneStatueSize = EntitySize.scalable(getTrappedWidth(), getTrappedHeight());
            refreshDimensions();
            this.setPos(prevX, this.getY(), prevZ);
        }
    }

    @Override
    public void kill() {
        this.remove();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    public int getCrackAmount() {
        return this.entityData.get(CRACK_AMOUNT);
    }

    public void setCrackAmount(int crackAmount) {
        this.entityData.set(CRACK_AMOUNT, crackAmount);
    }


    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }
}
