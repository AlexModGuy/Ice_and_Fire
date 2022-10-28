package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.tile.RenderGorgonHead;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.NonNullLazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ItemGorgonHead extends Item implements IItemRenderProperties {

    public ItemGorgonHead() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).durability(1));
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {

        consumer.accept(new IItemRenderProperties() {
            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new RenderGorgonHead(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int timeLeft) {
        double dist = 32;
        Vec3 Vector3d = entity.getEyePosition(1.0F);
        Vec3 Vector3d1 = entity.getViewVector(1.0F);
        Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist);
        double d1 = dist;
        Entity pointedEntity = null;
        List<Entity> list = worldIn.getEntities(entity, entity.getBoundingBox().expandTowards(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist).inflate(1.0D, 1.0D, 1.0D), new Predicate<Entity>() {
            public boolean apply(@Nullable Entity entity) {
                boolean blindness = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.BLINDNESS) || (entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) entity).canBeTurnedToStone());
                return entity != null && entity.isPickable() && !blindness && (entity instanceof Player || (entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity)));
            }
        });
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);
            AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(Vector3d, Vector3d2);

            if (axisalignedbb.contains(Vector3d)) {
                if (d2 >= 0.0D) {
                    //pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else if (optional.isPresent()) {
                double d3 = Vector3d.distanceTo(optional.get());

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == entity.getRootVehicle() && !entity.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                        }
                    } else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }
        if (pointedEntity != null) {
            if (pointedEntity instanceof LivingEntity) {
                pointedEntity.playSound(IafSoundRegistry.TURN_STONE, 1, 1);
                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity((LivingEntity) pointedEntity);
                if (pointedEntity instanceof Player) {
                    pointedEntity.hurt(IafDamageRegistry.causeGorgonDamage(pointedEntity), Integer.MAX_VALUE);
                } else {
                    if (!worldIn.isClientSide)
                        pointedEntity.remove(Entity.RemovalReason.KILLED);
                }
                statue.absMoveTo(pointedEntity.getX(), pointedEntity.getY(), pointedEntity.getZ(), pointedEntity.getYRot(), pointedEntity.getXRot());
                statue.yBodyRot = pointedEntity.getYRot();
                if (!worldIn.isClientSide) {
                    worldIn.addFreshEntity(statue);
                }
                if (entity instanceof Player && !((Player) entity).isCreative()) {
                    stack.shrink(1);
                }
            }
        }
        stack.getTag().putBoolean("Active", false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        playerIn.startUsingItem(hand);
        itemStackIn.getTag().putBoolean("Active", true);
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
    }
}
