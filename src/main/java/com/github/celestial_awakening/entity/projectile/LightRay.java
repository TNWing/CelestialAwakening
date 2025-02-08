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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//need to make a capability for this to store persistent nbt data like dmg and stuff
public class LightRay extends CA_Projectile {
    //TODO:
    /*
    Now the ray doesnt seem to match anymore
    Calc directions are fucked in general maybe? maybe not
    TESTING
    Asteron start pt:-30,-64
    standing point for player / end pos
    -30,-67: towards -z
    -33,-64: towards -x
    -27,-64: looks fine, moves towards +x
    -30,-61: looks fine, moves towards +z
     */

    //So the issue is w/ rendering, not the calcs
    //BeaconBlockEntity.BeaconBeamSection
    float damage=2f;
    int tickLiveTime=20;
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


    float width;
    float height;

    Vec3 rotDir;//represents the direction of movement and expansion
    public float zRot=0;
    public float xR;
    public float yR;

    public Vec3 moveDir;


///summon celestial_awakening:light_ray ~3 ~ ~
///summon celestial_awakening:light_ray ~1.7 ~2 ~
    //private static final EntityDataAccessor<Float> WIDTH = SynchedEntityData.defineId(LightRay.class, EntityDataSerializers.FLOAT);
    //private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(LightRay.class, EntityDataSerializers.FLOAT);

    //private static final EntityDataAccessor<Float> ZROT = SynchedEntityData.defineId(LightRay.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> XPR= SynchedEntityData.defineId(LightRay.class, EntityDataSerializers.FLOAT);


    public LightRay(EntityType<LightRay> entityType, Level level) {
        super(entityType,level,20);
        this.damage=2f;
        this.tickLiveTime=20;
        this.widthProgress=1f;
        this.heightProgress=1f;
        this.setNoGravity(true);
        //this.setSize(0.2f,1.5f);
        //when thea bove is commented out, the hitbox stays as 1f,1f
        //can hitbox not change dynamically
    }

    /*
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

     */


    public LightRay(Level level,int tickLiveTime) {
        super(EntityInit.LIGHT_RAY.get(),level,tickLiveTime);
        this.tickLiveTime=tickLiveTime;
        this.setNoGravity(true);
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
        this.damage=tag.getFloat("Damage");
        this.setWidth(tag.getFloat("Width"));
        this.setHeight(tag.getFloat("Height"));
        this.entityData.set(XPR,tag.getFloat("XPR"));
        this.setZRot(tag.getFloat("ZRot"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage",damage);
        tag.putFloat("Width",this.getWidth());
        tag.putFloat("Height",this.getHeight());
        tag.putFloat("XPR",this.entityData.get(XPR));
        tag.putFloat("ZRot",this.getZRot());

    }

    public void setSize(float w,float h){
        this.setWidth(w);
        this.setHeight(h);
        //System.out.println("SET SIZE TO " + this.entityData.get(WIDTH) + " , " + this.entityData.get(HEIGHT));
        //this.refreshDimensions();
        //this.setBoundingBox(this.updateAABB(this.position()));
        if (!this.level().isClientSide){
            //System.out.println("NEED TO SEND A PACKET TO SYNC SIZE CHANGE");
            //send a packet
        }
        else{
            //System.out.println("SET SIZE WAS CALLED CLIENT SIDE");
        }

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
        float f = this.width / 2.0F;
        float f1 = this.height;
        return new AABB(p_20385_ - (double)f, p_20386_, p_20387_ - (double)f, p_20385_ + (double)f, p_20386_ + (double)f1, p_20387_ + (double)f);
    }

    public void initDims(float w,float h,float minW,float minH,float maxW,float maxH,float wChange,float hChange){
        this.setWidth(w);
        this.setHeight(h);
        width=w;
        height=h;
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
        if (entity.hurt(damagesource, this.damage)) {
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
        if (entity.hurt(damagesource, this.damage)) {
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
        System.out.println("OUR YROT IS " + this.getYRot() + " on side client? " + this.level().isClientSide);
        double xz=Math.toRadians((-1*this.getHAng())+90);//yaw, TEST +90
        double y=Math.toRadians(this.getXPR());
        //the values above are not the correct sign (pos/neg), at least for collision.
        //my guess is that its due to how the XPR works, since it rotates over 180 so it flips stuff
        //might be yaw as well, since i add 90 to the yaw
        //yaw=-(Math.toDegrees(Math.atan2(dir.z,dir.x))+90f);
        if (this.getXPR()>=180){
        }
        //so Math.toRadians((-1*this.getYRot())+90) seems to be fine, except collision is not working now
        //okay so now it seems like the dir values for x and z are inverted (pos/neg)
        //but it sometimes still detects player, maybe too close to player and thats why

        Vec3 dir=new Vec3(Math.cos(xz)*Math.sin(y),Math.cos(y),Math.sin(xz)*Math.sin(y)).normalize();
                /*
        System.out.println("CURRENT HEIGHT " + this.getHeight());
        System.out.println(xz);
        System.out.println(this.getYRot());
        System.out.println(y);


        System.out.println("DIR IS " + dir);
        */
        System.out.println("ID " + this.getId() + " POS " + this.position());
        Vec3 end=this.position();
        end=end.add(dir.scale(this.getHeight()));
        System.out.println("ID " + this.getId() + "  END " + end);
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
        AABB rayBox=new AABB(this.position(),end);
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,rayBox);

        ArrayList<LivingEntity> entitiesToHit=new ArrayList<>();
        for (LivingEntity entity: livingEntityList) {
            //first 2 conds are used if the ray can buff or heal
            if (entity==this.getOwner()){

            }
            else if(entity instanceof AbstractTranscendent){//ally

            }
            else{//
                AABB aabb=entity.getBoundingBox();
                Optional<Vec3> clip = aabb.clip(this.position(), end);//does the entity intersect with the ray
                if (clip.isPresent()){
                    entitiesToHit.add(entity);
                    if (!hitMultiple){
                        break;
                    }
                }
            }
        }
        for (LivingEntity entity:entitiesToHit) {
            this.hitLivingEntity(entity);
        }
        if (!entitiesToHit.isEmpty() && destroyIfHitLiving){
            this.discard();
        }
        //aabb.clip()
        //ProjectileUtil.getEntityHitResult()

    }


    public void tick() {
        //super.tick();
        if (!this.level().isClientSide) {
            float tW=this.getWidth();
            float tH=this.getHeight();
            width= MathFuncs.clamp(tW+widthRateOfChange,minWidth,maxWidth);
            height=MathFuncs.clamp(tH+heightRateOfChange,minHeight,maxHeight);
            this.setWidth(width);
            this.setHeight(height);
        }


        this.setSize(this.getWidth(),this.getHeight());
        if (this.tickLiveTime>=0){
            this.tickLiveTime--;
            if (tickLiveTime<=0){
                System.out.println("DISCARD TIME");
                this.discard();
            }
        }
        Vec3 vec3 = this.getDeltaMovement();
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);

        if (this.inGround) {

        }
        else {
            if(this.isAlive()) {
                raycast();/*
                vec3 = this.getDeltaMovement();
                double d5 = vec3.x;
                double d6 = vec3.y;
                double d1 = vec3.z;

                double d7 = this.getX() + d5;
                double d2 = this.getY() + d6;
                double d3 = this.getZ() + d1;
                double d4 = vec3.horizontalDistance();
                this.setPos(d7, d2, d3);
                */
                this.checkInsideBlocks();
            }

        }

    }
}
