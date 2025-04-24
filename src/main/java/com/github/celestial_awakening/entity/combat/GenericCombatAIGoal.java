package com.github.celestial_awakening.entity.combat;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public abstract class GenericCombatAIGoal extends Goal {

    protected final AbstractCAMonster mob;
    private double targetX;
    private double targetY;
    private double targetZ;

    private Path path;
    private int ticksUntilNextPathRecalculation;

    protected GenericAbility currentAbility;

    protected int difficulty;
    double minRange;
    double maxRange;
    /*
    Controls combat ai for all custom entities
    each unique combat ai goal contains the following
    -a set of Abilities, which are derived from GenericAbility class
    -an optional order for ability casting
    -movement controller
     */

    protected GenericCombatAIGoal(AbstractCAMonster mob) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = mob.getTarget();
        if (livingentity!=null && livingentity.isAlive()){
            return true;
        }
        return false;
    }

    public void start(){
        super.start();
        System.out.println("STA5rting combat");
        this.mob.setCombatActive(true);
        this.mob.setActionId(1);
    }

    public void stop(){

    }

    public void tick(){

    }


    public void movementController(LivingEntity target){
        if (this.mob.canMove){

            boolean keepDist=this.mob.keepDist;
            double minDist=Math.pow(this.mob.minRange,2);
            double maxDist=Math.pow(this.mob.maxRange,2);
            double dist = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            if (keepDist){//move towards maxDist (NOT IMPLEMENTED)
                if (dist<maxDist && this.mob.canStillSenseTarget()){
                    //BlockPos pos=new BlockPos(0,0,0);
                    //this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(pos,0), this.mob.spdMod);
                }
                else{
                    this.mob.getNavigation().stop();
                }
            }
            else{//move towards mindist
                if (dist>minDist && this.mob.canStillSenseTarget()){
                    Path path=this.mob.getNavigation().createPath(target,0);
                    boolean b=this.mob.getNavigation().moveTo(path,this.mob.spdMod);
                }
                else{
                    this.mob.getNavigation().stop();
                }
            }

        }
        else{
            this.mob.getNavigation().stop();
        }

        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

    }
}
