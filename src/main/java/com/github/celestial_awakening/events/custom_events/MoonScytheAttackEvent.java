package com.github.celestial_awakening.events.custom_events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

public class MoonScytheAttackEvent extends Event {
    private ItemStack itemStack;
    private boolean isCrit;
    private Level level;
    private Vec3 deltaMove;
    private Vec3 spt;
    private LivingEntity owner;
    private float hAng;
    private float dmg;
    private int cd;
    private boolean enhanced;
    public MoonScytheAttackEvent(ItemStack itemStack, boolean isCrit, Level level,Vec3 deltaMove,Vec3 spt,LivingEntity owner,float dmg,float hA,int cd){
        this.itemStack=itemStack;
        this.hAng=hA;
        this.isCrit=isCrit;
        this.level=level;
        this.deltaMove=deltaMove;
        this.spt=spt;
        this.owner=owner;
        this.dmg=dmg;
        this.cd=cd;
        this.enhanced=false;
    }
    public MoonScytheAttackEvent(ItemStack itemStack, boolean isCrit, Level level,Vec3 deltaMove,Vec3 spt,LivingEntity owner,float dmg,float hA,int cd,boolean e){
        this.itemStack=itemStack;
        this.hAng=hA;
        this.isCrit=isCrit;
        this.level=level;
        this.deltaMove=deltaMove;
        this.spt=spt;
        this.owner=owner;
        this.dmg=dmg;
        this.cd=cd;
        this.enhanced=e;
    }
    public ItemStack getItemStack(){
        return this.itemStack;
    }
    public boolean getCrit(){
        return this.isCrit;
    }
    public Level getLevel(){
        return this.level;
    }
    public Vec3 getMovement(){
        return this.deltaMove;
    }
    public Vec3 getSpawnpoint(){
        return this.spt;
    }
    public LivingEntity getOwner(){
        return this.owner;
    }
    public int getCD(){
        return this.cd;
    }
    public float getDmg(){
        return this.dmg;
    }
    public float getHAng(){
        return this.hAng;
    }
    public boolean isEnhanced(){
        return this.enhanced;
    }
}
