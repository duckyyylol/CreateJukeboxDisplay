package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import java.util.ArrayList;
import java.util.List;

public class JukeboxArtistDisplaySource extends SingleLineDisplaySource {
    private List<BlockPos> refreshedJukeboxes = new ArrayList<>();

    @Override
    public int getPassiveRefreshTicks() {
        return 20;
    }

    @Override
    public boolean shouldPassiveReset() {
        return true;
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity entity = context.level().getBlockEntity(context.getSourcePos());

        if (entity instanceof JukeboxBlockEntity jukebox) {

            // "Etched" Mod Compat
            if (Createnowplaying.etched) {
                if (jukebox.getTheItem().toString().contains("etched_music_disc")) {
                    ItemStack playingItem = jukebox.getTheItem();
                    String content = playingItem.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).get(1).getString();
                    String[] split = content.split(" - ");
                    String artist = split[0];

                    if (artist != null) return Component.empty().append(artist);
                }
            }

            if (jukebox.getSongPlayer().isPlaying() && jukebox.getSongPlayer().getSong() != null) {
                String content = jukebox.getSongPlayer().getSong().description().getString();
                String[] split = content.split(" - ");
                String title = split[0];
                if (title == null) return Component.translatable("text.createnowplaying.no_artist");

                return Component.empty().append(title);
            } else {
                return Component.translatable("text.createnowplaying.no_artist");
            }


        } else if (Createnowplaying.etched) {
            // More Etched Compat
            if (entity instanceof AlbumJukeboxBlockEntity en) {
                en.recalculatePlayingIndex(false);

                int index = en.getPlayingIndex();
//                Createnowplaying.log("Index: " + index);

                if (index < 0) return Component.translatable("text.createnowplaying.no_artist");

                ItemStack item = en.getItem(index);
                if (item == null || item.is(Items.AIR)) {
//                    Createnowplaying.log("Item not found at index " + index + " in album jukebox");
                    return Component.translatable("text.createnowplaying.no_artist");
                } else {
                    List<Component> lines = item.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
//                    Createnowplaying.log(lines.size() + " lines: " + lines.toString());
                    if (lines.isEmpty()) return Component.translatable("text.createnowplaying.no_artist");

                    String content = (!lines.getFirst().getSiblings().isEmpty() && lines.getFirst().getSiblings().getFirst().getContents().toString().contains("item.minecraft.")) ? lines.getFirst().getSiblings().getFirst().getContents().toString().split("='")[1].split("',")[0].trim() : lines.get(1).getString();

                    if (content.trim().isEmpty()) return Component.translatable("text.createnowplaying.no_artist");

                    if (content.startsWith("item.minecraft")) content = content + ".desc";

                    String componentContent = content.startsWith("item.minecraft") ? Component.translatable(content).getString() : content;

                    String[] split = componentContent.split(" - ");
                    String artist = split[0];

                    return Component.empty().append(artist);
                }
            }
        }


        return Component.empty();
    }


    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }

    @Override
    protected String getTranslationKey() {
        return "jukebox_artist";
    }
}
