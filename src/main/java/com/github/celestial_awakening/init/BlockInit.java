package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.blocks.Astralter;
import com.github.celestial_awakening.blocks.entity.AstralterBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CelestialAwakening.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CelestialAwakening.MODID);

    //public static final RegistryObject<Block> PROTO_STAR = BLOCKS.register("proto_star", ()->new Block(BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel((p_50755_) -> 32)));
    public static final RegistryObject<Block> ASTRALTER_BLOCK=BLOCKS.register("astralter_block",()->new Astralter(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5.5f).sound(SoundType.AMETHYST)));
    public static final RegistryObject<BlockEntityType<AstralterBlockEntity>> ASTRALTER_ENTITY = BLOCK_ENTITY_TYPES.register("astralter",()-> BlockEntityType.Builder.of(AstralterBlockEntity::new, ASTRALTER_BLOCK.get()).build(null));
}
