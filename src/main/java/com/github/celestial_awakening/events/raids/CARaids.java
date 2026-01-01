package com.github.celestial_awakening.events.raids;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.raid.Raids;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public class CARaids {
    private final Map<Integer, ProwlerRaid> prowlerRaids = Maps.newHashMap();
    private int nextID=0;
    public void tick() {
        Iterator<ProwlerRaid> prowlerRaidIterator = this.prowlerRaids.values().iterator();
        while(prowlerRaidIterator.hasNext()) {
            ProwlerRaid raid=prowlerRaidIterator.next();
            if (raid.isDone()){
                prowlerRaidIterator.remove();
            }
            else{
                raid.tick();
            }
        }
    }

    public ProwlerRaid getProwlerRaidFromID(int id){
        return prowlerRaids.get(id);
    }

    public ProwlerRaid getOrCreateProwlerRaid(ServerLevel level, LevelCapability cap, BlockPos pos, byte mW, int str){
        ProwlerRaid raid= cap.raids.getNearbyProwlerRaid(pos,6400);
        return raid==null? raid:new ProwlerRaid(++nextID,level,pos,mW,str);//int p_37692_, ServerLevel p_37693_, BlockPos p_37694_, int maxWaves,int str
    }
    public ProwlerRaid createProwlerRaid(ServerLevel level, BlockPos pos, byte mW, int str){
        return new ProwlerRaid(++nextID,level,pos,mW,str);//int p_37692_, ServerLevel p_37693_, BlockPos p_37694_, int maxWaves,int str
    }
    public ProwlerRaid getNearbyProwlerRaid(ServerLevel level,LevelCapability cap, BlockPos pos){
        System.out.println(cap.raids.prowlerRaids.size() + " IS P RAID SIZE");
        ProwlerRaid raid= cap.raids.getNearbyProwlerRaid(pos,6400);
        System.out.println("Found :  " + raid);
        return raid;
    }

    public void addRaidToMap(ProwlerRaid raid){
        prowlerRaids.put(raid.getRaidID(),raid);
        System.out.println("RAID AT " + raid.getCenterPos());
        System.out.println("new PROWLER RAID SIZE IS " + prowlerRaids.size());
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

    public void removeProwlerRaid(int id){
        prowlerRaids.remove(id);
    }
/*

    private boolean active;

 */
    public CompoundTag saveRaids(){
        CompoundTag parentTag=new CompoundTag();
        ListTag pRaids=new ListTag();
        for (ProwlerRaid prowlerRaid:prowlerRaids.values()) {
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putInt("id",prowlerRaid.getRaidID());
            compoundTag.putInt("str",prowlerRaid.getRaidStrength());
            compoundTag.putByte("currentWave",prowlerRaid.getCurrentWave());
            compoundTag.putByte("maxWave",prowlerRaid.getMaxWave());
            compoundTag.putInt("status",prowlerRaid.getStatus().ordinal());
            compoundTag.putInt("delayTicks",prowlerRaid.getDelayTicks());
            compoundTag.putInt("waveInterval",prowlerRaid.getNextWaveInterval());
            CompoundTag pos=new CompoundTag();
            BlockPos center=prowlerRaid.getCenterPos();
            pos.putInt("x",center.getX());
            pos.putInt("y",center.getY());
            pos.putInt("z",center.getZ());
            compoundTag.put("center",pos);
            compoundTag.putLong("wtt",prowlerRaid.getWarningTriggerTime());
            pRaids.add(compoundTag);
        }
        //prowlerRaids.add()
        parentTag.put("prowler",pRaids);
        return parentTag;
    }

    public void loadRaids(CompoundTag tag,ServerLevel serverLevel){
        if (serverLevel!=null){
            ListTag prowlerRaids=(ListTag)tag.get("prowler");
            if (prowlerRaids!=null){
                for (int i=0;i<prowlerRaids.size();i++){
                    CompoundTag pTag=prowlerRaids.getCompound(i);
                    CompoundTag pos=pTag.getCompound("center");
                    ProwlerRaid prowlerRaid=new ProwlerRaid(pTag.getInt("id"),serverLevel,
                            new BlockPos(pos.getInt("x"),pos.getInt("y"),pos.getInt("z")),
                            pTag.getByte("maxWave"),pTag.getInt(("str")));
                    prowlerRaid.setStatus(AbstractCARaid.RaidStatus.values()[pTag.getInt(("status"))]);
                    prowlerRaid.setWarningTriggerTime(pTag.getLong("wtt"));
                    prowlerRaid.setNextWaveInterval(pTag.getInt("waveInterval"));
                    prowlerRaid.setCurrentWave(pTag.getByte("currentWave"));
                    prowlerRaid.setDelayTicks(pTag.getInt("delayTicks"));
                    addRaidToMap(prowlerRaid);
                }
            }
        }

    }
}
