package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafTileEntityRegistry {
    public static TileEntityType<TileEntityLectern> IAF_LECTERN = registerTileEntity(TileEntityType.Builder.create(TileEntityLectern::new, IafBlockRegistry.LECTERN), "iaf_lectern");
    public static TileEntityType<TileEntityPodium> PODIUM = registerTileEntity(TileEntityType.Builder.create(TileEntityPodium::new, IafBlockRegistry.PODIUM_OAK, IafBlockRegistry.PODIUM_BIRCH, IafBlockRegistry.PODIUM_SPRUCE, IafBlockRegistry.PODIUM_JUNGLE, IafBlockRegistry.PODIUM_DARK_OAK, IafBlockRegistry.PODIUM_ACACIA), "podium");
    public static TileEntityType<TileEntityEggInIce> EGG_IN_ICE = registerTileEntity(TileEntityType.Builder.create(TileEntityEggInIce::new, IafBlockRegistry.EGG_IN_ICE), "egg_in_ice");
    public static TileEntityType<TileEntityPixieHouse> PIXIE_HOUSE = registerTileEntity(TileEntityType.Builder.create(TileEntityPixieHouse::new, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlockRegistry.PIXIE_HOUSE_OAK, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_SPRUCE, IafBlockRegistry.PIXIE_HOUSE_DARK_OAK), "pixie_house");
    public static TileEntityType<TileEntityJar> PIXIE_JAR = registerTileEntity(TileEntityType.Builder.create(TileEntityJar::new, IafBlockRegistry.JAR_EMPTY, IafBlockRegistry.JAR_PIXIE_0, IafBlockRegistry.JAR_PIXIE_1, IafBlockRegistry.JAR_PIXIE_2, IafBlockRegistry.JAR_PIXIE_3, IafBlockRegistry.JAR_PIXIE_4, IafBlockRegistry.JAR_PIXIE_5), "pixie_jar");
    public static TileEntityType<TileEntityMyrmexCocoon> MYRMEX_COCOON = registerTileEntity(TileEntityType.Builder.create(TileEntityMyrmexCocoon::new, IafBlockRegistry.DESERT_MYRMEX_COCOON, IafBlockRegistry.JUNGLE_MYRMEX_COCOON), "myrmex_cocoon");
    public static TileEntityType<TileEntityDragonforge> DRAGONFORGE_CORE = registerTileEntity(TileEntityType.Builder.create(TileEntityDragonforge::new, IafBlockRegistry.DRAGONFORGE_FIRE_CORE, IafBlockRegistry.DRAGONFORGE_ICE_CORE, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED), "dragonforge_core");
    public static TileEntityType<TileEntityDragonforgeBrick> DRAGONFORGE_BRICK = registerTileEntity(TileEntityType.Builder.create(TileEntityDragonforgeBrick::new, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK, IafBlockRegistry.DRAGONFORGE_ICE_BRICK), "dragonforge_brick");
    public static TileEntityType<TileEntityDragonforgeInput> DRAGONFORGE_INPUT = registerTileEntity(TileEntityType.Builder.create(TileEntityDragonforgeBrick::new, IafBlockRegistry.DRAGONFORGE_FIRE_INPUT, IafBlockRegistry.DRAGONFORGE_ICE_INPUT), "dragonforge_input");
    public static TileEntityType<TileEntityDreadPortal> DREAD_PORTAL = registerTileEntity(TileEntityType.Builder.create(TileEntityDreadPortal::new, IafBlockRegistry.DREAD_PORTAL), "dread_portal");
    public static TileEntityType<TileEntityDreadSpawner> DREAD_SPAWNER = registerTileEntity(TileEntityType.Builder.create(TileEntityDreadSpawner::new, IafBlockRegistry.DREAD_SPAWNER), "dread_spawner");


    public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(IceAndFire.MODID, entityName);
        return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        try {
            for (Field f : IafTileEntityRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof TileEntityType) {
                    event.getRegistry().register((TileEntityType) obj);
                } else if (obj instanceof TileEntityType[]) {
                    for (TileEntityType te : (TileEntityType[]) obj) {
                        event.getRegistry().register(te);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
