package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

public class JukeboxRedstonePowerDisplaySource extends SingleLineDisplaySource {
    @Override
    public int getPassiveRefreshTicks() {
        return 20;
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity entity = context.level().getBlockEntity(context.getSourcePos());

        if(entity instanceof JukeboxBlockEntity jukebox) {
            if(jukebox.getSongPlayer().isPlaying() && jukebox.getSongPlayer().getSong() != null) {
                return Component.empty().append(String.format("%s", jukebox.getSongPlayer().getSong().comparatorOutput()));
            } else {
                return Component.literal("0");
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
        return "jukebox_redstone_power";
    }
}
