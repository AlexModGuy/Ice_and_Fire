package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.world.World;

public class EntityFireDragon extends EntityDragonBase{
	public static final double baseHealth = 10;
	public static final double baseDamage = 1;
	public static final double baseSpeed = 0.25D;
	public static final double maxHealth = 700;
	public static final double maxDamage = 12;
	public static final double maxSpeed = 0.6D;
	public EntityFireDragon(World worldIn) {
		super(worldIn);
		this.setSize(2.78F, 0.9F);
        this.tasks.addTask(1, new EntityAISwimming(this));
		minSize = 0.3F;
		maxSize = 8.58F;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
		this.setScale(this.getDragonSize());
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
		this.setDragonAge(1);
		this.updateSize();
		this.jump();
		this.setColor(this.getRNG().nextInt(5));
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(baseSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(baseHealth);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(baseDamage);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(32D);
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
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Math.round(this.baseHealth + (healthStep * this.getDragonAge())));
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(Math.round(this.baseDamage + (attackStep * this.getDragonAge())));
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.baseSpeed + (speedStep * this.getDragonAge()));

			if (this.isTeen())
			{
				this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.5D);
			}
			else if (this.isAdult())
			{
				this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(2.0D);
			}
			else
			{
				this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.0D);
			}
		}
	}
	@Override
	public void onInventoryChanged(InventoryBasic inventory) {
		// TODO Auto-generated method stub
		
	}

    protected String getLivingSound()
    {
        return "iceandfire:firedragon." + (this.isTeen() ? "teen" : this.isAdult() ? "adult" : "child") + ".idle";
    }

    protected String getHurtSound()
    {
        return "iceandfire:firedragon." + (this.isTeen() ? "teen" : this.isAdult() ? "adult" : "child") + ".hurt";
    }

    protected String getDeathSound()
    {
        return "iceandfire:firedragon." + (this.isTeen() ? "teen" : this.isAdult() ? "adult" : "child") + ".death";
    }

}
