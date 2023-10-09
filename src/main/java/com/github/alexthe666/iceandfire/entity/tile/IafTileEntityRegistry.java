package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IafTileEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister
        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, IceAndFire.MODID);

    //@formatter:off
    public static final RegistryObject<BlockEntityType<TileEntityLectern>> IAF_LECTERN = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityLectern::new, IafBlockRegistry.LECTERN.get()), "lectern");
    public static final RegistryObject<BlockEntityType<TileEntityPodium>> PODIUM = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityPodium::new, IafBlockRegistry.PODIUM_OAK.get(), IafBlockRegistry.PODIUM_BIRCH.get(), IafBlockRegistry.PODIUM_SPRUCE.get(), IafBlockRegistry.PODIUM_JUNGLE.get(), IafBlockRegistry.PODIUM_DARK_OAK.get(), IafBlockRegistry.PODIUM_ACACIA.get()), "podium");
    public static final RegistryObject<BlockEntityType<TileEntityEggInIce>> EGG_IN_ICE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityEggInIce::new, IafBlockRegistry.EGG_IN_ICE.get()), "egginice");
    public static final RegistryObject<BlockEntityType<TileEntityPixieHouse>> PIXIE_HOUSE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityPixieHouse::new, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get(), IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get(), IafBlockRegistry.PIXIE_HOUSE_OAK.get(), IafBlockRegistry.PIXIE_HOUSE_BIRCH.get(), IafBlockRegistry.PIXIE_HOUSE_BIRCH.get(), IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get(), IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get()), "pixie_house");
    public static final RegistryObject<BlockEntityType<TileEntityJar>> PIXIE_JAR = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityJar::new, IafBlockRegistry.JAR_EMPTY.get(), IafBlockRegistry.JAR_PIXIE_0.get(), IafBlockRegistry.JAR_PIXIE_1.get(), IafBlockRegistry.JAR_PIXIE_2.get(), IafBlockRegistry.JAR_PIXIE_3.get(), IafBlockRegistry.JAR_PIXIE_4.get()), "pixie_jar");
    public static final RegistryObject<BlockEntityType<TileEntityMyrmexCocoon>> MYRMEX_COCOON = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityMyrmexCocoon::new, IafBlockRegistry.DESERT_MYRMEX_COCOON.get(), IafBlockRegistry.JUNGLE_MYRMEX_COCOON.get()), "myrmex_cocoon");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforge>> DRAGONFORGE_CORE = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforge::new, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get(), IafBlockRegistry.DRAGONFORGE_ICE_CORE.get(), IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get(), IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get()), "dragonforge_core");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforgeBrick>> DRAGONFORGE_BRICK = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforgeBrick::new, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(), IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get()), "dragonforge_brick");
    public static final RegistryObject<BlockEntityType<TileEntityDragonforgeInput>> DRAGONFORGE_INPUT = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDragonforgeInput::new, IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get(), IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get()), "dragonforge_input");
    public static final RegistryObject<BlockEntityType<TileEntityDreadPortal>> DREAD_PORTAL = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDreadPortal::new, IafBlockRegistry.DREAD_PORTAL.get()), "dread_portal");
    public static final RegistryObject<BlockEntityType<TileEntityDreadSpawner>> DREAD_SPAWNER = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityDreadSpawner::new, IafBlockRegistry.DREAD_SPAWNER.get()), "dread_spawner");
    public static final RegistryObject<BlockEntityType<TileEntityGhostChest>> GHOST_CHEST = registerTileEntity(() -> BlockEntityType.Builder.of(TileEntityGhostChest::new, IafBlockRegistry.GHOST_CHEST.get()), "ghost_chest");


    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerTileEntity(
            Supplier<BlockEntityType.Builder<T>> supplier, String entityName) {
        return TYPES.register(entityName, () -> supplier.get().build(null));
    }
}
