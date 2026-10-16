package lol.duckyyy.createnowplaying;

import com.mojang.logging.LogUtils;
import lol.duckyyy.createnowplaying.event.ServerTickEvent;
import lol.duckyyy.createnowplaying.register.create.display.ModDisplaySources;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Createnowplaying.MODID)
public class Createnowplaying {
    public static final String MODID = "createnowplaying";
//    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Map<BlockPos, Long> ElapsedMap;
    public static boolean etched = false;


    public Createnowplaying(IEventBus modEventBus) {
        modEventBus.addListener(ModDisplaySources::register);
        NeoForge.EVENT_BUS.register(ServerTickEvent.class);

        ElapsedMap = new HashMap<BlockPos, Long>();

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
        delims.put("pt_br", " — ");

        return delims.getOrDefault(locale, delims.get("default"));
    }

//    @SubscribeEvent
//    public static void registerNetworking(final RegisterPayloadHandlersEvent ev) {
//        final PayloadRegistrar registrar = ev.registrar(MODID).versioned("1.0.0");
//
//        registrar.playToServer(TrackSyncPayload.TYPE, TrackSyncPayload.CODEC, TrackSyncHandler::handle);
//    }
}