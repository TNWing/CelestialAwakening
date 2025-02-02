package com.github.celestial_awakening.entity.living;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class LunaticShadow extends Monster {
    /**
     NOTE: just an idea of something
     Can occur due to insanity from the moon
     Lunatic Shadow is visible only to the affected player.
     Looks like the affected player
     Actions
     -Can build phantom structures, which only have collision with the affected player
     -can harvest resources (ex: trees, crops, etc)
     -can be hostile to the player
     -can use items (ex: bow, epearl)
     -can interact with various block entities (ex: chest)
     would be rather complex, so save this for much later
     */


    protected LunaticShadow(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
}
