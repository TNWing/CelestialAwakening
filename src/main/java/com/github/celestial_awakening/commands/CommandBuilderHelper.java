package com.github.celestial_awakening.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.Commands;

public class CommandBuilderHelper {

    public static ArgumentBuilder addThenLiteral(ArgumentBuilder builder, String lit){
        return builder.then(Commands.literal(lit));
    }

    public static ArgumentBuilder addThenArgument(ArgumentBuilder builder, String argName, ArgumentType type){
        return builder.then(Commands.argument(argName,type));
    }
}
