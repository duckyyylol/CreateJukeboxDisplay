package lol.duckyyy.createnowplaying.network;

import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TrackSyncPayload(BlockPos pos, int playingIndex, int trackIndex) implements CustomPacketPayload {
    public static final Type<TrackSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Createnowplaying.MODID, "track_sync"));
    public static final StreamCodec<FriendlyByteBuf, TrackSyncPayload> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, TrackSyncPayload::pos, ByteBufCodecs.VAR_INT, TrackSyncPayload::playingIndex, ByteBufCodecs.VAR_INT, TrackSyncPayload::trackIndex, TrackSyncPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
