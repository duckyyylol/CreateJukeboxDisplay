package lol.duckyyy.createnowplaying.network;

import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TrackSyncHandler {
    public static void handle(final TrackSyncPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockEntity entity = context.player().level().getBlockEntity(data.pos());
            if (entity instanceof AlbumJukeboxBlockEntity jukebox) {
                jukebox.setPlayingIndex(data.playingIndex(), data.trackIndex());
                jukebox.getLevel().sendBlockUpdated(data.pos(), jukebox.getBlockState(), jukebox.getBlockState(), 3);
                Createnowplaying.ElapsedMap.remove(jukebox.getBlockPos());
                Createnowplaying.DisplayLinkElapsedBoundBlocks.remove(jukebox.getBlockPos());
            } else if (entity instanceof JukeboxBlockEntity jukebox) {
                jukebox.getLevel().sendBlockUpdated(data.pos(), jukebox.getBlockState(), jukebox.getBlockState(), 3);
            }
        });
    }
}
