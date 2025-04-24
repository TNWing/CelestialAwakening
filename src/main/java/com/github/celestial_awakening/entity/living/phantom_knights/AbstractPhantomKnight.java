package com.github.celestial_awakening.entity.living.phantom_knights;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AbstractPhantomKnight extends AbstractCAMonster {


    private final ServerBossEvent bossEvent=new ServerBossEvent(Component.translatable("entity.celestial_awakening.pk_crescencia"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    protected AbstractPhantomKnight(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward=400;
        this.bossBarWindup=0;
        this.bossEvent.setProgress(0);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (!this.level().isClientSide){
            ServerLevel level= (ServerLevel) this.level();
            if (tag.hasUUID("PrevTarget")){
                level.getEntity(tag.getUUID("PrevTarget"));
            }

        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getTarget()!=null){
            tag.putUUID("PrevTarget",this.getTarget().getUUID());
        }
        else{
            //tag.putUUID("PrevTarget",null);
        }
    }

    @Override
    public boolean canStillSenseTarget() {
        return super.canStillSenseTarget();
    }
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (isCombatActive){
            if (true||bossBarWindup>=100){
                this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            }
            else{
                if (bossBarWindup<10){
                    ServerLevel serverLevel= (ServerLevel) this.level();
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
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        } else if (source.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else if (this.isSleeping() && !this.level().isClientSide) {
                this.stopSleeping();
            }
        if (bossBarWindup<100){
            amt*=0.05f;
        }
        if (source.getEntity()!=null){
            if (this.distanceTo(source.getEntity())> Config.pkDmgResDist){
                amt*=0.2f;
            }
        }
        if (this.getActionId()<=0){
            amt*=0.4f;
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
        if (isCombatActive || this.bossBarWindup>=10 || true){
            this.bossEvent.addPlayer(serverPlayer);
            if (bossBarWindup>=100 || true){
                this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            }
            else{
                this.bossEvent.setProgress(bossBarWindup/100f);
            }
        }
    }
}
