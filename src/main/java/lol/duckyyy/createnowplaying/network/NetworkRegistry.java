package lol.duckyyy.createnowplaying.network;

import lol.duckyyy.createnowplaying.Createnowplaying;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Createnowplaying.MODID)
public class NetworkRegistry {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent ev) {
        final PayloadRegistrar registrar = ev.registrar(Createnowplaying.MODID).versioned("1.0.0");
        registrar.playToServer(TrackSyncPayload.TYPE, TrackSyncPayload.CODEC, TrackSyncHandler::handle);
    }
}
