package lol.duckyyy.createnowplaying.network;

import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.lang.reflect.Method;

public class TrackSyncHandler {
    public static void handle(final TrackSyncPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockEntity entity = context.player().level().getBlockEntity(data.pos());
            if (entity instanceof AlbumJukeboxBlockEntity jukebox) {
                jukebox.setPlayingIndex(data.playingIndex(), data.trackIndex());
                jukebox.getLevel().sendBlockUpdated(data.pos(), jukebox.getBlockState(), jukebox.getBlockState(), 3);
                Createnowplaying.ElapsedMap.remove(jukebox.getBlockPos());
            }
        });
    }
}
