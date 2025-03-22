package com.github.celestial_awakening.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;

import java.util.List;
import java.util.function.Predicate;

public class CA_Predicates {
    //Gets all players and allied mobs
    public static Predicate getPlayersAndAlliedMobsPredicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            if (attacker.isAlliedTo(o)){
                return true;
            }
            if (o instanceof Player || o instanceof SnowGolem){
                return true;
            }
            if (o instanceof Wolf &&  ((Wolf) o).isAlliedTo(attacker)){
                return true;
            }
            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return true;
            }
            return false;
        };
        return predicate;
    }

    //Simply gets all players
    public static Predicate getPlayersPredicate(){
        Predicate predicate= o -> o instanceof Player;
        return predicate;
    }
    //Uses vanilla teams to determine allies, and will also select all vanilla allied mobs
    public static Predicate sameTeamAndAlliesPredicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            Team team=o.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)){
                return true;
            }
            if (o instanceof Wolf && ((Wolf) o).getOwner().isAlliedTo(attacker)){
                return true;
            }
            if (o instanceof IronGolem && (((IronGolem) o).getTarget() !=attacker)){
                return true;
            }
            return o.isAlliedTo(attacker);
        };
        return predicate;
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
            Team team=o.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }
    //Uses Vanila teams to determine enemies, but will always ignore players and allied mobs
    public static Predicate opposingTeams_IgnorePlayersAndAllies_Predicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            Team team=o.getTeam();
            Team ownerTeam=attacker.getTeam();
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


    //Uses Vanila teams to determine enemies, but ignores entities of the same class.
    public static Predicate opposingTeams_IgnoreSameClass_Predicate(LivingEntity attacker){
        Predicate<Entity> predicate= o -> {
            Team team=o.getTeam();
            Team ownerTeam=attacker.getTeam();
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
            Team team=o.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)  || classes.contains(o.getClass())){
                return false;
            }
            return !o.isAlliedTo(attacker);
        };
        return predicate;
    }
}
