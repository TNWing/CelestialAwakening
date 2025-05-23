package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.networking.packets.client.ClientLevelData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LevelCapS2CPacket {
    private final LevelCapability cap;
    public LevelCapS2CPacket(LevelCapability capability){
        this.cap=capability;
    }
    public LevelCapS2CPacket(FriendlyByteBuf buf){
        this.cap=new LevelCapability(null);
        CompoundTag tag=buf.readNbt();
        //okay so the tag does contain the data

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
            if (cap.levelResourceKey==null){
                cap.levelResourceKey=Minecraft.getInstance().level.dimension();
            }
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()-> ClientLevelData.setData( cap));
            Minecraft.getInstance().level.updateSkyBrightness();

        });
        return true;
    }
}
