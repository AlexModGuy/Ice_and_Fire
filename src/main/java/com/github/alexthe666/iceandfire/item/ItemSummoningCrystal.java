package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemSummoningCrystal extends Item {

    private ForgeChunkManager.Ticket lastChunkTicket = null;

    public ItemSummoningCrystal(String variant) {
        this.setTranslationKey("iceandfire.summoning_crystal_" + variant);
        this.setRegistryName(IceAndFire.MODID, "summoning_crystal_" + variant);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.addPropertyOverride(new ResourceLocation("has_dragon"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
            }
        });
        this.setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving) {
        return new ItemStack(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        boolean flag = false;
        boolean ice = stack.getItem() == IafItemRegistry.summoning_crystal_ice;
        if (stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Dragon")) {
                    NBTTagCompound draginTag = stack.getTagCompound().getCompoundTag(tagInfo);
                    String dragonName = I18n.format(ice ?  "entity.icedragon.name" : "entity.firedragon.name");
                    if (!draginTag.getString("CustomName").isEmpty()) {
                        dragonName = draginTag.getString("CustomName");
                    }
                    tooltip.add(I18n.format("item.iceandfire.summoning_crystal.bound", dragonName));
                    flag = true;
                }
            }
        }
        if(!flag){
            tooltip.add(I18n.format("item.iceandfire.summoning_crystal.desc_0"));
            tooltip.add(I18n.format("item.iceandfire.summoning_crystal.desc_1"));

        }

    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        boolean flag = false;
        BlockPos offsetPos = pos.offset(facing);
        float yaw = player.rotationYaw;
        boolean displayError = false;
        if (stack.getItem() == this && hasDragon(stack)) {
            int dragonCount = 0;
            if (stack.getTagCompound() != null) {
                for (String tagInfo : stack.getTagCompound().getKeySet()) {
                    if (tagInfo.contains("Dragon")) {
                        dragonCount++;
                        NBTTagCompound dragonTag = stack.getTagCompound().getCompoundTag(tagInfo);
                        UUID id = dragonTag.getUniqueId("DragonUUID");
                        if(id != null){
                            if(!worldIn.isRemote){
                                try{
                                    Entity entity = worldIn.getMinecraftServer().getEntityFromUuid(id);
                                    if(entity != null){
                                        flag = true;
                                        summonEntity(entity, worldIn, offsetPos, yaw);
                                    }
                                }catch (Exception e) {
                                    e.printStackTrace();
                                    displayError = true;
                                }
                                ForgeChunkManager.Ticket ticket = null;
                                DragonPosWorldData data = DragonPosWorldData.get(worldIn);
                                BlockPos dragonChunkPos = null;
                                if(data != null) {
                                    dragonChunkPos = data.getDragonPos(id);
                                }
                                if(IceAndFire.CONFIG.chunkLoadSummonCrystal){
                                    try {
                                        boolean flag2 = false;
                                        if (!flag) {//server side but couldn't find dragon
                                            if (data != null) {
                                                ticket = ForgeChunkManager.requestPlayerTicket(IceAndFire.INSTANCE, player.getName(), worldIn, ForgeChunkManager.Type.NORMAL);
                                                if (ticket != null) {
                                                    if (dragonChunkPos != null) {
                                                        ForgeChunkManager.forceChunk(ticket, new ChunkPos(dragonChunkPos));
                                                    } else {
                                                        displayError = true;
                                                    }
                                                    lastChunkTicket = ticket;
                                                    flag2 = true;
                                                }
                                            }
                                        }
                                        if (flag2) {
                                            try {
                                                Entity entity = worldIn.getMinecraftServer().getEntityFromUuid(id);
                                                if (entity != null) {
                                                    flag = true;
                                                    summonEntity(entity, worldIn, offsetPos, yaw);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (flag && lastChunkTicket != null && dragonChunkPos != null) {
                                            ForgeChunkManager.releaseTicket(lastChunkTicket);
                                            lastChunkTicket = null;
                                        }
                                    } catch (Exception e){
                                        IceAndFire.logger.warn("Could not load chunk when summoning dragon");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 1);
                player.swingArm(hand);
                player.sendStatusMessage(new TextComponentTranslation("message.iceandfire.dragonTeleport"), true);
                stack.setTagCompound(new NBTTagCompound());
            }else if(displayError){
                player.sendStatusMessage(new TextComponentTranslation("message.iceandfire.noDragonTeleport"), true);

            }
        }
        return EnumActionResult.PASS;
    }

    public void summonEntity(Entity entity, World worldIn, BlockPos offsetPos, float yaw){
        entity.setLocationAndAngles(offsetPos.getX() + 0.5D, offsetPos.getY() + 0.5D, offsetPos.getZ() + 0.5D, yaw, 0);
        if(entity instanceof EntityDragonBase){
            ((EntityDragonBase) entity).setCrystalBound(false);
        }
        if(IceAndFire.CONFIG.chunkLoadSummonCrystal) {
            DragonPosWorldData data = DragonPosWorldData.get(worldIn);
            if (data != null) {
                data.removeDragon(entity.getUniqueID());
            }
        }
    }

    public static boolean hasDragon(ItemStack stack) {
        if (stack.getItem() instanceof ItemSummoningCrystal && stack.getTagCompound() != null) {
            for (String tagInfo : stack.getTagCompound().getKeySet()) {
                if (tagInfo.contains("Dragon")) {
                    return true;
                }
            }
        }
        return false;
    }


}
