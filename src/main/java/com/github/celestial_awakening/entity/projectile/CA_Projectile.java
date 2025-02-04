package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.function.Predicate;

public class CA_Projectile extends Projectile {
    Vec3 rotMoveDir;
    int rmdTicks;
    MovementModifier currentMovementModifier;
    private static final EntityDataAccessor<Boolean> REAL = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> RENDERER_XSCALING = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RENDERER_YSCALING = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RENDERER_ZSCALING = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> WIDTH = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEPTH = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPD = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> H_ANG = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> V_ANG = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ZROT = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DMG = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_LIFE = SynchedEntityData.defineId(CA_Projectile.class, EntityDataSerializers.INT);
    List<MovementModifier> movementModifiers;
    protected CA_Projectile(EntityType<? extends Projectile> p_37248_, Level p_37249_,int lt) {
        super(p_37248_, p_37249_);
        this.setLifetime(lt);


    }
    protected CA_Projectile(EntityType<? extends Projectile> p_37248_, Level p_37249_,float spd, float hA,float vA,int lt) {
        super(p_37248_, p_37249_);
        setMoveValues(spd,hA,vA);
        this.setLifetime(lt);
    }
    protected CA_Projectile(EntityType<? extends Projectile> p_37248_, Level p_37249_,float spd, float hA,float vA,float zR,int lt) {
        super(p_37248_, p_37249_);
        setMoveValues(spd,hA,vA);
        this.setZRot(zR);
        this.setLifetime(lt);
    }


    public LazyOptional<ProjCapability> getProjCap() {
        return this.getCapability(ProjCapabilityProvider.ProjCap);
    }
    @Override
    protected void defineSynchedData() {
        this.entityData.define(H_ANG,0f);
        this.entityData.define(V_ANG,0f);
        this.entityData.define(ZROT,0f);
        this.entityData.define(SPD,1f);
        this.entityData.define(LIFETIME,70);
        this.entityData.define(DMG,2f);
        this.entityData.define(WIDTH,1f);
        this.entityData.define(HEIGHT,1f);
        this.entityData.define(DEPTH,1f);
        this.entityData.define(RENDERER_XSCALING,1f);
        this.entityData.define(RENDERER_YSCALING,1f);
        this.entityData.define(RENDERER_ZSCALING,1f);
        this.entityData.define(REAL,true);
    }
    protected void setRScales(float r){
        this.entityData.set(RENDERER_XSCALING,r);
        this.entityData.set(RENDERER_YSCALING,r);
        this.entityData.set(RENDERER_ZSCALING,r);
    }
    protected void setRScales(float x,float y,float z){
        this.entityData.set(RENDERER_XSCALING,x);
        this.entityData.set(RENDERER_YSCALING,y);
        this.entityData.set(RENDERER_ZSCALING,z);
    }

    protected void setDims(float w,float h,float d){
        this.entityData.set(WIDTH,w);
        this.entityData.set(HEIGHT,h);
        this.entityData.set(DEPTH,d);
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDims(tag.getFloat("Width"),tag.getFloat("Height"),tag.getFloat("Depth"));
        this.setSpd(tag.getFloat("Spd"));
        this.setHAng(tag.getFloat("HAng"),true);
        this.setVAng(tag.getFloat("VAng"),true);
        this.setLifetime(tag.getInt("Lifetime"));
        this.setDmg(tag.getFloat("Dmg"));
        this.setRScales(tag.getFloat("X_RScale"),tag.getFloat("Y_RScale"),tag.getFloat("Z_RScale"));
        this.setZRot(tag.getFloat("ZRot"));
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("ZRot",this.getZRot());
        tag.putFloat("Spd", this.getSpd());
        tag.putFloat("HAng", this.getHAng());
        tag.putFloat("VAng",this.getVAng());
        tag.putInt("Lifetime",this.getLifeTime());
        tag.putFloat("Dmg",this.getDmg());
        tag.putFloat("Width",this.getWidth());
        tag.putFloat("Height",this.getHeight());
        tag.putFloat("Depth",this.getDepth());
        tag.putFloat("X_RScale",this.getXRScale());
        tag.putFloat("Y_RScale",this.getYRScale());
        tag.putFloat("Z_RScale",this.getZRScale());
    }
    public float getWidth(){
        return this.entityData.get(WIDTH);
    }
    public float getHeight(){
        return this.entityData.get(HEIGHT);
    }
    public float getDepth(){
        return this.entityData.get(DEPTH);
    }
    public float getSpd(){
        return this.entityData.get(SPD);
    }
    public float getHAng(){
        return this.entityData.get(H_ANG);
    }
    public float getVAng(){
        return this.entityData.get(V_ANG);
    }
    public float getZRot(){
        return this.entityData.get(ZROT);
    }
    public int getLifeTime(){
        return this.entityData.get(LIFETIME);
    }
    public float getDmg(){
        return this.entityData.get(DMG);
    }
    public float getXRScale(){
        return this.entityData.get(RENDERER_XSCALING);
    }
    public float getYRScale(){
        return this.entityData.get(RENDERER_YSCALING);
    }
    public float getZRScale(){
        return this.entityData.get(RENDERER_ZSCALING);
    }
    public boolean isReal(){
        return this.entityData.get(REAL);
    }
    public void setDmg(float f){
        this.entityData.set(DMG,f);
    }
    public void setLifetime(int i){
        this.entityData.set(LIFETIME,i);
    }
    public void setSpd(float s){
        this.entityData.set(SPD,s);
    }
    public void setReal(boolean b){
        this.entityData.set(REAL,b);
    }
    public void setHAng(float h){
        this.entityData.set(H_ANG,h);

    }


    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float zRads= (float) Math.toRadians(this.getZRot());
        EntityDimensions dims=EntityDimensions.scalable(this.getWidth(), this.getHeight());
        if (this.getZRot()==90){
            //is it this?
            dims=EntityDimensions.scalable(this.getHeight(), this.getWidth());
        }
        return dims;
    }
    public void setZRot(float fa){
        float f=0;
        float width=this.getWidth();
        float height=this.getHeight();
        float depth=this.getDepth();

        this.entityData.set(ZROT,fa);

        double sin = Math.sin(Math.toRadians(f));
        double cos = Math.cos(Math.toRadians(f));
        double newWidth = Math.abs(width * cos) + Math.abs(height * sin);
        double newHeight = Math.abs(width * sin) + Math.abs(height * cos);
        this.setBoundingBox(new AABB(
                this.getX() - newWidth / 2,
                this.getY()-newHeight/2,
                this.getZ() - depth / 2,
                this.getX() + newWidth / 2,
                this.getY() + newHeight/2,
                this.getZ() + depth / 2
        ));
        this.refreshDimensions();
    }
    public void setHAng(float h,boolean doSet){
        if (doSet){
            this.entityData.set(H_ANG,h);
        }

    }
    public void setVAng(float v,boolean doSet){
        if (doSet){
            this.entityData.set(V_ANG,v);
        }

    }
    public void setMoveValues(float s,float h, float v){
        this.entityData.set(SPD,s);
        this.entityData.set(H_ANG,h);
        this.entityData.set(V_ANG,v);
    }
    public void setRotMoveDir(Vec3 rotDir, int ticks){
        rmdTicks=ticks;
        rotMoveDir=rotDir;
    }
    double zeroThreshold=1e-7;
    public void tick(){
        super.tick();
        ProjCapability cap=getProjCap().orElse(null);

        if (currentMovementModifier==null && cap!=null){
            currentMovementModifier=cap.popFromList();
        }
        //this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

        updateMovementFactors(cap);

        Vec3 dir=calculateMoveVec();
        this.setDeltaMovement(dir);
        Vec3 dm=this.getDeltaMovement();


        double d0 = this.getX() + dm.x;
        double d1 = this.getY() + dm.y;
        double d2 = this.getZ() + dm.z;

        this.setPos(d0, d1, d2);
        //System.out.println(this.getId()+  " AT NEW POS " + this.position());

    }
    //TODO: need to have v_ang affect the dir of horizontal move
    public Vec3 calculateMoveVec(){
        double hAngle = MathFuncs.clampAngle(this.getHAng());
        double vAngle = MathFuncs.clampAngle(this.getVAng());

        double hRad = Math.toRadians(hAngle);
        double vRad = Math.toRadians(vAngle);

        Vec3 dir=new Vec3(Math.sin(hRad)*Math.cos(vRad),Math.sin(vRad),Math.cos(hRad)*Math.cos(vRad));

        return dir.scale(this.getSpd()/20f);
    }
    public LivingEntity entityToHurt(TargetingConditions conds){
        AABB aabb=this.getBoundingBox();
        Level level=this.level();
        LivingEntity livingEntity=null;
        if (this.getOwner() instanceof LivingEntity){
            livingEntity= (LivingEntity) this.getOwner();
        }
        Vec3 pos=this.position();
        LivingEntity entity=level.getNearestEntity(LivingEntity.class,conds,livingEntity,pos.x,pos.y,pos.z,aabb);
        return entity;
    }
    public LivingEntity findNearestEntity(TargetingConditions conds,AABB aabb){
        Level level=this.level();
        LivingEntity livingEntity=null;
        if (this.getOwner() instanceof LivingEntity){
            livingEntity= (LivingEntity) this.getOwner();
        }
        Vec3 pos=this.position();
        LivingEntity entity=level.getNearestEntity(LivingEntity.class,conds,livingEntity,pos.x,pos.y,pos.z,aabb);
        return entity;
    }
    public List<LivingEntity> entitiesToHurt(Predicate pred){
        AABB aabb=this.getBoundingBox();
        Level level=this.level();
        List<LivingEntity> entities=level.getEntitiesOfClass(LivingEntity.class,aabb,pred);
        return entities;
    }
    public List<LivingEntity> entitiesToHurt(){
        AABB aabb=this.getBoundingBox();
        Level level=this.level();
        List<LivingEntity> entities=level.getEntitiesOfClass(LivingEntity.class,aabb);
        return entities;
    }


    public void updateMovementFactors(ProjCapability cap){
        if (currentMovementModifier!=null){

            if(currentMovementModifier.decrementDelay()){
                float spdVal=currentMovementModifier.getSpd();
                float hAng=currentMovementModifier.getHAng();
                float vAng=currentMovementModifier.getVAng();
                float zRot=currentMovementModifier.getZRot();

                if (spdVal!=0){
                    MovementModifier.modOperation spdOp=currentMovementModifier.getSpdOperation();
                    MovementModifier.modFunction spdFunc=currentMovementModifier.getSpdFunction();
                    float spdMod=0;
                    switch(spdFunc){
                        case NUM -> {
                            spdMod=spdVal;
                            break;
                        }
                        case TRIG -> {
                            spdMod= (float) Math.sin(Math.toRadians(spdVal));
                            break;
                        }
                    }
                    switch(spdOp){
                        case SET -> {

                            this.setSpd( spdMod);
                            break;
                        }
                        case ADD -> {
                            this.setSpd((this.getSpd()+spdMod/20f));
                            break;
                        }
                        case MULT -> {
                            float mult= (float) MathFuncs.findBaseFromExponentAndResult(20D,spdMod);
                            if (mult<zeroThreshold){
                                mult=0;
                            }
                            this.setSpd(this.getSpd()*mult);
                            break;
                        }
                    }
                }
                if (hAng!=0 || vAng!=0){
                    MovementModifier.modOperation angOp=currentMovementModifier.getAngOperation();
                    MovementModifier.modFunction angFunc=currentMovementModifier.getAngFunction();
                    float hMod=0;
                    float vMod=0;
                    switch(angFunc){
                        case TRIG -> {
                            hMod= (float) Math.sin(Math.toRadians(hAng));
                            vMod= (float) Math.sin(Math.toRadians(vAng));
                            break;
                        }
                        case NUM -> {
                            hMod=hAng;
                            vMod=vAng;
                            break;
                        }
                    }

                    switch(angOp){
                        case SET -> {
                            this.setHAng(hMod,hAng!=0);
                            this.setVAng(vMod,vAng!=0);
                            break;
                        }
                        case ADD -> {
                            this.setHAng(this.getHAng()+hMod/20f,hAng!=0);
                            this.setVAng(this.getVAng()+vMod/20f,vAng!=0);
                            break;
                        }
                        case MULT -> {
                            float hMult= (float) MathFuncs.findBaseFromExponentAndResult(20D,hMod);
                            if (hMult<zeroThreshold){
                                hMult=0;
                            }
                            float vMult= (float) MathFuncs.findBaseFromExponentAndResult(20D,vMod);
                            if (vMult<zeroThreshold){
                                vMult=0;
                            }
                            this.setHAng(this.getHAng()*hMult,hAng!=0);
                            this.setVAng(this.getVAng()*vMult,vAng!=0);
                            break;
                        }
                    }
                }
                if (zRot!=0){
                    MovementModifier.modOperation rotOp=currentMovementModifier.getRotOperation();
                    MovementModifier.modFunction rotFunc=currentMovementModifier.getRotFunction();
                    float zMod=0;
                    switch(rotFunc){
                        case NUM -> {
                            zMod=zRot;
                            break;
                        }
                        case TRIG -> {
                            zMod= (float) Math.sin(Math.toRadians(zMod));
                            break;
                        }
                    }
                    switch(rotOp){
                        case SET -> {

                            this.setZRot( zMod);
                            break;
                        }
                        case ADD -> {
                            this.setZRot((this.getZRot()+zMod/20f));
                            break;
                        }
                        case MULT -> {
                            float mult= (float) MathFuncs.findBaseFromExponentAndResult(20D,zRot);
                            if (mult<zeroThreshold){
                                mult=0;
                            }
                            this.setZRot(this.getZRot()*mult);
                            break;
                        }
                    }
                }

                if (currentMovementModifier.decrementTicks()){
                    currentMovementModifier=null;
                }
            }
        }
    }

    public List<MovementModifier> getMovementModifiers(){
        return this.movementModifiers;
    }

}
