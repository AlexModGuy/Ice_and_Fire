package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityDreadThrall extends EntityMob implements IDreadMob, IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_DIE = Animation.create(40);
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadThrall(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.8F);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setAnimation(ANIMATION_SPAWN);
        if(this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30){
            Block belowBlock = world.getBlockState(this.getPosition().down()).getBlock();
            if(belowBlock != Blocks.AIR){
                for(int i = 0; i < 5; i++)
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getEntityBoundingBox().minY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(belowBlock));
            }

        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_DIE};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }
}
