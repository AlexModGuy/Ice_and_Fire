package com.github.alexthe666.iceandfire.animation;

import net.ilexiconn.llibrary.common.animation.Animation;

public class AnimationBlend extends Animation{

	public boolean blend; 
	
	public AnimationBlend(int id, int d, boolean blend) {
		super(id, d);
		this.blend = blend;
	}

}
