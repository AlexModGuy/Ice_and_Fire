package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class IafTileEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister
        .create(ForgeRegistries.BLOCK_ENTITIES, IceAndFire.MODID);

    //@formatter:off
    public static final RegistryObject<BlockEntityType<TileEntityLectern>> IAF_LECTERN = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityLectern::new, IafBlockRegistry.LECTERN), "iaf_lectern");
    public static final RegistryObject<BlockEntityType<TileEntityPodium>> PODIUM = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityPodium::new, IafBlockRegistry.PODIUM_OAK, IafBlockRegistry.PODIUM_BIRCH, IafBlockRegistry.PODIUM_SPRUCE, IafBlockRegistry.PODIUM_JUNGLE, IafBlockRegistry.PODIUM_DARK_OAK, IafBlockRegistry.PODIUM_ACACIA), "podium");
    public static final RegistryObject<BlockEntityType<TileEntityEggInIce>> EGG_IN_ICE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityEggInIce::new, IafBlockRegistry.EGG_IN_ICE), "egg_in_ice");
    public static final RegistryObject<BlockEntityType<TileEntityPixieHouse>> PIXIE_HOUSE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityPixieHouse::new, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlockRegistry.PIXIE_HOUSE_OAK, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_SPRUCE, IafBlockRegistry.PIXIE_HOUSE_DARK_OAK), "pixie_house");
    public static final RegistryObject<BlockEntityType<TileEntityJar>> PIXIE_JAR = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityJar::new, IafBlockRegistry.JAR_EMPTY, IafBlockRegistry.JAR_PIXIE_0, IafBlockRegistry.JAR_PIXIE_1, IafBlockRegistry.JAR_PIXIE_2, IafBlockRegistry.JAR_PIXIE_3, IafBlockRegistry.JAR_PIXIE_4), "pixie_jar");
    public static final RegistryObject<BlockEntityType<TileEntityMyrmexCocoon>> MYRMEX_COCOON = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityMyrmexCocoon::new, IafBlockRegistry.DESERT_MYRMEX_COCOON, IafBlockRegistry.JUNGLE_MYRMEX_COCOON), "myrmex_cocoon");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforge>> DRAGONFORGE_CORE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforge::new, IafBlockRegistry.DRAGONFORGE_FIRE_CORE, IafBlockRegistry.DRAGONFORGE_ICE_CORE, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED), "dragonforge_core");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforgeBrick>> DRAGONFORGE_BRICK = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforgeBrick::new, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK, IafBlockRegistry.DRAGONFORGE_ICE_BRICK, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK), "dragonforge_brick");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforgeInput>> DRAGONFORGE_INPUT = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforgeInput::new, IafBlockRegistry.DRAGONFORGE_FIRE_INPUT, IafBlockRegistry.DRAGONFORGE_ICE_INPUT, IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT), "dragonforge_input");
    public static final RegistryObject<BlockEntityType<TileEntityDreadPortal>> DREAD_PORTAL = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDreadPortal::new, IafBlockRegistry.DREAD_PORTAL), "dread_portal");
    public static final RegistryObject<BlockEntityType<TileEntityDreadSpawner>> DREAD_SPAWNER = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDreadSpawner::new, IafBlockRegistry.DREAD_SPAWNER), "dread_spawner");
    public static final RegistryObject<BlockEntityType<TileEntityGhostChest>> GHOST_CHEST = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityGhostChest::new, IafBlockRegistry.GHOST_CHEST), "ghost_chest");


    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerTileEntity(
            Supplier<BlockEntityType.Builder<T>> supplier, String entityName) {
        return TYPES.register(entityName, () -> supplier.get().build(null));
    }
}
