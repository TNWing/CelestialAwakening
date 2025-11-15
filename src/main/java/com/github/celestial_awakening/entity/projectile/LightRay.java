package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//need to make a capability for this to store persistent nbt data like dmg and stuff
public class LightRay extends CA_Projectile {

    //BeaconBlockEntity.BeaconBeamSection
    float widthProgress;
    float heightProgress;

    float widthRateOfChange;
    float heightRateOfChange;


    protected boolean hitMultiple=false;//if true
    //can also just use an int for the hit multiple, to set a limit on number of hits
    protected boolean destroyIfHitLiving=true;

    boolean hasHitSomething=false;
    /*
    0:destroy self
    1:stick to block
    2:shrinks until it destroys self
     */
    //TODO: use these vars in the future
    protected int actionWhenHitBlock;

    int maxTickWhenInBlock=10;


    protected boolean inGround=false;


    float minWidth;
    float minHeight;

    float maxWidth;
    float maxHeight;

    Vec3 rotDir;//represents the direction of movement and expansion

    Vec3 end;
    public LightRay(EntityType<LightRay> entityType, Level level) {
        super(entityType,level,20);
        this.setDmg(2f);
        this.widthProgress=1f;
        this.heightProgress=1f;
        this.setNoGravity(true);
    }


    public static LightRay create(Level level, int tickLiveTime,float dmg) {
        LightRay entity = new LightRay(EntityInit.LIGHT_RAY.get(), level);
        entity.setLifetime(tickLiveTime);
        entity.setDmg(dmg);
        return entity;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33113_) {//never reached
        EntityDimensions returnVal=EntityDimensions.scalable(this.getWidth(),this.getHeight());
        return returnVal;
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public void setSize(float w,float h){
        this.setWidth(w);
        this.setHeight(h);

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
    }


    public void hitLivingEntity(LivingEntity entity) {
        Entity entity1 = this.getOwner();
        if (entity1 == null) {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC));
        }

        else {
            damagesource=new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC),entity1);
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
            if (entity != entity1 && entity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
            }
            //this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
        else {
            entity.setRemainingFireTicks(k);
        }
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
    }


    public void raycast(){
        AABB rayBox=getRayBox();
        List<LivingEntity> livingEntityList=this.level().getEntitiesOfClass(LivingEntity.class,rayBox,pred);

        ArrayList<LivingEntity> entitiesToHit=new ArrayList<>();
        //my idea for better collision is to use 4 raycasts
        for (LivingEntity entity: livingEntityList) {
                AABB aabb=entity.getBoundingBox();
                Vec3[] rayOffsets = new Vec3[] {
                        new Vec3(-0 / 2f, 0, 0),
                        new Vec3(0/ 2f, 0, 0),
                        new Vec3(0, -0 / 2f, 0),
                        new Vec3(0, 0 / 2f, 0)
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
        if (!entitiesToHit.isEmpty() ){
            hasHitSomething=true;
        }
        if (hasHitSomething &&  alertInterface!=null){
            this.alertInterface.onAlert();
        }
        else{//use default hit behavior if no alert interface
            for (LivingEntity entity:entitiesToHit) {
                this.hitLivingEntity(entity);
            }
        }
        if (!entitiesToHit.isEmpty() && destroyIfHitLiving){
            this.discard();
        }
    }
    public AABB getRayBox(){
        double xz=Math.toRadians((-1*this.getHAng())+90);//yaw, TEST +90
        double y=Math.toRadians(this.getVAng());
        if (this.getVAng()>=180){
        }

        Vec3 dir=new Vec3(Math.cos(xz)*Math.sin(y),Math.cos(y),Math.sin(xz)*Math.sin(y)).normalize();
        end=this.position();
        end=end.add(dir.scale(this.getHeight()));
        ClipContext clipContext=new ClipContext(this.position(),end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,this);
        BlockHitResult result= this.level().clip(clipContext);

        //block in the way, so cut the ray short
        BlockPos hitPos=result.getBlockPos();
        if (!this.level().getBlockState(hitPos).isAir()){
            end=result.getLocation();
            hasHitSomething=true;
        }

        AABB rayBox=new AABB(this.position(),end);
        return rayBox;
    }


    public void tick() {
        //super.tick();//todo (MAYBE): reenable in future and make sure it doesnt screw up things
        if (!this.level().isClientSide) {
            float tW=this.getWidth();
            float tH=this.getHeight();
            this.setWidth(MathFuncs.clamp(tW+widthRateOfChange,minWidth,maxWidth));
            this.setHeight(MathFuncs.clamp(tH+heightRateOfChange,minHeight,maxHeight));
        }


        this.setSize(this.getWidth(),this.getHeight());
        this.setCurrentLifetime(this.getCurrentLifeTime()+1);
        if (this.getCurrentLifeTime()>=this.getLifeTime()){
            this.discard();
        }

        if (this.inGround) {

        }
        else {
            if(this.isAlive() && this.hasCollision()) {
                raycast();
            }
        }
    }

    public Vec3 getEndPt(){
        return this.end;
    }
}
