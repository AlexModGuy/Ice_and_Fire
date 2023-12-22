package com.github.alexthe666.iceandfire.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class ShapeBuilder {
    Iterable<BlockPos> blocks;

    ShapeBuilder() {
    }

    public static ShapeBuilder start() {
        return new ShapeBuilder();
    }

    public ShapeBuilder getAllInSphereMutable(int radius, BlockPos center) {
        return getAllInSphereMutable(radius, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInSphereMutable(int radius, int c1, int c2, int c3) {
        return getAllInCutOffSphereMutable(radius, radius, c1, c2, c3);
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, BlockPos center) {
        return getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, int c1, int c2, int c3) {
        return getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, c1, c2, c3);
    }
    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, BlockPos center) {
        return getAllInCutOffSphereMutable(radiusX, yCutOffMax, yCutOffMin, center.getX(), center.getY(), center.getZ());
    }
    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, int c1, int c2, int c3) {
        int r2 = radiusX * radiusX;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            private int currRX = radiusX;
            private int currRY = yCutOffMax;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-currRY > yCutOffMin) {
                    return this.endOfData();
                } else {
                    if (isWithinRange(currRX, currRY, phase, offset, r2)) {
                        BlockPos pos = this.mutablePos.set(c1 + currRX, c2 + currRY, c3 + phase * offset);
                        offset++;
                        return pos;
                    } else {
                        if (phase == 1) {
                            phase = -1;
                            offset = 1;
                        } else if (phase == -1) {
                            phase = 1;
                            offset = 0;
                            currRX--;
                        }
                        if (-currRX > radiusX) {
                            currRY--;
                            currRX = radiusX;
                        }
                        return this.computeNext();
                    }
                }
            }

            private boolean isWithinRange(int currentRadiusX, int currentRadiusY, int phase, int offset, int radius2) {
                return Math.round((double) currentRadiusX * currentRadiusX + currentRadiusY * currentRadiusY + (phase * offset) * (phase * offset)) <= radius2;
            }
        };
        return this;
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOff, RandomSource rand, BlockPos center) {
        return getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, yCutOff, rand, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int ycutoffmin, RandomSource rand, int c1, int c2, int c3) {
        return getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, ycutoffmin, ycutoffmin, rand, c1, c2, c3);
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOffMax, int yCutOffMin, RandomSource rand, int c1, int c2, int c3) {
        int maxr2 = maxRadiusX * maxRadiusX;
        int minr2 = minRadiusX * minRadiusX;
        float rDifference = (float) minRadiusX / maxRadiusX;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            private int currRX = maxRadiusX;
            private int currRY = yCutOffMax;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-currRY > yCutOffMin) {
                    return this.endOfData();
                } else {
                    int distance = distance(currRX, currRY, phase, offset);
                    if (distance <= minr2 || distance <= maxr2 * Mth.clamp(rand.nextFloat(), rDifference, 1.0F)) {
                        BlockPos pos = this.mutablePos.set(c1 + currRX, c2 + currRY, c3 + phase * offset);
                        offset++;
                        return pos;
                    } else if (distance <= maxr2) {
                        offset++;
                        return this.computeNext();
                    } else {
                        if (phase == 1) {
                            phase = -1;
                            offset = 1;
                        } else if (phase == -1) {
                            phase = 1;
                            offset = 0;
                            currRX--;
                        }
                        if (-currRX > maxRadiusX) {
                            currRY--;
                            currRX = maxRadiusX;
                        }
                        return this.computeNext();
                    }
                }
            }

            private int distance(int currentRadiusX, int currentRadiusY, int phase, int offset) {
                return (int) Math.round((double) currentRadiusX * currentRadiusX + currentRadiusY * currentRadiusY + (phase * offset) * (phase * offset));
            }
        };
        return this;
    }

    public ShapeBuilder getAllInCircleMutable(int radius, int c1, int c2, int c3) {
        int r2 = radius * radius;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            private int totalAmount;
            private int currR = radius;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-currR > radius) {
                    return this.endOfData();
                } else {
                    if (isWithinRange(currR, phase, offset, r2)) {
                        BlockPos pos = this.mutablePos.set(c1 + currR, c2, c3 + phase * offset);
                        offset++;
                        return pos;
                    } else {
                        if (phase == 1) {
                            phase = -1;
                            offset = 1;
                        } else if (phase == -1) {
                            phase = 1;
                            offset = 0;
                            currR--;
                        }
                        return this.computeNext();
                    }
                }
            }

            private boolean isWithinRange(int currentRadius, int phase, int offset, int radius2) {
                return Math.floor((double) currentRadius * currentRadius + (phase * offset) * (phase * offset)) <= radius2;
            }
        };
        return this;
    }

    public Stream<BlockPos> toStream(boolean parallel) {
        return StreamSupport.stream(blocks.spliterator(), parallel);
    }

    public Iterable<BlockPos> toIterable() {
        return this.blocks;
    }

}


