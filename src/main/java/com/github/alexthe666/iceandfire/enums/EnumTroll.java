package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollLeather;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public enum EnumTroll {
    FOREST(BiomeDictionary.Type.FOREST, IafItemRegistry.troll_forest, Weapon.TRUNK, Weapon.COLUMN_FOREST, Weapon.AXE, Weapon.HAMMER),
    FROST(BiomeDictionary.Type.SNOWY, IafItemRegistry.troll_frost, Weapon.COLUMN_FROST, Weapon.TRUNK_FROST, Weapon.AXE, Weapon.HAMMER),
    MOUNTAIN(BiomeDictionary.Type.MOUNTAIN, IafItemRegistry.troll_mountain, Weapon.COLUMN, Weapon.AXE, Weapon.HAMMER);

    public ResourceLocation TEXTURE;
    public ResourceLocation TEXTURE_STONE;
    public ResourceLocation TEXTURE_EYES;
    public BiomeDictionary.Type spawnBiome;
    public ItemArmor.ArmorMaterial material;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_leather")
    public Item leather;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_helmet")
    public Item helmet;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_chestplate")
    public Item chestplate;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_leggings")
    public Item leggings;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":troll_boots")
    public Item boots;
    private Weapon[] weapons;

    EnumTroll(BiomeDictionary.Type biome, ItemArmor.ArmorMaterial material, Weapon... weapons) {
        spawnBiome = biome;
        this.weapons = weapons;
        this.material = material;
        TEXTURE = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase() + ".png");
        TEXTURE_STONE = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase() + "_stone.png");
        TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/troll/troll_" + this.name().toLowerCase() + "_eyes.png");
        leather = new ItemTrollLeather(this);
        helmet = new ItemTrollArmor(this, material, 0, EntityEquipmentSlot.HEAD);
        chestplate = new ItemTrollArmor(this, material, 1, EntityEquipmentSlot.CHEST);
        leggings = new ItemTrollArmor(this, material, 2, EntityEquipmentSlot.LEGS);
        boots = new ItemTrollArmor(this, material, 3, EntityEquipmentSlot.FEET);

    }

    public static EnumTroll getBiomeType(Biome biome) {
        List<EnumTroll> types = new ArrayList<EnumTroll>();
        for (EnumTroll type : values()) {
            if (BiomeDictionary.hasType(biome, type.spawnBiome)) {
                types.add(type);
            }
        }
        if (types.isEmpty()) {
            return values()[new Random().nextInt(values().length)];
        } else {
            return types.get(new Random().nextInt(types.size()));
        }
    }


    public static Weapon getWeaponForType(EnumTroll troll) {
        return troll.weapons[new Random().nextInt(troll.weapons.length)];
    }

    public enum Weapon {
        AXE, COLUMN, COLUMN_FOREST, COLUMN_FROST, HAMMER, TRUNK, TRUNK_FROST;
        public ResourceLocation TEXTURE;
        public Item item;

        Weapon() {
            TEXTURE = new ResourceLocation("iceandfire:textures/models/troll/weapon/weapon_" + this.name().toLowerCase() + ".png");
            item = new ItemTrollWeapon(this);
        }

    }
}
