package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.capabilities.*;
import com.github.celestial_awakening.commands.DivinerDataCommand;
import com.github.celestial_awakening.damage.DamageSourceNoIFrames;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.events.armor_events.*;
import com.github.celestial_awakening.events.custom_events.MoonScytheAttackEvent;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.items.CustomArmorItem;
import com.github.celestial_awakening.items.CustomArmorMaterial;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class EventManager {
    public static LunarEvents lunarEvents=new LunarEvents();
    public static SolarEvents solarEvents=new SolarEvents();

    private static final Map.Entry<ArmorMaterial,ArmorEffect> lunarArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.MOONSTONE,new LunarArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> radiantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.SUNSTONE,new RadiantArmor());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> remnantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.DYING_LIGHT_ESSENCE,new RemnantArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> umbraArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.EBONY_FUR,new UmbraArmor());



    private static final Map.Entry<ArmorMaterial,ArmorEffect> shadeArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.PULSATING_DARKNESS,new ShadeRobes());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> stellarRobes=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.COSMIC_HIDE,new StellarRobes());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> everlightArmor =new AbstractMap.SimpleEntry<>(CustomArmorMaterial.CONCENTRATED_LIGHT_ESSENCE,new EverlightArmor());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> knightmareSuit=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.MIDNIGHT_IRON,new KnightmareSuit());


    private static final Map<ArmorMaterial,ArmorEffect> armorMaterials =  (new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor)
            .put(radiantArmor)
            .put(remnantArmor)
            .put(shadeArmor)
            .put(stellarRobes)
            .put(umbraArmor)
            .put(everlightArmor)
            .put(knightmareSuit)
            .build();


    private static final Map<ArmorMaterial,ArmorEffect> armorEffectTick=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor).put(everlightArmor).put(radiantArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectEquipmentChange=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor).put(shadeArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectBlockBreak=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(radiantArmor).put(lunarArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingHurtOthers=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(umbraArmor).put(shadeArmor).put(remnantArmor).put(knightmareSuit)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingHurtSelf=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDamageOthers=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDamageSelf=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(shadeArmor).put(remnantArmor).put(lunarArmor).put(everlightArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDeath=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(radiantArmor).put(remnantArmor).put(umbraArmor).put(knightmareSuit).put(everlightArmor)
            .build();

    private static ParticleManager particleManager=ParticleManager.createParticleManager();

    private static int currentDay;


    //for some odd reason, this never fires, will have to manually spawn prowlers then since they are the only ones who can spawn naturally


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        new DivinerDataCommand(event.getDispatcher(),2);
        ConfigCommand.register(event.getDispatcher());

    }

    //TODO: spawn multiple prowlers in here, base it off the cel beacon spawning
    @SubscribeEvent
    public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event){

    }

    //TODO: make sure this works
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()){
            //It seems that, if player dies, their capability doesn't work anymore. See why
            //it seems to work now? maybe it was the onLivingEntityDeath thing i changed
            //should reset capability data so maybe change this
            event.getOriginal().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(
                    oldStore->event.getOriginal().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(newStore->newStore.copy(oldStore)));
        }

    }

    @SubscribeEvent
    public static void onServerload(LevelEvent.Unload event) {
        // Clear the data in DelayedFunctionManager when a world is unloaded
        DelayedFunctionManager.delayedFunctionManager.getLevelCommandMap().clear();
        DelayedFunctionManager.delayedFunctionManager.getPlayerCommandMap().clear();
    }

    @SubscribeEvent
    public void onScytheAttack(MoonScytheAttackEvent event){
        ItemStack itemStack=event.getItemStack();
        boolean isCrit=event.getCrit();
        MoonScytheCapability cap=itemStack.getCapability(MoonScytheCapabilityProvider.ScytheCap).orElse(null);
        Level level=event.getLevel();
        if (cap!=null && !level.isClientSide){
            ServerLevel serverLevel= (ServerLevel) level;
            LivingEntity owner=event.getOwner();
            float hAng=event.getHAng();
            //create(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR,float width,float height,float rs)
            if (isCrit){
                if (cap.getStrikeCD()<=0){//performs a powerful short ranged strike
                    LunarCrescent crescent=LunarCrescent.create(serverLevel,event.getDmg(),90,2.4f,hAng,0,90);
                    crescent.setPos(event.getSpawnpoint());
                    crescent.setYRot(owner.getYRot());
                    crescent.setOwner(event.getOwner());
                    serverLevel.addFreshEntity(crescent);
                    ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),crescent.level().dimension());
                    cap.changeStrikeCD(100);
                }
            }
            else{
                if (cap.getWaveCD()<=0){//performs a sweeping 180-arc wave
                    LunarCrescent crescent=LunarCrescent.create(serverLevel,event.getDmg(),90,2.4f,hAng,0,0);
                    crescent.setPos(event.getSpawnpoint());
                    crescent.setYRot(owner.getYRot());
                    crescent.setOwner(event.getOwner());
                    serverLevel.addFreshEntity(crescent);
                    ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),crescent.level().dimension());
                    cap.changeWaveCD(100);
                }
            }
        }
    }



    public void onLivingTick(LivingEvent.LivingTickEvent event){

    }


    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event){
        int cnt=0;
        Player player=event.getEntity();
        if (player!=null){

            for (ItemStack armorStack : player.getInventory().armor) {
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem)) {
                    ArmorItem armorItem= (ArmorItem) armorStack.getItem();
                    if (armorItem.getMaterial()==CustomArmorMaterial.MOONSTONE){
                        cnt++;
                    }
                }
            }
        }
        if (cnt>0){
            ((LunarArmor)lunarArmor.getValue()).onFishEvent(event,cnt);
        }
    }

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event){
        EquipmentSlot slot=event.getSlot();
        if (slot.isArmor() && event.getEntity() instanceof Player){
            Player player= (Player) event.getEntity();
            for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectEquipmentChange.entrySet()) {
                int cnt=countPieces(player,entry.getKey());
                entry.getValue().onEquipmentChange(event,player,cnt);
            }
        }
    }
    @SubscribeEvent
    public void onItemToolTip(ItemTooltipEvent event){
        ItemStack itemStack=event.getItemStack();
        Player player=event.getEntity();
        if (player!=null){
            if (itemStack.getItem() instanceof CustomArmorItem){
                CustomArmorItem armorItem= (CustomArmorItem) itemStack.getItem();
                CustomArmorMaterial material= (CustomArmorMaterial) armorItem.getMaterial();
                if(armorMaterials.containsKey(material)){
                    int cnt=countPieces(player,material);
                    armorMaterials.get(material).onItemTooltipEvent(event,cnt);
                }

            }
        }

    }
    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event){
        ItemStack itemStack=event.getItem();
        LivingEntity player=event.getEntity();
        if (player instanceof Player && itemStack.getFoodProperties(null)!=null && itemStack.hasTag() && itemStack.getTag().contains("LifeFragHeal")){
            float heal=itemStack.getTag().getFloat("LifeFragHeal");
            player.heal(heal);
        }

    }


    @SubscribeEvent
    public void onServerLevelLoad(LevelEvent.Load event){

        LevelAccessor levelAccessor=event.getLevel();

        if (!levelAccessor.isClientSide()){
            ServerLevelAccessor serverLevelAccessor= (ServerLevelAccessor) levelAccessor;
            ServerLevel serverLevel=serverLevelAccessor.getLevel();
            LevelCapability cap=serverLevel.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
            if (cap!=null){
                cap.loadNBTAfterLevelLoad();
                ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),serverLevel.dimension());
            }
        }

    }
    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event){
        Player player= event.getPlayer();
        Level level=player.level();
        if (level instanceof ServerLevel){
            LevelCapability cap=level.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
            BlockPos blockPos=event.getPos();
            if (cap!=null){
                if (cap.currentMoonstonePos.containsKey(blockPos)){
                    ItemEntity itemEntity =new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.MOONSTONE.get()));
                    level.addFreshEntity(itemEntity);
                    cap.currentMoonstonePos.remove(blockPos);
                }
            }
            solarEvents.dropSunstone(level,event);
            for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectBlockBreak.entrySet()) {
                int cnt=countPieces(player,entry.getKey());
                entry.getValue().onBlockBreak(event,player,cnt);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event){
        Entity directEntity=event.getSource().getDirectEntity();
        Entity causingEntity=event.getSource().getEntity();
        LivingEntity target=event.getEntity();
        if (!target.level().isClientSide){
            if (target instanceof Player){
                Player player=(Player) target;
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDamageSelf.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    entry.getValue().onLivingDamageSelf(event,player,cnt);
                }
                if (directEntity instanceof Mob){
                    Mob mob= (Mob) directEntity;
                    if (mob.hasEffect(MobEffectInit.MARK_OF_HAUNTING.get())){
                        if (!event.isCanceled()){
                            mob.removeEffect(MobEffectInit.MARK_OF_HAUNTING.get());
                            LivingEntityCapability cap=target.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
                            if (cap!=null){
                            }
                        }
                    }
                }
            }

            if(causingEntity instanceof Player){
                Player player=(Player) causingEntity;
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDamageOthers.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    entry.getValue().onLivingDamageOthers(event,player,cnt);
                }
            }
            /*
            if (livingEntity.hasEffect(MobEffectInit.EXPOSING_LIGHT.get())){
                ExposingLightMobEffectInstance exposingLightInstance= (ExposingLightMobEffectInstance) livingEntity.getEffect(MobEffectInit.EXPOSING_LIGHT.get());
                int stacks=exposingLightInstance.getStacks();
                event.setAmount(event.getAmount()*(1 + 0.015f*stacks));
                if (exposingLightInstance.getStackCD()==0){

                }
                exposingLightInstance.increaseStacks(1);
            }


             */

        }
    }


    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event){
        if (!event.getEntity().level().isClientSide){
            LivingEntity deadEntity=event.getEntity();
            if(event.getSource().getEntity() instanceof Player){
                Player player=(Player) event.getSource().getEntity();
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDeath.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    entry.getValue().onLivingDeath(event,player,cnt);
                }
            }
        }
    }


    @SubscribeEvent
    public void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        if (event.getTo() == null){

        }
    }
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event){
        LivingEntity target=event.getEntity();
        if (!target.level().isClientSide){
            float amt=event.getAmount();
            ServerLevel level= (ServerLevel) target.level();
            Entity causingEntity=event.getSource().getEntity();
            Entity directEntity=event.getSource().getDirectEntity();
            if (event.getSource() instanceof DamageSourceNoIFrames){
                target.invulnerableTime= ((DamageSourceNoIFrames) event.getSource()).invulTicks;
            }
            if (target instanceof Player){
                Player player=(Player) event.getEntity();
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingHurtSelf.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    entry.getValue().onLivingHurtSelf(event,player,cnt);
                }
            }
            if(causingEntity instanceof Player){
                Player player=(Player) event.getSource().getEntity();
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingHurtOthers.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    entry.getValue().onLivingHurtOthers(event,player,cnt);
                }
            }
            if (directEntity instanceof AbstractArrow){
                AbstractArrow arrow= (AbstractArrow) event.getSource().getDirectEntity();
                if (arrow.getTags().contains("CA_FluorescentBoost") && arrow.getOwner() instanceof LivingEntity){
                    Vec3 offset=new Vec3(3,0.5f,3);
                    AABB aabb=new AABB(target.position().subtract(offset),target.position().add(offset));
                    List<LivingEntity> livingEntityList= level.getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate((LivingEntity)arrow.getOwner()));
                    for (LivingEntity livingEntity:livingEntityList) {
                        livingEntity.hurt(level.damageSources().mobAttack((LivingEntity) arrow.getOwner()), amt*0.25f);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTrade(TradeWithVillagerEvent event){
        ItemStack costA=event.getMerchantOffer().getBaseCostA();
        ItemStack costB=event.getMerchantOffer().getCostB();
        Player player=event.getEntity();
        if (false){//if shady deal is active for player, refund part of costs
            if (costA.getCount()>1){
                ItemStack refundA=costA.copy();
                refundA.setCount((int)(costA.getCount()*0.1f));
                player.addItem(refundA);
            }
            if (costB.getCount()>1){
                ItemStack refundB=costB.copy();
                refundB.setCount((int)(costB.getCount()*0.1f));
                player.addItem(refundB);
            }
        }
    }


        @SubscribeEvent
        public void onEntityLoad(EntityJoinLevelEvent event) {
            if (!(event.getEntity() instanceof LivingEntity)) {
                return;
            }

            LivingEntity entity = (LivingEntity) event.getEntity();

            CompoundTag entityTag = entity.serializeNBT();
            if (!entityTag.contains("ActiveEffects")) {
                return;
            }

            ListTag effectsList = entityTag.getList("ActiveEffects", 10); // 10 = CompoundTag type
            System.out.println("ENTITY OF NAME " + entity + " HAS ACTIVE EFFECT LIST? " + effectsList);
            // Clear current effects so we can re-add them
            //entity.removeAllEffects();

            for (int i = 0; i < effectsList.size(); i++) {
                CompoundTag effectTag = effectsList.getCompound(i);
                System.out.println("ETAG IS " + effectTag);
                MobEffectInstance effectInstance = null;
                if (effectTag.contains("Stage")) {
                    System.out.println("WE HAVE A STAGE  TAG HERE");
                    // Use your custom loader to get an instance with updated custom data
                    //effectInstance = CustomMobEffectInstance.load(effectTag);
                } else {
                    // Fall back to vanilla loading if no custom data is present
                    effectInstance = MobEffectInstance.load(effectTag);
                }
                if (effectInstance != null) {
                    //entity.addEffect(effectInstance);
                }
            }
        }

    /*
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.phase== TickEvent.Phase.START){

        }
    }

     */

    @SubscribeEvent
    public void onServerLevelTick(TickEvent.LevelTickEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.phase== TickEvent.Phase.START && event.side.isServer()){
            ServerLevel level = (ServerLevel) event.level;
            level.isDay();
            DelayedFunctionManager.delayedFunctionManager.tickLevelMap(level);//this is called twice
            LevelCapability cap=level.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
            int time= (int) (level.getDayTime()%24000L);
            if (cap!=null){
                if (cap.divinerEyeFromState>-1){
                    solarEvents.detectPlayers(level,cap);
                }
                else{
                    solarEvents.canCreateDivinerEye(event);
                }
            }
            if (time>12000){
                int phase=level.getMoonPhase();
                switch(phase){
                    case 0:{
                        lunarEvents.revealMoonstone(level);
                        break;
                    }
                    default:{
                        break;
                    }
                }
                lunarEvents.detectIfLookingAtCelestialBody(level,-1);
                if (time==18000){
                    lunarEvents.midnightIronTransformation(level);
                    boolean didSpawnPK=lunarEvents.attemptPKSpawn(level);
                }

            }

            particleManager.generateParticles(level);
        }
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (event.phase== TickEvent.Phase.START && event.side.isServer()){
            DelayedFunctionManager.delayedFunctionManager.tickPlayerMap(player);
            for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectTick.entrySet()) {
                int cnt=countPieces(player,entry.getKey());
                entry.getValue().onPlayerTick(event,player,cnt);
            }
            LivingEntityCapability cap=player.getCapability(LivingEntityCapabilityProvider.playerCapability).orElse(null);
            if (cap!=null){
                cap.tickAbilityMap();
            }
        }


    }

    private int countPieces(Player player, ArmorMaterial material) {
        int cnt=0;
        if (player!=null){

            for (ItemStack armorStack : player.getInventory().armor) {
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem)) {
                    ArmorItem armorItem= (ArmorItem) armorStack.getItem();
                    if (armorItem.getMaterial()==material){
                        cnt++;
                    }
                }
            }
        }

        return cnt;
    }
}
