package com.github.celestial_awakening.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DelayedFunctionManager {
    //change level command map to resourcekey<level>
    //my guess is that the levelcommandmap doesnt clear btwn worlds
    ConcurrentHashMap<Level, ArrayList<CommandMapValue>> levelCommandMap=new ConcurrentHashMap<>();
    ConcurrentHashMap<Player, ArrayList<CommandMapValue>> playerCommandMap=new ConcurrentHashMap<>();


    public static DelayedFunctionManager delayedFunctionManager=new DelayedFunctionManager();

    public ConcurrentHashMap<Level, ArrayList<CommandMapValue>> getLevelCommandMap(){
        return levelCommandMap;
    }

    public ConcurrentHashMap<Player,ArrayList<CommandMapValue>> getPlayerCommandMap(){
        return playerCommandMap;
    }

    public void insertIntoLevelMap(Level level,GenericCommandPattern command, Integer ticks, Boolean bool){
        //prevent modification on client side?
        if (level!=null && !levelCommandMap.containsKey(level)){
            levelCommandMap.put(level,new ArrayList<>());
        }
        levelCommandMap.get(level).add(new CommandMapValue(command,ticks,bool));
    }
    public void insertIntoPlayerMap(Player player,GenericCommandPattern command, Integer ticks){
        if (!playerCommandMap.containsKey(player)){
            playerCommandMap.put(player,new ArrayList<>());
        }
        playerCommandMap.get(player).add(new CommandMapValue(command,ticks,false));
    }

    public void tickLevelMap(Level lvl){
        ArrayList<CommandMapValue> arrayList = levelCommandMap.get(lvl);
        if (arrayList != null) {
            List<CommandMapValue> toRemove = new ArrayList<>();
            List<CommandMapValue> toUpdate = new ArrayList<>();
            for (CommandMapValue val : arrayList) {
                val.decrementTicks();
                if (val.ticks <= 0) {
                    if (!val.repeat) {
                        val.pattern.execute();
                        toRemove.add(val);
                    } else {
                        if (val.pattern.execute()) {
                            toRemove.add(val);
                        } else {
                            toUpdate.add(val);
                        }
                    }
                }
            }

            arrayList.removeAll(toRemove);
            for (CommandMapValue val : toUpdate) {
                val.ticks = val.pattern.getDelay();
            }
        }
    }

    public void tickPlayerMap(Player player){
        ArrayList<CommandMapValue> arrayList=playerCommandMap.get(player);
        if (arrayList!=null){
            Iterator<CommandMapValue> iterator=arrayList.iterator();
            while (iterator.hasNext()){
                CommandMapValue val=iterator.next();
                val.decrementTicks();
                if (val.ticks<=0){
                    if (!val.repeat){
                        val.pattern.execute();
                        iterator.remove();
                    }
                    else{
                        if (val.pattern.execute()){
                            iterator.remove();
                        }
                        else{
                            val.ticks=val.pattern.getDelay();
                        }

                    }
                }
            }
        }

    }
}
