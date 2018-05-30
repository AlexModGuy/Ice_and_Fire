package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.github.alexthe666.iceandfire.entity.EntitySiren.isWearingEarplugs;

public class EventLiving {

	private boolean stepHeightSwitched = false;
	/*@SubscribeEvent
	public void onGetCollisionBoxes(GetCollisionBoxesEvent event) {
		if(event.getEntity() instanceof EntityDeathWorm && !event.getCollisionBoxesList().isEmpty()){
			for(AxisAlignedBB bb : event.getCollisionBoxesList()){
				BlockPos pos = new BlockPos(bb.minX, bb.minY, bb.minZ);
				if(event.getEntity().world.getBlockState(pos).getMaterial() == Material.SAND){
					event.getCollisionBoxesList().remove(bb);
				}
			}
		}
	}*/
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
					if(attacker instanceof EntityPlayer){
						EntityPlayer player = (EntityPlayer)attacker;
						if(!player.isCreative() && !cockatrice.isOwner(player)){
							cockatrice.setAttackTarget(player);
						}
					}
					else {
						cockatrice.setAttackTarget((EntityLivingBase)attacker);
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

	Random rand = new Random();
	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if(!event.getEntityLiving().world.isRemote && isAnimaniaChicken(event.getEntityLiving()) && event.getEntityLiving().getRNG().nextInt(1 + IceAndFire.CONFIG.cockatriceEggChance) == 0 && !event.getEntityLiving().isChild()){
			event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_HURT, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			event.getEntityLiving().playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			event.getEntityLiving().dropItem(ModItems.rotten_egg, 1);
		}
		SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), SirenEntityProperties.class);
		if(sirenProps != null && sirenProps.isCharmed){
			stepHeightSwitched = false;
			if(EntitySiren.isWearingEarplugs(event.getEntityLiving())){
				sirenProps.isCharmed = false;
			}
			if(sirenProps.getClosestSiren(event.getEntityLiving().world, event.getEntityLiving()) != null){
				if (rand.nextInt(7) == 0) {
					for(int i = 0; i < 5; i++){
						event.getEntityLiving().world.spawnParticle(EnumParticleTypes.HEART, event.getEntityLiving().posX + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posY + ((rand.nextDouble() - 0.5D) * 3), event.getEntityLiving().posZ + ((rand.nextDouble() - 0.5D) * 3), 0, 0, 0);
					}
				}
				EntityLivingBase entity = event.getEntityLiving();
				EntitySiren effectiveSiren = sirenProps.getClosestSiren(event.getEntityLiving().world, event.getEntityLiving());
				entity.motionX += (Math.signum(effectiveSiren.posX - entity.posX) * 0.5D - entity.motionX) * 0.100000000372529;
				entity.motionY += (Math.signum(effectiveSiren.posY - entity.posY + 1) * 0.5D - entity.motionY) * 0.100000000372529;
				entity.motionZ += (Math.signum(effectiveSiren.posZ - entity.posZ) * 0.5D - entity.motionZ) * 0.100000000372529;
				float angle = (float) (Math.atan2(entity.motionZ, entity.motionX) * 180.0D / Math.PI) - 90.0F;
				entity.stepHeight = 1;
				//entity.moveForward = 0.5F;
				double d0 = effectiveSiren.posX - entity.posX;
				double d2 = effectiveSiren.posZ - entity.posZ;
				double d1 = effectiveSiren.posY - 1 - entity.posY;
				if(entity.isRiding()){
					entity.dismountRidingEntity();
				}
				if(entity.onGround && entity.collidedHorizontally){
					entity.motionY =  0.42F;

					if (entity.isPotionActive(MobEffects.JUMP_BOOST))
					{
						entity.motionY += (double)((float)(entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
					}

					if (entity.isSprinting())
					{
						float f = entity.rotationYaw * 0.017453292F;
						entity.motionX -= (double)(MathHelper.sin(f) * 0.2F);
						entity.motionZ += (double)(MathHelper.cos(f) * 0.2F);
					}

					entity.isAirBorne = true;
					net.minecraftforge.common.ForgeHooks.onLivingJump(entity);
				}
				double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
				float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
				float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
				entity.rotationPitch = updateRotation(entity.rotationPitch, f1, 30F);
				entity.rotationYaw = updateRotation(entity.rotationYaw, f, 30F);
				if(entity.getDistance(effectiveSiren) < 5D){
					sirenProps.isCharmed = false;
					effectiveSiren.setSinging(false);
					effectiveSiren.setAttackTarget((EntityLivingBase) entity);
					effectiveSiren.setAggressive(true);
					effectiveSiren.triggerOtherSirens((EntityLivingBase) entity);
				}
				if(effectiveSiren.isDead || entity.getDistance(effectiveSiren) > 33 || sirenProps.getClosestSiren(event.getEntityLiving().world, event.getEntityLiving()) == null || entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()){
					sirenProps.isCharmed = false;
				}
			}
		}else if(sirenProps != null && !sirenProps.isCharmed && !stepHeightSwitched){
			event.getEntityLiving().stepHeight = 0.6F;
			stepHeightSwitched = true;
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
			System.out.println(properties.isStone);

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
		return className.contains("ferret");
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
