package com.github.celestial_awakening.entity.combat.transcendents.nebure.teams;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

import java.util.Collection;
//maybe not rely on vanilla teams
public class TranscendentTeam extends Team {

    private final Scoreboard scoreboard;
    private final String name;
    public TranscendentTeam(Scoreboard sb){
        this.scoreboard=sb;
        name="Transcendents";
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public MutableComponent getFormattedName(Component p_83538_) {
        return null;
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return true;
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return false;
    }

    @Override
    public Visibility getNameTagVisibility() {
        return null;
    }

    @Override
    public ChatFormatting getColor() {
        return null;
    }

    @Override
    public Collection<String> getPlayers() {
        return null;
    }

    @Override
    public Visibility getDeathMessageVisibility() {
        return null;
    }

    @Override
    public CollisionRule getCollisionRule() {
        return null;
    }
}
