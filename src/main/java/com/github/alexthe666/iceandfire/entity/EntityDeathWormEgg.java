package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class EntityDeathWormEgg extends EntityThrowable {

    private boolean giant;

    public EntityDeathWormEgg(World worldIn) {
        super(worldIn);
    }

    public EntityDeathWormEgg(World worldIn, EntityLivingBase throwerIn, boolean giant) {
        super(worldIn, throwerIn);
        this.giant = giant;
    }

    public EntityDeathWormEgg(World worldIn, double x, double y, double z, boolean giant) {
        super(worldIn, x, y, z);
        this.giant = giant;
    }

    public static void registerFixesEgg(DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEgg");
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(IafItemRegistry.deathworm_egg));
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

        if (!this.world.isRemote && this.thrower != null) {
            EntityDeathWorm deathworm = new EntityDeathWorm(this.world);
            deathworm.setVariant(new Random().nextInt(3));
            deathworm.setTamed(true);
            deathworm.setWormHome(new BlockPos(this));
            deathworm.setWormAge(1);
            deathworm.setDeathWormScale(giant ? (0.25F + (float) (Math.random() * 0.35F)) * 4 : 0.25F + (float) (Math.random() * 0.35F));
            deathworm.setOwnerId(this.thrower.getUniqueID());
            deathworm.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.world.spawnEntity(deathworm);
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }
}