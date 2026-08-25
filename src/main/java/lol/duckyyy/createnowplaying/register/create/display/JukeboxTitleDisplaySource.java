package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

import java.util.List;

public class JukeboxTitleDisplaySource extends SingleLineDisplaySource {
    @Override
    public int getPassiveRefreshTicks() {
        return 20;
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity entity = context.level().getBlockEntity(context.getSourcePos());

        if(entity instanceof JukeboxBlockEntity jukebox) {
            // "Etched" Mod Compat
            if(Createnowplaying.etched) {
                Createnowplaying.log("Etched is loaded");
                if(jukebox.getTheItem().toString().contains("etched_music_disc")) {
                    ItemStack playingItem = jukebox.getTheItem();
                    String content = playingItem.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).get(1).getString();
                    String[] split = content.split(" - ");
                    String title = split[1];

                    if(title != null) return Component.empty().append(title);
                }
            }

            if(jukebox.getSongPlayer().isPlaying() && jukebox.getSongPlayer().getSong() != null) {
                String content = jukebox.getSongPlayer().getSong().description().getString();
                String[] split = content.split(" - ");
                String title = split[1];
                if(title == null) return Component.translatable("text.createjukeboxdisplay.no_title");

                return Component.empty().append(title);
            } else {
                return Component.translatable("text.createjukeboxdisplay.no_title");
            }
        } else if (Createnowplaying.etched) {
            // More Etched Compat
            if (entity instanceof AlbumJukeboxBlockEntity en) {
                en.recalculatePlayingIndex(false);

                int index = en.getPlayingIndex();
//                Createnowplaying.log("Index: " + index);

                if (index < 0) return Component.translatable("text.createjukeboxdisplay.no_title");

                ItemStack item = en.getItem(index);
                if (item == null || item.is(Items.AIR)) {
//                    Createnowplaying.log("Item not found at index " + index + " in album jukebox");
                    return Component.translatable("text.createjukeboxdisplay.no_title");
                } else {
                    List<Component> lines = item.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
                    if (lines.isEmpty()) return Component.translatable("text.createjukeboxdisplay.no_title");

                    String content = (!lines.getFirst().getSiblings().isEmpty() && lines.getFirst().getSiblings().getFirst().getContents().toString().contains("item.minecraft.")) ? lines.getFirst().getSiblings().getFirst().getContents().toString().split("='")[1].split("',")[0].trim() : lines.get(1).getString();

                    if (content.trim().isEmpty()) return Component.translatable("text.createjukeboxdisplay.no_title");

                    if (content.startsWith("item.minecraft")) content = content + ".desc";

                    String componentContent = content.startsWith("item.minecraft") ? Component.translatable(content).getString() : content;

                    String[] split = componentContent.split(" - ");
                    if(split.length < 2) return Component.translatable("text.createjukeboxdisplay.no_title");
                    String title = split[1].trim();

                    return Component.empty().append(title);
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
        return "jukebox_title";
    }
}
