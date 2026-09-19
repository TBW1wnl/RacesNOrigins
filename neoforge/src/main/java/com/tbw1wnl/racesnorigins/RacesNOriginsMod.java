package com.tbw1wnl.racesnorigins;

import com.tbw1wnl.racesnorigins.command.TraitCommands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class RacesNOriginsMod {

    public RacesNOriginsMod(IEventBus eventBus) {

        CommonClass.init();
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> TraitCommands.register(event.getDispatcher()));
    }
}
