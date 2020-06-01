package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHorn extends Item {

    public ItemDragonHorn() {
        this.maxStackSize = 1;
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.dragon_horn");
        this.setRegistryName(IceAndFire.MODID, "dragon_horn");
        this.addPropertyOverride(new ResourceLocation("iceorfire"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return getDragonType(stack) * 0.25F;
            }
        });
    }

    public static int getDragonType(ItemStack stack) {
        if(stack.getTagCompound() != null){
            NBTTagCompound entity = stack.getTagCompound().getCompoundTag("EntityTag");
            if(!entity.isEmpty()){
                Class clazz = EntityList.getClassFromName(entity.getString("id"));
                if(clazz != null){
                    return  clazz == EntityFireDragon.class ? 1 : 2;
                }
            }
        }
        return 0;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbtPrev = stack.getTagCompound() == null ? new NBTTagCompound() : stack.getTagCompound();
        NBTTagCompound nbt = nbtPrev.getCompoundTag("EntityTag");
        if(!nbt.isEmpty()){
            Entity entity = EntityList.createEntityFromNBT(nbt, worldIn);
            entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, player.getRotationYawHead(), 0);
            stack.setTagCompound(new NBTTagCompound());
            worldIn.playSound(player, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3, 0.75F);
            if(!worldIn.isRemote){
                worldIn.spawnEntity(entity);
            }
            player.swingArm(hand);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack trueStack = playerIn.getHeldItem(hand);
        if (target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwner(playerIn) && (trueStack.getTagCompound() == null || trueStack.getTagCompound() != null && trueStack.getTagCompound().getCompoundTag("EntityTag").isEmpty())) {
            NBTTagCompound entityTag = new NBTTagCompound();
            entityTag.setString("id", EntityList.getKey(target).toString());
            target.writeEntityToNBT(entityTag);
            NBTTagCompound newTag = new NBTTagCompound();
            newTag.setTag("EntityTag", entityTag);
            trueStack.setTagCompound(newTag);
            playerIn.swingArm(hand);
            playerIn.world.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3, 0.75F);
            target.setDead();
            playerIn.world.removeEntity(target);
            return true;
        }
       return false;
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound entityTag = stack.getTagCompound().getCompoundTag("EntityTag");
            if(!entityTag.isEmpty()) {
                Class clazz = EntityList.getClassFromName(entityTag.getString("id"));
                EntityEntry entry = EntityRegistry.getEntry(clazz);
                if (entry != null) {
                    String name = I18n.format("entity." + entry.getName() + ".name");
                    tooltip.add(name);
                }
                String name = entityTag.getString("CustomName").isEmpty() ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + entityTag.getString("CustomName");
                tooltip.add("" + name);
                String gender = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((entityTag.getBoolean("Gender") ? "dragon.gender.male" : "dragon.gender.female"));
                tooltip.add("" + gender);
                int stagenumber = entityTag.getInteger("AgeTicks") / 24000;
                int stage1 = 0;
                {
                    if (stagenumber >= 100) {
                        stage1 = 5;
                    } else if (stagenumber >= 75) {
                        stage1 = 4;
                    } else if (stagenumber >= 50) {
                        stage1 = 3;
                    } else if (stagenumber >= 25) {
                        stage1 = 2;
                    } else {
                        stage1 = 1;
                    }
                }
                String stage = StatCollector.translateToLocal("dragon.stage") + stage1 + " " + StatCollector.translateToLocal("dragon.days.front") + stagenumber + " " + StatCollector.translateToLocal("dragon.days.back");
                tooltip.add("" + stage);
            }

        }
    }
}
