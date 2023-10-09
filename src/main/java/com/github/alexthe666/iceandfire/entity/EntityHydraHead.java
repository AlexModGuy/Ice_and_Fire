package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class EntityHydraHead extends EntityMutlipartPart {
    public int headIndex;
    public EntityHydra hydra;
    private boolean neck;

    public EntityHydraHead(EntityType<?> t, Level world) {
        super(t, world);
    }

    public EntityHydraHead(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.HYDRA_MULTIPART.get(), worldIn);
    }

    public EntityHydraHead(EntityHydra entity, float radius, float angle, float y, float width, float height, float damageMulti, int headIndex, boolean neck) {
        super(IafEntityRegistry.HYDRA_MULTIPART.get(), entity, radius, angle, y, width, height, damageMulti);
        this.headIndex = headIndex;
        this.neck = neck;
        this.hydra = entity;
    }

    @Override
    public void tick() {
        super.tick();
        if (hydra != null && hydra.getSeveredHead() != -1 && this.neck && !EntityGorgon.isStoneMob(hydra)) {
            if (hydra.getSeveredHead() == headIndex) {
                if (this.level().isClientSide) {
                    for (int k = 0; k < 5; ++k) {
                        double d2 = 0.4;
                        double d0 = 0.1;
                        double d1 = 0.1;
                        IceAndFire.PROXY.spawnParticle(EnumParticles.Blood, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, this.getY() - 0.5D, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, d2, d0, d1);
                    }
                }
            }
        }
    }


    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity parent = this.getParent();
        if (parent instanceof EntityHydra) {
            ((EntityHydra) parent).onHitHead(damage, headIndex);
            return parent.hurt(source, damage);
        } else {
            return parent != null && parent.hurt(source, damage);
        }
    }


}
