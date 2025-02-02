package com.github.celestial_awakening.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Comet extends Projectile {

    protected Comet(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Override
    protected void defineSynchedData() {

    }
    public void tick() {
        super.tick();

    }


    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
    }
}
