package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.github.alexthe666.iceandfire.message.MessageStoneStatue;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStoneStatue extends Item {

    public ItemStoneStatue() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        this.setRegistryName(IceAndFire.MODID, "stone_statue");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            boolean isPlayer = stack.getTag().getBoolean("IAFStoneStatuePlayerEntity");
            String id = stack.getTag().getString("IAFStoneStatueEntityID");
            if (EntityType.byKey(id).orElse(null) != null) {
                EntityType type = EntityType.byKey(id).orElse(null);
                ITextComponent untranslated = isPlayer ? new TranslationTextComponent("entity.player.name") : type.getName();
                tooltip.add(untranslated.applyTextStyle(TextFormatting.GRAY));
            }
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
        itemStack.getTag().putBoolean("IAFStoneStatuePlayerEntity", true);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getFace() != Direction.UP) {
            return ActionResultType.FAIL;
        } else {
            ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
            if (stack.getTag() != null) {

                if (stack.getTag().getBoolean("IAFStoneStatuePlayerEntity")) {
                    EntityStoneStatue statue = new EntityStoneStatue(context.getWorld());
                    statue.setPositionAndRotation(context.getPos().getX() + 0.5, context.getPos().getY() + 1, context.getPos().getZ() + 0.5, context.getPlayer().rotationYaw, 0);
                    statue.smallArms = true;
                    if (!context.getWorld().isRemote) {
                        context.getWorld().addEntity(statue);
                    }
                    statue.readEntityFromNBT(stack.getTag());
                    statue.setCrackAmount(0);
                    float yaw = MathHelper.wrapDegrees(context.getPlayer().rotationYaw + 180F);
                    statue.prevRotationYaw = yaw;
                    statue.rotationYaw = yaw;
                    statue.rotationYawHead = yaw;
                    statue.renderYawOffset = yaw;
                    statue.prevRenderYawOffset = yaw;
                    if (!context.getPlayer().isCreative()) {
                        stack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                } else {
                    String id = stack.getTag().getString("IAFStoneStatueEntityID");
                    EntityType type = EntityType.byKey(id).orElse(null);
                    Class classFromEntity = type.getClass();
                    Entity entity = null;
                    if (classFromEntity == null) {
                        return ActionResultType.SUCCESS;
                    }
                    if (Entity.class.isAssignableFrom(classFromEntity)) {
                        try {
                            entity = (Entity) classFromEntity.getDeclaredConstructor(EntityType.class, World.class).newInstance(type, context.getWorld());
                        } catch (ReflectiveOperationException e) {
                            e.printStackTrace();
                            return ActionResultType.SUCCESS;
                        }
                    }
                    if (entity != null && entity instanceof LivingEntity) {
                        entity.setLocationAndAngles(context.getPos().getX() + 0.5, context.getPos().getY() + 1, context.getPos().getZ() + 0.5, context.getPlayer().rotationYaw, 0);
                        if (!context.getWorld().isRemote) {
                            context.getWorld().addEntity(entity);
                        }
                        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                        properties.isStone = true;
                        IceAndFire.sendMSGToAll(new MessageStoneStatue(entity.getEntityId(), true));
                        ((LivingEntity) entity).read(stack.getTag());
                        float yaw = MathHelper.wrapDegrees(context.getPlayer().rotationYaw + 180F);
                        entity.prevRotationYaw = yaw;
                        entity.rotationYaw = yaw;
                        ((LivingEntity) entity).rotationYawHead = yaw;
                        ((LivingEntity) entity).renderYawOffset = yaw;
                        ((LivingEntity) entity).prevRenderYawOffset = yaw;
                        if (!context.getPlayer().isCreative()) {
                            stack.shrink(1);
                        }
                    }

                }
            }
        }
        return ActionResultType.SUCCESS;
    }
}
