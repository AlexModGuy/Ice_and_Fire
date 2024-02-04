package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.AiDebug;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.*;
import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.github.alexthe666.iceandfire.message.MessageSwingArm;
import com.github.alexthe666.iceandfire.message.MessageSyncPath;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenLightningDragonCave;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    // FIXME :: No check for shouldFear()?
    private static final Predicate<LivingEntity> VILLAGER_FEAR = entity -> entity instanceof IVillagerFear;
    private final Random rand = new Random();

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        final float d0 = IafConfig.cockatriceChickenSearchLength;
        final List<EntityCockatrice> list = chicken.level().getEntitiesOfClass(EntityCockatrice.class, (new AABB(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getX() + 1.0D, chicken.getY() + 1.0D, chicken.getZ() + 1.0D)).inflate(d0, 10.0D, d0));
        if (list.isEmpty()) return;

        for (final EntityCockatrice cockatrice : list) {
            if (!(attacker instanceof EntityCockatrice)) {
                if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
                    if (attacker instanceof Player player) {
                        if (!player.isCreative() && !cockatrice.isOwnedBy(player)) {
                            cockatrice.setTarget(player);
                        }
                    } else {
                        cockatrice.setTarget(attacker);
                    }
                }
            }
        }
    }

    private static void signalAmphithereAlarm(LivingEntity villager, LivingEntity attacker) {
        final float d0 = IafConfig.amphithereVillagerSearchLength;
        final List<EntityAmphithere> list = villager.level().getEntitiesOfClass(EntityAmphithere.class, (new AABB(villager.getX() - 1.0D, villager.getY() - 1.0D, villager.getZ() - 1.0D, villager.getX() + 1.0D, villager.getY() + 1.0D, villager.getZ() + 1.0D)).inflate(d0, d0, d0));
        if (list.isEmpty()) return;

        for (final Entity entity : list) {
            if (entity instanceof EntityAmphithere amphithere && !(attacker instanceof EntityAmphithere)) {
                if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
                    if (attacker instanceof Player player) {
                        if (!player.isCreative() && !amphithere.isOwnedBy(player)) {
                            amphithere.setTarget(player);
                        }
                    } else {
                        amphithere.setTarget(attacker);
                    }
                }
            }
        }
    }

    private static boolean isInEntityTag(ResourceLocation loc, EntityType<?> type) {
        return type.is(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags()).createTagKey(loc));
    }

    public static boolean isLivestock(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.FEAR_DRAGONS, entity.getType());
    }

    public static boolean isVillager(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.VILLAGERS, entity.getType());
    }

    public static boolean isSheep(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SHEEP, entity.getType());
    }

    public static boolean isChicken(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.CHICKENS, entity.getType());
    }

    public static boolean isCockatriceTarget(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.COCKATRICE_TARGETS, entity.getType());
    }

    public static boolean doesScareCockatrice(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SCARES_COCKATRICES, entity.getType());
    }

    public static boolean isBlindMob(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.BLINDED, entity.getType());
    }

    public static boolean isRidingOrBeingRiddenBy(final Entity first, final Entity entityIn) {
        if (first == null || entityIn == null) {
            return false;
        }

        for (final Entity entity : first.getPassengers()) {
            if (entity.equals(entityIn) || isRidingOrBeingRiddenBy(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onArrowCollide(final ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult result) {
            Entity shotEntity = result.getEntity();

            if (shotEntity instanceof EntityGhost) {
                event.setCanceled(true);
            } else if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() != null) {
                Entity shootingEntity = arrow.getOwner();

                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity)) {
                    if (shotEntity instanceof TamableAnimal tamable && tamable.isTame() && shotEntity.isAlliedTo(shootingEntity)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static final String[] VILLAGE_TYPES = new String[]{"plains", "desert", "snowy", "savanna", "taiga"};

    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        if (IafConfig.villagerHouseWeight > 0) {
            Registry<StructureTemplatePool> templatePoolRegistry = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();
            Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
            for (String type : VILLAGE_TYPES) {
                IafVillagerRegistry.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("village/" + type + "/houses"), "iceandfire:village/" + type + "_scriber_1", IafConfig.villagerHouseWeight);
            }
        }

    }

    @SubscribeEvent
    public void onPlayerAttackMob(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityMutlipartPart && event.getEntity() instanceof Player) {
            event.setCanceled(true);
            Entity parent = ((EntityMutlipartPart) event.getTarget()).getParent();
            try {
                //If the attacked entity is the parent itself parent will be null and also doesn't have to be attacked
                if (parent != null)
                    ((Player) event.getEntity()).attack(parent);
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Exception thrown while interacting with entity.", e);
            }
            int extraData = 0;
            if (event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra) {
                extraData = ((EntityHydraHead) event.getTarget()).headIndex;
                ((EntityHydra) parent).triggerHeadFlags(extraData);
            }
            if (event.getTarget().level().isClientSide && parent != null) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getId(), extraData));
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(final LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
                if (data.miscData.hasDismounted) {
                    event.setDamageMultiplier(0);
                    data.miscData.setDismounted(false);
                }
            });
        }
    }


    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (event.getSource().is(IafDamageRegistry.DRAGON_FIRE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_ICE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.3f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.2f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof WitherSkeleton) {
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    new ItemStack(IafItemRegistry.WITHERBONE.get(), event.getEntity().getRandom().nextInt(2))));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void makeItemDropsFireImmune(final LivingDropsEvent event) {
        boolean makeFireImmune = false;

        if (event.getSource().getDirectEntity() instanceof LightningBolt bolt && bolt.getTags().contains(BOLT_DONT_DESTROY_LOOT)) {
            makeFireImmune = true;
        } else if (event.getSource().getEntity() instanceof Player player && player.getItemInHand(player.getUsedItemHand()).is(IafItemTags.MAKE_ITEM_DROPS_FIREIMMUNE)) {
            makeFireImmune = true;
        }

        if (makeFireImmune) {
            Set<ItemEntity> fireImmuneDrops = event.getDrops().stream().map(itemEntity -> new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem()) {
                @Override
                public boolean fireImmune() {
                    return true;
                }
            }).collect(Collectors.toSet());

            event.getDrops().clear();
            event.getDrops().addAll(fireImmuneDrops);
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(final LivingAttackEvent event) {
        if (event.getSource() != null && event.getSource().getEntity() != null) {
            Entity attacker = event.getSource().getEntity();

            if (attacker instanceof LivingEntity) {
                EntityDataProvider.getCapability(attacker).ifPresent(data -> {
                    if (data.miscData.loveTicks > 0) {
                        event.setCanceled(true);
                    }
                });

                if (isChicken(event.getEntity())) {
                    signalChickenAlarm(event.getEntity(), (LivingEntity) attacker);
                } else if (DragonUtils.isVillager(event.getEntity())) {
                    signalAmphithereAlarm(event.getEntity(), (LivingEntity) attacker);
                }
            }
        }

    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingChangeTargetEvent event) {
        final LivingEntity target = event.getOriginalTarget();
        if (target != null) {
            final LivingEntity attacker = event.getEntity();
            if (isChicken(target)) {
                signalChickenAlarm(target, attacker);
            } else if (DragonUtils.isVillager(target)) {
                signalAmphithereAlarm(target, attacker);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(final AttackEntityEvent event) {
        if (event.getTarget() != null && isSheep(event.getTarget())) {
            float dist = IafConfig.cyclopesSheepSearchLength;
            final List<Entity> list = event.getTarget().level().getEntities(event.getEntity(), event.getEntity().getBoundingBox().expandTowards(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityCyclops cyclops) {
                        if (!cyclops.isBlinded() && !event.getEntity().isCreative()) {
                            cyclops.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }

        if (event.getTarget() instanceof EntityStoneStatue statue) {
            statue.setHealth(statue.getMaxHealth());

            if (event.getEntity() != null) {
                ItemStack stack = event.getEntity().getMainHandItem();
                event.getTarget().playSound(SoundEvents.STONE_BREAK, 2, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);

                if (stack.getItem().isCorrectToolForDrops(Blocks.STONE.defaultBlockState()) || stack.getItem().getDescriptionId().contains("pickaxe")) {
                    event.setCanceled(true);
                    statue.setCrackAmount(statue.getCrackAmount() + 1);

                    if (statue.getCrackAmount() > 9) {
                        CompoundTag writtenTag = new CompoundTag();
                        event.getTarget().saveWithoutId(writtenTag);
                        event.getTarget().playSound(SoundEvents.STONE_BREAK, 2, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.getTarget().remove(Entity.RemovalReason.KILLED);

                        if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
                            ItemStack statuette = new ItemStack(IafItemRegistry.STONE_STATUE.get());
                            CompoundTag tag = statuette.getOrCreateTag();
                            tag.putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            tag.putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            tag.put("IAFStoneStatueNBT", writtenTag);
                            statue.addAdditionalSaveData(tag);

                            if (!statue.level().isClientSide()) {
                                statue.spawnAtLocation(statuette, 1);
                            }
                        } else {
                            if (!statue.level().isClientSide) {
                                statue.spawnAtLocation(Blocks.COBBLESTONE.asItem(), 2 + event.getEntity().getRandom().nextInt(4));
                            }
                        }

                        statue.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
            if (event.getEntity().level().isClientSide()) {
                return;
            }

            if (!data.chainData.getChainedTo().isEmpty()) {
                ItemEntity entityitem = new ItemEntity(event.getEntity().level(),
                        event.getEntity().getX(),
                        event.getEntity().getY() + 1,
                        event.getEntity().getZ(),
                        new ItemStack(IafItemRegistry.CHAIN.get(), data.chainData.getChainedTo().size()));
                entityitem.setDefaultPickUpDelay();
                event.getEntity().level().addFreshEntity(entityitem);

                data.chainData.clearChains();
            }
        });

        if (event.getEntity().getUUID().equals(ServerEvents.ALEX_UUID)) {
            event.getEntity().spawnAtLocation(new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM.get()), 1);
        }

        if (event.getEntity() instanceof Player && IafConfig.ghostsFromPlayerDeaths) {
            Entity attacker = event.getEntity().getLastHurtByMob();
            if (attacker instanceof Player && event.getEntity().getRandom().nextInt(3) == 0) {
                CombatTracker combat = event.getEntity().getCombatTracker();
                CombatEntry entry = combat.getMostSignificantFall();
                boolean flag = entry != null && (entry.source().is(DamageTypes.FALL) || entry.source().is(DamageTypes.DROWN) || entry.source().is(DamageTypes.LAVA));
                if (event.getEntity().hasEffect(MobEffects.POISON)) {
                    flag = true;
                }
                if (flag) {
                    Level world = event.getEntity().level();
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(world);
                    ghost.copyPosition(event.getEntity());
                    if (!world.isClientSide) {
                        ghost.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(event.getEntity().blockPosition()), MobSpawnType.SPAWNER, null, null);
                        world.addFreshEntity(ghost);
                    }
                    ghost.setDaytimeMode(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityStopUsingItem(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() instanceof ItemDeathwormGauntlet || event.getItem().getItem() instanceof ItemCockatriceScepter) {
            event.setDuration(20);
        }
    }

    @SubscribeEvent
    public void onEntityUseItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() != null && event.getEntity().getXRot() > 87 && event.getEntity().getVehicle() != null && event.getEntity().getVehicle() instanceof EntityDragonBase) {
            ((EntityDragonBase) event.getEntity().getVehicle()).mobInteract(event.getEntity(), event.getHand());
        }
/*        if (event.getEntity() instanceof EntityDragonBase && !event.getEntity().isAlive()) {
            event.setResult(Event.Result.DENY);
            ((EntityDragonBase) event.getEntityLiving()).mobInteract(event.getPlayer(), event.getHand());
        }*/
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        if (AiDebug.isEnabled() && event.getEntity() instanceof Mob && AiDebug.contains((Mob) event.getEntity())) {
            AiDebug.logData();
        }
    }

    @SubscribeEvent
    public void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
        // Handle chain removal
        if (event.getTarget() instanceof LivingEntity target) {
            EntityDataProvider.getCapability(target).ifPresent(data -> {
                if (data.chainData.isChainedTo(event.getEntity())) {
                    data.chainData.removeChain(event.getEntity());

                    if (!event.getLevel().isClientSide()) {
                        event.getTarget().spawnAtLocation(IafItemRegistry.CHAIN.get(), 1);
                    }

                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });
        }

        // Handle debug path render
        if (!event.getLevel().isClientSide() && event.getTarget() instanceof Mob && event.getItemStack().getItem() == Items.STICK) {
            if (AiDebug.isEnabled())
                AiDebug.addEntity((Mob) event.getTarget());
            if (Pathfinding.isDebug()) {
                if (AbstractPathJob.trackingMap.getOrDefault(event.getEntity(), UUID.randomUUID()).equals(event.getTarget().getUUID())) {
                    AbstractPathJob.trackingMap.remove(event.getEntity());
                    IceAndFire.sendMSGToPlayer(new MessageSyncPath(new HashSet<>(), new HashSet<>(), new HashSet<>()), (ServerPlayer) event.getEntity());
                } else {
                    AbstractPathJob.trackingMap.put(event.getEntity(), event.getTarget().getUUID());
                }
            }
        }
    }

    @SubscribeEvent // TODO :: Can this be moved into the item itself?
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        onLeftClick(event.getEntity(), event.getItemStack());
        if (event.getLevel().isClientSide) {
            IceAndFire.sendMSGToServer(new MessageSwingArm());
        }
    }

    public static void onLeftClick(final Player playerEntity, final ItemStack stack) {
        if (stack.getItem() == IafItemRegistry.GHOST_SWORD.get()) {
            ItemGhostSword.spawnGhostSwordEntity(stack, playerEntity);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() != null && (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof AbstractChestBlock) && !event.getEntity().isCreative()) {
            float dist = IafConfig.dragonGoldSearchLength;
            final List<Entity> list = event.getLevel().getEntities(event.getEntity(), event.getEntity().getBoundingBox().inflate(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityDragonBase dragon) {
                        if (!dragon.isTame() && !dragon.isModelDead() && !dragon.isOwnedBy(event.getEntity())) {
                            dragon.setInSittingPose(false);
                            dragon.setOrderedToSit(false);
                            dragon.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }
        if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof WallBlock) {
            ItemChain.attachToFence(event.getEntity(), event.getLevel(), event.getPos());
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().getBlock() instanceof AbstractChestBlock || event.getState().getBlock() == IafBlockRegistry.GOLD_PILE.get() || event.getState().getBlock() == IafBlockRegistry.SILVER_PILE.get() || event.getState().getBlock() == IafBlockRegistry.COPPER_PILE.get())) {
            final float dist = IafConfig.dragonGoldSearchLength;
            List<Entity> list = event.getLevel().getEntities(event.getPlayer(), event.getPlayer().getBoundingBox().inflate(dist, dist, dist));
            if (list.isEmpty()) return;

            for (Entity entity : list) {
                if (entity instanceof EntityDragonBase dragon) {
                    if (!dragon.isTame() && !dragon.isModelDead() && !dragon.isOwnedBy(event.getPlayer()) && !event.getPlayer().isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setOrderedToSit(false);
                        dragon.setTarget(event.getPlayer());
                    }
                }
            }
        }
    }

    //@SubscribeEvent // FIXME :: Unused
    public static void onChestGenerated(LootTableLoadEvent event) {
        final ResourceLocation eventName = event.getName();
        final boolean condition1 = eventName.equals(BuiltInLootTables.SIMPLE_DUNGEON)
                || eventName.equals(BuiltInLootTables.ABANDONED_MINESHAFT)
                || eventName.equals(BuiltInLootTables.DESERT_PYRAMID)
                || eventName.equals(BuiltInLootTables.JUNGLE_TEMPLE)
                || eventName.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)
                || eventName.equals(BuiltInLootTables.STRONGHOLD_CROSSING);

        if (condition1 || eventName.equals(BuiltInLootTables.VILLAGE_CARTOGRAPHER)) {
            LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.MANUSCRIPT.get()).setQuality(20).setWeight(5);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_manuscript").add(item).when(LootItemRandomChanceCondition.randomChance(0.35f)).setRolls(UniformGenerator.between(1, 4)).setBonusRolls(UniformGenerator.between(0, 3));
            event.getTable().addPool(builder.build());
        }
        if (condition1
                || eventName.equals(BuiltInLootTables.IGLOO_CHEST)
                || eventName.equals(BuiltInLootTables.WOODLAND_MANSION)
                || eventName.equals(BuiltInLootTables.VILLAGE_TOOLSMITH)
                || eventName.equals(BuiltInLootTables.VILLAGE_ARMORER)) {


            LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.SILVER_INGOT.get()).setQuality(15).setWeight(12);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_silver_ingot").add(item).when(LootItemRandomChanceCondition.randomChance(0.5f)).setRolls(UniformGenerator.between(1, 3)).setBonusRolls(UniformGenerator.between(0, 3));
            event.getTable().addPool(builder.build());

        } else if ((event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST)
                || event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST_MALE)
                || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST)
                || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST_MALE)
                || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST)
                || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST_MALE))) {
            LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.WEEZER_BLUE_ALBUM.get()).setQuality(100).setWeight(1);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_weezer").add(item).when(LootItemRandomChanceCondition.randomChance(0.01f)).setRolls(UniformGenerator.between(1, 1));
            event.getTable().addPool(builder.build());
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null && !event.getEntity().getPassengers().isEmpty()) {
            for (Entity entity : event.getEntity().getPassengers()) {
                entity.stopRiding();
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        try {
            if (isSheep(mob) && mob instanceof Animal animal) {
                animal.goalSelector.addGoal(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
            }
            if (isVillager(mob) && IafConfig.villagersFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) mob, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
            }
            if (isLivestock(mob) && IafConfig.animalsFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) mob, LivingEntity.class, 30, 1.0D, 0.5D, entity -> entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(mob)));
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == IafVillagerRegistry.SCRIBE.get()) {
            IafVillagerRegistry.addScribeTrades(event.getTrades());
        }
    }

    public static String BOLT_DONT_DESTROY_LOOT = "iceandfire.bolt_skip_loot";

    @SubscribeEvent
    public void onLightningHit(final EntityStruckByLightningEvent event) {
        if ((event.getEntity() instanceof ItemEntity || event.getEntity() instanceof ExperienceOrb) && event.getLightning().getTags().contains(BOLT_DONT_DESTROY_LOOT)) {
            event.setCanceled(true);
        } else if (event.getLightning().getTags().contains(event.getEntity().getStringUUID())) {
            event.setCanceled(true);
        }
    }
}
