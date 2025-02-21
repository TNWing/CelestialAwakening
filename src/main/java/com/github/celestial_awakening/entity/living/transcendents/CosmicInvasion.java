package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class CosmicInvasion {
    private int invasionLevel;
    private final ServerLevel serverLevel;
    private final LivingEntityCapability cap;
    public CosmicInvasion(ServerLevel level, LivingEntityCapability cap, int invasionLevel){
        this.invasionLevel=invasionLevel;
        this.serverLevel=level;
        this.cap=cap;
    }
    //attempts to
    public boolean startInvasion(){
        return false;
    }

    public List<BlockPos> validPositions(){
        return null;
    }
}
