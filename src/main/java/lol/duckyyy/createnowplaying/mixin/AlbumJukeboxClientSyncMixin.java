package lol.duckyyy.createnowplaying.mixin;

import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import gg.moonflower.etched.common.network.play.SetAlbumJukeboxTrackPacket;
import lol.duckyyy.createnowplaying.Createnowplaying;
import lol.duckyyy.createnowplaying.network.TrackSyncPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AlbumJukeboxBlockEntity.class, remap = false)
public abstract class AlbumJukeboxClientSyncMixin {

    @Shadow public abstract int getPlayingIndex();
    @Shadow public abstract int getTrack();

    @Inject(method = "next", at = @At("TAIL"))
    private void createnowplaying$syncNextToServer(CallbackInfo ci) {
        this.createnowplaying$sendSyncPacket();
    }

    @Inject(method = "previous", at = @At("TAIL"))
    private void createnowplaying$syncPrevToServer(CallbackInfo ci) {
        this.createnowplaying$sendSyncPacket();
    }

    @Inject(method = "recalculatePlayingIndex", at = @At("TAIL"))
    private void createnowplaying$syncRecalcToServer(boolean reverse, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            this.createnowplaying$sendSyncPacket();
        }
    }

    @Unique
    private void createnowplaying$sendSyncPacket() {
        BlockEntity entity = (BlockEntity) (Object) this;
        Level level = entity.getLevel();

        if (level != null && level.isClientSide()) {
            Createnowplaying.log("Track change detected...");
            PacketDistributor.sendToServer(new TrackSyncPayload(entity.getBlockPos(), this.getPlayingIndex(), this.getTrack()));
        }
    }
}
