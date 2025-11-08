package com.github.celestial_awakening.events.raids;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;

import javax.annotation.Nullable;
import java.util.Map;

public class CARaids {
    private final Map<Integer, ProwlerRaid> prowlerRaids = Maps.newHashMap();
    public void tick() {

    }

    public ProwlerRaid getProwlerRaidFromID(int id){
        return prowlerRaids.get(id);
    }

    public ProwlerRaid getOrCreateProwlerRaid(LevelCapability cap, BlockPos pos){
        return cap.raids.getNearbyProwlerRaid(pos,9216);
    }
    public static ProwlerRaid loadProwlerRaids(ServerLevel p_150236_, CompoundTag p_150237_) {
        Raids raids = new Raids(p_150236_);
        /*
        raids.nextAvailableID = p_150237_.getInt("NextAvailableID");
        raids.tick = p_150237_.getInt("Tick");
        ListTag listtag = p_150237_.getList("Raids", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            Raid raid = new Raid(p_150236_, compoundtag);
            raids.raidMap.put(raid.getId(), raid);
        }

        return raids;

         */
        return null;
    }
    @Nullable
    public ProwlerRaid getNearbyProwlerRaid(BlockPos p_37971_, int p_37972_) {
        ProwlerRaid raid = null;
        double d0 = p_37972_;

        for(ProwlerRaid raid1 : this.prowlerRaids.values()) {
            double d1 = raid1.getCenterPos().distSqr(p_37971_);
            if (raid1.isActive() && d1 < d0) {
                raid = raid1;
                d0 = d1;
            }
        }

        return raid;
    }
}
