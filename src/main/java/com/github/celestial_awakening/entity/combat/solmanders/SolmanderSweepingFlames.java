package com.github.celestial_awakening.entity.combat.solmanders;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.util.CA_Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SolmanderSweepingFlames extends GenericAbility {
    public SolmanderSweepingFlames(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
        name="Sweeping Flames";
    }

    @Override
    public void executeAbility(LivingEntity target) {
        if (state==1){

        }
        this.currentStateTimer--;
        if (currentStateTimer<=0) {
            switch (state) {
                case 0:{
                    break;
                }
                case 1:{
                    break;
                }
                case 2:{
                    break;
                }
            }
        }
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 9;
    }
}
