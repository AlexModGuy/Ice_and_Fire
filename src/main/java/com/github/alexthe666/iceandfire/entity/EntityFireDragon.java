package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModSounds;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityFireDragon extends EntityDragonBase{
	public static final double baseHealth = 10;
	public static final double baseDamage = 1;
	public static final double baseSpeed = 0.25D;
	public static final double maxHealth = 700;
	public static final double maxDamage = 20;
	public static final double maxSpeed = 0.6D;
	public EntityFireDragon(World worldIn) {
		super(worldIn);
		this.setSize(2.78F, 1.4F);
        this.tasks.addTask(1, new EntityAISwimming(this));
		minSize = 0.3F;
		maxSize = 8.58F;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}
	@Override
	public String getTexture() {
		if(this.getSleeping() == 1){
			return "iceandfire:textures/models/firedragon/" + this.getColorName() + this.getStage() + "_sleep.png";
		}else{
			return "iceandfire:textures/models/firedragon/" + this.getColorName() + this.getStage() + ".png";
		}
	}

	private String getColorName() {
		switch(this.getColor()){
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
		this.setDragonAge(100);
		this.updateSize();
		this.jump();
		this.setColor(this.getRNG().nextInt(5));
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(baseSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(baseHealth);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(baseDamage);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
		updateSize();
	}

	@Override
	public void updateSize()
	{
		double healthStep;
		double attackStep;
		double speedStep;
		healthStep = (this.maxHealth - this.baseHealth) / (126);
		attackStep = (this.maxDamage - this.baseDamage) / (126);
		speedStep = (this.maxSpeed - this.baseSpeed) / (126);

		if (this.getDragonAge() <= 125)
		{
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.round(this.baseHealth + (healthStep * this.getDragonAge())));
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.round(this.baseDamage + (attackStep * this.getDragonAge())));
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.baseSpeed + (speedStep * this.getDragonAge()));

			if (this.isTeen())
			{
				this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
			}
			else if (this.isAdult())
			{
				this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2.0D);
			}
			else
			{
				this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
			}
		}
	}
	
	@Override
	public void onInventoryChanged(InventoryBasic inventory) {
		
	}
 
	protected SoundEvent getAmbientSound()
    {
        return this.isTeen() ? ModSounds.firedragon_teen_idle : this.isAdult() ? ModSounds.firedragon_adult_idle : ModSounds.firedragon_child_idle;
    }

    protected SoundEvent getHurtSound()
    {
        return this.isTeen() ? ModSounds.firedragon_teen_hurt : this.isAdult() ? ModSounds.firedragon_adult_hurt : ModSounds.firedragon_child_hurt;
    }

    protected SoundEvent getDeathSound()
    {
        return this.isTeen() ? ModSounds.firedragon_teen_death : this.isAdult() ? ModSounds.firedragon_adult_death : ModSounds.firedragon_child_death;
    }

}
