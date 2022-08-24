package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMyrmexSwarm extends Item {
    private final boolean jungle;

    public ItemMyrmexSwarm(boolean jungle) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        if (jungle) {
            this.setRegistryName(IceAndFire.MODID, "myrmex_jungle_swarm");
        } else {
            this.setRegistryName(IceAndFire.MODID, "myrmex_desert_swarm");
        }
        this.jungle = jungle;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
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
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.myrmex_swarm.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.myrmex_swarm.desc_1").withStyle(TextFormatting.GRAY));
    }
}