package com.github.celestial_awakening.networking;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.networking.packets.PlayerCapS2CPacket;
import com.github.celestial_awakening.networking.packets.ProjCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static SimpleChannel INSTANCE;
    private static int packetID=0;
    private static int id(){
        return packetID++;
    }

    public static void register(){
        SimpleChannel channel= NetworkRegistry.ChannelBuilder
                .named(CelestialAwakening.createResourceLocation("messages"))
                .networkProtocolVersion(()->"1.0")
                .clientAcceptedVersions(s->true)
                .serverAcceptedVersions(s->true)
                .simpleChannel();
        INSTANCE=channel;
        channel.messageBuilder(LevelCapS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LevelCapS2CPacket::new)
                .encoder(LevelCapS2CPacket::toBytes)
                .consumerMainThread(LevelCapS2CPacket::handle)
                .add();
        channel.messageBuilder(PlayerCapS2CPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerCapS2CPacket::new)
                .encoder(PlayerCapS2CPacket::toBytes)
                .consumerMainThread(PlayerCapS2CPacket::handle)
                .add();
        channel.messageBuilder(RefreshEntityDimsS2CPacket.class,id(),NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RefreshEntityDimsS2CPacket::new)
                .encoder(RefreshEntityDimsS2CPacket::toBytes)
                .consumerMainThread(RefreshEntityDimsS2CPacket::handle)
                .add();
        channel.messageBuilder(ProjCapS2CPacket.class,id(),NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ProjCapS2CPacket::new)
                .encoder(ProjCapS2CPacket::toBytes)
                .consumerMainThread(ProjCapS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToClientsInDim(MSG message, ResourceKey<Level> type){
        INSTANCE.send(PacketDistributor.DIMENSION.with(()->type),message);
    }
    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer client){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->client),message);
    }


    public static <MSG> void sendToAllClients(MSG message){
        INSTANCE.send(PacketDistributor.ALL.noArg(),message);
    }
}
