package com.github.celestial_awakening.entity.projectile;

import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

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
            super.tick();
        } else {
            this.discard();
        }
    }
}
