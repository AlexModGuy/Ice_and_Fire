package com.github.alexthe666.iceandfire.util;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class IAFBiomeUtil {
	// ! - Must exclude this
	// & - Must include this
	// | - Or this
	public static final String OPERATORS = "!&|";
	
	// # - For BiomeDictionary entries
	// @ - For Category
	public static final String IDENTIFIERS = "#@";
	
    public static String getBiomeName(Biome biome) {
        ResourceLocation loc = ForgeRegistries.BIOMES.getKey(biome);
        return loc == null ? "" : loc.toString();
    }

    protected static boolean anyBiomeInfoInList(Biome biome, List<? extends String> biomeTypes, List<? extends String> list) {
    	return anyBiomeInfoInList(biome, biomeTypes, list, false);
    }

    protected static boolean anyBiomeInfoInList(Biome biome, List<? extends String> biomeTypes, List<? extends String> list, boolean hasAll) {
    	if (list.size() == 0) return false;

    	List <? extends String> dictionaryChecks = list.stream()
			.filter(s -> s.charAt(0) == '#')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	List <? extends String> categoryChecks = list.stream()
			.filter(s -> s.charAt(0) == '@')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	List <? extends String> registryChecks = list.stream()
			.filter(s -> IDENTIFIERS.indexOf(s.charAt(0)) == -1)
			.collect(Collectors.toList());

    	if (hasAll) {
			return (registryChecks.size() == 0 || registryChecks.contains(biome.getRegistryName().toString().toLowerCase(Locale.ROOT)))
					&& (categoryChecks.size() == 0 || categoryChecks.contains(biome.getCategory().getName().toLowerCase(Locale.ROOT)))
					&& (dictionaryChecks.size() == 0 || dictionaryChecks.stream().allMatch(identifiers -> biomeTypes.contains(identifiers)));
    	} else {
	    	if (
    			registryChecks.size() > 0 && registryChecks.contains(biome.getRegistryName().toString().toLowerCase(Locale.ROOT)) ||
    			categoryChecks.size() > 0 && categoryChecks.contains(biome.getCategory().getName().toLowerCase(Locale.ROOT))
			) {
	    		return true;
	    	}
	    	return dictionaryChecks.stream().anyMatch(identifiers -> biomeTypes.contains(identifiers));
    	}
    }
    
    protected static boolean biomeMeetsListConditions(Biome biome, List<? extends String> list) {
    	if (list.size() == 0) return false;
    	
    	List<? extends String> trimmed = list.stream()
			.map(String::trim)
			.collect(Collectors.toList());

    	List<? extends String> aconditional = trimmed.stream()
			.filter(s -> OPERATORS.indexOf(s.charAt(0)) == -1)
			.collect(Collectors.toList());
    	List<? extends String> exclude = trimmed.stream()
			.filter(s -> s.charAt(0) == '!')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	List<? extends String> mustInclude = trimmed.stream()
			.filter(s -> s.charAt(0) == '&')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	List<? extends String> include = trimmed.stream()
			.filter(s -> s.charAt(0) == '|')
			.map(s -> s.substring(1))
			.collect(Collectors.toList());
    	
    	// Biome Dictionary
    	RegistryKey<Biome> biomeKey = RegistryKey.func_240903_a_(Registry.field_239720_u_, biome.getRegistryName());
    	List<? extends String> biomeTypes = BiomeDictionary.getTypes(biomeKey).stream()
			.map(t -> t.toString().toLowerCase(Locale.ROOT))
			.collect(Collectors.toList());

    	return (anyBiomeInfoInList(biome, biomeTypes, aconditional))
			|| ((exclude.size() == 0 || !anyBiomeInfoInList(biome, biomeTypes, exclude))
			&& (mustInclude.size() == 0 || anyBiomeInfoInList(biome, biomeTypes, mustInclude, true))
			&& (include.size() == 0 || anyBiomeInfoInList(biome, biomeTypes, include)));
    }

    public static boolean parseListForBiomeCheck(List<? extends String> list, Biome biome) {
    	if (list == null || list.size() == 0 || biome == null || biome.getRegistryName() == null) return false;
    	
    	// Lower case all of the entries and replace all the whitespace
    	List<? extends String> lcList = list.stream()
			.map(s -> s.toLowerCase(Locale.ROOT).replaceAll("\\s", ""))
			.collect(Collectors.toList());

    	if(list.size() == 1 && list.contains("*")) return true;
    	
    	// Get an entries that are an expression and evaluate them
    	boolean reducedAconditionals = lcList.stream()
    		.filter(s -> s.indexOf('+') > 0)
    		.map(e -> biomeMeetsListConditions(biome, Lists.newArrayList(e.split("\\+"))) )
    		.reduce(false, (acc, curr) -> acc || curr);
    	
    	return reducedAconditionals || biomeMeetsListConditions(biome, lcList.stream()
    		.filter(s -> s.indexOf('+') == -1)
    		.collect(Collectors.toList())
		);
    }
}
