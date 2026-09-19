package com.tbw1wnl.racesnorigins.registry;

import com.tbw1wnl.racesnorigins.Constants;
import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Loads every {@code data/<namespace>/<folder>/*.json} into {@link TraitDefinition}s via its codec,
 * datapack-reloadable like any other vanilla JSON resource. One instance per folder ("races",
 * "classes") - see {@link TraitRegistry#RACES} / {@link TraitRegistry#CLASSES}.
 */
public class TraitReloadListener extends SimpleJsonResourceReloadListener<TraitDefinition> {

    private final String name;
    private final Consumer<Map<Identifier, TraitDefinition>> onLoaded;

    public TraitReloadListener(String folder, String name, Consumer<Map<Identifier, TraitDefinition>> onLoaded) {
        super(TraitDefinition.CODEC, FileToIdConverter.json(folder));
        this.name = name;
        this.onLoaded = onLoaded;
    }

    @Override
    protected void apply(Map<Identifier, TraitDefinition> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        onLoaded.accept(data);
        Constants.LOG.info("Loaded {} {}", data.size(), name);
        TraitRegistry.notifyReloaded();
    }

    @Override
    public String getName() {
        return Constants.MOD_ID + ":" + name;
    }
}
