package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.networking.client.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerCapS2CPacket {
    private final PlayerCapability cap;

    public PlayerCapS2CPacket(PlayerCapability cap) {
        this.cap = cap;
    }
    public PlayerCapS2CPacket(FriendlyByteBuf buf){
        this.cap=new PlayerCapability();
        CompoundTag tag=buf.readNbt();
        cap.loadNBTData(tag,false);
    }
    public void toBytes(FriendlyByteBuf buf){//claims cap is null
        CompoundTag nbt=cap.initNBTData(new CompoundTag());
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context=supplier.get();
        context.enqueueWork(()->{
            //client-side

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->
                    ClientPlayerData.setData( cap)
            );
        });
        return true;
    }
}
