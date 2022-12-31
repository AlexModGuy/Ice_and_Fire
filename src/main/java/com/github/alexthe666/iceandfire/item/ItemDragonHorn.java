package com.github.alexthe666.iceandfire.item;


import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHorn extends Item {

    public ItemDragonHorn() {
        super((new Item.Properties()).tab(IceAndFire.TAB_ITEMS).stacksTo(1));
    }

    public static int getDragonType(ItemStack stack) {
        if (stack.getTag() != null) {
            String id = stack.getTag().getString("DragonHornEntityID");
            if (EntityType.byString(id).isPresent()) {
                EntityType entityType = EntityType.byString(id).get();
                if (entityType == IafEntityRegistry.FIRE_DRAGON.get())
                    return 1;

                if (entityType == IafEntityRegistry.ICE_DRAGON.get())
                    return 2;

                if (entityType == IafEntityRegistry.LIGHTNING_DRAGON.get())
                    return 3;
            }
        }

        return 0;
    }


    @Override
    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        itemStack.setTag(new CompoundTag());
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        ItemStack trueStack = playerIn.getItemInHand(hand);
        if (!playerIn.level.isClientSide && hand == InteractionHand.MAIN_HAND && target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwnedBy(playerIn) && (trueStack.getTag() == null || (trueStack.getTag() != null && trueStack.getTag().getCompound("EntityTag").isEmpty()))) {
            CompoundTag newTag = new CompoundTag();

            CompoundTag entityTag = new CompoundTag();
            target.save(entityTag);
            newTag.put("EntityTag", entityTag);

            newTag.putString("DragonHornEntityID", Registry.ENTITY_TYPE.getKey(target.getType()).toString());
            trueStack.setTag(newTag);

            playerIn.swing(hand);
            playerIn.level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 3.0F, 0.75F);
            target.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        if (stack.getTag() != null && !stack.getTag().getString("DragonHornEntityID").isEmpty()) {
            Level world = context.getLevel();
            String id = stack.getTag().getString("DragonHornEntityID");
            EntityType type = EntityType.byString(id).orElse(null);
            if (type != null) {
                Entity entity = type.create(world);
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    dragon.load(stack.getTag().getCompound("EntityTag"));
                }
                //Still needed to allow for intercompatibility
                if (stack.getTag().contains("EntityUUID"))
                    entity.setUUID(stack.getTag().getUUID("EntityUUID"));

                entity.absMoveTo(context.getClickedPos().getX() + 0.5D, (context.getClickedPos().getY() + 1), context.getClickedPos().getZ() + 0.5D, 180 + (context.getHorizontalDirection()).toYRot(), 0.0F);
                if (world.addFreshEntity(entity)) {
                    CompoundTag tag = stack.getTag();
                    tag.remove("DragonHornEntityID");
                    tag.remove("EntityTag");
                    tag.remove("EntityUUID");
                    stack.setTag(tag);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            CompoundTag entityTag = stack.getTag().getCompound("EntityTag");
            if (!entityTag.isEmpty()) {
                String id = stack.getTag().getString("DragonHornEntityID");
                if (EntityType.byString(id).isPresent()) {
                    EntityType type = EntityType.byString(id).get();
                    tooltip.add((new TranslatableComponent(type.getDescriptionId())).withStyle(getTextColorForEntityType(type)));
                    String name = (new TranslatableComponent("dragon.unnamed")).getString();
                    if (!entityTag.getString("CustomName").isEmpty()) {
                        MutableComponent component = Component.Serializer.fromJson(entityTag.getString("CustomName"));
                        if (component != null)
                            name = component.getString();
                    }

                    tooltip.add((new TextComponent(name)).withStyle(ChatFormatting.GRAY));
                    String gender = (new TranslatableComponent("dragon.gender")).getString() + " " + (new TranslatableComponent(entityTag.getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female")).getString();
                    tooltip.add((new TextComponent(gender)).withStyle(ChatFormatting.GRAY));
                    int stagenumber = entityTag.getInt("AgeTicks") / 24000;
                    int stage1 = 0;
                    if (stagenumber >= 100) {
                        stage1 = 5;
                    } else if (stagenumber >= 75) {
                        stage1 = 4;
                    } else if (stagenumber >= 50) {
                        stage1 = 3;
                    } else if (stagenumber >= 25) {
                        stage1 = 2;
                    } else {
                        stage1 = 1;
                    }
                    String stage = (new TranslatableComponent("dragon.stage")).getString() + " " + stage1 + " " + (new TranslatableComponent("dragon.days.front")).getString() + stagenumber + " " + (new TranslatableComponent("dragon.days.back")).getString();
                    tooltip.add((new TextComponent(stage)).withStyle(ChatFormatting.GRAY));
                }
            }

        }
    }

    private ChatFormatting getTextColorForEntityType(EntityType type) {
        if (type == IafEntityRegistry.FIRE_DRAGON.get())
            return ChatFormatting.DARK_RED;

        if (type == IafEntityRegistry.ICE_DRAGON.get())
            return ChatFormatting.BLUE;

        if (type == IafEntityRegistry.LIGHTNING_DRAGON.get())
            return ChatFormatting.DARK_PURPLE;

        return ChatFormatting.GRAY;
    }
}
