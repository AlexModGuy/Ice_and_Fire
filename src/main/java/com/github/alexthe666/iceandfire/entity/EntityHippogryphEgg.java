package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityHippogryphEgg extends ThrownEgg {

    private ItemStack itemstack;

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level world) {
        super(type, world);
    }

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level worldIn, double x, double y, double z,
                               ItemStack stack) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.itemstack = stack;
    }

    public EntityHippogryphEgg(EntityType<? extends ThrownEgg> type, Level worldIn, LivingEntity throwerIn,
                               ItemStack stack) {
        this(type, worldIn);
        this.setPos(throwerIn.getX(), throwerIn.getEyeY() - 0.1F, throwerIn.getZ());
        this.itemstack = stack;
        this.setOwner(throwerIn);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        Entity thrower = getOwner();
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().hurt(level().damageSources().thrown(this, thrower), 0.0F);
        }

        if (!this.level().isClientSide) {
            EntityHippogryph hippogryph = new EntityHippogryph(IafEntityRegistry.HIPPOGRYPH.get(), this.level());
            hippogryph.setAge(-24000);
            hippogryph.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            if (itemstack != null) {
                int variant = 0;
                CompoundTag tag = itemstack.getTag();
                if (tag != null) {
                    variant = tag.getInt("EggOrdinal");
                }
                hippogryph.setVariant(variant);
            }

            if (thrower instanceof Player) {
                hippogryph.tame((Player) thrower);
            }

            this.level().addFreshEntity(hippogryph);
        }

        this.level().broadcastEntityEvent(this, (byte) 3);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return IafItemRegistry.HIPPOGRYPH_EGG.get();
    }
}
