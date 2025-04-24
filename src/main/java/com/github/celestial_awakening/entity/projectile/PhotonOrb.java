package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PhotonOrb extends Projectile {
    //an entity that spawns other entities (such as projectiles)
    Vec3 dest;

    AbstractCAMonster owner;
    Entity entityToSpawn;


    public PhotonOrb(Level p_37249_, AbstractCAMonster owner, Entity entityToSpawn) {
        super(EntityInit.PHOTON_ORB.get(), p_37249_);
        this.owner=owner;
        this.entityToSpawn=entityToSpawn;
    }

    public PhotonOrb(EntityType<PhotonOrb> photonOrbEntityType, Level level) {
        super(EntityInit.PHOTON_ORB.get(), level);
    }


    @Override
    protected void defineSynchedData() {
//need to use synceddata to save the following
        /*
        what to spawn
        owner
        i could also just not worry and have projectiles despawn upon logging
         */
    }

    public void setDest(Vec3 dest) {
        this.dest = dest;
    }

    public void tick() {
        super.tick();
        Vec3 newPos=Vec3.ZERO;
        if (dest!=null && this.distanceToSqr(dest)<this.distanceToSqr(this.position().add(this.getDeltaMovement()))){//move to given pos
            newPos=dest;
        }
        else{
            newPos=this.position().add(this.getDeltaMovement());
        }
        this.setPos(newPos);
    }

    public void spawnEntity(){
        this.level().addFreshEntity(entityToSpawn);
    }
}
