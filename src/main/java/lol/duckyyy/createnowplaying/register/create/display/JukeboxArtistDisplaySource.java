package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

public class JukeboxArtistDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity entity = context.level().getBlockEntity(context.getSourcePos());

        if(entity instanceof JukeboxBlockEntity jukebox) {
            if(jukebox.getSongPlayer().isPlaying() && jukebox.getSongPlayer().getSong() != null) {
                String content = jukebox.getSongPlayer().getSong().description().getString();
                String[] split = content.split(" - ");
                String title = split[0];
                if(title == null) return Component.translatable("text.createnowplaying.no_artist");

                return Component.empty().append(title);
            } else {
                return Component.translatable("text.createnowplaying.no_artist");
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
