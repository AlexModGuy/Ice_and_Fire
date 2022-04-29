package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
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
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    private static final Predicate VILLAGER_FEAR = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity != null && entity instanceof IVillagerFear;
        }
    };
    private final Random rand = new Random();

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new IafRecipeRegistry());
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        onLeftClick(event.getPlayer(), event.getItemStack());
        if (event.getWorld().isRemote) {
            IceAndFire.sendMSGToServer(new MessageSwingArm());
        }
    }

    public static void onLeftClick(final PlayerEntity living, final ItemStack stack) {
        if (stack.getItem() == IafItemRegistry.GHOST_SWORD) {
            if (living.swingProgress == 0) {
                final Multimap<Attribute, AttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
                double totalDmg = 0D;
                for (AttributeModifier modifier : dmg.get(Attributes.ATTACK_DAMAGE)) {
                    totalDmg += modifier.getAmount();
                }
                living.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                final EntityGhostSword shot = new EntityGhostSword(IafEntityRegistry.GHOST_SWORD, living.world, living, totalDmg * 0.5F);
                final Vector3d vector3d = living.getLook(1.0F);
                final Vector3f vector3f = new Vector3f(vector3d);
                shot.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), 1.0F, 0.5F);
                living.world.addEntity(shot);

            }
        }
    }

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        final float d0 = IafConfig.cockatriceChickenSearchLength;
        final List<Entity> list = chicken.world.getEntitiesWithinAABB(EntityCockatrice.class, (new AxisAlignedBB(chicken.getPosX(), chicken.getPosY(), chicken.getPosZ(), chicken.getPosX() + 1.0D, chicken.getPosY() + 1.0D, chicken.getPosZ() + 1.0D)).grow(d0, 10.0D, d0));
        if (list.isEmpty()) return;

        for (final Entity entity : list) {
            if (entity instanceof EntityCockatrice && !(attacker instanceof EntityCockatrice)) {
                EntityCockatrice cockatrice = (EntityCockatrice) entity;
                if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
                    if (attacker instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) attacker;
                        if (!player.isCreative() && !cockatrice.isOwner(player)) {
                            cockatrice.setAttackTarget(player);
                        }
                    } else {
                        cockatrice.setAttackTarget(attacker);
                    }
                }
            }
        }
    }

    private static void signalAmphithereAlarm(LivingEntity villager, LivingEntity attacker) {
        final float d0 = IafConfig.amphithereVillagerSearchLength;
        final List<Entity> list = villager.world.getEntitiesWithinAABB(EntityAmphithere.class, (new AxisAlignedBB(villager.getPosX() - 1.0D, villager.getPosY() - 1.0D, villager.getPosZ() - 1.0D, villager.getPosX() + 1.0D, villager.getPosY() + 1.0D, villager.getPosZ() + 1.0D)).grow(d0, d0, d0));
        if (list.isEmpty()) return;

        for (final Entity entity : list) {
            if (entity instanceof EntityAmphithere && !(attacker instanceof EntityAmphithere)) {
                TameableEntity amphithere = (TameableEntity) entity;
                if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
                    if (attacker instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) attacker;
                        if (!player.isCreative() && !amphithere.isOwner(player)) {
                            amphithere.setAttackTarget(player);
                        }
                    } else {
                        amphithere.setAttackTarget(attacker);
                    }
                }
            }
        }
    }

    private static boolean isInEntityTag(ResourceLocation loc, EntityType type) {
        ITag<EntityType<?>> tag = EntityTypeTags.getCollection().get(loc);
        return tag != null && tag.contains(type);
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
        if (event.getEntity() instanceof AbstractArrowEntity && ((AbstractArrowEntity) event.getEntity()).getShooter() != null) {
            if (event.getRayTraceResult() instanceof EntityRayTraceResult && ((EntityRayTraceResult) event.getRayTraceResult()).getEntity() != null) {
                Entity shootingEntity = ((AbstractArrowEntity) event.getEntity()).getShooter();
                Entity shotEntity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity)) {
                    if (shotEntity instanceof TameableEntity && ((TameableEntity) shotEntity).isTamed() && shotEntity.isOnSameTeam(shootingEntity)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttackMob(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityMutlipartPart && event.getEntity() instanceof PlayerEntity) {
            event.setCanceled(true);
            Entity parent = ((EntityMutlipartPart) event.getTarget()).getParent();
            try {
                //If the attacked entity is the parent itself parent will be null and also doesn't have to be attacked
                if (parent != null)
                    ((PlayerEntity) event.getEntity()).attackTargetEntityWithCurrentItem(parent);
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Exception thrown while interacting with entity");
            }
            int extraData = 0;
            if (event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra) {
                extraData = ((EntityHydraHead) event.getTarget()).headIndex;
                ((EntityHydra) parent).triggerHeadFlags(extraData);
            }
            if (event.getTarget().world.isRemote && parent != null) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getEntityId(), extraData));
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            if (MiscProperties.hasDismounted(event.getEntityLiving())) {
                event.setDamageMultiplier(0);
                MiscProperties.setDismountedDragon(event.getEntityLiving(), false);
            }
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        /*
        if (event.getEntityBeingMounted() instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) event.getEntityBeingMounted();
            if (event.isDismounting() && event.getEntityMounting() instanceof PlayerEntity && !event.getEntityMounting().world.isRemote) {
                PlayerEntity player = (PlayerEntity) event.getEntityMounting();
                if (dragon.isOwner((PlayerEntity) event.getEntityMounting())) {
                    dragon.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, player.rotationPitch);
                    player.fallDistance = -dragon.height;
                } else {
                    dragon.renderYawOffset = dragon.rotationYaw;
                    float modTick_0 = dragon.getAnimationTick() - 25;
                    float modTick_1 = dragon.getAnimationTick() > 25 && dragon.getAnimationTick() < 55 ? 8 * MathHelper.clamp(MathHelper.sin((float) (Math.PI + modTick_0 * 0.25)), -0.8F, 0.8F) : 0;
                    float modTick_2 = dragon.getAnimationTick() > 30 ? 10 : Math.max(0, dragon.getAnimationTick() - 20);
                    float radius = 0.75F * (0.6F * dragon.getRenderSize() / 3) * -3;
                    float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (modTick_1 * 2F) * 0.015F;
                    double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                    double extraZ = (double) (radius * MathHelper.cos(angle));
                    double extraY = modTick_2 == 0 ? 0 : 0.035F * ((dragon.getRenderSize() / 3) + (modTick_2 * 0.5 * (dragon.getRenderSize() / 3)));
                    player.setPosition(dragon.getPosX() + extraX, dragon.getPosY() + extraY, dragon.getPosZ() + extraZ);
                }
            }

        }
        if (event.getEntityBeingMounted() instanceof EntityHippogryph) {
            EntityHippogryph hippogryph = (EntityHippogryph) event.getEntityBeingMounted();
            if (event.isDismounting() && event.getEntityMounting() instanceof PlayerEntity && !event.getEntityMounting().world.isRemote && hippogryph.isOwner((PlayerEntity) event.getEntityMounting())) {
                PlayerEntity player = (PlayerEntity) event.getEntityMounting();
                hippogryph.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, player.rotationPitch);
            }
        }
         */
    }

    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            float multi = 1;
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (event.getSource() == IafDamageRegistry.DRAGON_FIRE || event.getSource() == IafDamageRegistry.DRAGON_ICE ||
            event.getSource() == IafDamageRegistry.DRAGON_LIGHTNING) {
            float multi = 1;
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ItemScaleArmor ||
                event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ItemScaleArmor ||
                event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.3;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() instanceof ItemScaleArmor ||
                event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.2;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ItemScaleArmor ||
                event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1;
            }
            event.setAmount(event.getAmount() * multi);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof WitherSkeletonEntity) {
            event.getDrops().add(new ItemEntity(event.getEntity().world, event.getEntity().getPosX(), event.getEntity().getPosY(), event.getEntity().getPosZ(),
                    new ItemStack(IafItemRegistry.WITHERBONE, event.getEntityLiving().getRNG().nextInt(2))));
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(final LivingAttackEvent event) {
        if (event.getSource() != null && event.getSource().getTrueSource() != null) {
            final Entity attacker = event.getSource().getTrueSource();
            if (attacker instanceof LivingEntity) {
                if (MiscProperties.getLoveTicks((LivingEntity) attacker) > 0)
                    event.setCanceled(true);

                if (isChicken(event.getEntityLiving())) {
                    signalChickenAlarm(event.getEntityLiving(), (LivingEntity) attacker);
                } else if (DragonUtils.isVillager(event.getEntityLiving())) {
                    signalAmphithereAlarm(event.getEntityLiving(), (LivingEntity) attacker);
                }
            }
        }

    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
        final LivingEntity target = event.getTarget();
        if (target != null) {
            final LivingEntity attacker = event.getEntityLiving();
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
            final List<Entity> list = event.getTarget().world.getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityCyclops) {
                        EntityCyclops cyclops = (EntityCyclops) entity;
                        if (!cyclops.isBlinded() && !event.getPlayer().isCreative()) {
                            cyclops.setAttackTarget(event.getPlayer());
                        }
                    }
                }
            }
        }
        if (event.getTarget() instanceof EntityStoneStatue) {
            ((LivingEntity) event.getTarget()).setHealth(((LivingEntity) event.getTarget()).getMaxHealth());
            if (event.getPlayer() != null) {
                ItemStack stack = event.getPlayer().getHeldItemMainhand();
                event.getTarget().playSound(SoundEvents.BLOCK_STONE_BREAK, 2, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                if (stack.getItem() != null && (stack.getItem().canHarvestBlock(Blocks.STONE.getDefaultState()) || stack.getItem().getTranslationKey().contains("pickaxe"))) {
                    boolean ready = false;
                    event.setCanceled(true);
                    EntityStoneStatue statue = (EntityStoneStatue) event.getTarget();
                    statue.setCrackAmount(statue.getCrackAmount() + 1);
                    ready = statue.getCrackAmount() > 9;
                    if (ready) {
                        CompoundNBT writtenTag = new CompoundNBT();
                        event.getTarget().writeWithoutTypeId(writtenTag);
                        event.getTarget().playSound(SoundEvents.BLOCK_STONE_BREAK, 2, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.getTarget().remove();
                        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                        if (silkTouch) {
                            ItemStack statuette = new ItemStack(IafItemRegistry.STONE_STATUE);
                            statuette.setTag(new CompoundNBT());
                            statuette.getTag().putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            statuette.getTag().putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            statuette.getTag().put("IAFStoneStatueNBT", writtenTag);
                            ((LivingEntity) event.getTarget()).writeAdditional(statuette.getTag());
                            if (!event.getTarget().world.isRemote) {
                                event.getTarget().entityDropItem(statuette, 1);
                            }
                        } else {
                            if (!((LivingEntity) event.getTarget()).world.isRemote) {
                                event.getTarget().entityDropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + event.getEntityLiving().getRNG().nextInt(4));
                            }
                        }
                        event.getTarget().remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        if (!event.getEntity().world.isRemote && ChainProperties.hasChainData(event.getEntityLiving())) {
            ItemEntity entityitem = new ItemEntity(event.getEntity().world,
                event.getEntity().getPosX(),
                event.getEntity().getPosY() + (double) 1,
                event.getEntity().getPosZ(),
                new ItemStack(IafItemRegistry.CHAIN, ChainProperties.getChainedTo(event.getEntityLiving()).size()));
            entityitem.setDefaultPickupDelay();
            event.getEntity().world.addEntity(entityitem);
            ChainProperties.clearChainData(event.getEntityLiving());
        }
        if (event.getEntityLiving().getUniqueID().equals(ServerEvents.ALEX_UUID)) {
            event.getEntityLiving().entityDropItem(new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM), 1);
        }
        if (event.getEntityLiving() instanceof PlayerEntity && IafConfig.ghostsFromPlayerDeaths) {
            Entity attacker = event.getEntityLiving().getRevengeTarget();
            if (attacker instanceof PlayerEntity && event.getEntityLiving().getRNG().nextInt(3) == 0) {
                CombatTracker combat = event.getEntityLiving().getCombatTracker();
                CombatEntry entry = combat.getBestCombatEntry();
                boolean flag = false;
                if (entry != null && (entry.getDamageSrc() == DamageSource.FALL || entry.getDamageSrc() == DamageSource.DROWN || entry.getDamageSrc() == DamageSource.LAVA)) {
                    flag = true;
                }
                if (event.getEntityLiving().isPotionActive(Effects.POISON)) {
                    flag = true;
                }
                if (flag) {
                    World world = event.getEntityLiving().world;
                    EntityGhost ghost = IafEntityRegistry.GHOST.create(world);
                    ghost.copyLocationAndAnglesFrom(event.getEntityLiving());
                    if (!world.isRemote) {
                        ghost.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(event.getEntityLiving().getPosition()), SpawnReason.SPAWNER, null, null);
                        world.addEntity(ghost);
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
        if (event.getEntityLiving() instanceof PlayerEntity && event.getEntityLiving().rotationPitch > 87 && event.getEntityLiving().getRidingEntity() != null && event.getEntityLiving().getRidingEntity() instanceof EntityDragonBase) {
            ((EntityDragonBase) event.getEntityLiving().getRidingEntity()).getEntityInteractionResult((PlayerEntity) event.getEntityLiving(), event.getHand());
        }
        if (event.getEntityLiving() instanceof EntityDragonBase && !event.getEntityLiving().isAlive()) {
            event.setResult(Event.Result.DENY);
            ((EntityDragonBase) event.getEntityLiving()).getEntityInteractionResult(event.getPlayer(), event.getHand());
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {

        if (ChainProperties.hasChainData(event.getEntityLiving())) {
            ChainProperties.tickChain(event.getEntityLiving());
        }

        if (IafConfig.chickensLayRottenEggs && !event.getEntityLiving().world.isRemote && isChicken(event.getEntityLiving()) && !event.getEntityLiving().isChild() && event.getEntityLiving() instanceof AnimalEntity) {
            ChickenProperties.tickChicken(event.getEntityLiving());
        }

        if (FrozenProperties.isFrozen(event.getEntityLiving())) {
            FrozenProperties.tickFrozenEntity(event.getEntityLiving());

            if (!(event.getEntityLiving() instanceof PlayerEntity && ((PlayerEntity) event.getEntityLiving()).isCreative())) {
                event.getEntity().setMotion(event.getEntity().getMotion().mul(0.25F, 1, 0.25F));
                if (!(event.getEntityLiving() instanceof EnderDragonEntity) && !event.getEntityLiving().isOnGround()) {
                    event.getEntity().setMotion(event.getEntity().getMotion().add(0, -0.2, 0));
                }

            }
        }

        if (event.getEntityLiving() instanceof PlayerEntity || event.getEntityLiving() instanceof AbstractVillagerEntity || event.getEntityLiving() instanceof IHearsSiren) {
            SirenProperties.tickCharmedEntity(event.getEntityLiving());
        }

        if (MiscProperties.getLoveTicks(event.getEntityLiving()) > 0) {
            MiscProperties.tickLove(event.getEntityLiving());
        }
        if (AiDebug.isEnabled() && event.getEntityLiving() instanceof MobEntity && AiDebug.contains((MobEntity) event.getEntityLiving())){
            AiDebug.logData();
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getTarget();
            if (ChainProperties.isChainedTo(target, event.getPlayer())) {
                ChainProperties.removeChain(target, event.getPlayer());
                if (!event.getWorld().isRemote) {
                    event.getTarget().entityDropItem(IafItemRegistry.CHAIN, 1);
                }
            }
        }
        if (AiDebug.isEnabled() && !event.getWorld().isRemote() && event.getTarget() instanceof MobEntity && event.getItemStack().getItem() == Items.STICK ){
            AiDebug.addEntity((MobEntity) event.getTarget());
        }
    }

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() != null && event.getRayTraceResult() instanceof EntityRayTraceResult) {
            EntityRayTraceResult entityResult = (EntityRayTraceResult) event.getRayTraceResult();
            if (entityResult.getEntity() != null && entityResult.getEntity() instanceof EntityGhost) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer() != null && (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof AbstractChestBlock)) {
            float dist = IafConfig.dragonGoldSearchLength;
            final List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityDragonBase) {
                        EntityDragonBase dragon = (EntityDragonBase) entity;
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getPlayer()) && !event.getPlayer().isCreative()) {
                            dragon.setQueuedToSit(false);
                            dragon.setSitting(false);
                            dragon.setAttackTarget(event.getPlayer());
                        }
                    }
                }
            }
        }
        if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof WallBlock) {
            ItemChain.attachToFence(event.getPlayer(), event.getWorld(), event.getPos());
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().getBlock() instanceof AbstractChestBlock || event.getState().getBlock() == IafBlockRegistry.GOLD_PILE || event.getState().getBlock() == IafBlockRegistry.SILVER_PILE || event.getState().getBlock() == IafBlockRegistry.COPPER_PILE)) {
            final float dist = IafConfig.dragonGoldSearchLength;
            List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getBoundingBox().expand(dist, dist, dist));
            if (list.isEmpty()) return;

            for (Entity entity : list) {
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getPlayer()) && !event.getPlayer().isCreative()) {
                        dragon.setQueuedToSit(false);
                        dragon.setSitting(false);
                        dragon.setAttackTarget(event.getPlayer());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChestGenerated(LootTableLoadEvent event) {
        final ResourceLocation eventName = event.getName();
        final boolean condition1 = eventName.equals(LootTables.CHESTS_SIMPLE_DUNGEON)
                || eventName.equals(LootTables.CHESTS_ABANDONED_MINESHAFT)
                || eventName.equals(LootTables.CHESTS_DESERT_PYRAMID)
                || eventName.equals(LootTables.CHESTS_JUNGLE_TEMPLE)
                || eventName.equals(LootTables.CHESTS_STRONGHOLD_CORRIDOR)
                || eventName.equals(LootTables.CHESTS_STRONGHOLD_CROSSING);

        if (condition1 || eventName.equals(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER)) {
            LootEntry.Builder item = ItemLootEntry.builder(IafItemRegistry.MANUSCRIPT).quality(20).weight(5);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_manuscript").addEntry(item).acceptCondition(RandomChance.builder(0.35f)).rolls(new RandomValueRange(1, 4)).bonusRolls(0, 3);
            event.getTable().addPool(builder.build());
        }
        if (condition1
                || eventName.equals(LootTables.CHESTS_IGLOO_CHEST)
                || eventName.equals(LootTables.CHESTS_WOODLAND_MANSION)
                || eventName.equals(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH)
                || eventName.equals(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER)) {

            if (IafConfig.generateSilverOre) {
                LootEntry.Builder item = ItemLootEntry.builder(IafItemRegistry.SILVER_INGOT).quality(15).weight(12);
                LootPool.Builder builder = new LootPool.Builder().name("iaf_silver_ingot").addEntry(item).acceptCondition(RandomChance.builder(0.5f)).rolls(new RandomValueRange(1, 3)).bonusRolls(0, 2);
                event.getTable().addPool(builder.build());
            } else if (IafConfig.generateCopperOre) {
                LootEntry.Builder item = ItemLootEntry.builder(IafItemRegistry.COPPER_INGOT).quality(10).weight(14);
                LootPool.Builder builder = new LootPool.Builder().name("iaf_copper_ingot").addEntry(item).acceptCondition(RandomChance.builder(0.6f)).rolls(new RandomValueRange(1, 2)).bonusRolls(0, 3);
                event.getTable().addPool(builder.build());
            }
        } else if ((eventName.equals(WorldGenFireDragonCave.FIREDRAGON_CHEST)
                || eventName.equals(WorldGenFireDragonCave.FIREDRAGON_MALE_CHEST)
                || eventName.equals(WorldGenIceDragonCave.ICEDRAGON_CHEST)
                || eventName.equals(WorldGenIceDragonCave.ICEDRAGON_MALE_CHEST))) {
            LootEntry.Builder item = ItemLootEntry.builder(IafItemRegistry.WEEZER_BLUE_ALBUM).quality(100).weight(1);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_weezer").addEntry(item).acceptCondition(RandomChance.builder(0.01f)).rolls(new RandomValueRange(1, 1)).bonusRolls(0, 0);
            event.getTable().addPool(builder.build());
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().getPassengers().isEmpty()) {
            for (Entity entity : event.getPlayer().getPassengers()) {
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
    public void onEntityJoinWorld(LivingSpawnEvent.SpecialSpawn event) {
        try {
            if (event.getEntity() != null) {
                final Entity entity = event.getEntity();

                if (entity instanceof AnimalEntity && isSheep(entity)) {
                    AnimalEntity animal = (AnimalEntity) entity;
                    animal.goalSelector.addGoal(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
                }
                else if (IafConfig.villagersFearDragons && entity instanceof MobEntity && isVillager(entity)) {
                    MobEntity villager = (MobEntity) entity;
                    villager.goalSelector.addGoal(1, new VillagerAIFearUntamed((CreatureEntity) villager, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
                }
                else if (IafConfig.animalsFearDragons && entity instanceof MobEntity && isLivestock(entity)) {
                    MobEntity animal = (MobEntity) entity;
                    animal.goalSelector.addGoal(1, new VillagerAIFearUntamed((CreatureEntity) animal, LivingEntity.class, 30, 1.0D, 0.5D, new java.util.function.Predicate<LivingEntity>() {
                        public boolean test(LivingEntity entity) {
                            return entity != null && entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(animal);
                        }
                    }));
                }
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == IafVillagerRegistry.SCRIBE) {
            IafVillagerRegistry.addScribeTrades(event.getTrades());
        }
    }
}
