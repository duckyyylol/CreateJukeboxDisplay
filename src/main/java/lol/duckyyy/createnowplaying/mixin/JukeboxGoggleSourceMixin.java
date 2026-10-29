package lol.duckyyy.createnowplaying.mixin;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import lol.duckyyy.createnowplaying.CNPConfig;
import lol.duckyyy.createnowplaying.Createnowplaying;
import lol.duckyyy.createnowplaying.register.create.display.JukeboxPlaybackShortElapsedDisplaySource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxGoggleSourceMixin extends BlockEntity implements IHaveGoggleInformation {
    protected JukeboxGoggleSourceMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private boolean cjd$hasDisc() {
        return this.getBlockState().getValue(JukeboxBlock.HAS_RECORD);
    }

    @Override
    public ItemStack getIcon(boolean isPlayerSneaking) {
        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;
        ItemStack disc = jukebox.getTheItem();

        if(disc == null || disc.isEmpty() || !cjd$hasDisc()) return ItemStack.EMPTY;

        return disc;
    }

    @Unique
    private String cjd$withSpace(String input) {
        return String.format("    %s", input);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!Createnowplaying.ElapsedMap.containsKey(this.getBlockPos())) Createnowplaying.ElapsedMap.put(this.getBlockPos(), 0L);
        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;
        JukeboxSong song = jukebox.getSongPlayer().getSong();

        if(song == null || jukebox.getTheItem().isEmpty() || !cjd$hasDisc()) {
            if(!Createnowplaying.DisplayLinkElapsedBoundBlocks.contains(jukebox.getBlockPos())) Createnowplaying.ElapsedMap.remove(this.getBlockPos());
            return false;
        }

        String content = song.description().getString();

        if(Createnowplaying.CONFIG.goggleDisplayStyle.get() == CNPConfig.GoggleDisplayStyle.INFORMATIVE) {
            long elapsedSeconds = TimeUnit.SECONDS.toSeconds(Createnowplaying.ElapsedMap.getOrDefault(jukebox.getBlockPos(), 0L));
            long elapsedMinutes = TimeUnit.SECONDS.toMinutes(Createnowplaying.ElapsedMap.getOrDefault(jukebox.getBlockPos(), 0L));

            elapsedSeconds = elapsedSeconds - TimeUnit.MINUTES.toSeconds(elapsedMinutes);

            MutableComponent elapsedFormatted = JukeboxPlaybackShortElapsedDisplaySource.formatTime(elapsedMinutes, elapsedSeconds);


            tooltip.add(Component.literal(cjd$withSpace("Now Playing")).withStyle(ChatFormatting.GRAY).append(""));
            tooltip.add(Component.literal(content).withStyle(ChatFormatting.YELLOW));
            if(Createnowplaying.CONFIG.showTimeElapsed.get()) tooltip.add(elapsedFormatted.withStyle(ChatFormatting.GRAY));
        } else if(Createnowplaying.CONFIG.goggleDisplayStyle.get() == CNPConfig.GoggleDisplayStyle.COMPACT) {
            tooltip.add(Component.literal(cjd$withSpace(content)).withStyle(ChatFormatting.YELLOW));
        } else return false;

        return true;
    }
}
