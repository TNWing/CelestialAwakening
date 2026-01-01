package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.*;
import com.github.celestial_awakening.commands.DivinerDataCommand;
import com.github.celestial_awakening.commands.ProwlerRaidCommand;
import com.github.celestial_awakening.damage.DamageSourceNoIFrames;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.events.armor_events.*;
import com.github.celestial_awakening.events.custom_events.DivinerEyeSoundEvent;
import com.github.celestial_awakening.events.custom_events.MoonScytheAttackEvent;
import com.github.celestial_awakening.events.custom_events.TranscendentSpawnEvent;
import com.github.celestial_awakening.events.raids.ProwlerRaid;
import com.github.celestial_awakening.init.*;
import com.github.celestial_awakening.items.CustomArmorItem;
import com.github.celestial_awakening.items.CustomArmorMaterial;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.networking.packets.PlayerCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsManager;
import com.github.celestial_awakening.util.CA_Predicates;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.command.ConfigCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.celestial_awakening.nbt_strings.NBTStrings.*;
@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID)
public class EventManager {
    public static Supplier<Block>[] gaiaPlateBlocks = new Supplier[]{
            () -> Blocks.DEEPSLATE,
            BlockInit.SCORCHED_STONE,
            () -> Blocks.IRON_BLOCK,
            BlockInit.SCORCHED_STONE,
            () -> Blocks.DEEPSLATE
    };
    protected static final RandomSource randomSource=RandomSource.create();

    public static LunarEvents lunarEvents=new LunarEvents();
    public static SolarEvents solarEvents=new SolarEvents();

    private static final Map.Entry<ArmorMaterial,ArmorEffect> lunarArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.LUNAR,new LunarArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> radiantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.RADIANT,new RadiantArmor());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> remnantArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.REMNANT,new RemnantArmor());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> umbraArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.UMBRA,new UmbraArmor());



    private static final Map.Entry<ArmorMaterial,ArmorEffect> shadeArmor=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.SHADE,new ShadeRobes());
    private static final Map.Entry<ArmorMaterial,ArmorEffect> stellarRobes=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.STELLAR,new StellarRobes());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> everlightArmor =new AbstractMap.SimpleEntry<>(CustomArmorMaterial.EVERLIGHT,new EverlightArmor());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> knightmareSuit=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.KNIGHTMARE,new KnightmareSuit());

    private static final Map.Entry<ArmorMaterial,ArmorEffect> scorchedSuit=new AbstractMap.SimpleEntry<>(CustomArmorMaterial.SCORCHED,new ScorchedSuit());


    private static final Map<ArmorMaterial,ArmorEffect> armorMaterials =  (new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor)
            .put(radiantArmor)
            .put(remnantArmor)
            .put(shadeArmor)
            .put(stellarRobes)
            .put(umbraArmor)
            .put(everlightArmor)
            .put(knightmareSuit)
            .put(scorchedSuit)
            .build();


    private static final Map<ArmorMaterial,ArmorEffect> armorEffectTick=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor).put(everlightArmor).put(radiantArmor).put(scorchedSuit)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectEquipmentChange=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(lunarArmor).put(shadeArmor).put(scorchedSuit)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectBlockBreak=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(radiantArmor).put(lunarArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingHurtOthers=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(umbraArmor).put(shadeArmor).put(remnantArmor).put(knightmareSuit)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingHurtSelf=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(umbraArmor).build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDamageOthers=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>()).put(scorchedSuit)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDamageSelf=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(shadeArmor).put(remnantArmor).put(lunarArmor).put(everlightArmor)
            .build();

    private static final Map<ArmorMaterial,ArmorEffect> armorEffectLivingDeath=(new ImmutableMap.Builder<ArmorMaterial, ArmorEffect>())
            .put(remnantArmor).put(umbraArmor).put(knightmareSuit).put(everlightArmor)
            .build();

    private static final ParticleManager particleManager=ParticleManager.createParticleManager();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ProwlerRaid.initProwlerRaidData();
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        new DivinerDataCommand(event.getDispatcher(),2);
        new ProwlerRaidCommand(event.getDispatcher(),2);
        ConfigCommand.register(event.getDispatcher());
    }

    //TODO: spawn multiple prowlers in here, base it off the cel beacon spawning
    @SubscribeEvent
    public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event){
    }

    @SubscribeEvent
    public static void onItemDespawn(ItemExpireEvent event){
        ItemEntity itemEntity= event.getEntity();
        FoodProperties foodProperties=itemEntity.getItem().getFoodProperties(null);
        if (foodProperties!=null && foodProperties.isMeat()){
            ItemStack newStack=new ItemStack(ItemInit.LIFE_FRAG.get());
            newStack.setCount(itemEntity.getItem().getCount());
            itemEntity.setItem(newStack);

            event.setExtraLife(1200);
            event.setCanceled(true);

        }
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()){//Saves any necessary capability data
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(LivingEntityCapabilityProvider.capability).ifPresent(
                    oldStore->event.getEntity().getCapability(LivingEntityCapabilityProvider.capability).ifPresent(newStore->newStore.copyForRespawn(oldStore)));
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
                LunarCrescent crescent=null;
                if (isCrit){
                    if (cap.getStrikeCD()<=0){//performs a powerful short ranged strike
                        crescent=LunarCrescent.create(serverLevel,(float)event.getDmg(),lifeVal,spd,hAng,0,90,1.75f,0.35f,1.75f);

                        cap.changeStrikeCD(cd);
                    }
                }
                else{
                    if (cap.getWaveCD()<=0){//performs a sweeping 180-arc wave
                        crescent=LunarCrescent.create(serverLevel,(float)event.getDmg(),lifeVal,spd,hAng,0,0,1.75f,0.35f,1.75f);
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
                                LunarCrescent sideCresent=LunarCrescent.create(serverLevel,(float)event.getDmg()*0.4f,sideLife,spd*0.8f,hAng + i*15,0,90,1.25f,0.25f,1.25f);
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
                                LunarCrescent sideCresent=LunarCrescent.create(serverLevel,(float)event.getDmg()*0.4f,sideLife,spd*0.8f,hAng + i*32,0,0,1.25f,0.25f,1.25f);
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
        int cnt=0;
        Player player=event.getEntity();
        if (player!=null){

            for (ItemStack armorStack : player.getInventory().armor) {
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem armorItem)) {
                    if (armorItem.getMaterial()==CustomArmorMaterial.LUNAR){
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
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem armorItem)) {
                    if (armorItem.getMaterial()==CustomArmorMaterial.LUNAR){
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
        if (event.getEntity() instanceof Player player){
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
            if (itemStack.getItem() instanceof CustomArmorItem armorItem){
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
            if (entity instanceof FishingHook hook){
                if (hook.getPlayerOwner()!=null){
                    int cnt=0;
                    Player player=hook.getPlayerOwner();
                    for (ItemStack armorStack : player.getInventory().armor) {
                        if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem armorItem)) {
                            if (armorItem.getMaterial()==CustomArmorMaterial.LUNAR){
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
            @NotNull LazyOptional<LivingEntityCapability> capOptional=entity.getCapability(LivingEntityCapabilityProvider.capability);
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
                @NotNull LazyOptional<LivingEntityCapability> outcomeCapOptional=outcome.getCapability(LivingEntityCapabilityProvider.capability);
                outcomeCapOptional.ifPresent(cap->{
                    cap.insertIntoAbilityMap(KnightmareSuit.honorDuel,-10,dataUUIDs);
                });
                UUID otherUUID=dataUUIDs[0];//this will always be the other entity in the duel
                LivingEntity otherEntity= (LivingEntity) level.getEntity(otherUUID);
                @NotNull LazyOptional<LivingEntityCapability> otherCapOptional=otherEntity.getCapability(LivingEntityCapabilityProvider.capability);
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
                if (player.getItemInHand(InteractionHand.MAIN_HAND).is(Tags.Items.TOOLS_SHIELDS)){
                    ItemStack stack=player.getMainHandItem();
                    if (!player.getCooldowns().isOnCooldown(stack.getItem())){
                        int lvl=stack.getEnchantmentLevel(EnchantmentInit.GAIA_LINK.get());
                        if( player.getUseItem()!=stack && lvl>0){//GAIA LINK
                            amt*=1f-0.05f*lvl;
                        }
                    }
                    else{//REMNANTS
                        int lvl=stack.getEnchantmentLevel(EnchantmentInit.GAIA_REMNANTS.get());
                        if (lvl>0){
                            amt*=1-0.03f*lvl;
                        }
                    }
                }
                else if( player.getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_SHIELDS)){
                    ItemStack stack=player.getOffhandItem();
                    if (!player.getCooldowns().isOnCooldown(stack.getItem())){
                        int lvl=stack.getEnchantmentLevel(EnchantmentInit.GAIA_LINK.get());
                        if( player.getUseItem()!=stack && lvl>0){//GAIA LINK
                            amt*=1f-0.05f*lvl;
                        }
                    }
                    else{
                        int lvl=stack.getEnchantmentLevel(EnchantmentInit.GAIA_REMNANTS.get());
                        if (lvl>0){
                            amt*=1-0.03f*lvl;
                        }
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
                Entity owner=arrow.getOwner();
                assert arrow != null;
                Set<String> tags=arrow.getTags();
                if (tags.contains(sgBowBoost)){
                    if (target.hasEffect(MobEffects.GLOWING)){
                        event.setAmount(event.getAmount()+1.5f);

                    }
                    if (target.isOnFire()){
                        if (owner instanceof LivingEntity livingEntity){
                            livingEntity.heal(0.5f);
                        }
                    }
                }
                if (tags.contains(sgBowRay)){
                    LightRay ray=LightRay.create(event.getEntity().level(), 10,4,true,false);
                    ray.setOwner(owner);
                }
                if (tags.contains(fBowBoost) && owner instanceof LivingEntity){
                    Vec3 offset=new Vec3(3,0.5f,3);
                    AABB aabb=new AABB(target.position().subtract(offset),target.position().add(offset));
                    List<LivingEntity> livingEntityList= level.getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate((LivingEntity)arrow.getOwner()));
                    for (LivingEntity livingEntity:livingEntityList) {
                        livingEntity.hurt(level.damageSources().mobAttack((LivingEntity) arrow.getOwner()), amt*0.25f);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0));
                    }
                }

            }
            event.setAmount(amt);
        }
    }


    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event){
        Entity causingEntity=event.getSource().getEntity();
        LivingEntity target=event.getEntity();
        if (!target.level().isClientSide && causingEntity!=null){
            @NotNull LazyOptional<LivingEntityCapability> targetCapOptional= target.getCapability(LivingEntityCapabilityProvider.capability);
            @NotNull LazyOptional<LivingEntityCapability> attackerCapOptional= causingEntity.getCapability(LivingEntityCapabilityProvider.capability);
            LazyOptional<PlayerCapability> playerCapOptional=target.getCapability(PlayerCapabilityProvider.capability);
            if (target instanceof Player player){
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDamageSelf.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0) {
                        entry.getValue().onLivingDamageSelf(event, player, cnt);
                    }
                }
                if (causingEntity instanceof Mob mob){
                    if (mob.hasEffect(MobEffectInit.MARK_OF_HAUNTING.get())){
                        if (!event.isCanceled()){
                            mob.removeEffect(MobEffectInit.MARK_OF_HAUNTING.get());
                            playerCapOptional.ifPresent(cap->{
                                cap.changeInsanityVal((short) Config.insMoH);
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

            if(causingEntity instanceof Player player){
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
                        else{
                            event.setAmount(event.getAmount()*1.15f);
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
            @NotNull LazyOptional<LivingEntityCapability> deadEntityCapOptional=deadEntity.getCapability(LivingEntityCapabilityProvider.capability);
            deadEntityCapOptional.ifPresent(deadEntityCap->{
                Object[] honorDuelData=deadEntityCap.getAbilityData(KnightmareSuit.honorDuel);
                UUID deadID=deadEntity.getUUID();
                if (honorDuelData!=null){
                    UUID id1= (UUID) honorDuelData[0];
                    UUID id2= (UUID) honorDuelData[1];
                    if (id2.equals(deadID)){//source of duel died
                        if (level.getEntity(id1)!=null){
                            level.getEntity(id1).getCapability(LivingEntityCapabilityProvider.capability).ifPresent(cap->cap.removeFromAbilityMap(KnightmareSuit.honorDuel));
                        }
                    }
                    else{//target of duel died
                        Entity duelSource=level.getEntity(id2);
                        if (duelSource!=null){
                            duelSource.getCapability(LivingEntityCapabilityProvider.capability).ifPresent(cap->cap.removeFromAbilityMap(KnightmareSuit.honorDuel));
                        }
                    }
                }
            });
            if(event.getSource().getEntity() instanceof Player player){
                for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectLivingDeath.entrySet()) {
                    int cnt=countPieces(player,entry.getKey());
                    if (cnt>0){
                        entry.getValue().onLivingDeath(event,player,cnt);
                    }

                }
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHeal(LivingHealEvent event){
        LivingEntity entity=event.getEntity();
        if (entity.hasEffect(MobEffectInit.CAUTERIZE.get())){
            int amp=entity.getEffect(MobEffectInit.CAUTERIZE.get()).getAmplifier();
            event.setAmount(event.getAmount()*Math.max(0,(0.9f-0.09f*(1+amp))));
        }
        if (entity.hasEffect(MobEffectInit.MOON_CURSE.get())){
            LazyOptional<LivingEntityCapability> optional=entity.getCapability(LivingEntityCapabilityProvider.capability);
            optional.ifPresent(cap->{
                float amt= Mth.clamp(event.getAmount(),0,entity.getMaxHealth()-entity.getHealth());
                cap.changeMoonCurseVal(amt);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        /*
        if (event.getTo() == null){

        }

         */
    }



    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event){
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (event.getLevel().isClientSide){
            InsManager.INSTANCE.removeFromEntityMap(entity);
            return;
        }

        ServerLevel level= (ServerLevel) event.getLevel();
        @NotNull LazyOptional<LivingEntityCapability> capOptional=entity.getCapability(LivingEntityCapabilityProvider.capability);
        capOptional.ifPresent(cap->{
            if (cap.hasAbility(KnightmareSuit.honorDuel)){//Needs to remove honor duel
                Object[] honorDuelData=cap.getAbilityData(KnightmareSuit.honorDuel);
                UUID id1= (UUID) honorDuelData[0];

                LazyOptional<LivingEntityCapability> otherCapOptional=level.getEntity(id1).getCapability(LivingEntityCapabilityProvider.capability);
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
                    if (randomSource.nextInt(1-sunControl)<10){
                        System.out.println("deny crop growth");
                        event.setResult(Event.Result.DENY);
                    }
                }
            });
        }
    }


    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase== TickEvent.Phase.START){
            if (event.side.isServer()){

                ServerLevel serverLevel = (ServerLevel) event.level;
                @NotNull LazyOptional<LevelCapability> capOptional=serverLevel.getCapability(LevelCapabilityProvider.LevelCap);
                int time= (int) (serverLevel.getDayTime()%24000L);
                DelayedFunctionManager.delayedFunctionManager.tickLevelMap(serverLevel);
                capOptional.ifPresent(cap->{
                    if (Config.wipEnabled){
                        cap.raids.tick();
                        if (event.level.dimensionTypeId()== BuiltinDimensionTypes.OVERWORLD && time%2000==0){//TODO: possibly change it to be more accurate to time spent in deepslate layer
                            cap.increaseDeepLayerCounter(5*serverLevel.getPlayers(new Predicate<ServerPlayer>() {
                                @Override
                                public boolean test(ServerPlayer serverPlayer) {
                                    return serverPlayer.getY()<=0;
                                }
                            }).size());
                        }
                    }


                    if (cap.divinerEyeFromState>-1 && cap.divinerEyeToState>-1){
                        /*
                        if (time % 100==0){
                            System.out.println("cap dim is " + serverLevel.dimensionTypeId());
                        }

                         */
                        solarEvents.detectTargets(serverLevel,cap);

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
                    if (time % 100==0 && time<12000 && cap.divinerSunControlVal>0){
                        List<ServerPlayer> players=serverLevel.players();
                        for (ServerPlayer player:players) {
                            if (!player.isInWaterRainOrBubble()){
                                float temp=serverLevel.getBiome(player.blockPosition()).get().getBaseTemperature();
                                float tempMod=Math.max((temp-0.25f)*0.4f,-0.1f);
                                int localLight=Math.max(serverLevel.getBrightness(LightLayer.BLOCK,player.blockPosition()),5)-5;
                                player.causeFoodExhaustion(0.25f + localLight*0.075f + tempMod);
                                //
                            }
                        }
                    }

                });
                lunarEvents.detectIfLookingAtMoon(serverLevel,time>13000);
                if (time>13000){
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

                    if (time%100==0){
                        //lunarEvents.moonSanity(serverLevel);
                        if (time==18000){
                            lunarEvents.midnightIronTransformation(serverLevel);
                            lunarEvents.attemptPKSpawn(serverLevel);
                        }
                        else if (time==15500 && Config.wipEnabled){
                            lunarEvents.attemptProwlerRaid(serverLevel);
                        }
                    }


                }
                particleManager.generateParticles(serverLevel);
                lunarEvents.moonstoneMark(serverLevel);
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
        else{
            Level level=event.level;
            if (level.dimensionTypeId()== BuiltinDimensionTypes.OVERWORLD){
                //overworld is still darken 0
                //System.out.println("Level " +  level.dimensionTypeId() + "   sky darken end " + level.getSkyDarken()  + "  side is " + level.isClientSide());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        LivingEntity entity=event.getEntity();
        if (entity.tickCount%100==0){
            LazyOptional<LivingEntityCapability> optional=entity.getCapability(LivingEntityCapabilityProvider.capability);
            optional.ifPresent(cap->{
                cap.changeNaviGauge((short) -1);
            });
        }

    }

    public static void onRenderLiving(RenderLivingEvent.Pre event){
        Entity camera=Minecraft.getInstance().cameraEntity;
        LivingEntity livingEntity=event.getEntity();
        Player player=Minecraft.getInstance().player;
        LazyOptional<PlayerCapability> optional=player.getCapability(PlayerCapabilityProvider.capability);
        optional.ifPresent(cap->{
            if (cap.getInsanityPts()<12000){
                //event.getRenderer().getModel()
                //event.setCanceled(true);
                //event.getRenderer().getModel()

            }
        });
        //event.getRenderer().
    }

/*
    public static List<Tier> getTiersLowerThan(Tier tier)
    {
        if (!isTierSorted(tier)) return List.of();
        return sortedTiers.stream().takeWhile(t -> t != tier).toList();
    }
 */
    //Gain bonus mining speed when mining blocks whose required breaking level is lower than your tool
    @SubscribeEvent
    public static void playerBreakSpeed(PlayerEvent.BreakSpeed event){
        Player player=event.getEntity();

        int scorchCnt=countPieces(player,CustomArmorMaterial.SCORCHED);
        if (scorchCnt>0){
            Item playerItem=player.getMainHandItem().getItem();
            if (playerItem instanceof TieredItem tieredItem){
                Tier tier=tieredItem.getTier();
                if (TierSortingRegistry.isCorrectTierForDrops(tier,event.getState())){
                    Tier lowerTier=TierSortingRegistry.getTiersLowerThan(tier).isEmpty() ? null : TierSortingRegistry.getTiersLowerThan(tier).get(0);
                    if (lowerTier!=null && TierSortingRegistry.isCorrectTierForDrops(lowerTier,event.getState())){
                        float newSpd=event.getNewSpeed()*1.4f;
                        event.setNewSpeed(newSpd);
                    }
                }
            }
            if (scorchCnt==4 && 0>player.getY()){
                double y=player.getY();
                double diff=0f-y;
                float boost= (float)( (diff)*1.7f/(diff+20d));
                event.setNewSpeed(event.getNewSpeed()+boost);
            }
        }


    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player=event.player;
        if (event.phase== TickEvent.Phase.START && event.side.isServer() && player instanceof ServerPlayer serverPlayer){
            DelayedFunctionManager.delayedFunctionManager.tickPlayerMap(player);
            for (Map.Entry<ArmorMaterial,ArmorEffect> entry:armorEffectTick.entrySet()) {
                int cnt=countPieces(player,entry.getKey());
                if (cnt>0) {
                    entry.getValue().onPlayerTick(event, player, cnt);
                }
            }
            @NotNull LazyOptional<LivingEntityCapability> livingEntityOptional=player.getCapability(LivingEntityCapabilityProvider.capability);
            LazyOptional<PlayerCapability> playerOptional=player.getCapability(PlayerCapabilityProvider.capability);
            ServerLevel level= (ServerLevel) player.level();
            playerOptional.ifPresent(cap->{
                if (player.tickCount%200==0){
                    //maybe cap contains the ins sound cd so sound cd is dependent on the kind of sound played
                    /*
                    could also group sounds into the following and have separate CDs for each
                    -biome-specific
                    -generic mob sounds
                    -block sounds
                     */

                    if (Config.insSound && cap.getInsanityPts()<16000 &&  player.tickCount%2800==0){
                        BlockPos pos=player.blockPosition();
                        Holder<Biome> biomeHolder=level.getBiome(pos);

                        if (level.random.nextInt(5)==0){
                            /*
                            Another method would be to group things like so
                            -mob sounds
                            -block sounds

                            in each category, a sound can be generic, biome-boosted, or biome-specific
                            Ex:
                            generic: footsteps, creeper
                            biome-boosted:endermen (in the end)
                            biome-specific: sculk shrieker

                            use a weight system
                            biome-specific have high weights if in their biome, otherwise they have 0 weight
                            biome-boosted have slightly below generic weights but are boosted if inside their biome/dim
                             */
                            if (biomeHolder== Biomes.DEEP_DARK && level.random.nextInt(6)<4){
                                player.playSound(SoundEvents.SCULK_SHRIEKER_SHRIEK);
                            }
                            else{
                                switch(level.random.nextInt(3)){
                                    case 0:{
                                        player.playSound(SoundEvents.ENDERMAN_STARE);
                                        break;
                                    }
                                    case 1:{
                                        player.playSound(SoundEvents.PARROT_IMITATE_CREEPER);
                                        break;
                                    }
                                    case 2:{
                                        BlockState blockState=level.getBlockState(pos);
                                        //Block block=blockState.getBlock();
                                        SoundType soundtype = blockState.getSoundType(level, player.blockPosition(), player);
                                        player.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
                                        break;
                                    }
                                }
                            }


                        }
                    }
                    cap.changeInsanityVal((short) Config.insRec);
                    //System.out.println("Player " + player.getName() + " has san " + cap.getInsanityPts());
                    if (player.tickCount%400==0){
                        ModNetwork.sendToClient(new PlayerCapS2CPacket(cap),serverPlayer);
                    }

                }
            });
            livingEntityOptional.ifPresent(cap->{
                cap.tickAbilityMap();
                //forced updates every 15 sec in case of emergencies
                if (player.tickCount%300==0){
                    if (cap.hasAbility(KnightmareSuit.honorDuel)){
                        UUID targetID= (UUID) cap.getAbilityData(KnightmareSuit.honorDuel)[0];
                        Entity target=level.getEntity(targetID);
                        if (target==null || target.distanceTo(player)> Config.honorDuelDist){
                            cap.removeFromAbilityMap(KnightmareSuit.honorDuel);
                            if (target!=null){
                                @NotNull LazyOptional<LivingEntityCapability> targetCapOptional=target.getCapability(LivingEntityCapabilityProvider.capability);
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
                if(!armorStack.isEmpty() && (armorStack.getItem() instanceof ArmorItem armorItem)) {
                    if (armorItem.getMaterial()==material){
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    @SubscribeEvent
    public static void divinerSoundEvent(DivinerEyeSoundEvent event){
        if (event.getLevel().dimension() == Minecraft.getInstance().level.dimension()){
            if (event.isOpen()){
                Player player=Minecraft.getInstance().player;
                Minecraft.getInstance().level.playLocalSound(player.getX()  ,player.getY(),player.getZ(), SoundInit.TRANSCENDENT_DIV_OPEN.get(), SoundSource.HOSTILE,0.8f,1,false);
            }

        }

    }

    @SubscribeEvent
    public static void transcendentSpawnEvent(TranscendentSpawnEvent event){
        if (event.getLevel().dimension() == Minecraft.getInstance().level.dimension()){
                Vec3 pos=event.getPos();
                Minecraft.getInstance().level.playLocalSound(BlockPos.containing(pos), SoundInit.TRANSCENDENT_SPAWN_1.get(), SoundSource.HOSTILE,0.8f,1,false);
        }
    }
}
