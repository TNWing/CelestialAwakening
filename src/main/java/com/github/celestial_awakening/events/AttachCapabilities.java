package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.capabilities.*;
import com.github.celestial_awakening.entity.projectile.CA_Projectile;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.items.MoonScythe;
import com.github.celestial_awakening.items.SunStaff;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.PlayerCapS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class AttachCapabilities {

    @SubscribeEvent
    public void onAttachLevelCap(AttachCapabilitiesEvent<Level> event){
        Level level=event.getObject();
        if (!event.getCapabilities().containsValue(LevelCapabilityProvider.LevelCap)){
            event.addCapability(CelestialAwakening.createResourceLocation("level_data"),new LevelCapabilityProvider(level));

        }
    }

    @SubscribeEvent
    public void onAttachItemStackCap(AttachCapabilitiesEvent<ItemStack> event){
        ItemStack itemStack =event.getObject();
        if (itemStack.getItem() instanceof MoonScythe){
            if (!event.getCapabilities().containsValue(MoonScytheCapabilityProvider.ScytheCap)){
                event.addCapability(CelestialAwakening.createResourceLocation("scythe_data"),new MoonScytheCapabilityProvider());
            }
        }
        else if (itemStack.getItem() instanceof SunStaff){
            if (!event.getCapabilities().containsValue(SunStaffCapabilityProvider.cap)){
                event.addCapability(CelestialAwakening.createResourceLocation("sun_staff_data"),new SunStaffCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerID = player.getUUID();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            cap.setUUID(playerID);
            ModNetwork.sendToClient(new PlayerCapS2CPacket(cap),player);
        });
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity =  event.getEntity();
        UUID uuID = entity.getUUID();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=entity.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            cap.setUUID(uuID);
        });
    }

    @SubscribeEvent
    public void onAttachEntityCap(AttachCapabilitiesEvent<Entity> event){
        Entity entity=event.getObject();
        if (entity instanceof CA_Projectile){
            if (entity instanceof LightRay){
                if (!event.getCapabilities().containsValue(LightRayCapabilityProvider.cap)){
                    event.addCapability(CelestialAwakening.createResourceLocation("light_ray_data"),new LightRayCapabilityProvider());

                }
            }
            if (!event.getCapabilities().containsValue(ProjCapabilityProvider.ProjCap)){//TODO
                event.addCapability(CelestialAwakening.createResourceLocation("projectile_data"),new ProjCapabilityProvider((CA_Projectile) event.getObject()));
                //System.out.println("PROJ IS " + event.getObject().getName());
            }
        }

        else if (entity instanceof LivingEntity){
            if (!event.getCapabilities().containsValue(LivingEntityCapabilityProvider.capability)){
                event.addCapability(CelestialAwakening.createResourceLocation("living_entity_data"),new LivingEntityCapabilityProvider());
            }
            if (entity instanceof Player && !event.getCapabilities().containsValue(PlayerCapabilityProvider.capability)){
                event.addCapability(CelestialAwakening.createResourceLocation("player_data"),new PlayerCapabilityProvider());
            }
        }
    }


}
