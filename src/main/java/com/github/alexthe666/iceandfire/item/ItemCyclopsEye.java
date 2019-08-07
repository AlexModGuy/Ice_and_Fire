package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCyclopsEye extends Item {

    public ItemCyclopsEye() {
        super();
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.cyclops_eye");
        this.setRegistryName(IceAndFire.MODID, "cyclops_eye");
        this.maxStackSize = 1;
        this.setMaxDamage(500);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.isItemEqual(newStack);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean unused2) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        } else {
            if(entity instanceof EntityLivingBase){
                EntityLivingBase living = (EntityLivingBase) entity;
                if(living.getHeldItemMainhand() == stack || living.getHeldItemOffhand() == stack){
                    double range = 15;
                    boolean inflictedDamage = false;
                    for (EntityLiving entityliving : world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) living.posX - range, (double) living.posY - range, (double) living.posZ - range, (double) living.posX + range, (double) living.posY + range, (double) living.posZ + range))) {
                        if(!entityliving.isEntityEqual(living) && !entityliving.isOnSameTeam(living) && (entityliving.getAttackTarget() == living || entityliving.getRevengeTarget() == living || entityliving instanceof IMob)){
                            entityliving.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 10, 1));
                            inflictedDamage = true;
                        }
                    }
                    if(inflictedDamage){
                        stack.getTagCompound().setInteger("HurtingTicks", stack.getTagCompound().getInteger("HurtingTicks") + 1);
                    }
                }
                if(stack.getTagCompound().getInteger("HurtingTicks") > 120){
                    stack.damageItem(1, (EntityLivingBase)entity);
                    stack.getTagCompound().setInteger("HurtingTicks", 0);
                }
            }

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.cyclops_eye.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.cyclops_eye.desc_1"));
    }
}
