package mekanism.common.tile.interfaces;

import mekanism.api.Chunk3D;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITileWrapper {

    default BlockPos getBlockPos() {
        return ((BlockEntity) this).getBlockPos();
    }

    default Level getLevel() {
        return ((BlockEntity) this).getLevel();
    }

    default GlobalPos getTileGlobalPos() {
        return GlobalPos.of(getLevel().dimension(), getBlockPos());
    }

    default Chunk3D getTileChunk() {
        return new Chunk3D(getTileGlobalPos());
    }
}
