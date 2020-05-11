package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCockatriceEgg extends EggEntity {

    public EntityCockatriceEgg(World worldIn) {
        super(worldIn);
    }

    public EntityCockatriceEgg(World worldIn, LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityCockatriceEgg(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(IafItemRegistry.ROTTEN_EGG));
            }
            if (thrower != null && thrower instanceof PlayerEntity) {
                for (int i = 0; i < 8; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.HEART, this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
                }
            }
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote) {
            if (this.rand.nextInt(4) == 0) {
                int i = 1;

                if (this.rand.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    EntityCockatrice cockatrice = new EntityCockatrice(this.world);
                    cockatrice.setGrowingAge(-24000);
                    cockatrice.setHen(this.rand.nextBoolean());
                    cockatrice.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    if (thrower != null && thrower instanceof PlayerEntity) {
                        cockatrice.setTamedBy((PlayerEntity) thrower);
                    }
                    this.world.spawnEntity(cockatrice);
                }
            }

            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }
}
