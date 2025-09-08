package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.entity.living.phantom_knights.AbstractPhantomKnight;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.util.LevelFuncs;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.celestial_awakening.util.ResourceCheckerFuncs.validDim;


public class LunarEvents {
    static Random rand=new Random();
    //.then(Commands.literal("day").executes((p_288689_) -> {
    //         return queryTime(p_288689_.getSource(), (int)(p_288689_.getSource().getLevel().getDayTime() / 24000L % 2147483647L));
    public void attemptPKSpawn(ServerLevel level){

        if (validDim(level, Config.transcendentsDimensionTypes)){
            LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap->{
                int time=(int)(level.getDayTime() % 24000L);//ranges from 0-24k
                if (MathFuncs.isInRange(time,18000,0)){//for now, offset will be 0
                    switch (level.getMoonPhase()) {
                        //half
                        case 3, 7 -> {
                            break;
                        }
//gibb
                        case 2, 8 -> {
                            break;
                        }
//crescent
                        case 4, 6 -> {
                            if (level.getDayTime() / 24000L % 2147483647L > Config.pkCrescenciaMinDay && cap.pkRemainingSpawnAttempts>0) {//(p_288689_.getSource().getLevel().getDayTime() / 24000L % 2147483647L) query for get day command
                                BlockPos pos =canSpawnPK(level);
                                if (pos!=null) {//in the future, perform rng roll?
                                    PhantomKnight_Crescencia crescencia = new PhantomKnight_Crescencia(EntityInit.PK_CRESCENCIA.get(), level);
                                    crescencia.setPos(pos.getCenter());
                                    level.addFreshEntity(crescencia);
                                    cap.pkRemainingSpawnAttempts--;
                                }
                            }
                            break;
                        }
                        case 5 -> {//new moon
                            break;
                        }
                    }
                }
            });


        }
    }
    private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel p_47025_, ChunkAccess p_47026_, BlockPos.MutableBlockPos p_47027_, double p_47028_) {
        if (p_47028_ <= 576.0D) {
            return false;
        } else if (p_47025_.getSharedSpawnPos().closerToCenterThan(new Vec3((double)p_47027_.getX() + 0.5D, (double)p_47027_.getY(), (double)p_47027_.getZ() + 0.5D), 24.0D)) {
            return false;
        } else {
            return Objects.equals(new ChunkPos(p_47027_), p_47026_.getPos()) || p_47025_.isNaturalSpawningAllowed(p_47027_);
        }
    }

    //MobCategory.MONSTER
    public BlockPos canSpawnPK(ServerLevel serverLevel){//shared checks for spawning phantom knights
        List<ServerPlayer> serverPlayers=new ArrayList<>(serverLevel.players());
        Collections.shuffle(serverPlayers);
        AbstractPhantomKnight pk=null;
        EntityType type=null;
        BlockPos posToSpawn=null;
        switch(serverLevel.getMoonPhase()){
            case 3, 7 -> {
                break;
            }
//gibb
            case 2, 8 -> {
                break;
            }
//crescent
            case 4, 6 -> {
                type=EntityInit.PK_CRESCENCIA.get();
                break;
            }
            case 5 -> {//new moon
                break;
            }
            default -> {
                type=EntityInit.PK_CRESCENCIA.get();
                break;
            }
        }
        if (type!=null){
            for (ServerPlayer player:serverPlayers) {
                for (int i=0;i<10;i++){//10 attempts to spawn per player
                    int x=rand.nextInt(24,49);
                    int z=rand.nextInt(24,49);
                    BlockPos.MutableBlockPos mutableBlockPos=new BlockPos.MutableBlockPos(x,0,z);
                    mutableBlockPos.move(player.blockPosition().getX(),player.blockPosition().getY(),player.blockPosition().getZ());
                    ChunkAccess chunkAccess =serverLevel.getChunk(mutableBlockPos);;
                    MobSpawnSettings.SpawnerData mobspawnsettings$spawnerdata = null;
                    int y = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1;
                    mutableBlockPos.setY(y);
                    double d2=player.distanceToSqr(mutableBlockPos.getX(),mutableBlockPos.getY(),mutableBlockPos.getZ());
                    if (isRightDistanceToPlayerAndSpawnPoint(serverLevel, chunkAccess, mutableBlockPos, d2)) {
                        if(SpawnPlacements.Type.ON_GROUND.canSpawnAt(serverLevel,mutableBlockPos,type)){
                            if(SpawnPlacements.checkSpawnRules(type, serverLevel, MobSpawnType.NATURAL, mutableBlockPos, serverLevel.random)){
                                if (serverLevel.noCollision(type.getAABB(mutableBlockPos.getX() + 0.5D,mutableBlockPos.getY(),mutableBlockPos.getZ() + 0.5D))){
                                    posToSpawn=mutableBlockPos.immutable();
                                    return posToSpawn;
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
    /*
    add players to this when they are added to level?
     */
    static ConcurrentHashMap<UUID, Short> timeSpentLookingAtMoon=new ConcurrentHashMap<>();
    public void detectIfLookingAtMoon(Level level){
        for (Player player:level.players()){
            UUID uuid=player.getUUID();
            short val=1;
            if (LevelFuncs.detectIfLookingAtCelestialBody(level,player,-1)){
                if (timeSpentLookingAtMoon.containsKey(uuid)){
                    val= (short) Math.max(3000,timeSpentLookingAtMoon.get(uuid)+1);
                }
                timeSpentLookingAtMoon.put(uuid,val);
                //looking at moon
            }
            else{
                if (timeSpentLookingAtMoon.containsKey(uuid)){
                    val= (short) Math.max(3000,timeSpentLookingAtMoon.get(uuid)-1);
                    timeSpentLookingAtMoon.put(uuid,val);
                }
            }
        }
    }

    public void moonstoneMark(Level level){
        if (level.getDayTime() % 20==0){
            @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
            capOptional.ifPresent(cap-> {
                if (!cap.currentMoonstonePos.isEmpty()) {
                    MobEffectInstance instance=new MobEffectInstance(MobEffectInit.MARK_OF_HAUNTING.get(),2400);
                    for (BlockPos blockPos : cap.currentMoonstonePos.keySet()) {
                        AABB bound = new AABB(blockPos);
                        TargetingConditions conds = TargetingConditions.forNonCombat();
                        Monster monster = level.getNearestEntity(Monster.class, conds, null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), bound);
                        if (monster!=null){
                            monster.addEffect(instance);
                        }
                    }
                }
            });
        }

    }

    public void createMoonstone(Level level){
        Random rand = new Random();
        @NotNull LazyOptional<LevelCapability> capOptional=level.getCapability(LevelCapabilityProvider.LevelCap);
        capOptional.ifPresent(cap-> {
            if (validDim(level, Config.lunarMatDimensionTypes) && level.isNight()){

                int time=(int)(level.getDayTime() % 24000L);//ranges from 0-24k
                //time% % 24000L will give the actual daytime
            /*
            night is 12000-24000
            spawn at 15000,18000,21000
             */
                if ((time%12000)%3000==0 && time!=24000){//valid time
                    //for each player, attempt to create a moonstone in a nearby chunk
                    List<? extends Player> pList=level.players();
                    for (Player p:pList) {
                        if (Config.moonstoneDimLim>-1 && cap.currentMoonstonePos.keySet().size()>Config.moonstoneDimLim){
                            break;
                        }
                        ChunkPos chunkPos=p.chunkPosition();
                        ArrayList<BlockPos> applicableBlocks=new ArrayList<BlockPos>();
                        for (int cx=-1;cx<=1;cx++){
                            for (int cz=-1;cz<=1;cz++){
                                if (cx!=0 && cz!=0){
                                    LevelChunk chunk=level.getChunk(chunkPos.x+cx,chunkPos.z+cz);
                                    for (int x=0;x<16;x++){
                                        for (int z=0;z<16;z++){
                                            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                                            BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y-1,chunk.getPos().z*16+z);

                                            //check to see if block is exposed to surface and is one of X types
                                            BlockState blockState=level.getBlockState(blockPos);
                                            if (!cap.currentMoonstonePos.containsKey(blockPos)){
                                                if ((blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(BlockTags.SAND))){
                                                    applicableBlocks.add(blockPos);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //pick a random applicable block
                        if (applicableBlocks.size()>0){
                            BlockPos chosenSpot=applicableBlocks.get(rand.nextInt(applicableBlocks.size()));//err, bound must be pos
                            System.out.println("PLACING MOONSTONE at " + chosenSpot);
                            cap.currentMoonstonePos.put(chosenSpot, (short) 1800);
                        }

                    }
                }

            }
        });
    }

    public void midnightIronTransformation(Level level){
        if (!level.isClientSide()&& validDim(level,Config.lunarMatDimensionTypes)){
            WorldBorder border=level.getWorldBorder();
            AABB worldBox = new AABB(
                    border.getMinX(), level.getMinBuildHeight(),border.getMinZ(),
                    border.getMaxX(), level.getMaxBuildHeight(), border.getMaxZ());
            for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class,worldBox)) {
                if (itemEntity.getItem().getItem() == Items.IRON_INGOT) {
                    itemEntity.setItem(new ItemStack(ItemInit.MIDNIGHT_IRON_INGOT.get(), itemEntity.getItem().getCount()));
                }
            }

        }
    }

}
