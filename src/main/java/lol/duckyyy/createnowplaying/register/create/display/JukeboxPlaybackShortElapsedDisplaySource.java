package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import gg.moonflower.etched.common.blockentity.AlbumJukeboxBlockEntity;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

import java.util.concurrent.TimeUnit;

public class JukeboxPlaybackShortElapsedDisplaySource extends SingleLineDisplaySource {
    private MutableComponent formatTime(long minutes, long seconds) {
        return Component.literal(String.format("%s:%s", minutes >= 10 ? minutes : String.format("0%s", minutes),seconds >= 10 ? seconds : String.format("0%s", seconds)));
    }

    @Override
    public int getPassiveRefreshTicks() {
        return 20;
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity entity = context.level().getBlockEntity(context.getSourcePos());

        if(entity instanceof JukeboxBlockEntity jukebox) {
            if(jukebox.isEmpty() || jukebox.getTheItem().is(Items.AIR)) {
                Createnowplaying.ElapsedMap.remove(jukebox.getBlockPos());
                return formatTime(0, 0);
            }

            boolean playing = jukebox.getSongPlayer().getSong() != null;

            // "Etched" Mod Compat
            if (!playing && Createnowplaying.etched) {
                if (jukebox.getTheItem().toString().contains("etched_music_disc")) {
                    playing = true;
                }
            }

            if(playing) {
                long elapsedMinutes = 0L;
                long elapsedSeconds = 0L;
                if(!Createnowplaying.ElapsedMap.containsKey(jukebox.getBlockPos())) Createnowplaying.ElapsedMap.put(jukebox.getBlockPos(), 0L);

                elapsedSeconds = TimeUnit.SECONDS.toSeconds(Createnowplaying.ElapsedMap.get(jukebox.getBlockPos()));
                elapsedMinutes = TimeUnit.SECONDS.toMinutes(Createnowplaying.ElapsedMap.get(jukebox.getBlockPos()));

                elapsedSeconds = elapsedSeconds - TimeUnit.MINUTES.toSeconds(elapsedMinutes);

                return formatTime(elapsedMinutes, elapsedSeconds);
            } else {
                Createnowplaying.ElapsedMap.remove(jukebox.getBlockPos());
                return formatTime(0, 0);
            }
        }else if(Createnowplaying.etched) {
            // "Etched" mod compat
            if(entity instanceof AlbumJukeboxBlockEntity jukebox) {
                if(!jukebox.isPlaying()) {
                    Createnowplaying.ElapsedMap.remove(jukebox.getBlockPos());
                    return formatTime(0, 0);
                }

                long elapsedMinutes = 0L;
                long elapsedSeconds = 0L;
                if(!Createnowplaying.ElapsedMap.containsKey(jukebox.getBlockPos())) Createnowplaying.ElapsedMap.put(jukebox.getBlockPos(), 0L);

                elapsedSeconds = TimeUnit.SECONDS.toSeconds(Createnowplaying.ElapsedMap.get(jukebox.getBlockPos()));
                elapsedMinutes = TimeUnit.SECONDS.toMinutes(Createnowplaying.ElapsedMap.get(jukebox.getBlockPos()));

                elapsedSeconds = elapsedSeconds - TimeUnit.MINUTES.toSeconds(elapsedMinutes);

                return formatTime(elapsedMinutes, elapsedSeconds);
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
        return "jukebox_short_playback_elapsed";
    }
}