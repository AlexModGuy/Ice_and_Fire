package com.github.alexthe666.iceandfire.item;


import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHorn extends Item {

    public ItemDragonHorn() {
        super((new Item.Properties()).tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        setRegistryName("iceandfire", "dragon_horn");
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
    public void onCraftedBy(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }


    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        ItemStack trueStack = playerIn.getItemInHand(hand);
        if (!playerIn.level.isClientSide && hand == Hand.MAIN_HAND && target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwnedBy(playerIn) && (trueStack.getTag() == null || (trueStack.getTag() != null && trueStack.getTag().getCompound("EntityTag").isEmpty()))) {
            CompoundNBT newTag = new CompoundNBT();

            CompoundNBT entityTag = new CompoundNBT();
            target.save(entityTag);
            newTag.put("EntityTag", entityTag);

            newTag.putString("DragonHornEntityID", Registry.ENTITY_TYPE.getKey(target.getType()).toString());
            trueStack.setTag(newTag);

            playerIn.swing(hand);
            playerIn.level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3.0F, 0.75F);
            target.remove();
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }


    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getClickedFace() != Direction.UP)
            return ActionResultType.FAIL;
        ItemStack stack = context.getItemInHand();
        if (stack.getTag() != null && !stack.getTag().getString("DragonHornEntityID").isEmpty()) {
            World world = context.getLevel();
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
                    CompoundNBT tag = stack.getTag();
                    tag.remove("DragonHornEntityID");
                    tag.remove("EntityTag");
                    tag.remove("EntityUUID");
                    stack.setTag(tag);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            CompoundNBT entityTag = stack.getTag().getCompound("EntityTag");
            if (!entityTag.isEmpty()) {
                String id = stack.getTag().getString("DragonHornEntityID");
                if (EntityType.byString(id).isPresent()) {
                    EntityType type = EntityType.byString(id).get();
                    tooltip.add((new TranslationTextComponent(type.getDescriptionId())).withStyle(getTextColorForEntityType(type)));
                    String name = (new TranslationTextComponent("dragon.unnamed")).getString();
                    if (!entityTag.getString("CustomName").isEmpty()) {
                        IFormattableTextComponent component = ITextComponent.Serializer.fromJson(entityTag.getString("CustomName"));
                        if (component != null)
                            name = component.getString();
                    }

                    tooltip.add((new StringTextComponent(name)).withStyle(TextFormatting.GRAY));
                    String gender = (new TranslationTextComponent("dragon.gender")).getString() + " " + (new TranslationTextComponent(entityTag.getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female")).getString();
                    tooltip.add((new StringTextComponent(gender)).withStyle(TextFormatting.GRAY));
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
                    String stage = (new TranslationTextComponent("dragon.stage")).getString() + " " + stage1 + " " + (new TranslationTextComponent("dragon.days.front")).getString() + stagenumber + " " + (new TranslationTextComponent("dragon.days.back")).getString();
                    tooltip.add((new StringTextComponent(stage)).withStyle(TextFormatting.GRAY));
                }
            }

        }
    }

    private TextFormatting getTextColorForEntityType(EntityType type) {
        if (type == IafEntityRegistry.FIRE_DRAGON.get())
            return TextFormatting.DARK_RED;

        if (type == IafEntityRegistry.ICE_DRAGON.get())
            return TextFormatting.BLUE;

        if (type == IafEntityRegistry.LIGHTNING_DRAGON.get())
            return TextFormatting.DARK_PURPLE;

        return TextFormatting.GRAY;
    }
}
