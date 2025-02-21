package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.capabilities.*;
import com.github.celestial_awakening.entity.projectile.CA_Projectile;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.items.MoonScythe;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.PlayerCapS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class AttachCapabilities {

    @SubscribeEvent
    public void onAttachLevelCap(AttachCapabilitiesEvent<Level> event){
        Level level=event.getObject();
        if (!event.getCapabilities().containsValue(LevelCapabilityProvider.LevelCap)){
            event.addCapability(new ResourceLocation(CelestialAwakening.MODID,"level_data"),new LevelCapabilityProvider(level));

        }
    }

    @SubscribeEvent
    public void onAttachItemStackCap(AttachCapabilitiesEvent<ItemStack> event){
        ItemStack itemStack =event.getObject();
        if (itemStack.getItem() instanceof MoonScythe){
            if (!event.getCapabilities().containsValue(MoonScytheCapabilityProvider.ScytheCap)){
                event.addCapability(new ResourceLocation(CelestialAwakening.MODID,"scythe_data"),new MoonScytheCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerID = player.getUUID();
        LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
        if (cap!=null){
            cap.setUUID(playerID);
            ModNetwork.sendToClient(new PlayerCapS2CPacket(cap),player);
        }
    }
    @SubscribeEvent
    public void onAttachEntityCap(AttachCapabilitiesEvent<Entity> event){
        Entity entity=event.getObject();
        if (entity instanceof CA_Projectile){
            if (entity instanceof LightRay){
                if (!event.getCapabilities().containsValue(LightRayCapabilityProvider.cap)){
                    event.addCapability(new ResourceLocation(CelestialAwakening.MODID,"light_ray_data"),new LightRayCapabilityProvider());

                }
            }
            if (!event.getCapabilities().containsValue(ProjCapabilityProvider.ProjCap)){//TODO
                event.addCapability(new ResourceLocation(CelestialAwakening.MODID,"projectile_data"),new ProjCapabilityProvider((CA_Projectile) event.getObject()));
            }
        }

        else if (entity instanceof Player){
            if (!event.getCapabilities().containsValue(LivingEntityCapabilityProvider.playerCapability)){
                event.addCapability(new ResourceLocation(CelestialAwakening.MODID,"player_data"),new LivingEntityCapabilityProvider());
            }
        }
    }


}
