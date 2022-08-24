package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PixieAISteal extends Goal {
    private final EntityPixie temptedEntity;
    private PlayerEntity temptingPlayer;
    private int delayTemptCounter = 0;
    private boolean isRunning;

    public PixieAISteal(EntityPixie temptedEntityIn, double speedIn) {
        this.temptedEntity = temptedEntityIn;
    }

    @Override
    public boolean canUse() {
        if (!IafConfig.pixiesStealItems || !temptedEntity.getMainHandItem().isEmpty() || temptedEntity.stealCooldown > 0) {
            return false;
        }
        if (temptedEntity.getRandom().nextInt(200) == 0) {
            return false;
        }
        if (temptedEntity.isTame()) {
            return false;
        }
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.temptingPlayer = this.temptedEntity.level.getNearestPlayer(this.temptedEntity, 10.0D);
            return this.temptingPlayer != null && (this.temptedEntity.getItemInHand(Hand.MAIN_HAND).isEmpty() && !this.temptingPlayer.inventory.isEmpty() && !this.temptingPlayer.isCreative());
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !temptedEntity.isTame() && temptedEntity.getMainHandItem().isEmpty() && this.delayTemptCounter == 0 && temptedEntity.stealCooldown == 0;
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.temptingPlayer = null;
        if (this.delayTemptCounter < 10)
            this.delayTemptCounter += 10;
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.temptedEntity.getLookControl().setLookAt(this.temptingPlayer, this.temptedEntity.getMaxHeadYRot() + 20, this.temptedEntity.getMaxHeadXRot());
        ArrayList<Integer> slotlist = new ArrayList<>();
        if (this.temptedEntity.distanceToSqr(this.temptingPlayer) < 3D && !this.temptingPlayer.inventory.isEmpty()) {

            for (int i = 0; i < this.temptingPlayer.inventory.getContainerSize(); i++) {
                ItemStack targetStack = this.temptingPlayer.inventory.getItem(i);
                if (!PlayerInventory.isHotbarSlot(i) && !targetStack.isEmpty() && targetStack.isStackable()) {
                    slotlist.add(i);
                }
            }
            if (!slotlist.isEmpty()) {
                final int slot;
                if (slotlist.size() == 1) {
                    slot = slotlist.get(0);
                } else {
                    slot = slotlist.get(ThreadLocalRandom.current().nextInt(slotlist.size()));
                }
                ItemStack randomItem = this.temptingPlayer.inventory.getItem(slot);
                this.temptedEntity.setItemInHand(Hand.MAIN_HAND, randomItem);
                this.temptingPlayer.inventory.removeItemNoUpdate(slot);
                this.temptedEntity.flipAI(true);
                this.temptedEntity.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);

                for (EntityPixie pixie : this.temptingPlayer.level.getEntitiesOfClass(EntityPixie.class, temptedEntity.getBoundingBox().inflate(40))) {
                    pixie.stealCooldown = 1000 + pixie.getRandom().nextInt(3000);
                }
                if (temptingPlayer != null) {
                    this.temptingPlayer.addEffect(new EffectInstance(this.temptedEntity.negativePotions[this.temptedEntity.getColor()], 100));
                }
            } else {
                //If the pixie couldn't steal anything
                this.temptedEntity.flipAI(true);
                this.delayTemptCounter = 10 *20;
            }
        } else {
            this.temptedEntity.getMoveControl().setWantedPosition(this.temptingPlayer.getX(), this.temptingPlayer.getY() + 1.5F, this.temptingPlayer.getZ(), 1D);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}