package com.github.celestial_awakening.networking.packets;

import com.github.celestial_awakening.menus.AstralterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AstralterTransmuteC2SPacket {
    private final int sunAmt;
    private final int moonAmt;

    public AstralterTransmuteC2SPacket(int s, int m) {
        this.sunAmt = s;
        this.moonAmt = m;
    }

    public AstralterTransmuteC2SPacket(FriendlyByteBuf buf){
        sunAmt=buf.readInt();
        moonAmt=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){//claims cap is null
        buf.writeInt(sunAmt);
        buf.writeInt(moonAmt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context=supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player=context.getSender();
            AbstractContainerMenu abstractcontainermenu = player.containerMenu;
            if (abstractcontainermenu instanceof AstralterMenu astralterMenu) {
                if (!player.containerMenu.stillValid(player)) {
                    return;
                }
                astralterMenu.setStones(this.sunAmt,this.moonAmt);
            }
        });
        return true;
    }
}
