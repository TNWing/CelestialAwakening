package com.github.celestial_awakening.events.raids;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class AbstractCARaid {
    public int getRaidID() {
        return raidID;
    }

    public void setRaidID(int raidID) {
        this.raidID = raidID;
    }

    public int getRaidLvl() {
        return raidLvl;
    }

    public void setRaidLvl(int raidLvl) {
        this.raidLvl = raidLvl;
    }

    public ServerLevel getServerLevel() {
        return serverLevel;
    }

    public void setServerLevel(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    private ServerLevel serverLevel;
    private int raidLvl;
    private int raidID;
/*
    public CompoundTag save(CompoundTag p_37748_) {
        /*
        Vanilla raid data



        p_37748_.putInt("Id", this.id);
        p_37748_.putBoolean("Started", this.started);
        p_37748_.putBoolean("Active", this.active);
        p_37748_.putLong("TicksActive", this.ticksActive);
        p_37748_.putInt("BadOmenLevel", this.badOmenLevel);
        p_37748_.putInt("GroupsSpawned", this.groupsSpawned);
        p_37748_.putInt("PreRaidTicks", this.raidCooldownTicks);
        p_37748_.putInt("PostRaidTicks", this.postRaidTicks);
        p_37748_.putFloat("TotalHealth", this.totalHealth);
        p_37748_.putInt("NumGroups", this.numGroups);
        p_37748_.putString("Status", this.status.getName());
        p_37748_.putInt("CX", this.center.getX());
        p_37748_.putInt("CY", this.center.getY());
        p_37748_.putInt("CZ", this.center.getZ());
        ListTag listtag = new ListTag();

        for(UUID uuid : this.heroesOfTheVillage) {
            listtag.add(NbtUtils.createUUID(uuid));
        }

        p_37748_.put("HeroesOfTheVillage", listtag);


        return p_37748_;
    }
     */

}
