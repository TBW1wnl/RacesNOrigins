package com.tbw1wnl.racesnorigins.registry;

import com.tbw1wnl.racesnorigins.data.TraitDefinition;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Holds the currently-loaded race and class definitions, refreshed by {@link TraitReloadListener}
 * on every datapack (re)load. Platform code registers the two listener instances exposed here via
 * the {@code IReloadListenerRegistrar} service.
 */
public final class TraitRegistry {

    public static final TraitReloadListener RACES =
            new TraitReloadListener("races", "races", TraitRegistry::setRaces);
    public static final TraitReloadListener CLASSES =
            new TraitReloadListener("classes", "classes", TraitRegistry::setClasses);

    private static Map<Identifier, TraitDefinition> races = Map.of();
    private static Map<Identifier, TraitDefinition> classes = Map.of();
    private static final List<Runnable> reloadListeners = new ArrayList<>();

    private TraitRegistry() {
    }

    public static Map<Identifier, TraitDefinition> races() {
        return races;
    }

    public static Map<Identifier, TraitDefinition> classes() {
        return classes;
    }

    public static Optional<TraitDefinition> getRace(Identifier id) {
        return Optional.ofNullable(races.get(id));
    }

    public static Optional<TraitDefinition> getClassDefinition(Identifier id) {
        return Optional.ofNullable(classes.get(id));
    }

    private static void setRaces(Map<Identifier, TraitDefinition> data) {
        races = Map.copyOf(data);
    }

    private static void setClasses(Map<Identifier, TraitDefinition> data) {
        classes = Map.copyOf(data);
    }

    /**
     * Registers a callback invoked after every reload (once per reloaded folder - callbacks must be
     * idempotent). Used by the player-data layer to reapply modifiers when a definition changes.
     */
    public static void onReloaded(Runnable listener) {
        reloadListeners.add(listener);
    }

    static void notifyReloaded() {
        for (Runnable listener : reloadListeners) {
            listener.run();
        }
    }
}
