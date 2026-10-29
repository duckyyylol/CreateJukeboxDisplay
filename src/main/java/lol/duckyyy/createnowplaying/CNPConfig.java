package lol.duckyyy.createnowplaying;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;



public class CNPConfig  {
    public final ModConfigSpec.ConfigValue<Boolean> showTimeElapsed;
    public final ModConfigSpec.ConfigValue<Boolean> albumJukeboxShowNext;
    public final ModConfigSpec.EnumValue<GoggleDisplayStyle> goggleDisplayStyle;

    public enum GoggleDisplayStyle {
        INFORMATIVE,
        COMPACT,
        DISABLED
    }

    public CNPConfig(ModConfigSpec.Builder builder) {
        showTimeElapsed = builder.translation("createnowplaying.config.goggles.show_time_elapsed").comment("Show playback time elapsed in Jukebox goggle display?").define("goggles.show_time_elapsed", true);
        albumJukeboxShowNext = builder.translation("createnowplaying.config.goggles.album_jukebox_show_next").comment("Show next track in Album Jukebox goggle display?").define("goggles.album_jukebox_show_next", true);
        goggleDisplayStyle = builder.translation("createnowplaying.config.goggles.display_style").comment("How should the goggle overlay appear for jukebox-type blocks?").defineEnum("goggles.display_style", GoggleDisplayStyle.INFORMATIVE);
    }

}
