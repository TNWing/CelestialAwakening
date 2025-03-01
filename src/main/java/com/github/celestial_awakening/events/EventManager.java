package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.Config;
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
import com.github.celestial_awakening.recipes.LifeFragFood;
import com.github.celestial_awakening.recipes.serializers.LifeFragFoodSerializer;
import com.github.celestial_awakening.util.CA_Predicates;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(
                    oldStore->event.getEntity().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(newStore->newStore.copyForRespawn(oldStore)));
            event.getOriginal().invalidateCaps();
        }

    }

    @SubscribeEvent
    public static void onServerload(LevelEvent.Unload event) {
        // Clear the data in DelayedFunctionManager when a world is unloaded
        DelayedFunctionManager.delayedFunctionManager.getLevelCommandMap().clear();
        DelayedFunctionManager.delayedFunctionManager.getPlayerCommandMap().clear();
    }

    @SubscribeEvent
    public static void onScytheAttack(MoonScytheAttackEvent event){
        ItemStack itemStack=event.getItemStack();
        boolean isCrit=event.getCrit();
        @NotNull LazyOptional<MoonScytheCapability> capOptional=itemStack.getCapability(MoonScytheCapabilityProvider.ScytheCap);
        Level level=event.getLevel();
        if (!level.isClientSide){
            capOptional.ifPresent(cap->{
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


            });

        }
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event){
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
    public static void onEquipmentChange(LivingEquipmentChangeEvent event){
        EquipmentSlot slot=event.getSlot();
        if (event.getEntity() instanceof Player){
            Player player= (Player) event.getEntity();
            if (slot.isArmor() ){

                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectEquipmentChange.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onEquipmentChange(event, player, cnt);
                    }
                }
            }
        }

    }
    @SubscribeEvent
    public static void onItemToolTip(ItemTooltipEvent event){
        ItemStack itemStack=event.getItemStack();
        Player player=event.getEntity();
        if (player!=null){
            if (itemStack.getItem() instanceof CustomArmorItem){
                CustomArmorItem armorItem= (CustomArmorItem) itemStack.getItem();
                CustomArmorMaterial material= (CustomArmorMaterial) armorItem.getMaterial();
                if(armorMaterials.containsKey(material)){
                    int cnt=countPieces(player,material);
                    armorMaterials.get(material).onItemTooltipEvent(event, cnt);
                }

            }
        }

    }
    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event){
        ItemStack itemStack=event.getItem();
        LivingEntity player=event.getEntity();
        if (player instanceof Player && itemStack.getFoodProperties(null) != null && itemStack.hasTag()) {
            assert itemStack.getTag() != null;
            if (itemStack.getTag().contains("LifeFragHeal")) {
                float heal = itemStack.getTag().getFloat("LifeFragHeal");
                player.heal(heal);
            }
        }

    }


    @SubscribeEvent
    public static void onServerLevelLoad(LevelEvent.Load event){

        LevelAccessor levelAccessor=event.getLevel();

        if (!levelAccessor.isClientSide()){
            ServerLevelAccessor serverLevelAccessor= (ServerLevelAccessor) levelAccessor;
            ServerLevel serverLevel=serverLevelAccessor.getLevel();
            @NotNull LazyOptional<LevelCapability> capOptional=serverLevel.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap->{
                cap.loadNBTAfterLevelLoad();
                ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),serverLevel.dimension());
            });
        }

    }
    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event){
        Player player= event.getPlayer();
        Level level=player.level();
        if (level instanceof ServerLevel){
            @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            BlockPos blockPos=event.getPos();
            capOptional.ifPresent(cap->{
                if (cap.currentMoonstonePos.containsKey(blockPos)){
                    ItemEntity itemEntity =new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.MOONSTONE.get()));
                    level.addFreshEntity(itemEntity);
                    cap.currentMoonstonePos.remove(blockPos);
                }
                solarEvents.dropSunstone(level,event);
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectBlockBreak.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onBlockBreak(event, player, cnt);
                    }
                }
            });
        }
    }
    static ConcurrentHashMap<UUID,UUID[]> conversionDataStorage=new ConcurrentHashMap<>();//uuid of converting entity, uuid of other entity

    @SubscribeEvent
    public static void onLivingConversionPre(LivingConversionEvent.Pre event){
        LivingEntity entity= event.getEntity();
        if (!entity.level().isClientSide){
            @NotNull LazyOptional<LivingEntityCapability> capOptional=entity.getCapability(LivingEntityCapabilityProvider.playerCapability);
            capOptional.ifPresent(cap->{
                UUID[] data= (UUID[]) cap.getAbilityData(KnightmareSuit.honorDuel);
                if (data!=null){
                    conversionDataStorage.put(entity.getUUID(),data);
                }
            });
        }
    }


    @SubscribeEvent
    public static void onLivingConversionPost(LivingConversionEvent.Post event){
        LivingEntity entity= event.getEntity();
        if (!entity.level().isClientSide){
            ServerLevel level= (ServerLevel) entity.level();
            LivingEntity outcome=event.getOutcome();
            UUID  originalUUID=entity.getUUID();
            UUID outcomeUUID=outcome.getUUID();
            if (conversionDataStorage.containsKey(originalUUID)){
                UUID[] dataUUIDs=conversionDataStorage.get(originalUUID);
                @NotNull LazyOptional<LivingEntityCapability> outcomeCapOptional=outcome.getCapability(LivingEntityCapabilityProvider.playerCapability);
                outcomeCapOptional.ifPresent(cap->{
                    cap.insertIntoAbilityMap(KnightmareSuit.honorDuel,-10,dataUUIDs);
                });
                UUID otherUUID=dataUUIDs[0];//this will always be the other entity in the duel
                LivingEntity otherEntity= (LivingEntity) level.getEntity(otherUUID);
                @NotNull LazyOptional<LivingEntityCapability> otherCapOptional=otherEntity.getCapability(LivingEntityCapabilityProvider.playerCapability);
                otherCapOptional.ifPresent(cap->{
                    cap.insertIntoAbilityMap(KnightmareSuit.honorDuel,-10,new Object[]{outcomeUUID,dataUUIDs[1]});
                });
            }
        }

    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
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
                    if (cnt>0){
                        entry.getValue().onLivingHurtSelf(event,player,cnt);
                    }

                }
            }
            if(causingEntity instanceof Player){
                Player player=(Player) event.getSource().getEntity();
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingHurtOthers.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onLivingHurtOthers(event, player, cnt);
                    }
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
        if (event.isCanceled()) {
            System.out.println("LivingHurtEvent was canceled for: " + event.getEntity().getName().getString());
        }
    }


    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event){
        Entity directEntity=event.getSource().getDirectEntity();
        Entity causingEntity=event.getSource().getEntity();
        LivingEntity target=event.getEntity();
        if (!target.level().isClientSide && causingEntity!=null){
            @NotNull LazyOptional<LivingEntityCapability> targetCapOptional= target.getCapability(LivingEntityCapabilityProvider.playerCapability);
            @NotNull LazyOptional<LivingEntityCapability> attackerCapOptional= causingEntity.getCapability(LivingEntityCapabilityProvider.playerCapability);
            if (target instanceof Player){
                Player player=(Player) target;
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDamageSelf.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onLivingDamageSelf(event, player, cnt);
                    }
                }
                if (causingEntity instanceof Mob){
                    Mob mob= (Mob) causingEntity;
                    if (mob.hasEffect(MobEffectInit.MARK_OF_HAUNTING.get())){
                        if (!event.isCanceled()){
                            mob.removeEffect(MobEffectInit.MARK_OF_HAUNTING.get());
                        }
                    }
                }
                targetCapOptional.ifPresent(targetCap->{
                    Object[] targetData= targetCap.getAbilityData(KnightmareSuit.honorDuel);
                    if (targetData!=null){
                        UUID id1= (UUID) targetCap.getAbilityData(KnightmareSuit.honorDuel)[0];
                        if (!id1.equals(causingEntity.getUUID())){
                            event.setAmount(event.getAmount()*0.3f);
                        }
                    }
                });

            }

            if(causingEntity instanceof Player){
                Player player=(Player) causingEntity;
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDamageOthers.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onLivingDamageOthers(event, player,
                                cnt);
                    }
                }
                attackerCapOptional.ifPresent(attackerCap->{
                    Object[] attackerData= attackerCap.getAbilityData(KnightmareSuit.honorDuel);
                    if (attackerData!=null){
                        UUID id1= (UUID) attackerCap.getAbilityData(KnightmareSuit.honorDuel)[0];
                        if (!id1.equals(target.getUUID())){
                            event.setAmount(event.getAmount()*0.3f);
                        }
                    }
                });

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
    public static void onLivingDeath(LivingDeathEvent event){
        LivingEntity deadEntity=event.getEntity();
        if (!deadEntity.level().isClientSide){
            ServerLevel level= (ServerLevel) deadEntity.level();
            @NotNull LazyOptional<LivingEntityCapability> deadEntityCapOptional=deadEntity.getCapability(LivingEntityCapabilityProvider.playerCapability);
            deadEntityCapOptional.ifPresent(deadEntityCap->{
                Object[] honorDuelData=deadEntityCap.getAbilityData(KnightmareSuit.honorDuel);
                UUID deadID=deadEntity.getUUID();
                if (honorDuelData!=null){
                    System.out.println("LDEATH RELEASE");
                    UUID id1= (UUID) honorDuelData[0];
                    UUID id2= (UUID) honorDuelData[1];
                    if (id2.equals(deadID)){//source of duel died
                        if (level.getEntity(id1)!=null){
                            level.getEntity(id1).getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(cap->cap.removeFromAbilityMap(KnightmareSuit.honorDuel));
                        }
                    }
                    else{//target of duel died
                        Entity duelSource=level.getEntity(id2);
                        if (duelSource!=null){
                            duelSource.getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(cap->cap.removeFromAbilityMap(KnightmareSuit.honorDuel));
                        }
                    }
                }
            });
            if(event.getSource().getEntity() instanceof Player){
                Player player=(Player) event.getSource().getEntity();
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDeath.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0){
                        entry.getValue().onLivingDeath(event,player,cnt);
                    }

                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        if (event.getTo() == null){

        }
    }


    @SubscribeEvent
    public static void onTrade(TradeWithVillagerEvent event){
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
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
/*
        LivingEntity entity = (LivingEntity) event.getEntity();

        CompoundTag entityTag = entity.serializeNBT();
        if (!entityTag.contains("ActiveEffects")) {
            return;
        }

        ListTag effectsList = entityTag.getList("ActiveEffects", 10); // 10 = CompoundTag type
        // Clear current effects so we can re-add them
        //entity.removeAllEffects();

        for (int i = 0; i < effectsList.size(); i++) {
            CompoundTag effectTag = effectsList.getCompound(i);
            MobEffectInstance effectInstance = null;
            if (effectTag.contains("Stage")) {
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

 */
    }


    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event){
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (event.getLevel().isClientSide){
            return;
        }
        ServerLevel level= (ServerLevel) event.getLevel();
        LivingEntity entity = (LivingEntity) event.getEntity();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=entity.getCapability(LivingEntityCapabilityProvider.playerCapability);
        capOptional.ifPresent(cap->{
            if (cap.hasAbility(KnightmareSuit.honorDuel)){
                Object[] honorDuelData=cap.getAbilityData(KnightmareSuit.honorDuel);
                UUID id1= (UUID) honorDuelData[0];

                @NotNull LazyOptional<LivingEntityCapability> otherCapOptional=level.getEntity(id1).getCapability(LivingEntityCapabilityProvider.playerCapability);
                otherCapOptional.ifPresent(otherCap->{
                    if (otherCap.hasAbility(KnightmareSuit.honorDuel) && otherCap.getAbilityData(KnightmareSuit.honorDuel)[0]==entity.getUUID()){
                        System.out.println("RELEASING FROM HDUEL");
                        otherCap.removeFromAbilityMap(KnightmareSuit.honorDuel);
                    }
                });
                cap.removeFromAbilityMap(KnightmareSuit.honorDuel);
            }
        });
    }


    /*
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.phase== TickEvent.Phase.START){

        }
    }

     */

    @SubscribeEvent
    public static void onServerLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase== TickEvent.Phase.START && event.side.isServer()){
            ServerLevel level = (ServerLevel) event.level;
            level.isDay();
            DelayedFunctionManager.delayedFunctionManager.tickLevelMap(level);//this is called twice
            @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            int time= (int) (level.getDayTime()%24000L);
            capOptional.ifPresent(cap->{
                if (cap.divinerEyeFromState>-1){
                    try {
                        solarEvents.detectPlayers(level,cap);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    solarEvents.canCreateDivinerEye(event);
                }
            });
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
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (event.phase== TickEvent.Phase.START && event.side.isServer()){
            DelayedFunctionManager.delayedFunctionManager.tickPlayerMap(player);
            for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectTick.entrySet()) {
                int cnt=countPieces(player,entry.getKey());
                if (cnt>0) {
                    entry.getValue().onPlayerTick(event, player, cnt);
                }
            }
            @NotNull LazyOptional<LivingEntityCapability> capOptional=player.getCapability(LivingEntityCapabilityProvider.playerCapability);
            ServerLevel level= (ServerLevel) player.level();
            capOptional.ifPresent(cap->{
                cap.tickAbilityMap();
                //forced updates in case of emergencies
                if (player.tickCount%200==0){
                    if (cap.hasAbility(KnightmareSuit.honorDuel)){
                        UUID targetID= (UUID) cap.getAbilityData(KnightmareSuit.honorDuel)[0];
                        Entity target=level.getEntity(targetID);
                        if (target==null || target.distanceTo(player)> Config.honorDuelDist){
                            System.out.println("REMOVING HDUEL DUE TO DIST");
                            cap.removeFromAbilityMap(KnightmareSuit.honorDuel);
                            if (target!=null){
                                @NotNull LazyOptional<LivingEntityCapability> targetCapOptional=target.getCapability(LivingEntityCapabilityProvider.playerCapability);
                                targetCapOptional.ifPresent(targetCap->{
                                    if (targetCap.hasAbility(KnightmareSuit.honorDuel)){
                                        Object[] targetData=targetCap.getAbilityData(KnightmareSuit.honorDuel);
                                        if (player.getUUID().equals(targetData[0])){
                                            targetCap.removeFromAbilityMap(KnightmareSuit.honorDuel);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }


    }

    private static int countPieces(Player player, ArmorMaterial material) {
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
