package lol.duckyyy.createnowplaying;

import com.mojang.logging.LogUtils;
import lol.duckyyy.createnowplaying.event.ServerTickEvent;
import lol.duckyyy.createnowplaying.register.create.display.ModDisplaySources;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

@Mod(Createnowplaying.MODID)
public class Createnowplaying {
    public static final String MODID = "createnowplaying";
//    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Map<BlockPos, Long> ElapsedMap;
    public static Set<BlockPos> DisplayLinkElapsedBoundBlocks;
    public static boolean etched = false;
    public static CNPConfig CONFIG;
    public static ModConfigSpec CONFIG_SPEC;


    public Createnowplaying(IEventBus modEventBus, ModContainer container) {
        Pair<CNPConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CNPConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();

        container.registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC);

        modEventBus.addListener(ModDisplaySources::register);
        NeoForge.EVENT_BUS.register(ServerTickEvent.class);



        ElapsedMap = new HashMap<BlockPos, Long>();
        DisplayLinkElapsedBoundBlocks = new HashSet<BlockPos>();

        etched = ModList.get().isLoaded("etched");

        if(etched) LOGGER.info("\"Etched\" detected, will register Mixins");
    }

    public static void log(String inp) {
        LOGGER.info(inp);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String getDeliminatorForLocale(@Nullable String locale) {
        Map<String, String> delims = new HashMap<>();
        delims.put("default", " - ");
        List<String> emDashLocales = new ArrayList<>(List.of(
                "ba_ru",
                "be_by",
                "be_latn",
                "cv_cu",
                "eo_uy",
                "hal_ua",
                "it_it",
                "ka_ge",
                "kk_kz",
                "pl_pl",
                "pt_br",
                "pt_pt",
                "ro_ro",
                "rpr",
                "ru_ru",
                "sah_sah",
                "szl",
                "tt_ru",
                "uk_ua",
                "uz_uz"));
        List<String> enDashLocales = new ArrayList<>(List.of(
                "hn_no",
                "hr_hr",
                "hu_hu",
                "no_no",
                "sv_se"));

        for(String l : emDashLocales) {
            delims.put(l, " — ");
        }
        for(String l : enDashLocales) {
            delims.put(l, " – ");
        }

        delims.put("fi_fi", " –  ");

        return delims.getOrDefault(locale, delims.get("default"));
    }

//    @SubscribeEvent
//    public static void registerNetworking(final RegisterPayloadHandlersEvent ev) {
//        final PayloadRegistrar registrar = ev.registrar(MODID).versioned("1.0.0");
//
//        registrar.playToServer(TrackSyncPayload.TYPE, TrackSyncPayload.CODEC, TrackSyncHandler::handle);
//    }
}