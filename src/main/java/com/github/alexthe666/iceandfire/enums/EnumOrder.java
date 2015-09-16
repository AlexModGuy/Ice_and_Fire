package com.github.alexthe666.iceandfire.enums;

public enum EnumOrder {
	WANDER,
	SIT,
	FOLLOW,
	SLEEP;
	public final EnumOrder next()
    {
        return this.values()[(this.ordinal() + 1) % this.values().length];
    }
}
