package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ExposingLight extends MobEffect {
    /*
    Purifying Flames
Fire damage taken bypasses i frames and does not trigger I frames

     */

    /*
    REWORK IDEAS
    1. Exposing Light
    Part of taken damage that is negated is dealt as true damage
    say i get hit for 10 damage. I reduce it to 6 damage. 25% of the negated damage is taken as true damage, so i take 6 + 1 damage.

    2. Debilitating Light
    Target takes increased damage the lower their current HP % is

    3. Exposing Light
    Getting hit increases a counter. Higher the counter, the more damage you take
     */
    public ExposingLight(MobEffectCategory category, int color)  {
        super(category,color);
    }


}
