package com.github.celestial_awakening.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public abstract class BaseCommand {
    public BaseCommand(CommandDispatcher<CommandSourceStack> dispatcher, ArgumentBuilder argumentBuilder, int permLvl){
        //dispatcher.register(Commands.literal("celawake").requires(user -> {return user.hasPermission(permLvl);}).then(CommandBuilderHelper.add));
    }

    private int execute(){
        return 1;
    }
}
