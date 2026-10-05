package lol.duckyyy.createnowplaying.register.create.display;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModDisplaySources {
    public static final DisplaySource JUKEBOX_PLAYING = new JukeboxPlayingDisplaySource();
    public static final DisplaySource JUKEBOX_SONG_TITLE = new JukeboxTitleDisplaySource();
    public static final DisplaySource JUKEBOX_ARTIST = new JukeboxArtistDisplaySource();
    public static final DisplaySource JUKEBOX_SHORT_PLAYBACK_ELAPSED = new JukeboxPlaybackShortElapsedDisplaySource();
    public static final DisplaySource JUKEBOX_LONG_PLAYBACK_ELAPSED = new JukeboxPlaybackLongElapsedDisplaySource();
    public static final DisplaySource JUKEBOX_REDSTONE_POWER = new JukeboxRedstonePowerDisplaySource();

    // "Etched" Mod Compat
    public static final DisplaySource ALBUM_JUKEBOX_PROVIDER = new AlbumJukeboxProviderDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_NEXT_TITLE = new AlbumJukeboxNextTrackTitleDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_NEXT_ARTIST = new AlbumJukeboxNextTrackArtistDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_NEXT = new AlbumJukeboxNextTrackDisplaySource();

    public static final DisplaySource ALBUM_JUKEBOX_PLAYING = new JukeboxPlayingDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_SONG_TITLE = new JukeboxTitleDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_ARTIST = new JukeboxArtistDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_SHORT_PLAYBACK_ELAPSED = new JukeboxPlaybackShortElapsedDisplaySource();
    public static final DisplaySource ALBUM_JUKEBOX_LONG_PLAYBACK_ELAPSED = new JukeboxPlaybackLongElapsedDisplaySource();

    public static void register(RegisterEvent ev) {
        if(!ev.getRegistryKey().equals(CreateBuiltInRegistries.DISPLAY_SOURCE.key())) return;

        registerDisplaySource(JUKEBOX_PLAYING, "jukebox_playing", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_SONG_TITLE, "jukebox_title", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_ARTIST, "jukebox_artist", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_SHORT_PLAYBACK_ELAPSED, "jukebox_short_playback_elapsed", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_LONG_PLAYBACK_ELAPSED, "jukebox_long_playback_elapsed", Blocks.JUKEBOX);
        registerDisplaySource(JUKEBOX_REDSTONE_POWER, "jukebox_redstone_power", Blocks.JUKEBOX);

        if(Createnowplaying.etched) {
            Block albumJukeboxBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("etched", "album_jukebox"));

            if(albumJukeboxBlock != null) {
                Createnowplaying.log("etched:album_jukebox found, registering display sources");

                registerDisplaySource(ALBUM_JUKEBOX_PLAYING, "album_jukebox_playing", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_SONG_TITLE, "album_jukebox_title", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_ARTIST, "album_jukebox_artist", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_SHORT_PLAYBACK_ELAPSED, "album_jukebox_short_playback_elapsed", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_LONG_PLAYBACK_ELAPSED, "album_jukebox_long_playback_elapsed", albumJukeboxBlock);

                // Etched-Specific Sources
                registerDisplaySource(ALBUM_JUKEBOX_PROVIDER, "album_jukebox_provider", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_NEXT_TITLE, "album_jukebox_next_title", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_NEXT_ARTIST, "album_jukebox_next_artist", albumJukeboxBlock);
                registerDisplaySource(ALBUM_JUKEBOX_NEXT, "album_jukebox_next", albumJukeboxBlock);

            } else Createnowplaying.log("Album jukebox block not found, not registering display sources");

        }
    }


    private static void registerDisplaySource(DisplaySource displaySource, String displaySourceId, Block block) {
        registerDisplaySource(displaySource, Createnowplaying.asResource(displaySourceId), block);
    }

    private static void registerDisplaySource(DisplaySource displaySource, ResourceLocation location, Block block) {
        Registry.register(CreateBuiltInRegistries.DISPLAY_SOURCE, location, displaySource);
        DisplaySource.BY_BLOCK.add(block, displaySource);


    }
}
