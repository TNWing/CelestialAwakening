package com.github.celestial_awakening.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class CA_Predicates {
    //Gets all players and allied mobs
    public static Predicate getPlayersAndAlliedMobsPredicate(LivingEntity attacker){
        return (Predicate<Entity>) o -> {
            if (attacker.isAlliedTo(o) || o.equals(attacker)){
                return true;
            }
            if (o instanceof Player || o instanceof SnowGolem){
                return true;
            }
            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return true;
            }
            return false;
        };
    }

    //Simply gets all players
    public static Predicate getPlayersPredicate(){
        return o -> o instanceof Player;
    }
    //Uses vanilla teams to determine allies, and will also select all vanilla allied mobs
    public static Predicate sameTeamAndAlliesPredicate(LivingEntity attacker){
        return (Predicate<LivingEntity>) o -> {
            if (attacker.isAlliedTo(o) || o.equals(attacker) || (o instanceof SnowGolem && ((SnowGolem) o).getTarget()!=attacker)){
                return true;
            }
            if (o instanceof IronGolem && (((IronGolem) o).getTarget() !=attacker)){
                return true;
            }
            return o.isAlliedTo(attacker);
        };
    }
    //Uses vanilla teams to determine allies
    public static Predicate sameTeam(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            if (o.equals(attacker)){
                return true;
            }
            return o.isAlliedTo(attacker);
        };
        return predicate;
    }
    //Uses Vanila teams to determine enemies
    public static Predicate opposingTeamsPredicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            if (o.equals(attacker)){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }
    //Uses Vanila teams to determine enemies, but will always ignore players, allied mobs
    public static Predicate opposingTeams_IgnorePlayersAndAllies_Predicate(LivingEntity attacker){
        Predicate<LivingEntity> predicate= o -> {
            if (o instanceof Player || o instanceof SnowGolem){
                return false;
            }
            if (o instanceof Wolf &&  (((Wolf) o).getOwner()==attacker || ((Wolf) o).getOwner().isAlliedTo(attacker))){
                return false;
            }
            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }

    //Uses Vanila teams to determine enemies, but will always ignore players, allied mobs, and any passive mobs
    public static Predicate opposingTeams_IgnorePlayers_Allies_Passive_Predicate(LivingEntity attacker){
        Predicate<LivingEntity> predicate= o -> {
            if (o instanceof Player || o instanceof SnowGolem){
                return false;
            }
            if (o instanceof Wolf &&  (((Wolf) o).getOwner()==attacker || ((Wolf) o).getOwner().isAlliedTo(attacker))){
                return false;
            }
            if (o instanceof AbstractVillager){
                return false;
            }
            if (o instanceof Animal){
                LivingEntity target=((Animal) o).getTarget();
                return target!=null && !(target instanceof Player);
            }

            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }

    //ignores players, allied mobs, and mobs with no target
    public static Predicate opposingTeams_IgnorePlayers_Allies_NoTarget_Predicate(LivingEntity attacker){
        Predicate<LivingEntity> predicate= o -> {
            if (o instanceof Player || o instanceof SnowGolem){
                return false;
            }
            if (o instanceof Wolf &&  (((Wolf) o).getOwner()==attacker || ((Wolf) o).getOwner().isAlliedTo(attacker))){
                return false;
            }
            if (o instanceof AbstractVillager){
                return false;
            }
            if (o instanceof Animal){
                return !(((Animal) o).getTarget() instanceof Player);
            }

            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }

    //Uses Vanila teams to determine enemies, but ignores entities of the same class.
    public static Predicate opposingTeams_IgnoreSameClass_Predicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            if (o.equals(attacker)  || o.getClass()==attacker.getClass()){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }

    //Uses Vanila teams to determine enemies, but ignores entities that are listed in the provided parameter
    public static <T extends Entity> Predicate opposingTeams_IgnoreProvidedClasses_Predicate(LivingEntity attacker, List<Class<T>> classes){
        Predicate<Entity> predicate= o -> {
            if (o.equals(attacker)  || classes.contains(o.getClass())){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }
}
