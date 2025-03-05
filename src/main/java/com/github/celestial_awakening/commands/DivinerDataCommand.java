package com.github.celestial_awakening.commands;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class DivinerDataCommand{
    public DivinerDataCommand(CommandDispatcher<CommandSourceStack> dispatcher,int permLvl) {

        /*
              p_139167_.register(Commands.literal("weather").requires((p_139171_) -> {
         return p_139171_.hasPermission(2);
      }).then(Commands.literal("clear").executes((p_264806_) -> {
         return setClear(p_264806_.getSource(), -1);
      }).then(Commands.argument("duration", TimeArgument.time(1)).executes((p_264807_) -> {
         return setClear(p_264807_.getSource(), IntegerArgumentType.getInteger(p_264807_, "duration"));
      }))).then(Commands.literal("rain").executes((p_264805_) -> {
         return setRain(p_264805_.getSource(), -1);
      }).then(Commands.argument("duration", TimeArgument.time(1)).executes((p_264809_) -> {
         return setRain(p_264809_.getSource(), IntegerArgumentType.getInteger(p_264809_, "duration"));
      }))).then(Commands.literal("thunder").executes((p_264808_) -> {
         return setThunder(p_264808_.getSource(), -1);
      }).then(Commands.argument("duration", TimeArgument.time(1)).executes((p_264804_) -> {
         return setThunder(p_264804_.getSource(), IntegerArgumentType.getInteger(p_264804_, "duration"));
      }))));
         */

        //so have constructor pass argument builders to parent?
        //example commands
        /*
        literal literal literal literal
        /celawake solcult diviner query

        literal literal literal literal argument
        /celawake solcult diviner setCD
        /celawake solcult diviner setChance
         */
        //think of the .then() as an else if
        /*
        ,,,then if...them else if
         */
        dispatcher.register(Commands.literal("celawake").requires(user->user.hasPermission(permLvl))
                .then(Commands.literal("transcendents")
                        .then(Commands.literal("diviner")
                                .then(Commands.literal("query")
                                        .executes(context -> queryDiv(context.getSource()))
                                )
                                .then(Commands.literal("setCD")
                                        .then(Commands.argument("cd",  IntegerArgumentType.integer(0))
                                                .executes(context -> setDivCD(context.getSource(),IntegerArgumentType.getInteger(context, "cd")) ))
                                )
                                .then(Commands.literal("setChance")
                                        .then(Commands.argument("chance", FloatArgumentType.floatArg(0f,100f))
                                            .executes(context -> setDivChance(context.getSource(),FloatArgumentType.getFloat(context, "chance")) ))
                                )
                        )
                )
        );
    }

    private int queryDiv(CommandSourceStack stack){
        @NotNull LazyOptional<LevelCapability> capOptional;
        if (Config.divinerShared){
            capOptional=stack.getServer().overworld().getCapability(LevelCapabilityProvider.LevelCap);
        }
        else{
            capOptional=stack.getLevel().getCapability(LevelCapabilityProvider.LevelCap);
        }
        capOptional.ifPresent(cap->{
            String msg="";
            msg+="Diviner Eye Information\n";
            msg+="Current Chance:" + cap.divinerEyeChance + "%\n";
            msg+="Cooldown:" + (cap.divinerEyeCD/24000f) + " days\n";
            stack.sendSystemMessage(Component.literal(msg));

        });
        if (capOptional.isPresent()){
            return 1;
        }
        stack.sendSystemMessage(Component.literal("Failed to obtain diviner data"));
        return -1;
    }


    private int setDivCD(CommandSourceStack stack, int c) throws CommandSyntaxException {
        ServerPlayer p=stack.getPlayerOrException();
        @NotNull LazyOptional<LevelCapability> capOptional;
        if (Config.divinerShared){
            capOptional=p.server.overworld().getCapability(LevelCapabilityProvider.LevelCap);
        }
        else{
            capOptional=p.serverLevel().getCapability(LevelCapabilityProvider.LevelCap);
        }

        capOptional.ifPresent(cap->{
            cap.divinerEyeCD=c;
            stack.sendSystemMessage(Component.literal("Set diviner eye cooldown to " + c));
        });
        if (capOptional.isPresent()){
            return 1;
        }
        stack.sendSystemMessage(Component.literal("Failed to update diviner eye chance!"));
        return -1;
    }


    private int setDivChance(CommandSourceStack stack, float c) throws CommandSyntaxException {
        ServerPlayer p=stack.getPlayerOrException();
        @NotNull LazyOptional<LevelCapability> capOptional;
        if (Config.divinerShared){
            capOptional=p.server.overworld().getCapability(LevelCapabilityProvider.LevelCap);
        }
        else{
            capOptional=p.serverLevel().getCapability(LevelCapabilityProvider.LevelCap);
        }
        capOptional.ifPresent(cap->{
            cap.divinerEyeChance=c;
            stack.sendSystemMessage(Component.literal("Set diviner eye chance to " + c));

        });
        if (capOptional.isPresent()){
            return 1;
        }
        stack.sendSystemMessage(Component.literal("Failed to update diviner eye chance!"));
        return -1;
    }

}
