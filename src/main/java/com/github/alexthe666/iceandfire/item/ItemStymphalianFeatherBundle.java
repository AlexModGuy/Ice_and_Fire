package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianFeather;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStymphalianFeatherBundle extends Item {

    public ItemStymphalianFeatherBundle() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, "stymphalian_feather_bundle");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getItemInHand(hand);
        player.startUsingItem(hand);
        player.getCooldowns().addCooldown(this, 15);
        player.playSound(SoundEvents.EGG_THROW, 1, 1);
        float rotation = player.yHeadRot;
        for (int i = 0; i < 8; i++) {
            EntityStymphalianFeather feather = new EntityStymphalianFeather(IafEntityRegistry.STYMPHALIAN_FEATHER.get(),
                worldIn, player);
            rotation += 45;
            feather.shootFromRotation(player, 0, rotation, 0.0F, 1.5F, 1.0F);
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(feather);
            }
        }
        if (!player.isCreative()) {
            itemStackIn.shrink(1);
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.stymphalian_feather_bundle.desc_0").withStyle(TextFormatting.GRAY));
    }
}