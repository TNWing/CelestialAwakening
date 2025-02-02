package com.github.celestial_awakening.networking.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RefreshEntityDimsS2CPacket {
    private final int entityID;

    public RefreshEntityDimsS2CPacket(int entityID) {
        this.entityID = entityID;

    }
    public RefreshEntityDimsS2CPacket(FriendlyByteBuf buf){
        this.entityID=buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(this.entityID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context=supplier.get();
        context.enqueueWork(()->{

            Level level=Minecraft.getInstance().level;

            if (level!=null){
                Entity entity=level.getEntity(this.entityID);
                if (entity!=null){
                    entity.refreshDimensions();
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}
