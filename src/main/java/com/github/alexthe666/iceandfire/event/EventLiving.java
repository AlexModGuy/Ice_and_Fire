package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EventLiving {

	private boolean stepHeightSwitched = false;

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			MiscPlayerProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), MiscPlayerProperties.class);
			if(properties.hasDismountedDragon){
				event.setDamageMultiplier(0);
				properties.hasDismountedDragon = false;
			}
		}
	}
	@SubscribeEvent
	public void onEntityMount(EntityMountEvent event) {
		if(event.getEntityBeingMounted() instanceof EntityDragonBase){
			EntityDragonBase dragon = (EntityDragonBase)event.getEntityBeingMounted();
			if(event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote){
				EntityPlayer player = (EntityPlayer)event.getEntityMounting();
				if(dragon.isOwner((EntityPlayer)event.getEntityMounting())){
					dragon.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
					player.fallDistance = -dragon.height;
				} else{
					dragon.renderYawOffset = dragon.rotationYaw;
					float modTick_0 = dragon.getAnimationTick() - 25;
					float modTick_1 = dragon.getAnimationTick() > 25 && dragon.getAnimationTick() < 55 ? 8 * MathHelper.clamp(MathHelper.sin((float) (Math.PI + modTick_0 * 0.25)), -0.8F, 0.8F) : 0;
					float modTick_2 = dragon.getAnimationTick() > 30 ? 10 : Math.max(0, dragon.getAnimationTick() - 20);
					float radius = 0.75F * (0.6F * dragon.getRenderSize() / 3) * -3;
					float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (modTick_1 *2F) * 0.015F;
					double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
					double extraZ = (double) (radius * MathHelper.cos(angle));
					double extraY = modTick_2 == 0 ? 0 : 0.035F * ((dragon.getRenderSize() / 3) + (modTick_2 * 0.5 * (dragon.getRenderSize() / 3)));
					player.setPosition(dragon.posX + extraX, dragon.posY + extraY, dragon.posZ + extraZ);
				}
			}

		}
		if(event.getEntityBeingMounted() instanceof EntityHippogryph){
			EntityHippogryph hippogryph = (EntityHippogryph)event.getEntityBeingMounted();
			if(event.isDismounting() && event.getEntityMounting() instanceof EntityPlayer && !event.getEntityMounting().world.isRemote && hippogryph.isOwner((EntityPlayer)event.getEntityMounting())){
				EntityPlayer player = (EntityPlayer)event.getEntityMounting();
				hippogryph.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDamage(LivingHurtEvent event) {
		if(event.getSource().isProjectile()){
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

	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if (event.getEntityLiving() instanceof EntityWitherSkeleton) {
			event.getEntityLiving().dropItem(ModItems.witherbone, event.getEntityLiving().getRNG().nextInt(2));
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
		if(event.getSource().getTrueSource() != null){
			Entity attacker = event.getSource().getTrueSource();
			if (isAnimaniaChicken(event.getEntityLiving()) && attacker instanceof EntityLivingBase) {
				signalChickenAlarm(event.getEntityLiving(), (EntityLivingBase) attacker);
			}
		}
	}

	@SubscribeEvent
	public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
		if(event.getTarget() != null){
			EntityLivingBase attacker = event.getEntityLiving();
			if (isAnimaniaChicken(event.getTarget())) {
				signalChickenAlarm(event.getTarget(), attacker);
			}
		}
	}

	private static void signalChickenAlarm(EntityLivingBase chicken, EntityLivingBase attacker){
		float d0 = IceAndFire.CONFIG.cockatriceChickenSearchLength;
		List<Entity> list = chicken.world.getEntitiesWithinAABB(EntityCockatrice.class, (new AxisAlignedBB(chicken.posX, chicken.posY, chicken.posZ, chicken.posX + 1.0D, chicken.posY + 1.0D, chicken.posZ + 1.0D)).grow(d0, 10.0D, d0));
		Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(attacker));
		if (!list.isEmpty()) {
			Iterator<Entity> itr = list.iterator();
			while (itr.hasNext()) {
				Entity entity = itr.next();
				if (entity instanceof EntityCockatrice && !(attacker instanceof EntityCockatrice)) {
					EntityCockatrice cockatrice = (EntityCockatrice) entity;
					if(!DragonUtils.hasSameOwner(cockatrice, attacker)) {
						if (attacker instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) attacker;
							if (!player.isCreative() && !cockatrice.isOwner(player)) {
								cockatrice.setAttackTarget(player);
							}
						} else {
							cockatrice.setAttackTarget((EntityLivingBase) attacker);
						}
					}
				}
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
			StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties((EntityLiving) event.getTarget(), StoneEntityProperties.class);
			if (properties != null && properties.isStone || stonePlayer) {
				((EntityLiving) event.getTarget()).setHealth(((EntityLiving) event.getTarget()).getMaxHealth());
				if (event.getEntityPlayer() != null) {
					ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
					if (stack.getItem() != null && (stack.getItem() instanceof ItemPickaxe || stack.getItem().canHarvestBlock(Blocks.STONE.getDefaultState()))) {
						boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
						boolean ready = false;
						if (properties != null && !stonePlayer) {
							properties.breakLvl++;
							ready = properties.breakLvl > 9;
						}
						if (stonePlayer) {
							EntityStoneStatue statue = (EntityStoneStatue) event.getTarget();
							statue.setCrackAmount(statue.getCrackAmount() + 1);
							ready = statue.getCrackAmount() > 9;
						}
						if (ready) {
							event.getTarget().setDead();
							if (silkTouch) {
								ItemStack statuette = new ItemStack(ModItems.stone_statue);
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
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityUseItem(PlayerInteractEvent.RightClickItem event){
		if(event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().rotationPitch > 87 && event.getEntityLiving().getRidingEntity() != null && event.getEntityLiving().getRidingEntity() instanceof EntityDragonBase){
			((EntityDragonBase) event.getEntityLiving().getRidingEntity()).processInteract((EntityPlayer)event.getEntityLiving(), event.getHand());
		}
	}

	Random rand = new Random();
	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if(!event.getEntityLiving().world.isRemote && isAnimaniaChicken(event.getEntityLiving()) && !event.getEntityLiving().isChild() && event.getEntityLiving() instanceof EntityAnimal){
			ChickenEntityProperties chickenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), ChickenEntityProperties.class);
			if(chickenProps != null){
				if(chickenProps.timeUntilNextEgg == 0) {
					if (event.getEntityLiving().getRNG().nextInt(IceAndFire.CONFIG.cockatriceEggChance) == 0) {
						event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
						event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
						event.getEntityLiving().dropItem(ModItems.rotten_egg, 1);
					}
					chickenProps.timeUntilNextEgg = chickenProps.generateTime();
				}else{
					chickenProps.timeUntilNextEgg--;
				}
			}

		}
		FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), FrozenEntityProperties.class);
		if(frozenProps != null){
			boolean prevFrozen = frozenProps.isFrozen;
			if(event.getEntityLiving() instanceof EntityIceDragon){
				frozenProps.isFrozen = false;
			}
			if(!event.getEntityLiving().world.isRemote) {
				if(frozenProps.isFrozen && event.getEntityLiving().isBurning()){
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
			if(frozenProps.isFrozen && !(event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer)event.getEntityLiving()).isCreative())){
				event.getEntityLiving().motionX *= 0.25;
				event.getEntityLiving().motionZ *= 0.25;
				event.getEntityLiving().motionY -= 0.1D;
			}
			if(prevFrozen != frozenProps.isFrozen){
				if(frozenProps.isFrozen){
					event.getEntityLiving().playSound(SoundEvents.BLOCK_GLASS_PLACE, 1, 1);
				}else{
					for (int i = 0; i < 15; i++) {
						event.getEntityLiving().world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, event.getEntityLiving().posX + ((rand.nextDouble() - 0.5D) * event.getEntityLiving().width), event.getEntityLiving().posY + ((rand.nextDouble()) * event.getEntityLiving().height), event.getEntityLiving().posZ + ((rand.nextDouble() - 0.5D) * event.getEntityLiving().width), 0, 0, 0, new int[]{Block.getIdFromBlock(ModBlocks.dragon_ice)});
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
					stepHeightSwitched = false;
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
						entity.motionX += (Math.signum(closestSiren.posX - entity.posX) * 0.5D - entity.motionX) * 0.100000000372529;
						entity.motionY += (Math.signum(closestSiren.posY - entity.posY + 1) * 0.5D - entity.motionY) * 0.100000000372529;
						entity.motionZ += (Math.signum(closestSiren.posZ - entity.posZ) * 0.5D - entity.motionZ) * 0.100000000372529;
						float angle = (float) (Math.atan2(entity.motionZ, entity.motionX) * 180.0D / Math.PI) - 90.0F;
						entity.stepHeight = 1;
						double d0 = closestSiren.posX - entity.posX;
						double d2 = closestSiren.posZ - entity.posZ;
						double d1 = closestSiren.posY - 1 - entity.posY;
						if (entity.isRiding()) {
							entity.dismountRidingEntity();
						}
						if (entity.onGround && entity.collidedHorizontally) {
							entity.motionY = 0.42F;

							if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
								entity.motionY += (double) ((float) (entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
							}

							if (entity.isSprinting()) {
								float f = entity.rotationYaw * 0.017453292F;
								entity.motionX -= (double) (MathHelper.sin(f) * 0.2F);
								entity.motionZ += (double) (MathHelper.cos(f) * 0.2F);
							}

							entity.isAirBorne = true;
							net.minecraftforge.common.ForgeHooks.onLivingJump(entity);
						}
						double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
						float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
						float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
						entity.rotationPitch = updateRotation(entity.rotationPitch, f1, 30F);
						entity.rotationYaw = updateRotation(entity.rotationYaw, f, 30F);
						if (entity.getDistance(closestSiren) < 5D) {
							sirenProps.isCharmed = false;
							sirenProps.sirenID = 0;
							sirenProps.singTime = 0;
							closestSiren.singCooldown = IceAndFire.CONFIG.sirenTimeBetweenSongs;
							closestSiren.setSinging(false);
							closestSiren.setAttackTarget((EntityLivingBase) entity);
							closestSiren.setAggressive(true);
							closestSiren.triggerOtherSirens((EntityLivingBase) entity);
						}
						if (closestSiren.isDead || entity.getDistance(closestSiren) > EntitySiren.SEARCH_RANGE * 2 || sirenProps.getSiren(event.getEntityLiving().world) == null || entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
							sirenProps.isCharmed = false;
							sirenProps.sirenID = 0;
							sirenProps.singTime = 0;
						}
					}
				} else if (!sirenProps.isCharmed && !stepHeightSwitched) {
					event.getEntityLiving().stepHeight = 0.6F;
					stepHeightSwitched = true;
				}
			}
		}

		if (event.getEntityLiving() instanceof EntityLiving) {
			boolean stonePlayer = event.getEntityLiving() instanceof EntityStoneStatue;
			StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
			if (properties != null && properties.isStone || stonePlayer) {
				EntityLiving living = ((EntityLiving) event.getEntityLiving());
				if(!living.getPassengers().isEmpty()){
					for(Entity e : living.getPassengers()){
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
		@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
		if (event.getEntityLiving() instanceof EntityLiving) {
			StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
			if (properties != null && properties.isStone) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getEntityLiving() instanceof EntityLiving) {
			StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), StoneEntityProperties.class);
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
			Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(event.getEntityPlayer()));
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
	}


	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		if (event.getPlayer() != null && (event.getState().getBlock() == ModBlocks.goldPile || event.getState().getBlock() == ModBlocks.silverPile)) {
			float dist = IceAndFire.CONFIG.dragonGoldSearchLength;
			List<Entity> list = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getPlayer(), event.getPlayer().getEntityBoundingBox().expand(dist, dist, dist));
			Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(event.getPlayer()));
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
		if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			final LootPool pool1 = event.getTable().getPool("pool1");
			if (pool1 != null) {
				pool1.addEntry(new LootEntryItem(ModItems.manuscript, 10, 5, new LootFunction[0], new LootCondition[0], "iceandfire:manuscript"));
			}
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
		if(event.getEntity() != null && isAnimaniaSheep(event.getEntity()) && event.getEntity() instanceof EntityAnimal){
			EntityAnimal animal = (EntityAnimal)event.getEntity();
			animal.tasks.addTask(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
		}
		if(event.getEntity() != null && DragonUtils.isVillager(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFire.CONFIG.villagersFearDragons){
			EntityCreature villager = (EntityCreature)event.getEntity();
			villager.tasks.addTask(1, new VillagerAIFearUntamed(villager, EntityLivingBase.class, new Predicate<EntityLivingBase>(){
			public boolean apply(@Nullable EntityLivingBase entity) {
				return entity != null && entity instanceof IVillagerFear;
			}
        	}, 12.0F, 0.8D, 0.8D));
		}
		if(event.getEntity() != null && DragonUtils.isLivestock(event.getEntity()) && event.getEntity() instanceof EntityCreature && IceAndFire.CONFIG.animalsFearDragons){
			EntityCreature animal = (EntityCreature)event.getEntity();
			animal.tasks.addTask(1, new VillagerAIFearUntamed(animal, EntityLivingBase.class, new Predicate<EntityLivingBase>(){
				public boolean apply(@Nullable EntityLivingBase entity) {
					return entity != null && entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(animal);
				}
			}, 12.0F, 1.2D, 1.5D));
		}
	}

	public static boolean isAnimaniaSheep(Entity entity){
		String className = entity.getClass().getName();
		return className.contains("sheep") || entity instanceof EntitySheep;
	}

	public static boolean isAnimaniaChicken(Entity entity){
		String className = entity.getClass().getName();
		return className.contains("chicken") || entity instanceof EntityChicken;
	}

	public static boolean isAnimaniaFerret(Entity entity){
		String className = entity.getClass().getName();
		return className.contains("ferret") || className.contains("polecat");
	}

	//@SubscribeEvent
	//public void onItemEvent(PlayerEvent.ItemPickupEvent event) {
	//	if (event.pickedUp.getItem().getItem() == ModItems.manuscript) {
	//		event.player.addStat(ModAchievements.manuscript, 1);
	//	}
	//	if (event.pickedUp.getItem().getItem() == Item.getItemFromBlock((ModBlocks.silverOre))) {
	//		event.player.addStat(ModAchievements.silver, 1);
	//	}
	//	if (event.pickedUp.getItem().getItem() == Item.getItemFromBlock((ModBlocks.sapphireOre))) {
	//		event.player.addStat(ModAchievements.sapphire, 1);
	//	}
	//}

	//@SubscribeEvent
	//public void onCraftEvent(PlayerEvent.ItemCraftedEvent event) {
	//	EntityPlayer player = event.player;
	//	if (event.crafting.getItem() == ModItems.bestiary) {
	//		player.addStat(ModAchievements.bestiary, 1);
	//	}
	//	if (event.crafting.getItem() == ModItems.silver_sword || event.crafting.getItem() == ModItems.silver_pickaxe || event.crafting.getItem() == ModItems.silver_axe || event.crafting.getItem() == ModItems.silver_shovel || event.crafting.getItem() == ModItems.silver_hoe) {
	//		player.addStat(ModAchievements.silverTool, 1);
	//	}
	//	if (event.crafting.getItem() == ModItems.dragon_horn || event.crafting.getItem() == ModItems.dragonbone_bow || event.crafting.getItem() == ModItems.dragonbone_sword || event.crafting.getItem() == ModItems.dragonbone_pickaxe || event.crafting.getItem() == ModItems.dragonbone_axe || event.crafting.getItem() == ModItems.dragonbone_shovel || event.crafting.getItem() == ModItems.dragonbone_hoe) {
	//		player.addStat(ModAchievements.boneTool, 1);
	//	}
	//	if (event.crafting.getItem() instanceof ItemDragonArmor) {
	//		player.addStat(ModAchievements.dragonArmor, 1);
	//	}
	//	if (event.crafting.getItem() == ModItems.dragonbone_sword_fire) {
	//		player.addStat(ModAchievements.fireSword, 1);
	//	}
	//	if (event.crafting.getItem() == ModItems.dragonbone_sword_ice) {
	//		player.addStat(ModAchievements.iceSword, 1);
	//	}
}
