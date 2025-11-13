package com.github.celestial_awakening.effects;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;

public class CurseOfTheMoon extends MobEffect {
    public CurseOfTheMoon(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }
    /*
    stores amt healed every heal instance
    upon reaching a set amount, take dmg
    stored amt dec over time

    infliction
    -hit by any pk attack (refreshes duration if hit again)
    -removed automatically if a pk dies nearby

    the only issue is that this needs to be stored in the entity cap as you cannot store custom data in effect instances
     */

    @Override
    public boolean isDurationEffectTick(int ticks, int amplifier) {
        return ticks%10==0;
    }
    //perform the effect tick every 10 ticks?
    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        LazyOptional<LivingEntityCapability> optional=target.getCapability(LivingEntityCapabilityProvider.capability);
        optional.ifPresent(cap->{
            float v=cap.getMoonCurseVal();
            if (v>=target.getMaxHealth()*0.2f){
                float mult=1+amplifier*0.15f;
                float dmg=mult*0.5f*v;
                target.hurt(target.level().damageSources().magic(), dmg);
                cap.setMoonCurseVal(0);
            }
            else{
                cap.changeMoonCurseVal(-0.25f);
                /*
                default natural regen is 1 hp every 4 sec
                so 0.25hp/sec
                the curse val decreases by 0.5/sec, so this does allow minor healing such as healing from low saturation food
                 */
            }
        });
    }
}
