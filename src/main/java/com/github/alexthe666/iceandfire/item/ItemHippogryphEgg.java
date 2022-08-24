package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemHippogryphEgg extends Item implements ICustomRendered {

    public ItemHippogryphEgg() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, "hippogryph_egg");
    }

    public static ItemStack createEggStack(EnumHippogryphTypes parent1, EnumHippogryphTypes parent2) {
        EnumHippogryphTypes eggType = ThreadLocalRandom.current().nextBoolean() ? parent1 : parent2;
        ItemStack stack = new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG);
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("EggOrdinal", eggType.ordinal());
        stack.setTag(tag);
        return stack;
    }


    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
                ItemStack stack = new ItemStack(this);
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("EggOrdinal", type.ordinal());
                stack.setTag(tag);
                items.add(stack);

            }
        }

    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isClientSide) {
            EntityHippogryphEgg entityegg = new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), worldIn,
                playerIn, itemstack);
            entityegg.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
            worldIn.addFreshEntity(entityegg);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag();
        int eggOrdinal = 0;
        if (tag != null) {
            eggOrdinal = tag.getInt("EggOrdinal");
        }

        String type = EnumHippogryphTypes.values()[MathHelper.clamp(eggOrdinal, 0, EnumHippogryphTypes.values().length - 1)].name().toLowerCase();
        tooltip.add(new TranslationTextComponent("entity.iceandfire.hippogryph." + type).withStyle(TextFormatting.GRAY));
    }
}
