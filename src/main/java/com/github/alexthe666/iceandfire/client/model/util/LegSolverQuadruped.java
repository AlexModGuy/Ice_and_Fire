package com.github.alexthe666.iceandfire.client.model.util;
/*
  Code from JurassiCraft, used with permission
  By paul101
 */
public final class LegSolverQuadruped extends LegSolver {
    public final Leg backLeft, backRight, frontLeft, frontRight;

    public LegSolverQuadruped(float forward, float side) {
        this(0, forward, side);
    }

    public LegSolverQuadruped(float forwardCenter, float forward, float side) {
        super(
            new Leg(forwardCenter - forward, side),
            new Leg(forwardCenter - forward, -side),
            new Leg(forwardCenter + forward, side),
            new Leg(forwardCenter + forward, -side)
        );
        this.backLeft = this.legs[0];
        this.backRight = this.legs[1];
        this.frontLeft = this.legs[2];
        this.frontRight = this.legs[3];
    }
}
