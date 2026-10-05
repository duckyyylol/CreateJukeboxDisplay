package lol.duckyyy.createnowplaying;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import lol.duckyyy.createnowplaying.event.ServerTickEvent;
import lol.duckyyy.createnowplaying.network.TrackSyncHandler;
import lol.duckyyy.createnowplaying.network.TrackSyncPayload;
import lol.duckyyy.createnowplaying.register.create.display.ModDisplaySources;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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

//    @SubscribeEvent
//    public static void registerNetworking(final RegisterPayloadHandlersEvent ev) {
//        final PayloadRegistrar registrar = ev.registrar(MODID).versioned("1.0.0");
//
//        registrar.playToServer(TrackSyncPayload.TYPE, TrackSyncPayload.CODEC, TrackSyncHandler::handle);
//    }
}