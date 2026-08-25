package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import gg.moonflower.etched.core.registry.EtchedItems;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class AlbumJukeboxProviderDisplaySource extends SingleLineDisplaySource {
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

            int index = jukebox.getPlayingIndex();
//                Createnowplaying.log("Index: " + index);

            if (index < 0) return Component.empty();

            ItemStack item = jukebox.getItem(index);
            if (item == null || item.is(Items.AIR)) {
                return Component.empty();
            } else {
                if(!item.is(EtchedItems.ETCHED_MUSIC_DISC)) return Component.translatable("text.createjukeboxdisplay.providers.minecraft");

                List<Component> lines = item.getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL).stream().filter(l -> !l.getString().trim().isEmpty()).toList();
//                    Createnowplaying.log(lines.size() + " lines: " + lines.toString());
                if (lines.isEmpty() || lines.size() < 3) return Component.empty();

                String content = lines.get(2).getString();

                if (content.trim().isEmpty()) return Component.empty();

                String[] split = content.split("Provided by ");
                if(split.length < 2) return Component.empty();
                String provider = split[1].trim();

                return Component.empty().append(provider);
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
        return "album_jukebox_provider";
    }
}
