package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
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
        super(new Item.Properties().stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, "stone_statue");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            boolean isPlayer = stack.getTag().getBoolean("IAFStoneStatuePlayerEntity");
            String id = stack.getTag().getString("IAFStoneStatueEntityID");
            if (EntityType.byString(id).orElse(null) != null) {
                EntityType type = EntityType.byString(id).orElse(null);
                TranslationTextComponent untranslated = isPlayer ? new TranslationTextComponent("entity.player.name") : new TranslationTextComponent(type.getDescriptionId());
                tooltip.add(untranslated.withStyle(TextFormatting.GRAY));
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
        itemStack.getTag().putBoolean("IAFStoneStatuePlayerEntity", true);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return ActionResultType.FAIL;
        } else {
            ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
            if (stack.getTag() != null) {
                String id = stack.getTag().getString("IAFStoneStatueEntityID");
                CompoundNBT statueNBT = stack.getTag().getCompound("IAFStoneStatueNBT");
                EntityStoneStatue statue = new EntityStoneStatue(IafEntityRegistry.STONE_STATUE.get(),
                    context.getLevel());
                statue.readAdditionalSaveData(statueNBT);
                statue.setTrappedEntityTypeString(id);
                double d1 = context.getPlayer().getX() - (context.getClickedPos().getX() + 0.5);
                double d2 = context.getPlayer().getZ() - (context.getClickedPos().getZ() + 0.5);
                float yaw = (float) (MathHelper.atan2(d2, d1) * (180F / (float) Math.PI)) - 90;
                statue.yRotO = yaw;
                statue.yRot = yaw;
                statue.yHeadRot = yaw;
                statue.yBodyRot = yaw;
                statue.yBodyRotO = yaw;
                statue.absMoveTo(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1, context.getClickedPos().getZ() + 0.5, yaw, 0);
                if (!context.getLevel().isClientSide) {
                    context.getLevel().addFreshEntity(statue);
                    statue.readAdditionalSaveData(stack.getTag());
                }
                statue.setCrackAmount(0);

                if (!context.getPlayer().isCreative()) {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }
}
