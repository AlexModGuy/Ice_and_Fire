package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import javax.annotation.Nullable;
import java.util.List;

public class EntityTideTrident extends TridentEntity{

    private ItemStack thrownStack;

    public EntityTideTrident(EntityType type, World worldIn) {
        super(type, worldIn);
        thrownStack = new ItemStack(IafItemRegistry.TIDE_TRIDENT);
    }

    public EntityTideTrident(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(worldIn, thrower, thrownStackIn);
        thrownStack = thrownStackIn;
    }

    public EntityTideTrident(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        thrownStack = new ItemStack(IafItemRegistry.TIDE_TRIDENT);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingentity.getCreatureAttribute());
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource = DamageSource.causeTridentDamage(this, (Entity)(entity1 == null ? this : entity1));
        //this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity1, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity1);
                }

                this.arrowHit(livingentity1);
            }
        }

        this.setMotion(this.getMotion().mul(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.getPosition();
            if (this.world.canSeeSky(blockpos)) {
                LightningBoltEntity lightningboltentity = new LightningBoltEntity(this.world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D, false);
                lightningboltentity.setCaster(entity1 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity1 : null);
                ((ServerWorld)this.world).addLightningBolt(lightningboltentity);
                soundevent = SoundEvents.ITEM_TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }
}