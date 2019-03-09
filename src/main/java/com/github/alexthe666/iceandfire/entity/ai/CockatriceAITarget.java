package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;

public class CockatriceAITarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
	private EntityCockatrice cockatrice;

	public CockatriceAITarget(EntityCockatrice entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
		super(entityIn, classTarget, 0, checkSight, false, targetSelector);
		this.cockatrice = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		if (this.taskOwner.getRNG().nextInt(20) != 0 || this.cockatrice.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.cockatrice.getClass())) {
			if (this.targetEntity instanceof EntityPlayer && !cockatrice.isOwner(this.targetEntity)) {
				return !cockatrice.isTamed();
			} else {
				if (!cockatrice.isOwner(this.targetEntity) && cockatrice.canMove()) {
					return true;
				}
			}
		}
		return false;
	}
}