package com.github.alexthe666.iceandfire.client.model.util;

public enum EnumDragonPoses implements IEnumDragonPoses {
	MALE("base_male"),
	FEMALE("base_female"),
	FLYING_POSE("flying"),
	DIVING_POSE("diving"),
	GROUND_POSE("ground"),
	HOVERING_POSE("hovering"),
	SITTING_POSE("sitting"),
	SLEEPING_POSE("sleeping"),
	SWIM_POSE("swimming"),
	SWIM1("swim1"),
	SWIM2("swim2"),
	SWIM3("swim3"),
	SWIM4("swim4"),
	SWIM5("swim5"),
	BITE1("bite1"),
	BITE2("bite2"),
	BITE3("bite3"),
	BLAST_BREATH("attack_blast_breath"),
	BLAST_CHARGE1("attack_blast_charge1"),
	BLAST_CHARGE2("attack_blast_charge2"),
	BLAST_CHARGE3("attack_blast_charge3"),
	STREAM_BREATH("attack_stream_breath"),
	STREAM_CHARGE1("attack_stream_charge1"),
	STREAM_CHARGE2("attack_stream_charge2"),
	STREAM_CHARGE3("attack_stream_charge3"),
	DEAD("dead"),
	GRAB1("grab1"),
	GRAB2("grab2"),
	GRAB_SHAKE1("grab_shake1"),
	GRAB_SHAKE2("grab_shake2"),
	GRAB_SHAKE3("grab_shake3"),
	ROAR1("roar1"),
	ROAR2("roar2"),
	ROAR3("roar3"),
	EPIC_ROAR1("epic_roar1"),
	EPIC_ROAR2("epic_roar2"),
	EPIC_ROAR3("epic_roar3"),
	TAIL_WHIP1("tail1"),
	TAIL_WHIP2("tail2"),
	TAIL_WHIP3("tail3"),
	WING_BLAST1("wing_blast1"),
	WING_BLAST2("wing_blast2"),
	WING_BLAST3("wing_blast3"),
	WING_BLAST4("wing_blast4"),
	WING_BLAST5("wing_blast5"),
	WING_BLAST6("wing_blast6"),
	WING_BLAST7("wing_blast7"),
	WALK1("walk1"),
	WALK2("walk2"),
	WALK3("walk3"),
	WALK4("walk4"),
	FLIGHT1("flight1"),
	FLIGHT2("flight2"),
	FLIGHT3("flight3"),
	FLIGHT4("flight4"),
	FLIGHT5("flight5"),
	FLIGHT6("flight6"),
	TACKLE("attack_tackle"),
	SIT_ON_PLAYER_POSE("sitting_shoulder");

	private final String pose;

	EnumDragonPoses(String pose) {
		this.pose = pose;
	}

	@Override
	public String getPose() {
		return pose;
	}
}
