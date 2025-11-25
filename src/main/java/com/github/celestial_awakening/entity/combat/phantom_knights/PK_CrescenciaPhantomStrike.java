package com.github.celestial_awakening.entity.combat.phantom_knights;

import com.github.celestial_awakening.capabilities.MovementModifier;
import com.github.celestial_awakening.capabilities.ProjCapability;
import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.entity.AlertInterface;
import com.github.celestial_awakening.entity.combat.GenericAbility;
import com.github.celestial_awakening.entity.living.AbstractCAMonster;
import com.github.celestial_awakening.entity.living.phantom_knights.AbstractPhantomKnight;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.entity.projectile.LunarCrescent;
import com.github.celestial_awakening.entity.projectile.MoonlightOrb;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.ProjCapS2CPacket;
import com.github.celestial_awakening.networking.packets.RefreshEntityDimsS2CPacket;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PK_CrescenciaPhantomStrike extends GenericAbility {

    public PK_CrescenciaPhantomStrike(AbstractCAMonster mob, int castTime, int CD, int executeTime, int recoveryTime) {
        super(mob, castTime, CD, executeTime, recoveryTime);

    }
    ArrayList<MoonlightOrb> orbs;
    DamageSourceIgnoreIFrames strikeSource=new DamageSourceIgnoreIFrames(this.mob.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),this.mob);
    @Override
    public void executeAbility(LivingEntity target) {
        //does not need to use states as this is a passively used ability
        RandomSource randomSource=this.mob.level().getRandom();
        float randAng=randomSource.nextInt(40,300);
        if (randomSource.nextBoolean()){
            randAng=-randAng;
        }
        MoonlightOrb orb=MoonlightOrb.create(this.mob.level(),0,300,7,(float)MathFuncs.angBtwnVec(this.mob.position(),target.position())+ randAng,0,0);
        int id=orb.getId();
        ServerLevel serverLevel= (ServerLevel) this.mob.level();
        orb.setOwner(this.mob);
        orb.setPos(this.mob.position());
        LazyOptional<ProjCapability> optional=orb.getProjCap();
        orbs.add(orb);
        optional.ifPresent(cap->{
            MovementModifier modifier=new MovementModifier(MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,-7f/4f,0,0,0,10);
            MovementModifier mod2=new MovementModifier(MovementModifier.modFunction.NUM, MovementModifier.modOperation.SET,
                    MovementModifier.modFunction.NUM, MovementModifier.modOperation.ADD,0,0,0,0,1);//just to ensure it stays 0
            cap.putInBackOfList(modifier);
            cap.putInBackOfList(mod2);
            ModNetwork.sendToClientsInDim(new ProjCapS2CPacket(id, cap),serverLevel.dimension());
        });
        orb.setAlertInterface(new AlertInterface() {
            @Override
            public void onAlert() {
                /*
                onds,this.mob,this.mob.position().x,this.mob.position().y,this.mob.position().z,aabb);

                 */
                ServerLevel level= (ServerLevel) orb.level();
                TargetingConditions conds=TargetingConditions.DEFAULT;
                conds.selector(CA_Predicates.opposingTeams_IgnoreProvidedClasses_Predicate((LivingEntity) orb.getOwner(), List.of(AbstractPhantomKnight.class)));
                //getNearestEntity(Class<? extends T> p_45964_, TargetingConditions p_45965_, @Nullable LivingEntity p_45966_, double p_45967_, double p_45968_, double p_45969_, AABB p_45970_) {
                //
                LivingEntity nearestEntity=level.getNearestEntity(LivingEntity.class,conds,(LivingEntity) orb.getOwner(), orb.getX(),orb.getY(),orb.getZ(),orb.getBoundingBox());
                nearestEntity.hurt(strikeSource,2f);
                if (orb.level().getDifficulty().getId()>2){
                    for (MoonlightOrb o:orbs) {
                        if (!o.isRemoved() && !o.equals(orb)){
                            //todo: check to see if this would access the orbs present at the time of creation of the orbs stored currently
                            Vec3 targetPos=nearestEntity.position();
                            Vec3 dir=targetPos.subtract(o.position()).normalize();
                            LunarCrescent crescent=LunarCrescent.create(o.level(),1.5f,80,9, MathFuncs.getAngFrom2DVec(dir),MathFuncs.getVertAngFromVec(dir),0);
                            crescent.setOwner(o.getOwner());
                            crescent.setPos(o.position());
                            serverLevel.addFreshEntity(crescent);
                            ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(crescent.getId()),serverLevel.dimension());
                        }
                    }
                }
            }

            @Override
            public void alertOthers() {
            }
        });
        serverLevel.addFreshEntity(orb);
        ModNetwork.sendToClientsInDim(new RefreshEntityDimsS2CPacket(id),serverLevel.dimension());
    }

    @Override
    protected double getAbilityRange(LivingEntity target) {
        return 0;
    }
}
