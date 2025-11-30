package com.github.celestial_awakening.events.custom_events;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

public class TranscendentSpawnEvent extends Event {
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }


    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    Vec3 pos;
    Level level;
    public TranscendentSpawnEvent(Vec3 p, Level l) {
        this.level=l;
        this.pos=p;
    }
}
