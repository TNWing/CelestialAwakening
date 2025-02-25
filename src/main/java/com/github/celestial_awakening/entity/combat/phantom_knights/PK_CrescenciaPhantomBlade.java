package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCALivingEntity;
import net.minecraft.world.entity.LivingEntity;

public class PK_CrescenciaPhantomBlade extends GenericAbility {
    /*
    MAY NOT NEED TO BE AN ABILITY, and rather a trigger
    After a certain HP threshold, Phantom blade is triggered
    All basic attacks will leave slow moving crescents behind
    Will always attempt to swing even if target is not in range. If it swings when target not in range, the cd for swinging is much shorter
     */
    public PK_CrescenciaPhantomBlade(AbstractCALivingEntity mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);
    }

    @Override
    public void executeAbility(LivingEntity target) {

    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
