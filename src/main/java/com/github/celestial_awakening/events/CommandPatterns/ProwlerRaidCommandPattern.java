package com.github.celestial_awakening.events.CommandPatterns;

import com.github.celestial_awakening.events.GenericCommandPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;

public class ProwlerRaidCommandPattern extends GenericCommandPattern {
    public ProwlerRaidCommandPattern(Object[] params,int delay) {
        super(params,delay);
    }

    @Override
    protected boolean execute() {
        /*
        ChunkPos chunkPos=chunkAccess.getPos();
        ArrayList<BlockPos> applicableBlocks=new ArrayList<>();
        for (int cx=-1;cx<=1;cx++){
            for (int cz=-1;cz<=1;cz++){
                LevelChunk chunk=level.getChunk(chunkPos.x+cx,chunkPos.z+cz);
                for (int x=0;x<16;x++){
                    for (int z=0;z<16;z++){
                        //for future changes
                        //instead of the 16 by 16
                        //check each 4 by 4 square in a chunk
                        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                        if (Math.abs(y-targetCenter.getY())<=15){
                            BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y,chunk.getPos().z*16+z);
                            applicableBlocks.add(blockPos);
                        }
                    }
                }
            }
        }
        return false;

         */
        return false;
    }
}
