package com.github.celestial_awakening.items;

import com.github.celestial_awakening.capabilities.SunStaffCapability;
import com.github.celestial_awakening.capabilities.SunStaffCapabilityProvider;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.projectile.LightRay;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SunStaff extends CustomItem{
    protected int abilityNameColor=0xe7e82c;
    protected int abilityDescColor =0xe2e2e1;
    public SunStaff(Properties properties) {
        super(properties);
    }

    /*
    Abilities
Searing Flash
Left Click
Deal a burst of fire damage to all entities in a 5 block radius
CD of 12 seconds
Shining Ray
Right Click
Cast a ray of energy that explodes on impact, dealing damage and healing allies.
CD of 6 seconds

maybe have the base staff have only the 2 abilities above, and an upgraded version have 3 abilities
Solar Wind
Hold Shift + Hold Right Click for at least 1 second
While holding shift and right click, create an expanding zone that deals fire damage to all entities in it.
The zone can last for up to 10 seconds, and the area can have a max radius of 7 blocks.
CD of 4-10 seconds, depending on how long the storm lasted.

     */
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component flashName=Component.translatable("tooltip.celestial_awakening.sun_staff.flash_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(flashName);
        Component flashButton=Component.translatable("tooltip.celestial_awakening.sun_staff.flash_control").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(flashButton);
        components.add(Component.translatable("tooltip.celestial_awakening.sun_staff.flash_desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        Component rayName=Component.translatable("tooltip.celestial_awakening.sun_staff.ray_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(rayName);
        Component rayButton=Component.translatable("tooltip.celestial_awakening.sun_staff.ray_control").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(abilityNameColor)));
        components.add(rayButton);
        components.add(Component.translatable("tooltip.celestial_awakening.sun_staff.ray_desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(abilityDescColor))));
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        AtomicInteger abilityCastType= new AtomicInteger();
        if (!level.isClientSide) {
            @NotNull LazyOptional<SunStaffCapability> capOptional=itemStack.getCapability(SunStaffCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                if (player.isCrouching()){
                    if (cap.getWindCD()==0){
                        abilityCastType.set(2);
                    }
                }
                else if (cap.getRayCD()==0){
                    abilityCastType.set(1);
                    Vec3 dir=player.getLookAngle();
                    float hAng= (float) ((Math.toDegrees (Math.atan2(dir.z,dir.x))));
                    float vAng=MathFuncs.getVertAngFromVec(dir);
                    shiningRay((ServerLevel) level,player,hAng,vAng);
                    cap.setRayCD(220);
                }
            });
        }
        switch (abilityCastType.get()){
            case 1:{
                return InteractionResultHolder.consume(itemStack);
            }
            case 2:{
                player.startUsingItem(interactionHand);
                return InteractionResultHolder.consume(itemStack);
            }
            default:{
                return InteractionResultHolder.fail(itemStack);//failed to use either ability
            }
        }
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity holder, int vanillaIndex, boolean isSelectedIndex) {
        if (!level.isClientSide()){
            @NotNull LazyOptional<SunStaffCapability> capOptional=itemStack.getCapability(SunStaffCapabilityProvider.cap);
            capOptional.ifPresent(cap->{
                cap.decrementCD();
            });
        }
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
    }
    @Override
    public boolean onEntitySwing(ItemStack itemStack, LivingEntity entity)
    {
        super.onEntitySwing(itemStack,entity);
        if (!entity.level().isClientSide){

            ServerLevel serverLevel= (ServerLevel) entity.level();
            @NotNull LazyOptional<SunStaffCapability> capOptional=itemStack.getCapability(SunStaffCapabilityProvider.cap);
            capOptional.ifPresent(cap->{

                if (cap.getFlashCD()==0){

                    DamageSourceIgnoreIFrames source=new DamageSourceIgnoreIFrames(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FIREBALL),entity);
                    AABB aabb=new AABB(entity.position(),entity.position());
                    aabb=aabb.inflate(5f,1.2f,5f);
                    List<LivingEntity> entityList= serverLevel.getEntitiesOfClass(LivingEntity.class,aabb, CA_Predicates.opposingTeamsPredicate(entity));
                    for (LivingEntity livingEntity:entityList) {
                        livingEntity.hurt(source,2.5f);
                        livingEntity.setSecondsOnFire(5);
                    }
                    cap.setFlashCD(110);
                }
            });
        }
        return false;
    }
//TODO: trigger event at some point
    void shiningRay(ServerLevel serverLevel,LivingEntity owner, float hAng,float vAng){
        LightRay ray=LightRay.create(serverLevel,20,4.5f);
        ray.initDims(0.4f,0,0.4f,0,0.4f,12f,0,12/4f);
        ray.setOwner(owner);
        ray.setHAng(90-hAng);
        ray.setVAng(90-vAng);
        Vec3 offset=new Vec3(Math.cos(Math.toRadians(hAng)),1,Math.sin(Math.toRadians(hAng)));
        ray.setPos(offset.add(owner.position()));
        ray.setAlertInterface(new AlertInterface() {
            @Override
            public void onAlert() {
                Vec3 endPt=ray.getEndPt();
                AABB aabb=ray.getRayBox();
                aabb=aabb.inflate(2);
                ServerLevel level= (ServerLevel) ray.level();
                List<LivingEntity> entitiesToHurt=level.getEntitiesOfClass(LivingEntity.class,aabb,CA_Predicates.opposingTeams_IgnorePlayersAndAllies_Predicate((LivingEntity) ray.getOwner()));
                List<LivingEntity> entitiesToHeal=level.getEntitiesOfClass(LivingEntity.class,aabb,CA_Predicates.sameTeamAndAlliesPredicate((LivingEntity) ray.getOwner()));
                for (LivingEntity livingEntity:entitiesToHurt){
                    ray.hitLivingEntity(livingEntity);
                }
                for (LivingEntity livingEntity:entitiesToHeal){
                    livingEntity.heal(1.5f);
                }
                ray.discard();
            }

            @Override
            public void alertOthers() {

            }
        });
        serverLevel.addFreshEntity(ray);
    }

    void solarWind(){

    }
}
