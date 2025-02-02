package com.github.celestial_awakening.events;

public class CommandMapValue {
    int ticks;
    GenericCommandPattern pattern;
    boolean repeat;
    public CommandMapValue(GenericCommandPattern gc,int t, boolean b){
        this.pattern=gc;
        this.ticks=t;
        this.repeat=b;
    }
    public void decrementTicks(){
        this.ticks--;
    }

    public void decrementTicks(int i){
        this.ticks-=i;
    }

    public GenericCommandPattern getPattern(){
        return pattern;
    }

    public int getTicks(){
        return ticks;
    }
    public boolean getRepeatBool(){
        return repeat;
    }
}
