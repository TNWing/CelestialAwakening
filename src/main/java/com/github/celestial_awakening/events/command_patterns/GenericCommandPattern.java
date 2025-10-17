package com.github.celestial_awakening.events.command_patterns;

public abstract class GenericCommandPattern {
    protected Object[] params;
    private int delay;
    public GenericCommandPattern(Object[] params){
        this.params=params;
        delay=0;
    }
    public GenericCommandPattern(Object[] params, int delay){
        this.params=params;
        this.delay=delay;
    }

    public Object[] getParams(){
        return params;
    }
    public Object getParamFromInd(int ind){
        return params[ind];
    }


    public int getDelay(){
        return delay;
    }

    public void setDelay(int i){
        delay=i;
    }
    //abstract protected void execute();
    abstract public boolean execute();
}
