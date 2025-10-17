package com.github.celestial_awakening.entity.combat;

import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.scores.Team;

/*
functions like TargetGoal with the following differences
-reads mob data to determine if the mob can detect targets through walls
 */
public abstract class CATargetGoal extends TargetGoal {
    private int unseenCATicks;

    public CATargetGoal(Mob p_26140_, boolean p_26141_) {
        super(p_26140_, p_26141_);
    }
    public CATargetGoal(Mob p_26143_, boolean p_26144_, boolean p_26145_) {
        super(p_26143_,p_26144_,p_26145_);
    }

    @Override
    public void start() {
        super.start();
        this.unseenCATicks = 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            livingentity = this.targetMob;
        }

        if (livingentity == null) {
            return false;
        } else if (!this.mob.canAttack(livingentity)) {
            return false;
        } else {
            Team team = this.mob.getTeam();
            Team team1 = livingentity.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowDistance();
                if (this.mob.distanceToSqr(livingentity) > d0 * d0) {
                    return false;
                } else {
                    if (this.mustSee && !((AbstractCAMonster)mob).hasXRay() ) {

                        if (this.mob.getSensing().hasLineOfSight(livingentity)) {
                            this.unseenCATicks = 0;
                        } else if (++this.unseenCATicks > reducedTickDelay(this.unseenMemoryTicks)) {
                            return false;
                        }
                    }

                    this.mob.setTarget(livingentity);
                    return true;
                }
            }
        }
    }
}
