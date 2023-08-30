package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;


@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    static PackResources resources = new PackResources();

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            // Hacky workaround to avoid reloading datapacks
            // FIXME: Won't work in 1.19.2
            createResources((Registry<Biome>) ForgeRegistries.BIOMES);
            event.addRepositorySource(pOnLoad -> {
                Pack pack = Pack.create(IceAndFire.MODID + ":data", Component.nullToEmpty(""), true, pId -> resources,
                        Pack.readPackInfo("server", pId -> resources),event.getPackType(), Pack.Position.TOP, true, PackSource.DEFAULT);
                pOnLoad.accept(pack);
            });
        }
    }

/*    @Deprecated(since = "1.19.2", forRemoval = true)
    public static void createResources(Stream<Holder<Biome>> biomeStream) {
        HashMap<TagKey<?>, TagBuilder> builders = new HashMap<>();
        builders.put(IafWorldRegistry.HAS_MAUSOLEUM, TagBuilder.create());
        builders.put(IafWorldRegistry.HAS_GRAVEYARD, TagBuilder.create());
        builders.put(IafWorldRegistry.HAS_GORGON_TEMPLE, TagBuilder.create());

        biomeStream.forEach(biomeReference -> {
            if (BiomeConfig.test(BiomeConfig.gorgonTempleBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE).addElement(ForgeRegistries.BIOMES.getKey(biomeReference.value()));
            }
            if (BiomeConfig.test(BiomeConfig.graveyardBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GRAVEYARD).addElement(ForgeRegistries.BIOMES.getKey(biomeReference.value()));
            }
            if (BiomeConfig.test(BiomeConfig.mausoleumBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_MAUSOLEUM).addElement(ForgeRegistries.BIOMES.getKey(biomeReference.value()));
            }
        });

        addBiomeTag("has_structure/mausoleum.json", builders.get(IafWorldRegistry.HAS_MAUSOLEUM));
        addBiomeTag("has_structure/graveyard.json", builders.get(IafWorldRegistry.HAS_GRAVEYARD));
        addBiomeTag("has_structure/gorgon_temple.json", builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE));
    }*/

    public static void createResources(Registry<Biome> biomes) {
        HashMap<TagKey<?>, TagBuilder> builders = new HashMap<>();
        builders.put(IafWorldRegistry.HAS_MAUSOLEUM, TagBuilder.create());
        builders.put(IafWorldRegistry.HAS_GRAVEYARD, TagBuilder.create());
        builders.put(IafWorldRegistry.HAS_GORGON_TEMPLE, TagBuilder.create());

        biomes.holders().forEach(biomeReference -> {
            if (BiomeConfig.test(BiomeConfig.gorgonTempleBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE).addElement(biomeReference.key().location());
            }
            if (BiomeConfig.test(BiomeConfig.graveyardBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_GRAVEYARD).addElement(biomeReference.key().location());
            }
            if (BiomeConfig.test(BiomeConfig.mausoleumBiomes, biomeReference)) {
                builders.get(IafWorldRegistry.HAS_MAUSOLEUM).addElement(biomeReference.key().location());
            }
        });

        addBiomeTag("has_structure/mausoleum.json", builders.get(IafWorldRegistry.HAS_MAUSOLEUM));
        addBiomeTag("has_structure/graveyard.json", builders.get(IafWorldRegistry.HAS_GRAVEYARD));
        addBiomeTag("has_structure/gorgon_temple.json", builders.get(IafWorldRegistry.HAS_GORGON_TEMPLE));
    }

    static void addBiomeTag(String location, TagBuilder builder) {
        resources.add(new ResourceLocation(IceAndFire.MODID, "tags/worldgen/biome/" + location), serializeToJson(builder));
    }

    static JsonObject serializeToJson(TagBuilder builder) {
        JsonObject jsonobject = new JsonObject();
        JsonArray jsonarray = new JsonArray();

        for(TagEntry tagEntry : builder.build()) {
            jsonarray.add("#" + tagEntry.getId());
        }

        jsonobject.addProperty("replace", builder.isReplace());
        jsonobject.add("values", jsonarray);
        builder.getRemoveEntries();
        if (builder.getRemoveEntries().findAny().isPresent()) {
            JsonArray removeArray = new JsonArray();
            for (Object removeEntry : builder.getRemoveEntries().toArray()) {
                if (removeEntry instanceof TagEntry) {
                    removeArray.add("#" + ((TagEntry) removeEntry).getId());
                }
            }
            if (!removeArray.isEmpty())
                jsonobject.add("remove", removeArray);
        }

        return jsonobject;
    }

    public static class PackResources implements net.minecraft.server.packs.PackResources {
        public static final int PACK_FORMAT = 9;
        Map<ResourceLocation, JsonObject> DATA = new HashMap<>();

        public void add(ResourceLocation location, @NotNull JsonObject data) {
            DATA.put(location, data);
        }

        @Override
        public IoSupplier<InputStream> getRootResource(String @NotNull ... pFileName) {
            return null;
        }

        @Override
        public IoSupplier<InputStream> getResource(@NotNull PackType pType, @NotNull ResourceLocation pLocation) {
            if (pType == PackType.SERVER_DATA) {
                return IoSupplier.create(Path.of(pLocation.getPath()));
            }
            return null;
        }

        @Override
        public void listResources(@NotNull PackType pType, @NotNull String pNamespace, @NotNull String pPath, net.minecraft.server.packs.PackResources.@NotNull ResourceOutput pResourceOutput) {
            Collection<ResourceLocation> resources = new ArrayList<>();
            if (pType == PackType.SERVER_DATA) {
                DATA.forEach(((resourceLocation, _jsonObject) -> {
                    if (resourceLocation.getNamespace().equals(pNamespace) && resourceLocation.toString().startsWith(pPath) && pPath.equals(resourceLocation.getPath())) {
                        resources.add(resourceLocation);
                    }
                }
                ));
            }
//            return resources;
        }

        public boolean hasResource(PackType pType, ResourceLocation pLocation) {
            if (pType != PackType.SERVER_DATA) {
                return false;
            }

            return DATA.containsKey(pLocation);
        }

        @Override
        public @NotNull String packId() {
            return "iceandfire:data";
        }

        @Override
        public @NotNull Set<String> getNamespaces(@NotNull PackType pType) {
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
        public <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) {
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
