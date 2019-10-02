package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMobSkull extends Item implements ICustomRendered {

    private EnumSkullType skull;

    public ItemMobSkull(EnumSkullType skull) {
        this.maxStackSize = 1;
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire." + skull.itemResourceName);
        this.setRegistryName(IceAndFire.MODID, skull.itemResourceName);

        this.skull = skull;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        EntityMobSkull skull = new EntityMobSkull(worldIn);
        ItemStack stack = player.getHeldItem(hand);
        BlockPos offset = pos.offset(side, 1);
        skull.setLocationAndAngles(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        float yaw = player.rotationYaw;
        if (side != EnumFacing.UP) {
            yaw = player.getHorizontalFacing().getHorizontalAngle();
        }
        skull.setYaw(yaw);
        skull.setSkullType(this.skull);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(skull);
        }
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }
}
