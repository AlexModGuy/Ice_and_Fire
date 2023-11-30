package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.AiDebug;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.entity.props.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
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
import com.google.common.base.Predicate;
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
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    private static final Predicate VILLAGER_FEAR = new Predicate<LivingEntity>() {
        @Override
        public boolean apply(@Nullable LivingEntity entity) {
            return entity != null && entity instanceof IVillagerFear;
        }
    };
    private final Random rand = new Random();

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        final float d0 = IafConfig.cockatriceChickenSearchLength;
        final List<EntityCockatrice> list = chicken.level().getEntitiesOfClass(EntityCockatrice.class, (new AABB(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getX() + 1.0D, chicken.getY() + 1.0D, chicken.getZ() + 1.0D)).inflate(d0, 10.0D, d0));
        if (list.isEmpty()) return;

        for (final EntityCockatrice cockatrice : list) {
            if (!(attacker instanceof EntityCockatrice)) {
                if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
                    if (attacker instanceof Player) {
                        Player player = (Player) attacker;
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
            if (entity instanceof EntityAmphithere && !(attacker instanceof EntityAmphithere)) {
                TamableAnimal amphithere = (TamableAnimal) entity;
                if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
                    if (attacker instanceof Player) {
                        Player player = (Player) attacker;
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
        for (final Entity entity : first.getPassengers()) {
            if (entity.equals(entityIn) || isRidingOrBeingRiddenBy(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onArrowCollide(ProjectileImpactEvent event) {
        if (event.getEntity() instanceof AbstractArrow && ((AbstractArrow) event.getEntity()).getOwner() != null) {
            if (event.getRayTraceResult() instanceof EntityHitResult && ((EntityHitResult) event.getRayTraceResult()).getEntity() != null) {
                Entity shootingEntity = ((AbstractArrow) event.getEntity()).getOwner();
                Entity shotEntity = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity)) {
                    if (shotEntity instanceof TamableAnimal && ((TamableAnimal) shotEntity).isTame() && shotEntity.isAlliedTo(shootingEntity)) {
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
    public void onEntityFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            if (MiscProperties.hasDismounted(event.getEntity())) {
                event.setDamageMultiplier(0);
                MiscProperties.setDismountedDragon(event.getEntity(), false);
            }
        }
    }


    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            event.setAmount(event.getAmount() * multi);
        }
        String damageType = event.getSource().getMsgId();
        if (IafDamageRegistry.DRAGON_FIRE_TYPE.equals(damageType) || IafDamageRegistry.DRAGON_ICE_TYPE.equals(damageType) ||
            IafDamageRegistry.DRAGON_LIGHTNING_TYPE.equals(damageType)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor ||
                event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor ||
                event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.3;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor ||
                event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.2;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor ||
                event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1;
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

        if (event.getSource().getDirectEntity() instanceof LightningBolt bolt && bolt.getTags().contains(BOLT_DONT_DESTROY_ITEMS)) {
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
            final Entity attacker = event.getSource().getEntity();
            if (attacker instanceof LivingEntity) {
                if (MiscProperties.getLoveTicks((LivingEntity) attacker) > 0)
                    event.setCanceled(true);

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
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.getTarget() != null && isSheep(event.getTarget())) {
            float dist = IafConfig.cyclopesSheepSearchLength;
            final List<Entity> list = event.getTarget().level().getEntities(event.getEntity(), event.getEntity().getBoundingBox().expandTowards(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityCyclops) {
                        EntityCyclops cyclops = (EntityCyclops) entity;
                        if (!cyclops.isBlinded() && !event.getEntity().isCreative()) {
                            cyclops.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }
        if (event.getTarget() instanceof EntityStoneStatue) {
            ((LivingEntity) event.getTarget()).setHealth(((LivingEntity) event.getTarget()).getMaxHealth());
            if (event.getEntity() != null) {
                ItemStack stack = event.getEntity().getMainHandItem();
                event.getTarget().playSound(SoundEvents.STONE_BREAK, 2, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                if (stack.getItem() != null && (stack.getItem().isCorrectToolForDrops(Blocks.STONE.defaultBlockState()) || stack.getItem().getDescriptionId().contains("pickaxe"))) {
                    boolean ready = false;
                    event.setCanceled(true);
                    EntityStoneStatue statue = (EntityStoneStatue) event.getTarget();
                    statue.setCrackAmount(statue.getCrackAmount() + 1);
                    ready = statue.getCrackAmount() > 9;
                    if (ready) {
                        CompoundTag writtenTag = new CompoundTag();
                        event.getTarget().saveWithoutId(writtenTag);
                        event.getTarget().playSound(SoundEvents.STONE_BREAK, 2, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.getTarget().remove(Entity.RemovalReason.KILLED);
                        boolean silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                        if (silkTouch) {
                            ItemStack statuette = new ItemStack(IafItemRegistry.STONE_STATUE.get());
                            statuette.setTag(new CompoundTag());
                            statuette.getTag().putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            statuette.getTag().putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            statuette.getTag().put("IAFStoneStatueNBT", writtenTag);
                            ((LivingEntity) event.getTarget()).addAdditionalSaveData(statuette.getTag());
                            if (!event.getTarget().level().isClientSide) {
                                event.getTarget().spawnAtLocation(statuette, 1);
                            }
                        } else {
                            if (!event.getTarget().level().isClientSide) {
                                event.getTarget().spawnAtLocation(Blocks.COBBLESTONE.asItem(), 2 + event.getEntity().getRandom().nextInt(4));
                            }
                        }
                        event.getTarget().remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide && ChainProperties.hasChainData(event.getEntity())) {
            ItemEntity entityitem = new ItemEntity(event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY() + 1,
                event.getEntity().getZ(),
                new ItemStack(IafItemRegistry.CHAIN.get(), ChainProperties.getChainedTo(event.getEntity()).size()));
            entityitem.setDefaultPickUpDelay();
            event.getEntity().level().addFreshEntity(entityitem);
            ChainProperties.clearChainData(event.getEntity());
        }
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

        if (ChainProperties.hasChainData(event.getEntity())) {
            ChainProperties.tickChain(event.getEntity());
        }

        if (IafConfig.chickensLayRottenEggs && !event.getEntity().level().isClientSide && isChicken(event.getEntity()) && !event.getEntity().isBaby() && event.getEntity() instanceof Animal) {
            ChickenProperties.tickChicken(event.getEntity());
        }

        if (FrozenProperties.isFrozen(event.getEntity())) {
            FrozenProperties.tickFrozenEntity(event.getEntity());

            if (!(event.getEntity() instanceof Player && ((Player) event.getEntity()).isCreative())) {
                event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().multiply(0.25F, 1, 0.25F));
                if (!(event.getEntity() instanceof EnderDragon) && !event.getEntity().onGround()) {
                    event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().add(0, -0.2, 0));
                }

            }
        }

        if (event.getEntity() instanceof Player || event.getEntity() instanceof AbstractVillager || event.getEntity() instanceof IHearsSiren) {
            SirenProperties.tickCharmedEntity(event.getEntity());
        }

        if (MiscProperties.getLoveTicks(event.getEntity()) > 0) {
            MiscProperties.tickLove(event.getEntity());
        }
        if (AiDebug.isEnabled() && event.getEntity() instanceof Mob && AiDebug.contains((Mob) event.getEntity())) {
            AiDebug.logData();
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // Handle chain removal
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getTarget();
            if (ChainProperties.isChainedTo(target, event.getEntity())) {
                ChainProperties.removeChain(target, event.getEntity());
                if (!event.getLevel().isClientSide) {
                    event.getTarget().spawnAtLocation(IafItemRegistry.CHAIN.get(), 1);
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
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

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() != null && event.getRayTraceResult() instanceof EntityHitResult) {
            EntityHitResult entityResult = (EntityHitResult) event.getRayTraceResult();
            if (entityResult.getEntity() != null && entityResult.getEntity() instanceof EntityGhost) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
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
                    if (entity instanceof EntityDragonBase) {
                        EntityDragonBase dragon = (EntityDragonBase) entity;
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
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    if (!dragon.isTame() && !dragon.isModelDead() && !dragon.isOwnedBy(event.getPlayer()) && !event.getPlayer().isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setOrderedToSit(false);
                        dragon.setTarget(event.getPlayer());
                    }
                }
            }
        }
    }

    //@SubscribeEvent
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
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            // Make sure that when a player starts tracking an entity that has additional data
            // it gets relayed from the server to the client
            LivingEntity target = (LivingEntity) event.getTarget();
            if (ChainProperties.hasChainData(target))
                ChainProperties.updateData(target);
            if (FrozenProperties.isFrozen(target))
                FrozenProperties.updateData(target);
            if (MiscProperties.getLoveTicks(target) > 0)
                MiscProperties.updateData(target);
            if (SirenProperties.isCharmed(target))
                SirenProperties.updateData(target);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(MobSpawnEvent.FinalizeSpawn event) {
        try {
            if (event.getEntity() != null && isSheep(event.getEntity()) && event.getEntity() instanceof Animal) {
                Animal animal = (Animal) event.getEntity();
                animal.goalSelector.addGoal(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
            }
            if (event.getEntity() != null && isVillager(event.getEntity()) && event.getEntity() != null && IafConfig.villagersFearDragons) {
                Mob villager = event.getEntity();
                villager.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) villager, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
            }
            if (event.getEntity() != null && isLivestock(event.getEntity()) && event.getEntity() != null && IafConfig.animalsFearDragons) {
                Mob animal = event.getEntity();
                animal.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) animal, LivingEntity.class, 30, 1.0D, 0.5D, entity -> entity != null && entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(animal)));
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

    public static String BOLT_DONT_DESTROY_ITEMS = "skip_items";

    @SubscribeEvent
    public void onLightningHit(final EntityStruckByLightningEvent event) {
        if (event.getLightning().getTags().contains(BOLT_DONT_DESTROY_ITEMS) && (event.getEntity() instanceof ItemEntity || event.getEntity() instanceof ExperienceOrb)) {
            event.setCanceled(true);
        }
    }
}
