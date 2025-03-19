package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.capabilities.ProjCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static com.github.celestial_awakening.nbt_strings.MovementModifierNBTNames.*;
public class ProjCapS2CPacket {
    private final int entityID;
    private final ProjCapability cap;
    public ProjCapS2CPacket(int entityID,ProjCapability cap){
        this.entityID=entityID;
        this.cap=cap;
    }
    public ProjCapS2CPacket(FriendlyByteBuf buf){
        this.entityID=buf.readInt();
        cap=new ProjCapability(entityID);

        while(buf.isReadable()){
            CompoundTag tag=buf.readNbt();
            if (tag!=null){
                float spdMod=tag.getFloat(mm_spd);
                MovementModifier.modFunction sFunc=MovementModifier.modFunction.values()[tag.getInt(mm_spdFunc)];
                MovementModifier.modOperation sOp=MovementModifier.modOperation.values()[tag.getInt(mm_spdOp)];

                float hA=tag.getFloat(mm_hAng);
                float vA=tag.getFloat(mm_vAng);
                MovementModifier.modFunction aFunc=MovementModifier.modFunction.values()[tag.getInt(mm_angFunc)];
                MovementModifier.modOperation aOp=MovementModifier.modOperation.values()[tag.getInt(mm_angOp)];

                float zR=tag.getFloat(mm_rot);
                MovementModifier.modFunction rFunc=MovementModifier.modFunction.values()[tag.getInt(mm_rotFunc)];
                MovementModifier.modOperation rOp=MovementModifier.modOperation.values()[tag.getInt(mm_rotOp)];

                int d=tag.getInt(mm_delay);
                int timer=tag.getInt(mm_remainingTicks);
                int i=tag.getInt(mm_initialTicks);
                long sTime=tag.getLong(mm_serverTime);

                long currentTime= Minecraft.getInstance().level.getGameTime();
                long timeDiff=currentTime-sTime;
                if (timeDiff>0){
                    int tempDelay=d;
                    tempDelay-=timeDiff;
                    if (tempDelay<0){
                        d=0;
                    }
                    else{
                        d=tempDelay;
                    }
                }
                MovementModifier mod=new MovementModifier(sFunc,sOp,aFunc,aOp,rFunc,rOp,spdMod,hA,vA,zR,d,i,sTime,timer);
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
            int sFunc=mod.getSpdFunction().ordinal();
            int sOp=mod.getSpdOperation().ordinal();
            int aFunc=mod.getAngFunction().ordinal();
            int aOp=mod.getAngOperation().ordinal();

            tag.putFloat(mm_spd,spdChange);
            tag.putInt(mm_spdFunc,sFunc);
            tag.putInt(mm_spdOp,sOp);

            tag.putFloat(mm_hAng,hChange);
            tag.putFloat(mm_vAng,vChange);
            tag.putInt(mm_angFunc,aFunc);
            tag.putInt(mm_angOp,aOp);

            tag.putInt(mm_delay,mod.getDelay());
            tag.putInt(mm_remainingTicks,mod.getRemainingTicks());
            tag.putLong(mm_serverTime,mod.getServerTime());
            tag.putInt(mm_initialTicks,mod.getStartingTicks());
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
                    @NotNull LazyOptional<ProjCapability> clientCapOptional=entity.getCapability(ProjCapabilityProvider.ProjCap);
                    clientCapOptional.ifPresent(clientCap->{
                        clientCap.updateData(this.cap);
                    });
                }
            }
        });
        return true;
    }
}
