package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMyrmexSwarm extends Item {
    private boolean jungle;

    public ItemMyrmexSwarm(boolean jungle) {
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        if (jungle) {
            this.setTranslationKey("iceandfire.myrmex_jungle_swarm");
            this.setRegistryName(IceAndFire.MODID, "myrmex_jungle_swarm");
        } else {
            this.setTranslationKey("iceandfire.myrmex_desert_swarm");
            this.setRegistryName(IceAndFire.MODID, "myrmex_desert_swarm");
        }
        this.maxStackSize = 1;
        this.jungle = jungle;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        playerIn.swingArm(hand);
        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.shrink(1);
            playerIn.getCooldownTracker().setCooldown(this, 20);
        }
        for (int i = 0; i < 5; i++) {
            EntityMyrmexSwarmer myrmex = new EntityMyrmexSwarmer(worldIn);
            myrmex.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
            myrmex.setJungleVariant(jungle);
            myrmex.setSummonedBy(playerIn);
            myrmex.setFlying(true);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(myrmex);
            }
        }
        playerIn.getCooldownTracker().setCooldown(this, 1800);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.myrmex_swarm.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.myrmex_swarm.desc_1"));
    }
}