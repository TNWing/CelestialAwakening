package com.github.celestial_awakening.events.custom_events;

import com.github.celestial_awakening.entity.CA_Entity;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class CA_ProjectileCollisionEvent extends Event {
    List<CA_Entity> entitiesToAlert;
    
    public void alert(){
        for (CA_Entity entity:entitiesToAlert) {
            if (entity.getAlertInterface()!=null){
                entity.getAlertInterface().onAlert();
            }

        }
    }
}
