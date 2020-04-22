package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemAmphithereMacuahuitl extends ItemSword {

    public ItemAmphithereMacuahuitl() {
        super(IafItemRegistry.amphithere_sword_tools);
        this.setTranslationKey("iceandfire.amphithere_macuahuitl");
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setRegistryName(IceAndFire.MODID, "amphithere_macuahuitl");
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase targetEntity, EntityLivingBase attacker) {
        targetEntity.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        targetEntity.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1, 1);
        targetEntity.isAirBorne = true;
        double xRatio = (double) -MathHelper.sin(attacker.rotationYaw * 0.017453292F);
        double zRatio = (double) (MathHelper.cos(attacker.rotationYaw * 0.017453292F));
        float strength = -0.6F;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
        targetEntity.motionX /= 2.0D;
        targetEntity.motionZ /= 2.0D;
        targetEntity.motionX -= xRatio / (double) f * (double) strength;
        targetEntity.motionZ -= zRatio / (double) f * (double) strength;
        targetEntity.motionY = 0.8D;
        Random rand = new Random();
        for (int i = 0; i < 20; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            targetEntity.world.spawnParticle(EnumParticleTypes.CLOUD, targetEntity.posX + (double) (rand.nextFloat() * targetEntity.width * 5.0F) - (double) targetEntity.width - d0 * 10.0D, targetEntity.posY + (double) (rand.nextFloat() * targetEntity.height) - d1 * 10.0D, targetEntity.posZ + (double) (rand.nextFloat() * targetEntity.width * 5.0F) - (double) targetEntity.width - d2 * 10.0D, d0, d1, d2);
        }
        return super.hitEntity(stack, targetEntity, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.amphithere_macuahuitl.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.amphithere_macuahuitl.desc_1"));
    }
}
