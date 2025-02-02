package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ProjCapS2CPacket {
    private final int entityID;
    private final ProjCapability cap;
    public ProjCapS2CPacket(int entityID,ProjCapability cap){
        this.entityID=entityID;
        this.cap=cap;
    }
    public ProjCapS2CPacket(FriendlyByteBuf buf){
        System.out.println("Receiving on client");
        this.entityID=buf.readInt();
        cap=new ProjCapability(entityID);

        while(buf.isReadable()){
            CompoundTag tag=buf.readNbt();
            if (tag!=null){
                long serverTime=tag.getLong("ServerTime");
                float spd=tag.getFloat("spd");
                MovementModifier.modFunction spdFunc=MovementModifier.modFunction.values()[tag.getInt("spdFunc")];
                MovementModifier.modOperation spdOp=MovementModifier.modOperation.values()[tag.getInt("spdOp")];

                float hAng=tag.getFloat("hAng");
                float vAng=tag.getFloat("vAng");
                MovementModifier.modFunction angFunc=MovementModifier.modFunction.values()[tag.getInt("angFunc")];
                MovementModifier.modOperation angOp=MovementModifier.modOperation.values()[tag.getInt("angOp")];

                int delay=tag.getInt("Delay");
                int timer=tag.getInt("Timer");
                long currentTime= Minecraft.getInstance().level.getGameTime();
                long timeDiff=currentTime-serverTime;
                if (timeDiff>0){
                    long tempTD=timeDiff;
                    int tempDelay=delay;
                    tempDelay-=timeDiff;
                    tempTD-=delay;
                    if (tempDelay<0){
                        delay=0;
                    }
                    else{
                        delay=tempDelay;
                    }
                }
                MovementModifier mod=new MovementModifier(spdFunc,spdOp,angFunc,angOp,spd,hAng,vAng,delay,timer);
                cap.putInBackOfList(mod);
            }
        }

    }
    public void toBytes(FriendlyByteBuf buf){//claims cap is null
        buf.writeInt(this.entityID);
        List<MovementModifier> mmList=cap.getMMList();
        for (int i=0;i<mmList.size();i++){
            CompoundTag tag=new CompoundTag();
            MovementModifier mod=mmList.get(i);
            float spdChange=mod.getSpd();
            float hChange=mod.getHAng();
            float vChange=mod.getVAng();
            int spdFunc=mod.getSpdFunction().ordinal();
            int spdOp=mod.getSpdOperation().ordinal();
            int angFunc=mod.getAngFunction().ordinal();
            int angOp=mod.getAngOperation().ordinal();

            tag.putFloat("spd",spdChange);
            tag.putInt("spdFunc",spdFunc);
            tag.putInt("spdOp",spdOp);

            tag.putFloat("hAng",hChange);
            tag.putFloat("vAng",vChange);
            tag.putInt("angFunc",angFunc);
            tag.putInt("angOp",angOp);

            tag.putInt("Delay",mod.getDelay());
            tag.putInt("Timer",mod.getRemainingTicks());
            tag.putLong("ServerTime",mod.getServerTime());
            buf.writeNbt(tag);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context=supplier.get();
        context.enqueueWork(()->{
            Level level=Minecraft.getInstance().level;
            if (level!=null){
                Entity entity=level.getEntity(this.entityID);
                //List<CA_Projectile> ca_projectiles=level.getEntitiesOfClass(CA_Projectile.class,new AABB(-25,-7,75,25,105,25));
                if (entity!=null){
                    ProjCapability clientCap=entity.getCapability(ProjCapabilityProvider.ProjCap).orElse(null);
                    if (clientCap!=null){
                        clientCap.updateData(this.cap);
                    }
                }
            }
        });
        return true;
    }
}
