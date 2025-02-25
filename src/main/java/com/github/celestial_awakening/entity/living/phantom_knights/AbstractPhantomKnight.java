package com.github.celestial_awakening.entity.living.phantom_knights;

import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractPhantomKnight extends AbstractCALivingEntity {


    private final ServerBossEvent bossEvent=new ServerBossEvent(Component.translatable("entity.celestial_awakening.pk_crescencia"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    protected AbstractPhantomKnight(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward=40;
        this.bossBarWindup=0;
        this.bossEvent.setProgress(0);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.putInt("BossBarWindup",bossBarWindup);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.bossBarWindup=tag.getInt("BossBarWindup");
    }

    @Override
    public boolean canStillSenseTarget() {
        return super.canStillSenseTarget();
    }
    /*TODO
        the 3 miniboss phantom knights are these
        -Crescencia (crescent phase)
        -Gideon (gibbous phase)
        -Halsey (half)


        the big boss is
        Fulton(full)
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Stream<WrappedGoal> runningGoals=this.goalSelector.getRunningGoals();
        if (isCombatActive){
            if (bossBarWindup>=100){
                this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            }
            else{
                if (bossBarWindup<10){
                    ServerLevel serverLevel= (ServerLevel) this.level();
                    //Vec3 offset=new Vec3()
                    ServerChunkCache chunkSource=serverLevel.getChunkSource();
                    ChunkMap chunkMap=chunkSource.chunkMap;
                    List<ServerPlayer> serverPlayers = chunkMap.getPlayers(this.chunkPosition(),false);
                    for (ServerPlayer serverPlayer:serverPlayers) {
                        if (serverPlayer.hasLineOfSight(this)){
                            this.bossEvent.addPlayer(serverPlayer);
                            //((ServerGamePacketListenerImpl) serverPlayer.connection).send(new ClientboundAddEntityPacket(this));
                        }
                    }
                }
                this.bossEvent.setProgress(bossBarWindup/100f);
                bossBarWindup++;
            }
        }
        else{
            this.heal(0.1f);
        }
    }

    @Override
    public boolean hurt(DamageSource source,float amt){
        if (bossBarWindup<100){
            amt*=0.05f;
        }
        if (source.getEntity()!=null){
            if (this.distanceToSqr(source.getEntity())>15*15){
                amt*=0.2f;
            }
        }

        boolean returnVal=super.hurt(source,amt);
        return returnVal;
    }

    public void startUpBossBar(){
        this.bossEvent.setProgress(bossBarWindup*this.getMaxHealth()/100);
    }

    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        if (isCombatActive){
            this.bossEvent.addPlayer(serverPlayer);
        }
    }
}
