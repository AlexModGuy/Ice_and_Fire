package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.message.MessageStoneStatue;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ItemGorgonHead extends Item implements ICustomRendered {

	public ItemGorgonHead() {
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire.gorgon_head");
		this.maxStackSize = 1;
		this.setRegistryName(IceAndFire.MODID, "gorgon_head");
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
		double dist = 32;
		Vec3d vec3d = entity.getPositionEyes(1.0F);
		Vec3d vec3d1 = entity.getLook(1.0F);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
		double d1 = dist;
		Entity pointedEntity = null;
		List<Entity> list = worldIn.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(@Nullable Entity entity) {
				boolean blindness = entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS) || (entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) entity).canBeTurnedToStone());
				return entity != null && entity.canBeCollidedWith() && !blindness && (entity instanceof EntityPlayer || (entity instanceof EntityLiving && EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class) != null && !EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class).isStone));
			}
		}));
		double d2 = d1;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

			if (axisalignedbb.contains(vec3d)) {
				if (d2 >= 0.0D) {
					pointedEntity = entity1;
					d2 = 0.0D;
				}
			} else if (raytraceresult != null) {
				double d3 = vec3d.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity.canRiderInteract()) {
						if (d2 == 0.0D) {
							pointedEntity = entity1;
						}
					} else {
						pointedEntity = entity1;
						d2 = d3;
					}
				}
			}
		}
		if (pointedEntity != null) {
			if (pointedEntity instanceof EntityLiving || pointedEntity instanceof EntityPlayer) {
				if (pointedEntity instanceof EntityPlayer) {
					pointedEntity.playSound(ModSounds.GORGON_TURN_STONE, 1, 1);
					pointedEntity.attackEntityFrom(IceAndFire.gorgon, Integer.MAX_VALUE);
					EntityStoneStatue statue = new EntityStoneStatue(worldIn);
					statue.setPositionAndRotation(pointedEntity.posX, pointedEntity.posY, pointedEntity.posZ, pointedEntity.rotationYaw, pointedEntity.rotationPitch);
					statue.smallArms = true;
					if (!worldIn.isRemote) {
						worldIn.spawnEntity(statue);
					}
				} else {
					StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(pointedEntity, StoneEntityProperties.class);
					if (properties != null) {
						properties.isStone = true;
					}
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStoneStatue(pointedEntity.getEntityId(), true));
					if (pointedEntity instanceof EntityDragonBase) {
						EntityDragonBase dragon = (EntityDragonBase) pointedEntity;
						dragon.setFlying(false);
						dragon.setHovering(false);
						dragon.airTarget = null;
					}
					if (pointedEntity instanceof EntityHippogryph) {
						EntityHippogryph dragon = (EntityHippogryph) pointedEntity;
						dragon.setFlying(false);
						dragon.setHovering(false);
						dragon.airTarget = null;
					}
					if(pointedEntity instanceof IDropArmor){
						((IDropArmor) pointedEntity).dropArmor();
					}
				}

				if (pointedEntity instanceof EntityGorgon) {
					entity.playSound(ModSounds.GORGON_PETRIFY, 1, 1);
				} else {
					entity.playSound(ModSounds.GORGON_TURN_STONE, 1, 1);
				}
				SoundEvent deathSound = null;
				Method deathSoundMethod = ReflectionHelper.findMethod(EntityLivingBase.class, "getDeathSound", "func_184615_bR");
				try {
					deathSound = (SoundEvent) deathSoundMethod.invoke(pointedEntity, (Object[]) null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				if (deathSound != null) {
					entity.playSound(deathSound, 1, 1);

				}
				if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())) {
					stack.shrink(1);
				}
			}
		}
		stack.setItemDamage(0);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		int i = this.getMaxItemUseDuration(stack) - count;
		if (i > 20 && stack.getMetadata() == 0) {
			stack.setItemDamage(1);
		}
	}
}
