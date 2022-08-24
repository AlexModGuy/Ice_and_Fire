package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWormEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemDeathwormEgg extends Item implements ICustomRendered {
    private final boolean gigantic;

    public ItemDeathwormEgg(boolean gigantic) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, gigantic ? "deathworm_egg_giant" : "deathworm_egg");
        this.gigantic = gigantic;
    }


    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isClientSide) {
            EntityDeathWormEgg entityegg = new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), playerIn,
                worldIn, gigantic);
            entityegg.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
            worldIn.addFreshEntity(entityegg);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
