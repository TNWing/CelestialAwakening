package com.github.celestial_awakening.entity.living.transcendents;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class CosmicInvasion {
    private int invasionLevel;
    private final ServerLevel serverLevel;
    private final PlayerCapability cap;
    public CosmicInvasion(ServerLevel level, PlayerCapability cap, int invasionLevel){
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
