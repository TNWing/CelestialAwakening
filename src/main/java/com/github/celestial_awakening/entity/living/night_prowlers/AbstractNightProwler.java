package com.github.celestial_awakening.entity.living.night_prowlers;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.events.raids.ProwlerRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractNightProwler extends AbstractCAMonster {
    private static final EntityDataAccessor<Integer> INFUSE = SynchedEntityData.defineId(AbstractNightProwler.class, EntityDataSerializers.INT);
    protected HashMap<Integer,AnimationState> actionIDToAnimMap=new HashMap();
    public AABB standardAABB;
    @Nullable
    protected ProwlerRaid raid;
    protected int raidValue;
    public AbstractNightProwler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(INFUSE,1);//for now, default to 1 to test fire
    }
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(INFUSE,tag.getInt("Infuse"));
        this.setRaidValue(tag.getInt("RVal"));
        if (tag.contains("rID")){
            if (this.level() instanceof ServerLevel) {
                LazyOptional<LevelCapability> optional=this.level().getCapability(LevelCapabilityProvider.LevelCap);
                optional.ifPresent(cap->
                    {
                        this.raid=cap.raids.getProwlerRaidFromID(tag.getInt("rID"));
                    }
                );
            }
            if(this.raid!=null){
                /*
                add to raid
                 */
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Infuse",this.entityData.get(INFUSE));
        tag.putInt("RVal", raidValue);

        if (this.raid != null) {
            tag.putInt("rID", this.raid.getRaidID());
        }

    }

    public void travel(Vec3 vec3){
        super.travel(vec3);
    }
    public void tick() {
        super.tick();
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        //super.updateWalkAnimation(pPartialTick);

        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }
    @Override
    public void setDeltaMovement(Vec3 dm){
        super.setDeltaMovement(dm);
    }


    @Override
    public boolean hurt(DamageSource source, float amt){
        return super.hurt(source,amt);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        Level level = this.level();
        if (level instanceof ServerLevel) {
            if (getInfuse()!=0){
                if (Config.prowlerDestruction== Config.ProwlerDestruction.ALL || (Config.prowlerDestruction== Config.ProwlerDestruction.RAID && this.raid!=null)){
                    BlockPos centerPos=this.blockPosition();
                    if (getInfuse()==1){//fire
                        for (int x=-3;x<=3;x++){
                            for (int z=-3;z<=3;z++){
                                for (int y=-1;y<=1;y++){
                                    BlockPos pos=centerPos.offset(x,y,z);
                                    BlockState blockState=this.level().getBlockState(pos);
                                    for(Direction direction : Direction.values()) {
                                        if (BaseFireBlock.canBePlacedAt(level, pos,direction)){
                                            BlockState blockstate1 = BaseFireBlock.getState(level, pos);
                                            level.setBlock(pos, blockstate1, 11);
                                        }
                                        if (blockState.isFlammable(this.level(), pos.relative(direction), direction.getOpposite())){
                                        }
                                    }

                                }
                            }
                        }
                        AABB aabb=null;
                        List<LivingEntity> list=this.level().getNearbyEntities(LivingEntity.class,null,this,aabb);
                        list.forEach(entity->{
                            entity.setSecondsOnFire(3);
                        });
                    }
                    else{//ice
                        for (int x=-3;x<=3;x++) {
                            for (int z = -3; z <= 3; z++) {
                                for (int y = -1; y <= 1; y++) {
                                    /*
                                    the issue is there is no generic tag for stuff that have cobbled variants, stuff that are made of brick ingots, etc
                                    destroys cobblestone, converts stone to cobblestone, converts sandstone to sand, destroys bricks/nether bricks and drops 2 brick ingots
                                     */
                                    BlockPos pos=centerPos.offset(x,y,z);
                                    BlockState blockState=this.level().getBlockState(pos);
                                    if (blockState.is(Tags.Blocks.STONE) ){
                                        //this.level().setBlockAndUpdate(pos,);
                                    }
                                    else if (blockState.is(Tags.Blocks.SANDSTONE)){
                                        this.level().setBlockAndUpdate(pos,Blocks.SAND.defaultBlockState());
                                    }
                                    else if (blockState.is(Tags.Blocks.COBBLESTONE)){
                                        this.level().destroyBlock(pos,true,this);
                                    }
                                    /*
                                    else if (blockState.getBlock() == Blocks.BRICKS){

                                    }
                                    else if (blockState.getBlock() == Blocks.NETHER_BRICKS || blockState.getBlock() == Blocks.RED_NETHER_BRICKS){

                                    }

                                     */
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public int getInfuse(){
        return this.entityData.get(INFUSE);
    }

    public int getRaidValue(){
        return this.raidValue;
    }

    public void setRaidValue(int w){
        this.raidValue =w;
    }


    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(3)==0){
            return SoundEvents.WOLF_GROWL;
        }
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource p_30424_) {
        return SoundEvents.WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }
}
