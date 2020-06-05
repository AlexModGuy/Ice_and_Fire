package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityHydraHead extends EntityMutlipartPart {
    public int headIndex;
    public EntityHydra hydra;
    private boolean neck;

    public EntityHydraHead(EntityType t, World world) {
        super(t, world);
    }

    public EntityHydraHead(EntityHydra entity, float radius, float angle, float y, float width, float height, float damageMulti, int headIndex, boolean neck) {
        super(IafEntityRegistry.HYDRA_MULTIPART, entity, radius, angle, y, width, height, damageMulti);
        this.headIndex = headIndex;
        this.neck = neck;
        this.hydra = entity;
    }

    @Override
    public void tick() {
        super.tick();
        if(hydra != null && hydra.getSeveredHead() != -1 && this.neck && !EntityGorgon.isStoneMob(hydra)){
            if(hydra.getSeveredHead() == headIndex){
                if (this.world.isRemote) {
                    for (int k = 0; k < 5; ++k) {
                        double d2 = 0.4;
                        double d0 = 0.1;
                        double d1 = 0.1;
                        IceAndFire.PROXY.spawnParticle("blood", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, this.getPosY() - 0.5D, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, d2, d0, d1);
                    }
                }
            }
        }
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.parent instanceof EntityHydra) {
            ((EntityHydra) this.parent).onHitHead(damage, headIndex);
            return this.parent.attackEntityFrom(source, damage);
        } else {
            return this.parent.attackEntityFrom(source, damage);
        }
    }
}
