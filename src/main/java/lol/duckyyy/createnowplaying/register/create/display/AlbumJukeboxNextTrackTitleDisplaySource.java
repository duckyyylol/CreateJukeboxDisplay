package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import gg.moonflower.etched.core.registry.EtchedItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class AlbumJukeboxNextTrackTitleDisplaySource extends SingleLineDisplaySource {
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

        if(entity instanceof AlbumJukeboxBlockEntity jukebox) {
            jukebox.recalculatePlayingIndex(false);

            int index = jukebox.getPlayingIndex()+1;

            if(jukebox.getItem(index) == null || jukebox.getItem(index).is(Items.AIR)) {
                index = 0;
            }

            if (index < 0) return Component.empty();

            ItemStack item = jukebox.getItem(index);
            if (item == null || item.is(Items.AIR)) {
                return Component.empty();
            } else {
                List<Component> lines = item.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
                if (lines.isEmpty()) return Component.empty();

                String content = (!lines.getFirst().getSiblings().isEmpty() && lines.getFirst().getSiblings().getFirst().getContents().toString().contains("item.minecraft.")) ? lines.getFirst().getSiblings().getFirst().getContents().toString().split("='")[1].split("',")[0].trim() : lines.get(1).getString();

                if (content.trim().isEmpty()) return Component.empty();

                if (content.startsWith("item.minecraft")) content = content + ".desc";

                String componentContent = content.startsWith("item.minecraft") ? Component.translatable(content).getString() : content;

                String[] split = componentContent.split(" - ");
                if(split.length < 2) return Component.empty();
                String title = split[1].trim();

                return Component.empty().append(title);
            }
        }

        return Component.empty();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext displayLinkContext) {
        return true;
    }

    @Override
    protected String getTranslationKey() {
        return "album_jukebox_next_title";
    }
}
