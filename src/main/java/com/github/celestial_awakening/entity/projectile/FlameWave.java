package com.github.celestial_awakening.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlameWave extends CA_Projectile{

    class Flame{
        BlockPos pos;
        Direction dir;
        SpreadMethod spreadMethod;
    }

    public enum SpreadMethod{
        STRAIGHT,
    }

    List<Flame> flameList=new ArrayList<>();

    public void tick(){
        for (Flame flame:flameList) {
            Direction d = flame.dir;
            Vec3i moveVec=d.getNormal();

            //flame.pos
            /*
            perform flame movement
             */
            this.level().setBlockAndUpdate(flame.pos, Blocks.FIRE.defaultBlockState());
        }
    }



    protected FlameWave(EntityType<? extends Projectile> p_37248_, Level p_37249_, int lt) {
        super(p_37248_, p_37249_, lt);
    }
}
