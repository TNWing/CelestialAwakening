package com.github.celestial_awakening.commands;

import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;

public class SanityCommand {
    public SanityCommand(CommandDispatcher<CommandSourceStack> dispatcher, int permLvl) {
        dispatcher.register(Commands.literal("celawake").requires(user->user.hasPermission(permLvl))
                .then(Commands.literal("sanity")
                        .then(Commands.argument("targets", EntityArgument.player())
                            .then(Commands.literal("query"))
                                .executes(context -> querySanity(context.getSource(), EntityArgument.getPlayers(context, "targets"))))
                )
        );
    }

    public int querySanity(CommandSourceStack stack, Collection<ServerPlayer> entities){
        ServerLevel serverLevel= stack.getLevel();
        LazyOptional<LevelCapability> levelOptional=serverLevel.getCapability(LevelCapabilityProvider.LevelCap);
        levelOptional.ifPresent(cap->{
            for(ServerPlayer serverPlayer : entities) {
                cap.raids.createProwlerRaid(serverLevel,serverPlayer.blockPosition(),1,1);
            }
        });

        return 1;
    }
}
