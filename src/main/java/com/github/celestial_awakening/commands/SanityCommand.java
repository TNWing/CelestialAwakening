package com.github.celestial_awakening.commands;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;

public class SanityCommand {
    public SanityCommand(CommandDispatcher<CommandSourceStack> dispatcher, int permLvl) {
        dispatcher.register(Commands.literal("celawake").requires(user->user.hasPermission(permLvl))
                .then(Commands.literal("sanity")
                        .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("query"))
                                .executes(context -> querySanity(context.getSource(), EntityArgument.getPlayers(context, "targets"))))
                )
        );
    }

    public int querySanity(CommandSourceStack stack, Collection<ServerPlayer> entities){
        for(ServerPlayer serverPlayer : entities) {
            //cap.raids.createProwlerRaid(serverLevel,serverPlayer.blockPosition(), (byte) 1,1);
            LazyOptional<PlayerCapability> optional=serverPlayer.getCapability(PlayerCapabilityProvider.capability);
            optional.ifPresent(cap->{
                Component component=Component.literal(serverPlayer.getName()+ " has sanity " + cap.getInsanityPts());
                stack.sendSystemMessage(component);
            });
        }
        return 1;
    }
}
