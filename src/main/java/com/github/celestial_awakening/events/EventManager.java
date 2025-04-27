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
import com.github.celestial_awakening.util.CA_Predicates;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.server.command.ConfigCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static com.github.celestial_awakening.nbt_strings.NBTStrings.*;
@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class EventManager {
    protected static final RandomSource randomSource=RandomSource.create();

    public static LunarEvents lunarEvents=new LunarEvents();
    public static SolarEvents solarEvents=new SolarEvents();

    private static final Map.Entry<ArmorMaterial,ArmorEffect> lunarArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.MOONSTONE,new LunarArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> radiantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.SUNSTONE,new RadiantArmor());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> remnantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.DYING_LIGHT_ESSENCE,new RemnantArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> umbraArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.ONYX_FUR,new UmbraArmor());



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
            .put(umbraArmor).build();

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
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        new DivinerDataCommand(event.getDispatcher(),2);
        ConfigCommand.register(event.getDispatcher());
    }


    //TODO: spawn multiple prowlers in here, base it off the cel beacon spawning
    @SubscribeEvent
    public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event){
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()){//Saves any necessary capability data
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(
                    oldStore->event.getEntity().getCapability(LivingEntityCapabilityProvider.playerCapability).ifPresent(newStore->newStore.copyForRespawn(oldStore)));
            event.getOriginal().invalidateCaps();
        }

    }

    @SubscribeEvent
    public static void onServerUnload(LevelEvent.Unload event) {
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
                float spd=7.25f;
                int lifeVal=15;
                int orbs=cap.getLunarOrbs();
                int cd=event.getCD();
                cd-=orbs*5;
                //create(Level level, float damage, int lifeVal,float spd,float hAng,float vAng,float zR,float width,float height,float rs)
                LunarCrescent crescent=null;
                if (isCrit){
                    if (cap.getStrikeCD()<=0){//performs a powerful short ranged strike
                        crescent=LunarCrescent.create(serverLevel,event.getDmg(),lifeVal,spd,hAng,0,90,1.75f,0.35f,1.75f);

                        cap.changeStrikeCD(cd);
                    }
                }
                else{
                    if (cap.getWaveCD()<=0){//performs a sweeping 180-arc wave
                        crescent=LunarCrescent.create(serverLevel,event.getDmg(),lifeVal,spd,hAng,0,0,1.75f,0.35f,1.75f);
                        cap.changeWaveCD(cd);
                    }
                }

                if (crescent!=null){
                    crescent.setPos(event.getSpawnpoint());
                    crescent.setYRot(owner.getYRot());
                    crescent.setOwner(event.getOwner());
                    crescent.itemStackSource=itemStack;

                    if (orbs==6){
                        int sideLife= (int) (lifeVal*0.8f);
                        if (isCrit){
                            for(int i=-1;i<=1;i+=2){
                                LunarCrescent sideCresent=LunarCrescent.create(serverLevel,event.getDmg()*0.4f,sideLife,spd*0.8f,hAng + i*15,0,90,1.25f,0.25f,1.25f);
                                sideCresent.setPos(event.getSpawnpoint());
                                sideCresent.setYRot(owner.getYRot());
                                sideCresent.setOwner(event.getOwner());
                                sideCresent.itemStackSource=itemStack;
                                serverLevel.addFreshEntity(sideCresent);
                                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(sideCresent.getId()),sideCresent.level().dimension());
                            }
                        }
                        else{
                            for(int i=-1;i<=1;i+=2){
                                LunarCrescent sideCresent=LunarCrescent.create(serverLevel,event.getDmg()*0.4f,sideLife,spd*0.8f,hAng + i*32,0,0,1.25f,0.25f,1.25f);
                                sideCresent.setPos(event.getSpawnpoint());
                                sideCresent.setYRot(owner.getYRot());
                                sideCresent.setOwner(event.getOwner());
                                sideCresent.itemStackSource=itemStack;
                                serverLevel.addFreshEntity(sideCresent);
                                ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(sideCresent.getId()),sideCresent.level().dimension());
                            }
                        }
                    }
                    serverLevel.addFreshEntity(crescent);
                    ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),crescent.level().dimension());
                }

            });

        }
    }
    //TODO: replace with loot table later
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event){
        int storedLuck= ObfuscationReflectionHelper.getPrivateValue(FishingHook.class,event.getHookEntity(),"f_37096_");
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
            if (cnt>0){
                ((LunarArmor)lunarArmor.getValue()).onFishEvent(event,cnt);
            }
        }

    }
    @SubscribeEvent
    public static void onTrade(TradeWithVillagerEvent event){
        Player player =event.getEntity();
        if (!player.level().isClientSide){
            int cnt=0;
            for (ItemStack armorStack : player.getInventory().armor) {
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem)) {
                    ArmorItem armorItem= (ArmorItem) armorStack.getItem();
                    if (armorItem.getMaterial()==CustomArmorMaterial.MOONSTONE){
                        cnt++;
                    }
                }
            }
            if (cnt==4){
                ServerLevel serverLevel= (ServerLevel) player.level();
                ((LunarArmor)lunarArmor.getValue()).onTrade(serverLevel, event,player);
            }


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
            if (itemStack.getTag().contains(lifeFrag)) {
                float heal = itemStack.getTag().getFloat(lifeFrag);
                player.heal(heal);
            }
        }

    }

    @SubscribeEvent
    public static void onEntityCreation(EntityJoinLevelEvent event){
        Entity entity=event.getEntity();
        if (!event.getLevel().isClientSide){
            ServerLevel serverLevel= (ServerLevel) event.getLevel();
            if (entity instanceof FishingHook){
                FishingHook hook= (FishingHook) entity;
                if (hook.getPlayerOwner()!=null){
                    int cnt=0;
                    Player player=hook.getPlayerOwner();
                    for (ItemStack armorStack : player.getInventory().armor) {
                        if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem)) {
                            ArmorItem armorItem= (ArmorItem) armorStack.getItem();
                            if (armorItem.getMaterial()==CustomArmorMaterial.MOONSTONE){
                                cnt++;
                            }
                        }
                    }
                    if (cnt==4){
                        ((LunarArmor)lunarArmor.getValue()).onFishHookCreationEvent(serverLevel,hook);
                    }
                }

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
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event){
        Player player=event.getEntity();
        Level level=player.level();
        @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
        capOptional.ifPresent(cap->{
            cap.loadNBTAfterLevelLoad();
            ModNetwork.sendToClient(new LevelCapS2CPacket(cap), (ServerPlayer) player);
        });
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

    static ConcurrentHashMap<UUID,UUID[]> conversionDataStorage=new ConcurrentHashMap<>();//Keeps track of uuids for converting entities

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
                if (arrow.getTags().contains(fBowBoost) && arrow.getOwner() instanceof LivingEntity){
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
    public static void onLivingDamage(LivingDamageEvent event){
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
                            targetCapOptional.ifPresent(cap->{
                                cap.changeInsanityVal((short) 10);
                            });
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
            if (cap.hasAbility(KnightmareSuit.honorDuel)){//Needs to remove honor duel
                Object[] honorDuelData=cap.getAbilityData(KnightmareSuit.honorDuel);
                UUID id1= (UUID) honorDuelData[0];

                @NotNull LazyOptional<LivingEntityCapability> otherCapOptional=level.getEntity(id1).getCapability(LivingEntityCapabilityProvider.playerCapability);
                otherCapOptional.ifPresent(otherCap->{
                    if (otherCap.hasAbility(KnightmareSuit.honorDuel) && otherCap.getAbilityData(KnightmareSuit.honorDuel)[0]==entity.getUUID()){
                        otherCap.removeFromAbilityMap(KnightmareSuit.honorDuel);
                    }
                });
                cap.removeFromAbilityMap(KnightmareSuit.honorDuel);
            }
        });
    }

    @SubscribeEvent
    public static void onCropGrowEventPre(BlockEvent.CropGrowEvent.Pre event){
        LevelAccessor levelAccessor=event.getLevel();
        if (levelAccessor instanceof Level level){
            @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap->{
                int sunControl=cap.divinerSunControlVal;
                if (sunControl<0){
                    if (randomSource.nextInt(sunControl+1)<sunControl){
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase== TickEvent.Phase.START){
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase== TickEvent.Phase.START){
            //server side stuff only
            if (event.side.isServer()){

                ServerLevel serverLevel = (ServerLevel) event.level;
                @NotNull LazyOptional<LevelCapability> capOptional=serverLevel.getCapability(LevelCapabilityProvider.LevelCap);
                int time= (int) (serverLevel.getDayTime()%24000L);
                DelayedFunctionManager.delayedFunctionManager.tickLevelMap(serverLevel);//this is called twice
                if (time==0){

                }
                capOptional.ifPresent(cap->{
                    if (cap.divinerEyeFromState>-1 && cap.divinerEyeToState>-1){
                        solarEvents.detectPlayers(serverLevel,cap);

                    }
                    else{
                        solarEvents.canCreateDivinerEye(event);
                    }
                    if (time==12000){
                        cap.pkRemainingSpawnAttempts=Config.pkSpawnCap;
                    }
                    if (cap.decrementSunControlTimer()){
                        ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),serverLevel.dimension());
                    }

                });
                if (time>12000){
                    int phase=serverLevel.getMoonPhase();
                    lunarEvents.createMoonstone(serverLevel);
                    switch(phase){
                        case 0:{

                            break;
                        }
                        default:{
                            break;
                        }
                    }
                    lunarEvents.detectIfLookingAtMoon(serverLevel);
                    if (time==18000){
                        lunarEvents.midnightIronTransformation(serverLevel);
                        lunarEvents.attemptPKSpawn(serverLevel);
                    }

                }
                particleManager.generateParticles(serverLevel);
            }
            //both sides
            LazyOptional<LevelCapability> capOptional=event.level.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap->{
                if (cap.divinerEyeToState>-2 && cap.divinerEyeFrameProgress<100f){
                    cap.divinerEyeFrameProgress+=100f/32f;
                    if (cap.divinerEyeFrameProgress>100f){
                        cap.divinerEyeFrameProgress=100f;
                    }
                }
            });
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
                //forced updates every 15 sec in case of emergencies
                if (player.tickCount%300==0){
                    if (cap.hasAbility(KnightmareSuit.honorDuel)){
                        UUID targetID= (UUID) cap.getAbilityData(KnightmareSuit.honorDuel)[0];
                        Entity target=level.getEntity(targetID);
                        if (target==null || target.distanceTo(player)> Config.honorDuelDist){
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
