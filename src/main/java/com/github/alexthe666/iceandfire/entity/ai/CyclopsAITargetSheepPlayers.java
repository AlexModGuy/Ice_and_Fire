package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CyclopsAITargetSheepPlayers<T extends EntityLivingBase> extends EntityAITarget {
    protected final Class<T> targetClass;
    protected final CyclopsAITargetSheepPlayers.Sorter sorter;
    protected final Predicate<? super T> targetEntitySelector;
    private final int targetChance;
    protected T targetEntity;

    public CyclopsAITargetSheepPlayers(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, checkSight, true);
    }

    public CyclopsAITargetSheepPlayers(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }

    public CyclopsAITargetSheepPlayers(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.sorter = new CyclopsAITargetSheepPlayers.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>() {
            public boolean apply(@Nullable T p_apply_1_) {
                if (p_apply_1_ == null) {
                    return false;
                } else if (targetSelector != null && !targetSelector.apply(p_apply_1_)) {
                    return false;
                } else {
                    return EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) && CyclopsAITargetSheepPlayers.this.isSuitableTarget(p_apply_1_, false);
                }
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class) {
            List<T> list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.sorter);
                this.targetEntity = list.get(0);
                return true;
            }
        } else {
            this.targetEntity = (T) this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + (double) this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), new Function<EntityPlayer, Double>() {
                @Nullable
                public Double apply(@Nullable EntityPlayer player) {
                    ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                    ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                    double subHelm = helmet != null && helmet.getItem() != null && helmet.getItem() == IafItemRegistry.sheep_helmet ? 0.2D : 0;
                    double subChest = chestplate != null && chestplate.getItem() != null && chestplate.getItem() == IafItemRegistry.sheep_chestplate ? 0.2D : 0;
                    double subLegs = leggings != null && leggings.getItem() != null && leggings.getItem() == IafItemRegistry.sheep_leggings ? 0.2D : 0;
                    double subBoots = boots != null && boots.getItem() != null && boots.getItem() == IafItemRegistry.sheep_boots ? 0.2D : 0;
                    double subSneaking = player.isSneaking() ? 0.2D : 0;
                    return 1.0D - subHelm - subChest - subLegs - subBoots - subSneaking;
                }
            }, (Predicate<EntityPlayer>) this.targetEntitySelector);
            return this.targetEntity != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        public Sorter(Entity entityIn) {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1) {
                return -1;
            } else {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}