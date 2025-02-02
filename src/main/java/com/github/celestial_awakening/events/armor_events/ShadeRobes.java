package com.github.celestial_awakening.events.armor_events;

import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.GenericCommandPattern;
import com.github.celestial_awakening.util.CA_UUIDs;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

public class ShadeRobes extends ArmorEffect {
    @Override
    public void performActions(Player player,int cnt, Event event) {
        if (event instanceof LivingEquipmentChangeEvent){
            pieceEffect(player,cnt);
        }
        else if(event instanceof LivingHurtEvent){
            if (cnt==4){
                fadeEffect(player, (LivingHurtEvent) event);

            }
        }
        else if (event instanceof LivingDamageEvent){
            if (cnt==4){
                shadowStrikeEffect(player, (LivingDamageEvent) event);
            }
        }
        else if(event instanceof PlayerEvent.PlayerLoggedInEvent){
            pieceEffect(player,cnt);
        }
        else if (event instanceof PlayerEvent.Clone){
            pieceEffect(player,cnt);
        }
        /*
            @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()){
            //It seems that, if player dies, their capability doesn't work anymore. See why
            //it seems to work now? maybe it was the onLivingEntityDeath thing i changed
            //should reset capability data so maybe change this
            event.getOriginal().getCapability(EntityEnchantmentDataProvider.EnchantmentData).ifPresent(
                    oldStore->event.getOriginal().getCapability(EntityEnchantmentDataProvider.EnchantmentData).ifPresent(newStore->newStore.copyDataFrom(oldStore)));
        }
    }
         */
    }

    @Override
    public void onItemTooltipEvent(ItemTooltipEvent event, int cnt) {

    }
    @Override
    void effectNames(ItemTooltipEvent event, int cnt) {

    }

    @Override
    void longDesc(ItemTooltipEvent event, int cnt) {

    }
    class FadeMitigationCommandPattern extends GenericCommandPattern {
        public FadeMitigationCommandPattern(Object[] params) {
            super(params);
        }
        /*
        params consist of
        player entity to damage
        dmge amount
        location of the einitial damage instance
         */
        protected boolean execute(){
            Player player= (Player) this.params[0];
            float dmg= (float) this.params[1];
            Vec3 prevPos= (Vec3) this.params[2];
            Vec3 currentPos=player.position();
            double dist=currentPos.distanceTo(prevPos);
            if (dist>5){
                dist=5;
            }
            //0 to 5 dist
            //0 to 54% dmg reduction
            float dmgMult= (100f-(float) (4.5f*Math.pow(dist,1.55D)))/100f;
            System.out.println("WE ARE GOING FADE TO REDUCE FROM " + dmg + " TO " + dmg*dmgMult);
            player.setHealth(player.getHealth()-dmg*dmgMult);
            return false;
        }

    }

    void pieceEffect(Player player,int amt){
        if (amt==0 && player.getAttribute(ForgeMod.ENTITY_REACH.get()).getModifier(CA_UUIDs.shadePieceEffectID)!=null){
            player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(CA_UUIDs.shadePieceEffectID);
        }
        else{

            float rangeIncrease= (float) (0.1f*Math.pow(2,amt-1));
            if (amt==0){
                rangeIncrease=0;
            }
            AttributeModifier attributeModifier=new AttributeModifier(CA_UUIDs.shadePieceEffectID,"Expanding Darkness",rangeIncrease,AttributeModifier.Operation.ADDITION);
            if (player.getAttribute(ForgeMod.ENTITY_REACH.get()).getModifier(CA_UUIDs.shadePieceEffectID)!=null){
                player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(CA_UUIDs.shadePieceEffectID);
            }
            player.getAttribute(ForgeMod.ENTITY_REACH.get()).addTransientModifier(attributeModifier);
        }

    }
    void fadeEffect(Player player,LivingHurtEvent event){
        if (event.getEntity()==player){
            float dmg=event.getAmount();
            Vec3 dmgPos=player.position();
            FadeMitigationCommandPattern command=new FadeMitigationCommandPattern(new Object[]{player,dmg,dmgPos});
            DelayedFunctionManager.delayedFunctionManager.insertIntoPlayerMap(player,command,100);
            event.setAmount(0);
        }
    }
    void shadowStrikeEffect(Player player,LivingDamageEvent event){
        if (event.getSource().getDirectEntity()==player){
            Vec3 playerPos=player.position();
            Vec3 targetPos=event.getEntity().position();
            double dist=targetPos.distanceTo(playerPos);
            float mult=((int) (dist*10));
            event.setAmount(event.getAmount()+0.125f*mult);
        }
    }
}
