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
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class JukeboxPlayingDisplaySource extends SingleLineDisplaySource {
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
                if(jukebox.getTheItem().toString().contains("etched_music_disc")) {
                    ItemStack playingItem = jukebox.getTheItem();
                    Component content = playingItem.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).get(1);

                    if(content != null) return Component.empty().append(content.getString());
                }
            }

            if(jukebox.getSongPlayer().isPlaying() && jukebox.getSongPlayer().getSong() != null) {
                return Component.empty().append(jukebox.getSongPlayer().getSong().description());
            } else {
                return Component.translatable("text.createnowplaying.not_playing");
            }
        } else if (Createnowplaying.etched) {
            // More Etched Compat
            if (entity instanceof AlbumJukeboxBlockEntity en) {
                en.recalculatePlayingIndex(false);

                int index = en.getPlayingIndex();
//                Createnowplaying.log("Index: " + index);

                if (index < 0) return Component.translatable("text.createnowplaying.not_playing");

                ItemStack item = en.getItem(index);
                if (item == null || item.is(Items.AIR)) {
//                    Createnowplaying.log("Item not found at index " + index + " in album jukebox");
                    return Component.translatable("text.createnowplaying.not_playing");
                } else {
                    List<Component> lines = item.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
//                    Createnowplaying.log(lines.size() + " lines: " + lines.toString());
                    if (lines.isEmpty()) return Component.translatable("text.createnowplaying.not_playing");

                    String content = (!lines.getFirst().getSiblings().isEmpty() && lines.getFirst().getSiblings().getFirst().getContents().toString().contains("item.minecraft.")) ? lines.getFirst().getSiblings().getFirst().getContents().toString().split("='")[1].split("',")[0].trim() : lines.get(1).getString();

                    if (content.trim().isEmpty()) return Component.translatable("text.createnowplaying.not_playing");

                    if (content.startsWith("item.minecraft")) content = content + ".desc";

                    String componentContent = content.startsWith("item.minecraft") ? Component.translatable(content).getString() : content;

                    return Component.empty().append(componentContent);
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
        return "jukebox_playing";
    }
}
