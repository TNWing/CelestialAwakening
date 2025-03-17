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
    public static Predicate getPlayersAndAlliedMobsPredicate(LivingEntity attacker){
        Predicate predicate= o -> {
            if (o instanceof Player || o instanceof SnowGolem){
                return true;
            }
            if (o instanceof Wolf &&  (((Wolf) o).getOwner()==attacker || ((Wolf) o).getOwner().isAlliedTo(attacker))){
                return true;
            }
            if (o instanceof IronGolem && !(((IronGolem) o).getTarget() instanceof Player)){
                return true;
            }
            return false;
        };
        return predicate;
    }

    public static Predicate getPlayersPredicate(){
        Predicate predicate= o -> {
            if (o instanceof Player){
                return true;
            }
            return false;
        };
        return predicate;
    }
    public static Predicate sameTeamAndAlliesPredicate(LivingEntity attacker){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)){
                return true;
            }
            if (o instanceof Wolf &&  (((Wolf) o).getOwner()==attacker || ((Wolf) o).getOwner().isAlliedTo(attacker))){
                return true;
            }
            if (o instanceof IronGolem && (((IronGolem) o).getTarget() !=attacker)){
                return true;
            }
            if (team==null || ownerTeam==null){
                return false;
            }
            return ownerTeam.equals(team);
        };
        return predicate;
    }
    public static Predicate sameTeam(LivingEntity attacker){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)){
                return true;
            }
            if (team==null || ownerTeam==null){
                return false;
            }
            return ownerTeam.equals(team);
        };
        return predicate;
    }
    public static Predicate opposingTeamsPredicate(LivingEntity attacker){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)){
                return false;
            }
            if (team==null || ownerTeam==null){
                return true;
            }
            return !ownerTeam.equals(team);
        };
        return predicate;
    }
    public static Predicate opposingTeams_IgnorePlayersAndAllies_Predicate(LivingEntity attacker){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
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
            if (o.equals(attacker)){
                return false;
            }
            if (team==null || ownerTeam==null){
                return true;
            }
            return !ownerTeam.equals(team);
        };
        return predicate;
    }

    public static Predicate opposingTeams_IgnoreSameClass_Predicate(LivingEntity attacker){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)  || o.getClass()==attacker.getClass()){
                return false;
            }
            if (team==null || ownerTeam==null){
                return true;
            }
            return !ownerTeam.equals(team);
        };
        return predicate;
    }
    public static <T extends Entity> Predicate opposingTeams_IgnoreProvidedClasses_Predicate(LivingEntity attacker, List<Class<T>> classes){
        Predicate predicate= o -> {
            LivingEntity livingEntity= (LivingEntity) o;
            Team team=livingEntity.getTeam();
            Team ownerTeam=attacker.getTeam();
            if (o.equals(attacker)  || classes.contains(o.getClass())){
                return false;
            }
            if (team==null || ownerTeam==null){
                return true;
            }
            return !ownerTeam.equals(team);
        };
        return predicate;
    }
}
