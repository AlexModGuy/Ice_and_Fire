package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTrollArmor extends ItemArmor {

    public EnumTroll troll;

    public ItemTrollArmor(EnumTroll troll, int renderIndex, EntityEquipmentSlot slot) {
        super(troll == EnumTroll.MOUNTAIN ? ModItems.troll_mountain : troll == EnumTroll.FOREST ? ModItems.troll_forest : ModItems.troll_frost, renderIndex, slot);
        this.troll = troll;
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire." + troll.name().toLowerCase() + "_troll_leather_" + getArmorPart(slot));
        this.setRegistryName(troll.name().toLowerCase() + "_troll_leather_" + getArmorPart(slot));

    }

    private String getArmorPart(EntityEquipmentSlot slot) {
        switch (slot){
            case HEAD:
                return "helmet";
            case CHEST:
                return "chestplate";
            case LEGS:
                return "leggings";
            case FEET:
                return "boots";
        }
        return "";
    }

    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event, DamageSource source) {
        Entity attacker = source.getImmediateSource();
        if (attacker != null) {
            event.setAmount(event.getAmount() * 0.8F);
        }
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 7 : 6);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/armor_troll_" + troll.name().toLowerCase() + (renderIndex == 2 ? "_legs.png" : ".png");
    }
}
