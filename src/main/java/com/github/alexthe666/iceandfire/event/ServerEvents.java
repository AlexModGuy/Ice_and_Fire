package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockBurntTorch;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.item.*;
import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.*;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.util.*;

public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    private static final Predicate VILLAGER_FEAR = new Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase entity) {
            return entity != null && entity instanceof IVillagerFear;
        }
    };
    private Random rand = new Random();

    private static void signalChickenAlarm(EntityLivingBase chicken, EntityLivingBase attacker) {
        float d0 = IceAndFire.CONFIG.cockatriceChickenSearchLength;
        List<Entity> list = chicken.world.getEntitiesWithinAABB(EntityCockatrice.class, (new AxisAlignedBB(chicken.posX, chicken.posY, chicken.posZ, chicken.posX + 1.0D, chicken.posY + 1.0D, chicken.posZ + 1.0D)).grow(d0, 10.0D, d0));
        Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(attacker));
        if (!list.isEmpty()) {
            Iterator<Entity> itr = list.iterator();
            while (itr.hasNext()) {
                Entity entity = itr.next();
                if (entity instanceof EntityCockatrice && !(attacker instanceof EntityCockatrice)) {
                    EntityCockatrice cockatrice = (EntityCockatrice) entity;
                    if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
                        if (attacker instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) attacker;
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
    }

    private static void signalAmphithereAlarm(EntityLivingBase villager, EntityLivingBase attacker) {
        float d0 = IceAndFire.CONFIG.amphithereVillagerSearchLength;
        List<Entity> list = villager.world.getEntitiesWithinAABB(EntityAmphithere.class, (new AxisAlignedBB(villager.posX - 1.0D, villager.posY - 1.0D, villager.posZ - 1.0D, villager.posX + 1.0D, villager.posY + 1.0D, villager.posZ + 1.0D)).grow(d0, d0, d0));
        Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(attacker));
        if (!list.isEmpty()) {
            Iterator<Entity> itr = list.iterator();
            while (itr.hasNext()) {
                Entity entity = itr.next();
                if (entity instanceof EntityAmphithere && !(attacker instanceof EntityAmphithere)) {
                    EntityAmphithere amphithere = (EntityAmphithere) entity;
                    if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
                        if (attacker instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) attacker;
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
    }

    public static float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }

    public static boolean isLivestock(Entity entity) {
        String className = entity.getClass().getSimpleName();
        return entity instanceof EntityCow || entity instanceof EntitySheep || entity instanceof EntityPig || entity instanceof EntityChicken
                || entity instanceof EntityRabbit || entity instanceof AbstractHorse
                || className.contains("Cow") || className.contains("Sheep") || className.contains("Pig") || className.contains("Chicken")
                || className.contains("Rabbit") || className.contains("Peacock") || className.contains("Goat") || className.contains("Ferret")
                || className.contains("Hedgehog") || className.contains("Peahen") || className.contains("Peafowl") || className.contains("Sow")
                || className.contains("Hog") || className.contains("Hog");
    }

    public static boolean isVillager(Entity entity) {
        String className = entity.getClass().getSimpleName();
        return entity instanceof INpc || className.contains("VillagerMCA") || className.contains("MillVillager") || className.contains("Citizen");
    }

    public static boolean isAnimaniaSheep(Entity entity) {
        String className = entity.getClass().getName();
        return className.contains("sheep") || entity instanceof EntitySheep;
    }

    public static boolean isAnimaniaChicken(Entity entity) {
        String className = entity.getClass().getName();
        return (className.contains("chicken") || entity instanceof EntityChicken) && entity instanceof EntityLiving && !entity.isCreatureType(EnumCreatureType.MONSTER, false);
    }

    public static boolean isAnimaniaFerret(Entity entity) {
        String className = entity.getClass().getName();
        return className.contains("ferret") || className.contains("polecat");
    }

    protected static boolean isExceptionBlockForAttaching(Block attachBlock) {
        return attachBlock instanceof BlockShulkerBox || attachBlock instanceof BlockLeaves || attachBlock instanceof BlockTrapDoor || attachBlock == Blocks.BEACON || attachBlock == Blocks.CAULDRON || attachBlock == Blocks.GLASS || attachBlock == Blocks.GLOWSTONE || attachBlock == Blocks.ICE || attachBlock == Blocks.SEA_LANTERN || attachBlock == Blocks.STAINED_GLASS;
    }

    protected static boolean isExceptBlockForAttachWithPiston(Block attachBlock) {
        return isExceptionBlockForAttaching(attachBlock) || attachBlock == Blocks.PISTON || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
    }

    @SubscribeEvent
    public void onArrowCollide(ProjectileImpactEvent event) {
        if (event.getEntity() instanceof EntityArrow && ((EntityArrow) event.getEntity()).shootingEntity != null) {
            if (event.getRayTraceResult() != null && event.getRayTraceResult().entityHit != null) {
                Entity shootingEntity = ((EntityArrow) event.getEntity()).shootingEntity;
                Entity shotEntity = event.getRayTraceResult().entityHit;
                if (shootingEntity instanceof EntityLivingBase && shootingEntity.isRidingOrBeingRiddenBy(shotEntity)) {
                    if (shotEntity instanceof EntityTameable && ((EntityTameable) shotEntity).isTamed() && shotEntity.isOnSameTeam(shootingEntity)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttackMob(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityMutlipartPart && event.getEntity() instanceof EntityPlayer) {
            event.setCanceled(true);
            EntityLivingBase parent = ((EntityMutlipartPart) event.getTarget()).getParent();
            ((EntityPlayer) event.getEntity()).attackTargetEntityWithCurrentItem(parent);
            int extraData = 0;
            if(event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra){
                extraData = ((EntityHydraHead)event.getTarget()).headIndex;
                ((EntityHydra) parent).triggerHeadFlags(extraData);
            }
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getEntityId(), extraData));

        }
    }

    @SubscribeEvent
    public void onGatherCollisionBoxes(GetCollisionBoxesEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof IPhasesThroughBlock) {
            Iterator<AxisAlignedBB> itr = event.getCollisionBoxesList().iterator();
            while (itr.hasNext()) {
                AxisAlignedBB aabb = itr.next();
                BlockPos pos = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
                if (((IPhasesThroughBlock) event.getEntity()).canPhaseThroughBlock(event.getWorld(), pos)) {
                    itr.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), MiscEntityProperties.class);
            if (properties.hasDismountedDragon) {
                event.setDamageMultiplier(0);
                properties.hasDismountedDragon = false;
            }
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        /*
        if (event.getEntityBeingMounted() instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) event.getEntityBeingMounted();
            if (event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote) {
                EntityPlayer player = (EntityPlayer) event.getEntityMounting();
                if (dragon.isOwner((EntityPlayer) event.getEntityMounting())) {
                    dragon.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
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
                    player.setPosition(dragon.posX + extraX, dragon.posY + extraY, dragon.posZ + extraZ);
                }
            }

        }
        if (event.getEntityBeingMounted() instanceof EntityHippogryph) {
            EntityHippogryph hippogryph = (EntityHippogryph) event.getEntityBeingMounted();
            if (event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote && hippogryph.isOwner((EntityPlayer) event.getEntityMounting())) {
                EntityPlayer player = (EntityPlayer) event.getEntityMounting();
                hippogryph.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
            }
        }
         */
    }

    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().isProjectile()) {
            float multi = 1;
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (event.getSource() == IceAndFire.dragonFire || event.getSource() == IceAndFire.dragonIce) {
            float multi = 1;
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor) {
                multi -= 0.1;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor) {
                multi -= 0.3;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor) {
                multi -= 0.2;
            }
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemScaleArmor) {
                multi -= 0.1;
            }
            event.setAmount(event.getAmount() * multi);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityWitherSkeleton) {
            event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,
                    new ItemStack(IafItemRegistry.witherbone, event.getEntityLiving().getRNG().nextInt(2))));
        }

        if (event.getEntityLiving() instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void onEntityDespawn(LivingSpawnEvent.AllowDespawn event) {
        if (event.getEntityLiving() instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null) {
            Entity attacker = event.getSource().getTrueSource();
            MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(attacker, MiscEntityProperties.class);
            if (properties != null && properties.inLoveTicks > 0) {
                event.setCanceled(true);
            }
            if (isAnimaniaChicken(event.getEntityLiving()) && attacker instanceof EntityLivingBase) {
                signalChickenAlarm(event.getEntityLiving(), (EntityLivingBase) attacker);
            }
            if (DragonUtils.isVillager(event.getEntityLiving()) && attacker instanceof EntityLivingBase) {
                signalAmphithereAlarm(event.getEntityLiving(), (EntityLivingBase) attacker);
            }

        }

    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null) {
            EntityLivingBase attacker = event.getEntityLiving();
            if (isAnimaniaChicken(event.getTarget())) {
                signalChickenAlarm(event.getTarget(), attacker);
            }
            if (DragonUtils.isVillager(event.getTarget())) {
                signalAmphithereAlarm(event.getTarget(), attacker);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.getTarget() != null && isAnimaniaSheep(event.getTarget())) {
            float dist = IceAndFire.CONFIG.cyclopesSheepSearchLength;
            List<Entity> list = event.getTarget().world.getEntitiesWithinAABBExcludingEntity(event.getEntityPlayer(), event.getEntityPlayer().getEntityBoundingBox().expand(dist, dist, dist));
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(event.getEntityPlayer()));
            if (!list.isEmpty()) {
                Iterator<Entity> itr = list.iterator();
                while (itr.hasNext()) {
                    Entity entity = itr.next();
                    if (entity instanceof EntityCyclops) {
                        EntityCyclops cyclops = (EntityCyclops) entity;
                        if (!cyclops.isBlinded() && !event.getEntityPlayer().capabilities.isCreativeMode) {
                            cyclops.setAttackTarget(event.getEntityPlayer());
                        }
                    }
                }
            }
        }
        if (event.getTarget() instanceof EntityLiving) {
            boolean stonePlayer = event.getTarget() instanceof EntityStoneStatue;
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getTarget(), StoneEntityProperties.class);
            if (properties != null && properties.isStone || stonePlayer) {
                ((EntityLiving) event.getTarget()).setHealth(((EntityLiving) event.getTarget()).getMaxHealth());
                if (event.getEntityPlayer() != null) {
                    ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
                    if (stack.getItem() != null && (stack.getItem().canHarvestBlock(Blocks.STONE.getDefaultState()) || stack.getItem().getTranslationKey().contains("pickaxe"))) {
                        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                        boolean ready = false;
                        if (properties != null && !stonePlayer) {
                            properties.breakLvl++;
                            ready = properties.breakLvl > 9;
                        }
                        event.getTarget().playSound(SoundEvents.BLOCK_STONE_HIT, 1, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.setCanceled(true);
                        if (stonePlayer) {
                            EntityStoneStatue statue = (EntityStoneStatue) event.getTarget();
                            statue.setCrackAmount(statue.getCrackAmount() + 1);
                            ready = statue.getCrackAmount() > 9;
                        }
                        if (ready) {
                            event.getTarget().playSound(SoundEvents.BLOCK_STONE_BREAK, 1, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                            event.getTarget().setDead();
                            if (silkTouch) {
                                ItemStack statuette = new ItemStack(IafItemRegistry.stone_statue);
                                statuette.setTagCompound(new NBTTagCompound());
                                statuette.getTagCompound().setBoolean("IAFStoneStatueEntityPlayer", stonePlayer);
                                statuette.getTagCompound().setInteger("IAFStoneStatueEntityID", stonePlayer ? 90 : EntityList.getID(event.getTarget().getClass()));
                                ((EntityLiving) event.getTarget()).writeEntityToNBT(statuette.getTagCompound());
                                if (!event.getTarget().world.isRemote) {
                                    event.getTarget().entityDropItem(statuette, 1);
                                }
                            } else {
                                if (!((EntityLiving) event.getTarget()).world.isRemote) {
                                    event.getTarget().dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 2 + event.getEntityLiving().getRNG().nextInt(4));
                                }
                            }
                            event.getTarget().world.removeEntityDangerously(event.getTarget());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), ChainEntityProperties.class);
        if (chainProperties != null) {
            chainProperties.minimizeLists();
            if (!event.getEntity().world.isRemote) {
                EntityItem entityitem = new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY + (double) 1, event.getEntity().posZ, new ItemStack(IafItemRegistry.chain, chainProperties.connectedEntities.size()));
                entityitem.setDefaultPickupDelay();
                event.getEntity().world.spawnEntity(entityitem);
            }
            chainProperties.clearChained();
        }
        if (event.getEntityLiving().getUniqueID().equals(ServerEvents.ALEX_UUID)) {
            event.getEntityLiving().entityDropItem(new ItemStack(IafItemRegistry.weezer_blue_album), 1);
        }
    }

    @SubscribeEvent
    public void onEntityStopUsingItem(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() instanceof ItemDeathwormGauntlet || event.getItem().getItem() instanceof ItemCockatriceScepter) {
            event.setDuration(20);
        }
    }

    @SubscribeEvent
    public void onEntityRightClickBlock(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() == Item.getItemFromBlock(Blocks.TORCH) && event.getEntityPlayer().dimension == IceAndFire.CONFIG.dreadlandsDimensionId) {
            event.setCanceled(true);
            if (Blocks.TORCH.canPlaceBlockAt(event.getWorld(), event.getPos())) {
                IBlockState state = IafBlockRegistry.burnt_torch.getDefaultState();
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    if (canTorchPlaceAt(event.getWorld(), event.getPos(), enumfacing)) {
                        state = state.withProperty(BlockBurntTorch.FACING, enumfacing);
                    }
                }
                event.getWorld().setBlockState(event.getPos(), state);
            }
        }
    }

    private boolean canTorchPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

        if (facing.equals(EnumFacing.UP) && this.canPlaceTorchOn(worldIn, blockpos)) {
            return true;
        } else if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
            return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID;
        } else {
            return false;
        }
    }

    private boolean canPlaceTorchOn(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    @SubscribeEvent
    public void onEntityUseItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().rotationPitch > 87 && event.getEntityLiving().getRidingEntity() != null && event.getEntityLiving().getRidingEntity() instanceof EntityDragonBase) {
            ((EntityDragonBase) event.getEntityLiving().getRidingEntity()).processInteract((EntityPlayer) event.getEntityLiving(), event.getHand());
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), ChainEntityProperties.class);
        if (chainProperties != null && chainProperties.isChained()) {
            if (chainProperties.wasJustDisconnected) {
                chainProperties.wasJustDisconnected = false;
            }
            if (!event.getEntityLiving().world.isRemote) {
                chainProperties.updateConnectedEntities(event.getEntityLiving());
                for (Entity chainer : chainProperties.connectedEntities) {
                    float f = event.getEntityLiving().getDistance(chainer);
                    if (f > 7) {
                        double d0 = (chainer.posX - event.getEntityLiving().posX) / (double) f;
                        double d1 = (chainer.posY - event.getEntityLiving().posY) / (double) f;
                        double d2 = (chainer.posZ - event.getEntityLiving().posZ) / (double) f;
                        event.getEntityLiving().motionX += d0 * Math.abs(d0) * 0.4D;
                        event.getEntityLiving().motionY += d1 * Math.abs(d1) * 0.2D;
                        event.getEntityLiving().motionZ += d2 * Math.abs(d2) * 0.4D;
                    }
                }
            }
        }
        if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemSeaSerpentArmor || event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSeaSerpentArmor || event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemSeaSerpentArmor || event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemSeaSerpentArmor) {
            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 50, 0, false, false));
            if (event.getEntityLiving().isWet()) {
                int headMod = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int chestMod = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int legMod = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                int footMod = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemSeaSerpentArmor ? 1 : 0;
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 50, headMod + chestMod + legMod + footMod - 1, false, false));


            }
        }
        if (IceAndFire.CONFIG.chickensLayRottenEggs && !event.getEntityLiving().world.isRemote && isAnimaniaChicken(event.getEntityLiving()) && !event.getEntityLiving().isChild() && event.getEntityLiving() instanceof EntityAnimal) {
            ChickenEntityProperties chickenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), ChickenEntityProperties.class);
            if (chickenProps != null) {
                if (chickenProps.timeUntilNextEgg < 0) {
                    chickenProps.timeUntilNextEgg = 0;
                }
                if (chickenProps.timeUntilNextEgg == 0) {
                    if (event.getEntityLiving().getRNG().nextInt(IceAndFire.CONFIG.cockatriceEggChance + 1) == 0 && event.getEntityLiving().ticksExisted > 30) {
                        event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        event.getEntityLiving().dropItem(IafItemRegistry.rotten_egg, 1);
                    }
                    chickenProps.timeUntilNextEgg = chickenProps.generateTime();
                } else if (chickenProps.timeUntilNextEgg > 0) {
                    chickenProps.timeUntilNextEgg--;
                }
            }

        }
        FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), FrozenEntityProperties.class);
        if (frozenProps != null) {
            boolean prevFrozen = frozenProps.isFrozen;
            if (event.getEntityLiving() instanceof EntityIceDragon) {
                frozenProps.isFrozen = false;
            }
            if (!event.getEntityLiving().world.isRemote) {
                if (frozenProps.isFrozen && event.getEntityLiving().isBurning()) {
                    frozenProps.isFrozen = false;
                    event.getEntityLiving().extinguish();
                }
                if (event.getEntityLiving().deathTime > 0) {
                    frozenProps.isFrozen = false;
                }
                if (frozenProps.ticksUntilUnfrozen > 0) {
                    frozenProps.ticksUntilUnfrozen--;
                } else {
                    frozenProps.ticksUntilUnfrozen = 0;
                    frozenProps.isFrozen = false;
                }
            }
            if (frozenProps.isFrozen && !(event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).isCreative())) {
                event.getEntityLiving().motionX *= 0.25;
                event.getEntityLiving().motionZ *= 0.25;
                if (!(event.getEntityLiving() instanceof EntityDragon) && !event.getEntityLiving().onGround) {
                    event.getEntityLiving().motionY -= 0.2D;
                }
            }
            if (prevFrozen != frozenProps.isFrozen) {
                if (frozenProps.isFrozen) {
                    event.getEntityLiving().playSound(SoundEvents.BLOCK_GLASS_PLACE, 1, 1);
                } else {
                    for (int i = 0; i < 15; i++) {
                        event.getEntityLiving().world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, event.getEntityLiving().posX + ((rand.nextDouble() - 0.5D) * event.getEntityLiving().width), event.getEntityLiving().posY + ((rand.nextDouble()) * event.getEntityLiving().height), event.getEntityLiving().posZ + ((rand.nextDouble() - 0.5D) * event.getEntityLiving().width), 0, 0, 0, Block.getIdFromBlock(IafBlockRegistry.dragon_ice));
                    }
                    event.getEntityLiving().playSound(SoundEvents.BLOCK_GLASS_BREAK, 3, 1);
                }
            }
        }

        if (event.getEntityLiving() instanceof EntityPlayer || event.getEntityLiving() instanceof EntityVillager || event.getEntityLiving() instanceof IHearsSiren) {
            SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), SirenEntityProperties.class);
            if (sirenProps != null && sirenProps.sirenID != 0) {
                EntitySiren closestSiren = sirenProps.getSiren(event.getEntityLiving().world);
                if (closestSiren != null && closestSiren.isActuallySinging()) {
                    if (EntitySiren.isWearingEarplugs(event.getEntityLiving()) || sirenProps.singTime > IceAndFire.CONFIG.sirenMaxSingTime) {
                        sirenProps.isCharmed = false;
                        sirenProps.sirenID = 0;
                        sirenProps.singTime = 0;
                        closestSiren.singCooldown = IceAndFire.CONFIG.sirenTimeBetweenSongs;
                    } else {
                        sirenProps.isCharmed = true;
                        sirenProps.singTime++;
                        if (rand.nextInt(7) == 0) {
                            for (int i = 0; i < 5; i++) {
                                event.getEntityLiving().world.spawnParticle(EnumParticleTypes.HEART, event.getEntityLiving().posX + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posY + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posZ + ((rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
                            }
                        }
                        EntityLivingBase entity = event.getEntityLiving();
                        if (entity.collidedHorizontally) {
                            if (entity instanceof EntityLiving) {
                                ((EntityLiving) entity).getJumpHelper().setJumping();
                            } else if (entity.onGround) {
                                entity.motionY = 0.42F;
                            }
                        }
                        entity.motionX += (Math.signum(closestSiren.posX - entity.posX) * 0.5D - entity.motionX) * 0.100000000372529;
                        entity.motionY += (Math.signum(closestSiren.posY - entity.posY + 1) * 0.5D - entity.motionY) * 0.100000000372529;
                        entity.motionZ += (Math.signum(closestSiren.posZ - entity.posZ) * 0.5D - entity.motionZ) * 0.100000000372529;
                        float angle = (float) (Math.atan2(entity.motionZ, entity.motionX) * 180.0D / Math.PI) - 90.0F;
                        double d0 = closestSiren.posX - entity.posX;
                        double d2 = closestSiren.posZ - entity.posZ;
                        double d1 = closestSiren.posY - 1 - entity.posY;
                        if (entity.isRiding()) {
                            entity.dismountRidingEntity();
                        }
                        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                        float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                        float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                        if(!(entity instanceof EntityPlayer)){
                            entity.rotationPitch = updateRotation(entity.rotationPitch, f1, 30F);
                            entity.rotationYaw = updateRotation(entity.rotationYaw, f, 30F);
                        }
                        if (entity.getDistance(closestSiren) < 5D) {
                            sirenProps.isCharmed = false;
                            sirenProps.sirenID = 0;
                            sirenProps.singTime = 0;
                            closestSiren.singCooldown = IceAndFire.CONFIG.sirenTimeBetweenSongs;
                            closestSiren.setSinging(false);
                            closestSiren.setAttackTarget(entity);
                            closestSiren.setAggressive(true);
                            closestSiren.triggerOtherSirens(entity);
                        }
                        if (closestSiren.isDead || entity.getDistance(closestSiren) > EntitySiren.SEARCH_RANGE * 2 || sirenProps.getSiren(event.getEntityLiving().world) == null || entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
                            sirenProps.isCharmed = false;
                            sirenProps.sirenID = 0;
                            sirenProps.singTime = 0;
                        }
                    }
                }
            }
        }

        if (event.getEntityLiving() instanceof EntityLiving) {
            boolean stonePlayer = event.getEntityLiving() instanceof EntityStoneStatue;
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
            if (properties != null && properties.isStone || stonePlayer) {
                EntityLiving living = ((EntityLiving) event.getEntityLiving());
                if (!living.getPassengers().isEmpty()) {
                    for (Entity e : living.getPassengers()) {
                        e.dismountRidingEntity();
                    }
                }
                living.motionX *= 0D;
                living.motionZ *= 0D;
                living.motionY -= 0.1D;
                living.swingProgress = 0;
                living.limbSwing = 0;
                living.setInvisible(!stonePlayer);
                living.livingSoundTime = 0;
                living.hurtTime = 0;
                living.hurtResistantTime = living.maxHurtResistantTime - 1;
                living.extinguish();
                if (living instanceof EntityAnimal) {
                    ((EntityAnimal) living).resetInLove();
                }
                if (!living.isAIDisabled()) {
                    living.setNoAI(true);
                }
                if (living.getAttackTarget() != null) {
                    living.setAttackTarget(null);
                }
                if (living instanceof EntityHorse) {
                    EntityHorse horse = (EntityHorse) living;
                    horse.tailCounter = 0;
                    horse.setEatingHaystack(false);
                }
            }
        }
        MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), MiscEntityProperties.class);
        if (properties != null && properties.entitiesWeAreGlaringAt.size() > 0) {
            Iterator<Entity> itr = properties.entitiesWeAreGlaringAt.iterator();
            while (itr.hasNext()) {
                Entity next = itr.next();
                double d5 = 80F;
                double d0 = next.posX - event.getEntityLiving().posX;
                double d1 = next.posY + (double) (next.height * 0.5F) - (event.getEntityLiving().posY + (double) event.getEntityLiving().getEyeHeight() * 0.5D);
                double d2 = next.posZ - event.getEntityLiving().posZ;
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d0 = d0 / d3;
                d1 = d1 / d3;
                d2 = d2 / d3;
                double d4 = this.rand.nextDouble();
                while (d4 < d3) {
                    d4 += 1.0D;
                    event.getEntityLiving().world.spawnParticle(EnumParticleTypes.SPELL_MOB, event.getEntityLiving().posX + d0 * d4, event.getEntityLiving().posY + d1 * d4 + (double) event.getEntityLiving().getEyeHeight() * 0.5D, event.getEntityLiving().posZ + d2 * d4, 0.0D, 0.0D, 0.0D, 3484199);
                }
                ((EntityLivingBase) next).addPotionEffect(new PotionEffect(MobEffects.WITHER, 40, 2));
                if (event.getEntityLiving().ticksExisted % 20 == 0) {
                    properties.specialWeaponDmg++;
                    next.attackEntityFrom(DamageSource.WITHER, 2);
                }
                if (next == null || !next.isEntityAlive()) {
                    itr.remove();
                }
            }
        }
        if (properties != null && properties.glarers.size() > 0) {
            Iterator<Entity> itr = properties.glarers.iterator();
            while (itr.hasNext()) {
                Entity next = itr.next();
                if (next instanceof EntityLivingBase && !EntityGorgon.isEntityLookingAt((EntityLivingBase) next, event.getEntityLiving(), 0.2F)) {
                    MiscEntityProperties theirProperties = EntityPropertiesHandler.INSTANCE.getProperties(next, MiscEntityProperties.class);
                    theirProperties.entitiesWeAreGlaringAt.remove(event.getEntityLiving());
                    itr.remove();

                }
            }
        }
        if (properties != null && properties.inLoveTicks > 0) {
            properties.inLoveTicks--;
            if (event.getEntityLiving() instanceof EntityLiving) {
                ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
            }
            if (rand.nextInt(7) == 0) {
                for (int i = 0; i < 5; i++) {
                    event.getEntityLiving().world.spawnParticle(EnumParticleTypes.HEART, event.getEntityLiving().posX + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posY + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posZ + ((rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getTarget(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.setCanceled(true);
            }
        }
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(event.getTarget(), ChainEntityProperties.class);
        if (chainProperties != null) {
            //chainProperties.debug();
            chainProperties.updateConnectedEntities(event.getTarget());
            if (chainProperties.isChained() && chainProperties.isConnectedToEntity(event.getTarget(), event.getEntityPlayer())) {
                chainProperties.removeChain(event.getTarget(), event.getEntityPlayer());
                if (!event.getWorld().isRemote) {
                    event.getTarget().dropItem(IafItemRegistry.chain, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityLiving) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getTarget(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer() != null && (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockChest)) {
            float dist = IceAndFire.CONFIG.dragonGoldSearchLength;
            List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getEntityPlayer(), event.getEntityPlayer().getEntityBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                Iterator<Entity> itr = list.iterator();
                while (itr.hasNext()) {
                    Entity entity = itr.next();
                    if (entity instanceof EntityDragonBase) {
                        EntityDragonBase dragon = (EntityDragonBase) entity;
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getEntityPlayer()) && !event.getEntityPlayer().capabilities.isCreativeMode) {
                            dragon.setSleeping(false);
                            dragon.setSitting(false);
                            dragon.setAttackTarget(event.getEntityPlayer());
                        }
                    }
                }
            }
        }
        if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockWall) {
            ItemChain.attachToFence(event.getEntityPlayer(), event.getWorld(), event.getPos());
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().getBlock() instanceof BlockChest || event.getState().getBlock() == IafBlockRegistry.goldPile || event.getState().getBlock() == IafBlockRegistry.silverPile)) {
            float dist = IceAndFire.CONFIG.dragonGoldSearchLength;
            List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getEntityBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                Iterator<Entity> itr = list.iterator();
                while (itr.hasNext()) {
                    Entity entity = itr.next();
                    if (entity instanceof EntityDragonBase) {
                        EntityDragonBase dragon = (EntityDragonBase) entity;
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getPlayer()) && !event.getPlayer().capabilities.isCreativeMode) {
                            dragon.setSleeping(false);
                            dragon.setSitting(false);
                            dragon.setAttackTarget(event.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChestGenerated(LootTableLoadEvent event) {
        if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON) || event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)
                || event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID) || event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)
                || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CROSSING)) {
            LootCondition chance = new RandomChance(0.4f);
            LootEntryItem item = new LootEntryItem(IafItemRegistry.manuscript, 20, 5, new LootFunction[0], new LootCondition[0], "iceandfire:manuscript");
            LootPool pool = new LootPool(new LootEntry[]{item}, new LootCondition[]{chance}, new RandomValueRange(1, 5), new RandomValueRange(0, 3), "manuscript");
            event.getTable().addPool(pool);
        }
        if (IceAndFire.CONFIG.generateSilverOre && (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON) || event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)
                || event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID) || event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)
                || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR) || event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CROSSING)
                || event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST) || event.getName().equals(LootTableList.CHESTS_WOODLAND_MANSION)
                || event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))) {
            LootCondition chance = new RandomChance(0.2f);
            LootEntryItem silver = new LootEntryItem(IafItemRegistry.silverIngot, 15, 12, new LootFunction[0], new LootCondition[0], "iceandfire:silver_ingot");
            LootEntryItem nugget = new LootEntryItem(IafItemRegistry.silverNugget, 20, 6, new LootFunction[0], new LootCondition[0], "iceandfire:silver_nugget");
            LootPool pool = new LootPool(new LootEntry[]{silver, nugget}, new LootCondition[]{chance}, new RandomValueRange(1, 3), new RandomValueRange(1, 2), "silver_ingot");
            event.getTable().addPool(pool);
        }
        if ((event.getName().equals(WorldGenFireDragonCave.FIREDRAGON_CHEST)
                || event.getName().equals(WorldGenFireDragonCave.FIREDRAGON_MALE_CHEST)
                || event.getName().equals(WorldGenIceDragonCave.ICEDRAGON_CHEST)
                || event.getName().equals(WorldGenIceDragonCave.ICEDRAGON_MALE_CHEST))) {
            LootCondition chance = new RandomChance(0.01f);
            LootEntryItem silver = new LootEntryItem(IafItemRegistry.weezer_blue_album, 1, 20, new LootFunction[0], new LootCondition[0], "iceandfire:weezer");
            LootPool pool = new LootPool(new LootEntry[]{silver}, new LootCondition[]{chance}, new RandomValueRange(1, 1), new RandomValueRange(1, 1), "weezer");
            event.getTable().addPool(pool);
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null && !event.player.getPassengers().isEmpty()) {
            Iterator<Entity> itr = event.player.getPassengers().iterator();
            while (itr.hasNext()) {
                (itr.next()).dismountRidingEntity();
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        try {
            if (event.getEntity() instanceof EntityLivingBase) {
                try {
                    ChainEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), ChainEntityProperties.class);
                    if (properties != null) {
                        properties.updateConnectedEntities(event.getEntity());
                    }
                } catch (Exception e) {
                    IceAndFire.logger.warn("could not instantiate chain properties for " + event.getEntity().getName());
                }
            }
            if (event.getEntity() != null && isAnimaniaSheep(event.getEntity()) && event.getEntity() instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) event.getEntity();
                animal.tasks.addTask(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
            }
            if (event.getEntity() != null && isVillager(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFire.CONFIG.villagersFearDragons) {
                EntityCreature villager = (EntityCreature) event.getEntity();
                villager.tasks.addTask(1, new VillagerAIFearUntamed(villager, EntityLivingBase.class, VILLAGER_FEAR, 8.0F, 0.8D, 0.8D));
            }
            if (event.getEntity() != null && isLivestock(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFire.CONFIG.animalsFearDragons) {
                EntityCreature animal = (EntityCreature) event.getEntity();
                animal.tasks.addTask(1, new VillagerAIFearUntamed(animal, EntityLivingBase.class, new Predicate<EntityLivingBase>() {
                    public boolean apply(@Nullable EntityLivingBase entity) {
                        return entity != null && entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(animal);
                    }
                }, 12.0F, 1.2D, 1.5D));
            }
        } catch (Exception e) {
            IceAndFire.logger.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }
}
