package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import lol.duckyyy.createnowplaying.Createnowplaying;
import lol.duckyyy.createnowplaying.register.create.display.JukeboxPlayingDisplaySource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModDisplaySources {
    public static final DisplaySource JUKEBOX_PLAYING = new JukeboxPlayingDisplaySource();
    public static final DisplaySource JUKEBOX_SONG_TITLE = new JukeboxTitleDisplaySource();
    public static final DisplaySource JUKEBOX_ARTIST = new JukeboxArtistDisplaySource();

    public static void register(RegisterEvent ev) {
        if(!ev.getRegistryKey().equals(CreateBuiltInRegistries.DISPLAY_SOURCE.key())) return;

        registerDisplaySource(JUKEBOX_PLAYING, "jukebox_playing", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_SONG_TITLE, "jukebox_title", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_ARTIST, "jukebox_artist", Blocks.JUKEBOX);
    }


    private static void registerDisplaySource(DisplaySource displaySource, String displaySourceId, Block block) {
        registerDisplaySource(displaySource, Createnowplaying.asResource(displaySourceId), block);
    }

    private static void registerDisplaySource(DisplaySource displaySource, ResourceLocation location, Block block) {
        Registry.register(CreateBuiltInRegistries.DISPLAY_SOURCE, location, displaySource);
        DisplaySource.BY_BLOCK.add(block, displaySource);
    }
}
