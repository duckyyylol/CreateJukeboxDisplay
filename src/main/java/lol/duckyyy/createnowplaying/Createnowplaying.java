package lol.duckyyy.createnowplaying;

import com.simibubi.create.foundation.data.CreateRegistrate;
import lol.duckyyy.createnowplaying.register.create.display.ModDisplaySources;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Createnowplaying.MODID)
public class Createnowplaying {
    public static final String MODID = "createnowplaying";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public Createnowplaying(IEventBus modEventBus) {
        modEventBus.addListener(ModDisplaySources::register);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}