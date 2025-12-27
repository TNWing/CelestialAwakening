package com.github.celestial_awakening.commands;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;

public class ProwlerRaidCommand {
    public ProwlerRaidCommand(CommandDispatcher<CommandSourceStack> dispatcher, int permLvl) {
        dispatcher.register(Commands.literal("celawake").requires(user->user.hasPermission(permLvl))
                .then(Commands.literal("prowlers")
                        .then(Commands.literal("raid")
                            .then(Commands.literal("create")
                                .then(Commands.argument("targets", EntityArgument.player())
                                        .executes(context -> createRaid(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                                ))
                        .then(Commands.literal("counter")
                            .then(Commands.literal("set")
                                .then(Commands.argument("targets",EntityArgument.player())
                                    .then(Commands.argument("cnt",IntegerArgumentType.integer(0,10))
                                        .executes(context -> setCounter(context.getSource(),EntityArgument.getPlayer(context,"targets"), (short) IntegerArgumentType.getInteger(context,"cnt")))
                                     ))
                            )
                                .then(Commands.literal("query")
                                        .then(Commands.argument("target", EntityArgument.player())
                                                .executes(context -> queryCounter(context.getSource(),EntityArgument.getPlayer(context,"target")))
                                        )

                                )
        ))));
    }

    public int createRaid(CommandSourceStack stack, Collection<ServerPlayer> entities){
        ServerLevel serverLevel= stack.getLevel();
        LazyOptional<LevelCapability> levelOptional=serverLevel.getCapability(LevelCapabilityProvider.LevelCap);
        levelOptional.ifPresent(cap->{
            for(ServerPlayer serverPlayer : entities) {
                cap.raids.getOrCreateProwlerRaid(serverLevel,cap,serverPlayer.blockPosition(), (byte) 1,21);
            }
        });

        return 1;
    }

    public int setCounter(CommandSourceStack stack, ServerPlayer serverPlayer, short cnt){
            LazyOptional<PlayerCapability> playerOptional=serverPlayer.getCapability(PlayerCapabilityProvider.capability);
            playerOptional.ifPresent(cap->{
                cap.setProwlerRaidCounter(cnt);
            });

        return 1;
    }

    public int queryCounter(CommandSourceStack stack,ServerPlayer serverPlayer){
        LazyOptional<PlayerCapability> playerOptional=serverPlayer.getCapability(PlayerCapabilityProvider.capability);
        playerOptional.ifPresent(cap->{
            String msg="";
            msg+=String.format("Prowler Raid Counter for %s\n",serverPlayer.getName().getString());
            msg+=cap.getProwlerRaidCounter();
            stack.sendSystemMessage(Component.literal(msg));
        });

        return 1;
    }
}
