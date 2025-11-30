package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.networking.packets.client.ClientLEPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LivingEntityCapS2CPacket {
    private final LivingEntityCapability cap;

    public LivingEntityCapS2CPacket(LivingEntityCapability cap) {
        this.cap = cap;
    }
    public LivingEntityCapS2CPacket(FriendlyByteBuf buf){
        this.cap=new LivingEntityCapability();
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

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->
                    ClientLEPlayerData.setData( cap)
            );
        });
        return true;
    }
}
