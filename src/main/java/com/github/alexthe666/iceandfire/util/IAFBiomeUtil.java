package com.github.alexthe666.iceandfire.util;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class IAFBiomeUtil {
    public static String getBiomeName(Biome biome) {
        ResourceLocation loc = ForgeRegistries.BIOMES.getKey(biome);
        return loc == null ? "" : loc.toString();
    }

    protected static boolean biomeInfoInList(Biome biome, List<? extends String> biomeTypes, List<? extends String> list) {
    	if (
    			list.contains(biome.getRegistryName().toString().toLowerCase(Locale.ROOT)) ||
    			list.contains(biome.getCategory().getName().toLowerCase(Locale.ROOT))
		) {
    		return true;
    	}
    	return list.stream().anyMatch(identifiers -> biomeTypes.contains(identifiers));
    }
    
    public static boolean biomeMeetsListConditions(Biome biome, List<? extends String> list) {
    	if (list == null || biome == null || biome.getRegistryName() == null) return false;
    	
    	List<? extends String> lcList = list.stream()
			.map(s -> s.toLowerCase(Locale.ROOT))
			.collect(Collectors.toList());
    	List<? extends String> include = lcList.stream()
			.filter(s -> s.charAt(0) != '!')
			.collect(Collectors.toList());
    	List<? extends String> exclude = lcList.stream()
			.filter(s -> s.charAt(0) == '!')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	
    	RegistryKey<Biome> biomeKey = RegistryKey.func_240903_a_(Registry.field_239720_u_, biome.getRegistryName());
    	List<? extends String> biomeTypes = BiomeDictionary.getTypes(biomeKey).stream()
			.map(t -> t.toString().toLowerCase(Locale.ROOT))
			.collect(Collectors.toList());
    	return !biomeInfoInList(biome, biomeTypes, exclude) && biomeInfoInList(biome, biomeTypes, include);
    }
}
