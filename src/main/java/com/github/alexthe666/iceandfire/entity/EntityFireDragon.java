package com.github.alexthe666.iceandfire.entity;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.core.ModSounds;

import fossilsarcheology.api.EnumDiet;

public class EntityFireDragon extends EntityDragonBase {

	public EntityFireDragon(World worldIn) {
		super(worldIn, EnumDiet.CARNIVORE, 1, 18, 20, 700, 0.15F, 0.5F);
		this.setSize(1.78F, 1.2F);
		minimumSize = 0.3F;
		maximumSize = 8.58F;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}

	@Override
	protected void initEntityAI() {
		
	}

	@Override
	public String getTexture() {
		if (this.isSleeping()) {
			return "iceandfire:textures/models/firedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "_sleep";
		} else {
			return "iceandfire:textures/models/firedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "";
		}
	}

	public String getVariantName(int variant) {
		switch (variant) {
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

	@Override
	public String getTextureOverlay() {
		return null;
	}

}
