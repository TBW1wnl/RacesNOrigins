package com.tbw1wnl.racesnorigins.client;

import com.tbw1wnl.racesnorigins.network.ServerboundSelectTraitsPayload;
import com.tbw1wnl.racesnorigins.network.TraitSummary;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Consumer;

/**
 * Two-step "choose your race, then your class" picker, shown on a player's first join (see
 * {@code FabricNetworking}/{@code NeoForgeNetworking} - opened when a received
 * {@code ClientboundTraitListPayload} has {@code hasChosen() == false}). Not dismissible via Escape
 * since both choices are mandatory; the actual network send is delegated to the loader-specific
 * {@code onConfirm} callback rather than a common service, since this screen (like all client-only
 * code) is only ever constructed from client code paths to begin with.
 */
public class RaceClassSelectionScreen extends Screen {

    private static final int ROW_HEIGHT = 32;
    private static final int ROW_SPACING = 4;
    private static final int ROW_WIDTH = 280;
    private static final int LIST_TOP = 50;

    private enum Step {
        RACE, CLASS
    }

    private final List<TraitSummary> races;
    private final List<TraitSummary> classes;
    private final Consumer<ServerboundSelectTraitsPayload> onConfirm;

    private Step step = Step.RACE;
    private Identifier selectedRaceId;
    private Identifier selectedClassId;
    private Button confirmButton;

    public RaceClassSelectionScreen(List<TraitSummary> races, List<TraitSummary> classes,
                                     Consumer<ServerboundSelectTraitsPayload> onConfirm) {
        super(Component.literal("Choose your origin"));
        this.races = races;
        this.classes = classes;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void init() {
        List<TraitSummary> entries = step == Step.RACE ? races : classes;
        int x = (this.width - ROW_WIDTH) / 2;
        int y = LIST_TOP;
        for (TraitSummary summary : entries) {
            addRenderableWidget(new TraitEntryWidget(x, y, ROW_WIDTH, ROW_HEIGHT, summary,
                    () -> summary.id().equals(currentSelection()), this::select));
            y += ROW_HEIGHT + ROW_SPACING;
        }

        confirmButton = addRenderableWidget(Button.builder(
                        Component.literal(step == Step.RACE ? "Next" : "Confirm"),
                        button -> onConfirmPressed())
                .bounds((this.width - 150) / 2, this.height - 50, 150, 20)
                .build());
        confirmButton.active = currentSelection() != null;
    }

    private Identifier currentSelection() {
        return step == Step.RACE ? selectedRaceId : selectedClassId;
    }

    private void select(TraitSummary summary) {
        if (step == Step.RACE) {
            selectedRaceId = summary.id();
        } else {
            selectedClassId = summary.id();
        }
        confirmButton.active = true;
    }

    private void onConfirmPressed() {
        if (currentSelection() == null) {
            return;
        }
        if (step == Step.RACE) {
            step = Step.CLASS;
            rebuildWidgets();
        } else {
            onConfirm.accept(new ServerboundSelectTraitsPayload(selectedRaceId, selectedClassId));
            onClose();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        Component title = Component.literal(step == Step.RACE ? "Choose your race" : "Choose your class");
        graphics.text(this.font, title, (this.width - this.font.width(title)) / 2, 16, 0xFFFFFFFF);
    }
}
