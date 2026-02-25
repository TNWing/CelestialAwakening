package com.github.celestial_awakening.enchantments;

import com.github.celestial_awakening.damage.DamageSourceIgnoreIFrames;
import com.github.celestial_awakening.util.CA_Predicates;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeEnchantment;

import java.util.Iterator;
import java.util.List;

public class DevastateEnchantment extends Enchantment implements IForgeEnchantment {
    public static String name="Devastate";
    public DevastateEnchantment(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot... p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }
    static ParticleOptions particleOption =
            new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.DIRT.defaultBlockState());
    public static void trigger(LivingEntity causingEntity, ServerLevel serverLevel,Vec3 spt,int modifier,float ang){
        float dist=modifier*1.5f;
        for (int reps=0;reps<3;reps++){
            for (float a=-90+ang;a<=90+ang;a+=30/(reps+1)){
                Vec3 pos=spt.add(MathFuncs.get2DVecFromAngle(a).scale(1+reps*0.75f));
                serverLevel.sendParticles(particleOption, pos.x(),pos.y()+0.7f,pos.z, 3, 0.3f, 0.1f, 0.3f,0.1f);
            }
        }

        AABB generalArea=new AABB(spt,spt).inflate(dist,1,dist);
        List<LivingEntity> entityList=causingEntity.level().getEntitiesOfClass(LivingEntity.class,generalArea, CA_Predicates.opposingTeamsPredicate(causingEntity));
        Iterator<LivingEntity> iter=entityList.iterator();
        while (iter.hasNext()){
            LivingEntity livingEntity=iter.next();
            double angToEntity=MathFuncs.getAngFrom2DVec( MathFuncs.getDirVec(spt,livingEntity.position()));
            double angDiff=Math.abs(ang-angToEntity);
            if (angDiff>90){
                iter.remove();
            }
        }
        DamageSourceIgnoreIFrames wave=new DamageSourceIgnoreIFrames(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FLY_INTO_WALL),causingEntity);


        for (LivingEntity livingEntity:entityList) {
            System.out.println("HURTING entity    " + livingEntity.getName() + "   " +   livingEntity.getUUID());
            livingEntity.hurt(wave,2.5f);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,50,0));
        }
    }
}
