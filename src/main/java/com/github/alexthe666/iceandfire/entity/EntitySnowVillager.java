package com.github.alexthe666.iceandfire.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.core.ModVillagers;

import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntitySnowVillager extends EntityVillager {

	private String professionName;
	private net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof;

	public EntitySnowVillager(World worldIn) {
		super(worldIn);
	}

	public EntitySnowVillager(World worldIn, int profession) {
		super(worldIn, profession);
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
		if (properties != null && properties.isStone) {
			return false;
		}
		return super.processInteract(player, hand);
	}

	public void setProfession(int professionId) {
		if (professionId > 2) {
			professionId = 2;
		}
		this.dataManager.set(PROFFESSION(), Integer.valueOf(professionId));

	}

	private DataParameter<Boolean> BABY() {
		Field field = ReflectionHelper.findField(EntityAgeable.class, ObfuscationReflectionHelper.remapFieldNames(EntityAgeable.class.getName(), new String[]{"BABY", "field_184751_bv"}));
		try {
			Field modifier = Field.class.getDeclaredField("modifiers");
			modifier.setAccessible(true);
			modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			DataParameter<Boolean> bay = (DataParameter<Boolean>) field.get(this);
			return bay;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private DataParameter<Integer> PROFFESSION() {
		Field field = ReflectionHelper.findField(EntityVillager.class, ObfuscationReflectionHelper.remapFieldNames(EntityVillager.class.getName(), new String[]{"PROFESSION", "field_184752_bw"}));
		try {
			Field modifier = Field.class.getDeclaredField("modifiers");
			modifier.setAccessible(true);
			modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			DataParameter<Integer> prof = (DataParameter<Integer>) field.get(this);
			return prof;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (HAND_STATES.equals(key) && this.world.isRemote) {
			if (this.isHandActive() && this.activeItemStack.isEmpty()) {
				this.activeItemStack = this.getHeldItem(this.getActiveHand());

				if (!this.activeItemStack.isEmpty()) {
					this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
				}
			} else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
				this.activeItemStack = ItemStack.EMPTY;
				this.activeItemStackUseCount = 0;
			}
		}
//field_184751_bv
		if (BABY().equals(key)) {
			this.setScaleForAge(this.isChild());
		}
//field_184752_bw

	}

	public void onDeath(DamageSource cause) {
		if (cause.getTrueSource() != null && cause.getTrueSource() instanceof EntityZombie && (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD)) {
			return;
		} else {
			super.onDeath(cause);
		}
	}

	@SuppressWarnings("deprecation")
	public void setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
		if (ModVillagers.INSTANCE.professions.containsValue(prof)) {
			this.setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.getId(prof));
		} else {
			ModVillagers.INSTANCE.setRandomProfession(this, this.world.rand);
		}
	}

	public EntityVillager createChild(EntityAgeable ageable) {
		EntitySnowVillager entityvillager = new EntitySnowVillager(this.world);
		entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null);
		return entityvillager;
	}

	public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfessionForge() {
		if (this.prof == null) {
			String p = this.getEntityData().getString("ProfessionName");
			if (p.isEmpty()) {
				this.prof = ModVillagers.INSTANCE.professions.get(this.getRNG().nextInt(3));

			} else {
				this.prof = ModVillagers.INSTANCE.professions.get(intFromProfesion(p));
			}
			try {
				ReflectionHelper.findField(EntityVillager.class, new String[]{"field_175563_bv", "careerId"}).set(this, 1);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this.prof;
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setProfession(compound.getInteger("Profession"));
		if (compound.hasKey("ProfessionName")) {
			net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession p =
					ModVillagers.INSTANCE.professions.get(intFromProfesion(compound.getString("ProfessionName")));
			if (p == null)
				p = ModVillagers.INSTANCE.professions.get(0);
			this.setProfession(p);
		}

	}

	public IEntityLivingData finalizeMobSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data, boolean forgeCheck) {
		this.prof = ModVillagers.INSTANCE.professions.get(this.getRNG().nextInt(3));
		return data;
	}


	private int intFromProfesion(String prof) {
		if (prof.contains("fisherman")) {
			return 0;
		}
		if (prof.contains("craftsman")) {
			return 1;
		}
		if (prof.contains("shaman")) {
			return 2;
		}
		return 0;
	}

}
