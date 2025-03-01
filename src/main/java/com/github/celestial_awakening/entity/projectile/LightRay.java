package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//need to make a capability for this to store persistent nbt data like dmg and stuff
public class LightRay extends CA_Projectile {

    //BeaconBlockEntity.BeaconBeamSection
    int tickLiveTime=0;
    //may not need this w/ the RoC vars
    float widthProgress;
    float heightProgress;

    float widthRateOfChange;
    float heightRateOfChange;


    protected boolean hitMultiple=false;//if true
    //can also just use an int for the hit multiple, to set a limit on number of hits
    protected boolean destroyIfHitLiving=true;

    /*
    0:destroy self
    1:stick to block
    2:shrinks until it destroys self
     */
    protected int actionWhenHitBlock;

    int maxTickWhenInBlock=10;


    protected boolean inGround=false;


    float minWidth;
    float minHeight;

    float maxWidth;
    float maxHeight;

    Vec3 rotDir;//represents the direction of movement and expansion
    //TODO:
    /*
    Replace XPR with VAng
    Utilize currentlife instead of ticklivetime
    FOR ALL ENTITIES, REMOVE ALL CONSTRUCTORS AND REPLACE WITH A STATIC CREATE METHOD
     */
    private static final EntityDataAccessor<Float> XPR= SynchedEntityData.defineId(LightRay.class, EntityDataSerializers.FLOAT);


    //for some reason, server calls base ray while client calls asteron ray?
    public LightRay(EntityType<LightRay> entityType, Level level) {
        super(entityType,level,20);
        System.out.println("base ray ON CLIENT? " + this.level().isClientSide);
        this.setDmg(2f);
        this.widthProgress=1f;
        this.heightProgress=1f;
        this.setNoGravity(true);
    }


    public static LightRay create(Level level, int tickLiveTime,float dmg) {
        LightRay entity = new LightRay(EntityInit.LIGHT_RAY.get(), level);
        entity.setLifetime(tickLiveTime);
        entity.setDmg(dmg);
        entity.widthProgress=1f;
        entity.heightProgress=1f;
        entity.setNoGravity(true);
        return entity;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(XPR,0f);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33113_) {//never reached
        EntityDimensions returnVal=EntityDimensions.scalable(this.getWidth(),this.getHeight());
        //System.out.println("GETTING DIMS " + returnVal.width + " , " + returnVal.height);
        return returnVal;//this gets changed properly but hitbox is not changing
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
        if (this.level().isClientSide()){
            //System.out.println("CLIENT REFRESH");
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(XPR,tag.getFloat("XPR"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("XPR",this.entityData.get(XPR));

    }

    public void setSize(float w,float h){
        this.setWidth(w);
        this.setHeight(h);

    }
    public float getXPR(){
        return this.entityData.get(XPR);
    }
    public void setXPR(float r){
        this.entityData.set(XPR,r);
    }


    public AABB updateAABB(Vec3 p_20394_) {
        return this.updateAABB(p_20394_.x, p_20394_.y, p_20394_.z);
    }

    public AABB updateAABB(double p_20385_, double p_20386_, double p_20387_) {
        float f = this.getWidth() / 2.0F;
        float f1 = this.getHeight();
        return new AABB(p_20385_ - (double)f, p_20386_, p_20387_ - (double)f, p_20385_ + (double)f, p_20386_ + (double)f1, p_20387_ + (double)f);
    }

    public void initDims(float w,float h,float minW,float minH,float maxW,float maxH,float wChange,float hChange){
        this.setWidth(w);
        this.setHeight(h);
        maxWidth=maxW;
        maxHeight=maxH;
        minWidth=minW;
        minHeight=minH;
        widthRateOfChange=wChange;
        heightRateOfChange=hChange;
        //System.out.println("INIT DIMS TO " + this.entityData.get(WIDTH) + " , " + this.entityData.get(HEIGHT));
        this.refreshDimensions();
        this.setBoundingBox(this.updateAABB(this.position()));

    }


    protected void hitLivingEntity(LivingEntity entity) {
        Entity entity1 = this.getOwner();
        if (entity1 == null) {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC));
        }

        else {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }

        int k = entity.getRemainingFireTicks();
        if (entity.hurt(damagesource, this.getDmg())) {
/*
                    if (!this.level().isClientSide && entity1 instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                        EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                    }

 */
            //this.doPostHurtEffects(livingentity);
            if (entity1 != null && entity != entity1 && entity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
            }
            //this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
        else {
            entity.setRemainingFireTicks(k);
        }
    }


    protected void onHitEntity(EntityHitResult res) {
        Entity entity = res.getEntity();
        Entity entity1 = this.getOwner();
        DamageSource damagesource=null;
        if (entity1 == null) {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC));
        }

        else {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }

        int k = entity.getRemainingFireTicks();
        if (entity.hurt(damagesource, this.getDmg())) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
/*
                    if (!this.level().isClientSide && entity1 instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                        EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
                    }

 */
                //this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

            }

            //this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
        else {
            entity.setRemainingFireTicks(k);
        }
        this.discard();
    }

    @Override
    protected boolean canHitEntity(Entity p_37250_) {
        Entity entity = this.getOwner();
        return entity == null || !entity.isPassengerOfSameVehicle(p_37250_);
    }

    public void updateMovement(Vec3 newVec){
        this.setDeltaMovement(newVec);
        this.updateRotation();
    }

    public void setDir(Vec3 dir){
        this.rotDir=dir;
        this.updateRotation();
        /*code for .updateRotation();
              double d0 = vec3.horizontalDistance();
      this.setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI))));
      this.setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI))));
         */
    }

    public void raycast(){
        //TODO
        /*
        right direction now, but the value from star tto end is really small
        so im guessing the subsequent calcs are wrong

        So i need to
        -run the mod again and look at the dir log in the piercingrays code, and compare it to what dir i get
        -try and multiple the dir by the height entirely, as rn it just multiplies the x value by the height
        -will likely need to add an inflate to the rayBox, but gotta study how inflate works
         */
        //also got to figure out why this is called even when ray is gone
        //z is no longer used

        //current xz:Math.toRadians((-1*this.getYRot())-90)
        double xz=Math.toRadians((-1*this.getHAng())+90);//yaw, TEST +90
        double y=Math.toRadians(this.getXPR());
        if (this.getXPR()>=180){
        }

        Vec3 dir=new Vec3(Math.cos(xz)*Math.sin(y),Math.cos(y),Math.sin(xz)*Math.sin(y)).normalize();
        Vec3 end=this.position();
        end=end.add(dir.scale(this.getHeight()));
        ClipContext clipContext=new ClipContext(this.position(),end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,this);
        BlockHitResult result= this.level().clip(clipContext);

        if (result!=null){//this is occuring, presumably if air block
            //block in the way, so cut the ray short
            BlockPos hitPos=result.getBlockPos();

            if (!this.level().getBlockState(hitPos).isAir()){
                end=result.getLocation();
            }

        }
        else{

        }

        //System.out.println("LIGHT RAY " + this.getId() + " has startPos " + this.position() + " AND ENd " + end);
        AABB rayBox=new AABB(this.position(),end);
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,rayBox);

        ArrayList<LivingEntity> entitiesToHit=new ArrayList<>();
        //my idea for better collision is to use 4 raycasts
        for (LivingEntity entity: livingEntityList) {
            //first 2 conds are used if the ray can buff or heal
            if (entity==this.getOwner()){

            }
            else if(entity instanceof AbstractTranscendent){//ally

            }
            else{//
                AABB aabb=entity.getBoundingBox();
                Vec3[] rayOffsets = new Vec3[] {
                        new Vec3(-0 / 2, 0, 0),
                        new Vec3(0/ 2, 0, 0),
                        new Vec3(0, -0 / 2, 0),
                        new Vec3(0, 0 / 2, 0)
                };
                for (int i=0;i<4;i++){

                    Vec3 rayOffset=rayOffsets[i];
                    Optional<Vec3> edgeRay= aabb.clip(this.position().add(rayOffset), end.add(rayOffset));//does the entity intersect with the ray;
                    if (edgeRay.isPresent()){
                        entitiesToHit.add(entity);
                        if (!hitMultiple){
                            break;
                        }
                    }
                }
                if (!entitiesToHit.isEmpty() && !hitMultiple){
                    break;
                }
            }
        }
        for (LivingEntity entity:entitiesToHit) {
            this.hitLivingEntity(entity);
        }
        if (!entitiesToHit.isEmpty() && destroyIfHitLiving){
            this.discard();
        }
    }


    public void tick() {
        //super.tick();
        if (!this.level().isClientSide) {
            float tW=this.getWidth();
            float tH=this.getHeight();
            this.setWidth(MathFuncs.clamp(tW+widthRateOfChange,minWidth,maxWidth));
            this.setHeight(MathFuncs.clamp(tH+heightRateOfChange,minHeight,maxHeight));
        }


        this.setSize(this.getWidth(),this.getHeight());
/*
OG
        if (this.tickLiveTime>=0){
            this.tickLiveTime--;
            if (tickLiveTime<=0){
                System.out.println("DISCARD TIME");
                System.out.println("ON SIDE " + this.level().isClientSide);
                this.discard();
            }
        }
 */
        System.out.println("TICKLIVETIME ON SIDE CLIENT? " + this.level().isClientSide + " IS " + this.tickLiveTime + "  AND LTIME IS  "  + this.getLifeTime());
        this.tickLiveTime++;
        if (tickLiveTime>=this.getLifeTime()){
            System.out.println("DISCARD TIME");
            System.out.println("ON SIDE " + this.level().isClientSide);
            this.discard();
        }

        if (this.inGround) {

        }
        else {
            if(this.isAlive()) {
                raycast();
            }
        }

    }
//the code below breaks the placement of the thing
        /*
    planned tick
        public void tick() {
        //super.tick();
        if (!this.level().isClientSide) {
            float tW=this.getWidth();
            float tH=this.getHeight();
            this.setWidth(MathFuncs.clamp(tW+widthRateOfChange,minWidth,maxWidth));
            this.setHeight(MathFuncs.clamp(tH+heightRateOfChange,minHeight,maxHeight));
        }


        this.setSize(this.getWidth(),this.getHeight());

        if (this.tickLiveTime<=this.getLifeTime()){
            this.tickLiveTime++;
            System.out.println("OU+R LIFE IS " + this.tickLiveTime);
            System.out.println("OUR POS IS " + this.position());
        }
        else{
            System.out.println("DISCARD TIME");
            System.out.println("ON SIDE " + this.level().isClientSide);
            System.out.println("OUR CURRENT LIFE IS " + this.tickLiveTime);
            this.discard();
        }

        if (this.inGround) {

        }
        else {
            if(this.isAlive()) {
                raycast();
            }
        }

    }
     */
}
