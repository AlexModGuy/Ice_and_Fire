package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMyrmexSwarm extends Item {
    private final boolean jungle;

    public ItemMyrmexSwarm(boolean jungle) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
        this.jungle = jungle;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        playerIn.startUsingItem(hand);
        playerIn.swing(hand);
        if (!playerIn.isCreative()) {
            itemStackIn.shrink(1);
            playerIn.getCooldowns().addCooldown(this, 20);
        }
        for (int i = 0; i < 5; i++) {
            EntityMyrmexSwarmer myrmex = new EntityMyrmexSwarmer(IafEntityRegistry.MYRMEX_SWARMER.get(), worldIn);
            myrmex.setPos(playerIn.getX(), playerIn.getY(), playerIn.getZ());
            myrmex.setJungleVariant(jungle);
            myrmex.setSummonedBy(playerIn);
            myrmex.setFlying(true);
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(myrmex);
            }
        }
        playerIn.getCooldowns().addCooldown(this, 1800);
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.myrmex_swarm.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.myrmex_swarm.desc_1").withStyle(ChatFormatting.GRAY));
    }
}