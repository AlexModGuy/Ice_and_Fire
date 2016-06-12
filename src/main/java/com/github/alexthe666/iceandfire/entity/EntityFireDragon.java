package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModSounds;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityFireDragon extends EntityDragonBase {

	public EntityFireDragon(World worldIn) {
		super(worldIn, 10, 1, 0.25, 700, 20, 0.6);
		this.setSize(2.78F, 1.4F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		minSize = 0.3F;
		maxSize = 8.58F;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}

	@Override
	public String getTexture() {
		if (this.getSleeping() == 1) {
			return "iceandfire:textures/models/firedragon/" + this.getColorName() + this.getStage() + "_sleep.png";
		} else {
			return "iceandfire:textures/models/firedragon/" + this.getColorName() + this.getStage() + ".png";
		}
	}

	private String getColorName() {
		switch (this.getColor()) {
		default:
			return "red_";
		case 1:
			return "green_";
		case 2:
			return "bronze_";
		case 3:
			return "gray_";
		}
	}

	@Override
	public void onSpawn() {
		this.setAgeInDays(100);
		this.updateAbilities();
		this.jump();
		this.setColor(this.getRNG().nextInt(5));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? ModSounds.firedragon_teen_idle : this.isAdult() ? ModSounds.firedragon_adult_idle : ModSounds.firedragon_child_idle;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return this.isTeen() ? ModSounds.firedragon_teen_hurt : this.isAdult() ? ModSounds.firedragon_adult_hurt : ModSounds.firedragon_child_hurt;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? ModSounds.firedragon_teen_death : this.isAdult() ? ModSounds.firedragon_adult_death : ModSounds.firedragon_child_death;
	}

	@Override
	public void onInventoryChanged(InventoryBasic invBasic) {

	}

}
