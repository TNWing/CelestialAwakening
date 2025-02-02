package com.github.celestial_awakening.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Team;

import java.util.List;
import java.util.function.Predicate;

public class CA_Predicates {
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
