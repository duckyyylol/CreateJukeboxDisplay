package lol.duckyyy.createnowplaying.event;

import lol.duckyyy.createnowplaying.Createnowplaying;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class ServerTickEvent {
    private static final int INTERVAL = 20;
    private static int ticks = 0;

    @SubscribeEvent
    public static void onServerTick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post ev) {
        if(++ticks >= INTERVAL) {
            ticks = 0;

            Createnowplaying.ElapsedMap.forEach((BlockPos jukebox, Long seconds) -> {
                Createnowplaying.ElapsedMap.put(jukebox, seconds+1);
            });
        }
    }
}
