package com.github.celestial_awakening.events.raids;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;

public class ProwlerRaid extends AbstractCARaid{
    public BlockPos getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
    }

    private BlockPos centerPos;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;

    private int warningTriggerTime;

    private int delayTicks=60;
    public ProwlerRaid(int p_37692_, ServerLevel p_37693_, BlockPos p_37694_) {
        this.setRaidID(p_37692_);
        this.setServerLevel(p_37693_);
        this.active = true;
        //this.raidCooldownTicks = 300;
        //this.raidEvent.setProgress(0.0F);
        this.setCenterPos(p_37694_);
        //this.numGroups = this.getNumGroups(p_37693_.getDifficulty());
        //this.status = Raid.RaidStatus.ONGOING;
    }

    /*
Data to store
-raid id
-warning trigger time
-delay before start
-raid strength
-raid center pos

Important notes
-vanilla illagers store the raid ids, in order to associate themselves with raids. when said illager dies, it informs the raid of its death
-warning trigger time is the time when the warning of the raid played. as such, trigger time+delay will produce the time the raid should occur.


Config should have these settings
-raid warnings prevent sleeping
-raid prowlers despawn during day
 */
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

         */
        return p_37748_;
    }
}
