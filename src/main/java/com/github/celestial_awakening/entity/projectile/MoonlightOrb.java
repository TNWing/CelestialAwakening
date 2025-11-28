package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MoonlightOrb extends CA_Projectile{
    public MoonlightOrb(EntityType<? extends Projectile> p_37248_, Level p_37249_, int lt) {
        super(p_37248_, p_37249_, lt);
    }

    public MoonlightOrb(EntityType<MoonlightOrb> moonlightOrbEntityType, Level level) {
        super(moonlightOrbEntityType,level,0);
    }

    public static MoonlightOrb create(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR){
        MoonlightOrb orb=new MoonlightOrb(EntityInit.MOONLIGHT_ORB.get(),level,lifeVal);
        orb.setDmg(damage);
        orb.setSpd(spd);
        orb.setHAng(hAng);
        orb.setVAng(vAng);
        orb.setZRot(zR);
        return orb;
    }
    public void tick(){
        Entity entity = this.getOwner();

        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            Vec3 prevPos=this.position();
            this.calcAndMove();
            List<LivingEntity> entities=this.entitiesToHurt(pred);
            if (!entities.isEmpty() && this.alertInterface!=null){
                this.alertInterface.onAlert();
            }
            super.tick();
            this.setCurrentLifetime(this.getCurrentLifeTime()+1);
            if (this.getCurrentLifeTime()>=this.getLifeTime()){
                this.discard();
            }
        } else {
            this.discard();
        }
    }
}
