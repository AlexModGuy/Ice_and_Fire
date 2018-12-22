package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageSetMyrmexHiveNull;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        this.setCreativeTab(IceAndFire.TAB);
        if(jungle){
            this.setUnlocalizedName("iceandfire.myrmex_jungle_staff");
            this.setRegistryName(IceAndFire.MODID, "myrmex_jungle_staff");
        }else{
            this.setUnlocalizedName("iceandfire.myrmex_desert_staff");
            this.setRegistryName(IceAndFire.MODID, "myrmex_desert_staff");
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setUniqueId("HiveUUID", new UUID(0,0));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if(playerIn.isSneaking()){
            return super.onItemRightClick(worldIn, playerIn, hand);
        }
        UUID id = itemStackIn.getTagCompound().getUniqueId("HiveUUID");
        if (!worldIn.isRemote) {
            System.out.println(id);
            MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
            MyrmexWorldData.get(worldIn).addHive(worldIn, new MyrmexHive());
            if(hive != null){
                IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageGetMyrmexHive(hive));
            }else{
                IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageSetMyrmexHiveNull());
            }
        }else if(id != null && !id.equals(new UUID(0, 0))){
            IceAndFire.PROXY.openMyrmexStaffGui(itemStackIn);
        }
        playerIn.swingArm(hand);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
       if(!player.isSneaking()){
           return super.onItemUse(player, worldIn, pos, hand, side, hitX, hitY, hitZ);
       }else{
           UUID id = player.getHeldItem(hand).getTagCompound().getUniqueId("HiveUUID");
           if (!worldIn.isRemote) {
               MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
               if(hive != null){
                   IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageGetMyrmexHive(hive));
               }else{
                   IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageSetMyrmexHiveNull());
               }
           }else if(id != null && !id.equals(new UUID(0, 0))){
               IceAndFire.PROXY.openMyrmexAddRoomGui(player.getHeldItem(hand), pos, player.getHorizontalFacing());
           }
           player.swingArm(hand);
           return EnumActionResult.SUCCESS;
       }
    }
}
