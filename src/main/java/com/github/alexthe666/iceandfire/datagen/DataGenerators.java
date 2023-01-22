package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    static PackResources resources = new PackResources();

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource((packConsumer, packConstructor) -> {
                Pack pack = Pack.create(IceAndFire.MODID + ":data", true, () -> resources, packConstructor, Pack.Position.TOP, PackSource.DEFAULT);
                packConsumer.accept(pack);
            });
        }
    }

    public static void createResources(Registry<Biome> biomes) {
        HashMap<TagKey<?>, Tag.Builder> builders = new HashMap<>();
        builders.put(IafWorldRegistry.HAS_MAUSOLEUM, Tag.Builder.tag());
        builders.put(IafWorldRegistry.HAS_GRAVEYARD, Tag.Builder.tag());
        builders.put(IafWorldRegistry.HAS_GORGON_TEMPLE, Tag.Builder.tag());

        biomes.holders().forEach(biomeReference -> {
            if (BiomeConfig.test(BiomeConfig.gorgonTempleBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE).addElement(biomeReference.key().location(), "forge");
            }
            if (BiomeConfig.test(BiomeConfig.graveyardBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GRAVEYARD).addElement(biomeReference.key().location(), "forge");
            }
            if (BiomeConfig.test(BiomeConfig.mausoleumBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_MAUSOLEUM).addElement(biomeReference.key().location(), "forge");
            }
        });

        IafWorldRegistry.HAS_GRAVEYARD.location();
        addBiomeTag("has_structure/mausoleum.json", builders.get(IafWorldRegistry.HAS_MAUSOLEUM));
        addBiomeTag("has_structure/graveyard.json", builders.get(IafWorldRegistry.HAS_GRAVEYARD));
        addBiomeTag("has_structure/gorgon_temple.json", builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE));
    }

    static void addBiomeTag(String location, Tag.Builder builder) {
        resources.add(new ResourceLocation(IceAndFire.MODID,"tags/worldgen/biome/" + location), builder.serializeToJson());
    }

    public static class PackResources implements net.minecraft.server.packs.PackResources {
        public static final int PACK_FORMAT = 9;
        Map<ResourceLocation, JsonObject> DATA = new HashMap<>();

        public void add(ResourceLocation location, @NotNull JsonObject data) {
            DATA.put(location, data);
        }

        @Nullable
        @Override
        public InputStream getRootResource(String pFileName) throws IOException {
            return null;
        }

        @Override
        public InputStream getResource(PackType pType, ResourceLocation pLocation) throws IOException {
            if (pType == PackType.SERVER_DATA) {
                return new ByteArrayInputStream(DATA.get(pLocation).toString().getBytes());
            }
            throw new IOException("Data wasn't found");
        }

        @Override
        public Collection<ResourceLocation> getResources(PackType pType, String pNamespace, String pPath, int pMaxDepth, Predicate<String> pFilter) {
            Collection<ResourceLocation> resources = new ArrayList<>();
            if (pType == PackType.SERVER_DATA) {
                DATA.forEach(((resourceLocation, _jsonObject) -> {
                    if (resourceLocation.getNamespace().equals(pNamespace) && resourceLocation.toString().startsWith(pPath) && pFilter.test(resourceLocation.getPath())) {
                        resources.add(resourceLocation);
                    }
                }
                ));
            }
            return resources;
        }

        @Override
        public boolean hasResource(PackType pType, ResourceLocation pLocation) {
            if (pType != PackType.SERVER_DATA) {
                return false;
            }

            return DATA.containsKey(pLocation);
        }

        @Override
        public String getName() {
            return "iceandfire:data";
        }

        @Override
        public Set<String> getNamespaces(PackType pType) {
            Set<String> namespaces = new HashSet<>();
            if (pType != PackType.SERVER_DATA) {
                return namespaces;
            }

            DATA.forEach((resourceLocation, jsonObject) -> {
                namespaces.add(resourceLocation.getNamespace());
            });

            return namespaces;
        }

        @Override
        public void close() {
        }

        @Nullable
        @Override
        public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) throws IOException {
            if (pDeserializer.getMetadataSectionName().equals("pack")) {
                JsonObject object = new JsonObject();
                object.addProperty("pack_format", PACK_FORMAT);
                object.addProperty("description", "Dynamically generated tags");
                return pDeserializer.fromJson(object);
            }

            return pDeserializer.fromJson(new JsonObject());
        }
    }
}
