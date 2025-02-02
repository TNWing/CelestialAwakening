package com.github.celestial_awakening.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class BaseSolCultCommand extends BaseCommand {
    public BaseSolCultCommand(CommandDispatcher<CommandSourceStack> dispatcher, ArgumentBuilder argumentBuilder, int permLvl) {
        super(dispatcher, argumentBuilder,permLvl);
    }
}
