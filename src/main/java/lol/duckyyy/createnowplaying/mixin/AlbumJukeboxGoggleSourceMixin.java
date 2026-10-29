package lol.duckyyy.createnowplaying.mixin;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.CNPConfig;
import lol.duckyyy.createnowplaying.Createnowplaying;
import lol.duckyyy.createnowplaying.register.create.display.JukeboxPlaybackShortElapsedDisplaySource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Mixin(AlbumJukeboxBlockEntity.class)


public class AlbumJukeboxGoggleSourceMixin implements IHaveGoggleInformation {
    private String extractStringContent(ItemStack discRelatedItem) {
        List<Component> lines = discRelatedItem.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
        if (lines.isEmpty()) return "";

        String content = (!lines.getFirst().getSiblings().isEmpty() && lines.getFirst().getSiblings().getFirst().getContents().toString().contains("item.minecraft.")) ? lines.getFirst().getSiblings().getFirst().getContents().toString().split("='")[1].split("',")[0].trim() : lines.get(1).getString();

        if (content.trim().isEmpty()) return "";

        if (content.startsWith("item.minecraft")) content = content + ".desc";

        return content.startsWith("item.minecraft") ? Component.translatable(content).getString() : content;
    }

    @Override
    public ItemStack getIcon(boolean isPlayerSneaking) {
        AlbumJukeboxBlockEntity jukebox = (AlbumJukeboxBlockEntity) (Object) this;
        jukebox.recalculatePlayingIndex(false);
        int index = jukebox.getPlayingIndex();
        if (index < 0) return ItemStack.EMPTY;
        ItemStack disc = jukebox.getItem(jukebox.getPlayingIndex());

        if (disc == null || disc.isEmpty()) return ItemStack.EMPTY;

        return disc;
    }

    @Unique
    private String cjd$withSpace(String input) {
        return String.format("    %s", input);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        AlbumJukeboxBlockEntity en = (AlbumJukeboxBlockEntity) (Object) this;
        if (!Createnowplaying.ElapsedMap.containsKey(en.getBlockPos()))
            Createnowplaying.ElapsedMap.put(en.getBlockPos(), 0L);
        en.recalculatePlayingIndex(false);

        int index = en.getPlayingIndex();
        int nextIndex = index + 1;
        if (en.getItem(nextIndex).isEmpty()) nextIndex = 0;
//                Createnowplaying.log("Index: " + index);

        if (index < 0) return false;

        ItemStack nowPlayingItem = en.getItem(index);
        if (nowPlayingItem == null || nowPlayingItem.is(Items.AIR)) {
//                    Createnowplaying.log("Item not found at index " + index + " in album jukebox");
            return false;
        }

        String nowPlayingComponentContent = extractStringContent(nowPlayingItem);

        if (Createnowplaying.CONFIG.goggleDisplayStyle.get() == CNPConfig.GoggleDisplayStyle.INFORMATIVE) {
            ItemStack nextSongItem = en.getItem(nextIndex);

            String nextSongComponentContent = extractStringContent(nextSongItem);

            if (nowPlayingComponentContent.isEmpty()) return false;

            long elapsedSeconds = TimeUnit.SECONDS.toSeconds(Createnowplaying.ElapsedMap.getOrDefault(en.getBlockPos(), 0L));
            long elapsedMinutes = TimeUnit.SECONDS.toMinutes(Createnowplaying.ElapsedMap.getOrDefault(en.getBlockPos(), 0L));

            elapsedSeconds = elapsedSeconds - TimeUnit.MINUTES.toSeconds(elapsedMinutes);

            MutableComponent elapsedFormatted = JukeboxPlaybackShortElapsedDisplaySource.formatTime(elapsedMinutes, elapsedSeconds);

            tooltip.add(Component.literal(cjd$withSpace("Now Playing")).withStyle(ChatFormatting.GRAY).append(""));
            tooltip.add(Component.literal(nowPlayingComponentContent).withStyle(ChatFormatting.YELLOW));
            if (Createnowplaying.CONFIG.showTimeElapsed.get())
                tooltip.add(elapsedFormatted.withStyle(ChatFormatting.GRAY));
            if (Createnowplaying.CONFIG.albumJukeboxShowNext.get())
                tooltip.add(Component.literal("Next: ").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC).append(Component.literal(String.format("%s", nextSongComponentContent)).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC)));
        } else if (Createnowplaying.CONFIG.goggleDisplayStyle.get() == CNPConfig.GoggleDisplayStyle.COMPACT) {
            tooltip.add(Component.literal(cjd$withSpace(nowPlayingComponentContent)).withStyle(ChatFormatting.YELLOW));
        } else return false;

        return true;
    }
}
