package com.github.celestial_awakening.events.custom_events;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class DivinerEyeSoundEvent extends Event {
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    boolean open;
    Level level;
    public DivinerEyeSoundEvent(boolean isOpen, Level l) {
        this.level=l;
        this.open=isOpen;
    }
}
