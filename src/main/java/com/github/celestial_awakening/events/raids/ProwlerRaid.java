package com.github.celestial_awakening.events.raids;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;

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

    public boolean attemptToStart(Player target){
        Level level=target.level();
        DifficultyInstance difficultyInstance= level.getCurrentDifficultyAt(target.blockPosition());
        float effectiveDifficulty=difficultyInstance.getEffectiveDifficulty();
        System.out.println("Effective diff is " + difficultyInstance);
        if (effectiveDifficulty>1.5f){
            int lY = level.getHeight(Heightmap.Types.WORLD_SURFACE, target.getBlockX(), target.getBlockZ());
            if (this.getCenterPos()!=null && this.getCenterPos().getY()>=lY-20){//will not attempt spawn if too far underground

                return true;
            }
        }
        return false;
    }


    /*
    code used to determine valid spawning blocks for illager raids
     */
    @Nullable
    private BlockPos findRandomSpawnPos(int p_37708_, int p_37709_) {
        int i = p_37708_ == 0 ? 2 : 2 - p_37708_;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i1 = 0; i1 < p_37709_; ++i1) {
            float f = this.getServerLevel().random.nextFloat() * ((float)Math.PI * 2F);
            int j = this.getCenterPos().getX() + Mth.floor(Mth.cos(f) * 32.0F * (float)i) + this.getServerLevel().random.nextInt(5);
            int l = this.getCenterPos().getZ() + Mth.floor(Mth.sin(f) * 32.0F * (float)i) + this.getServerLevel().random.nextInt(5);
            int k = this.getServerLevel().getHeight(Heightmap.Types.WORLD_SURFACE, j, l);
            blockpos$mutableblockpos.set(j, k, l);
            if (!this.getServerLevel().isVillage(blockpos$mutableblockpos) || p_37708_ >= 2) {
                int j1 = 10;
                if (this.getServerLevel()
                        .hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)
                        && this.getServerLevel().isPositionEntityTicking(blockpos$mutableblockpos)
                        && (
                                NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.getServerLevel(), blockpos$mutableblockpos, EntityType.RAVAGER)
                                        || this.getServerLevel().getBlockState(blockpos$mutableblockpos.below()).is(Blocks.SNOW) && this.getServerLevel().getBlockState(blockpos$mutableblockpos).isAir())) {
                    return blockpos$mutableblockpos;
                }
            }
        }

        return null;
    }
}
