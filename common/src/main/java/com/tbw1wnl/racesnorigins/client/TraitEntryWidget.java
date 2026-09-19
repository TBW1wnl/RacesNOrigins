package com.tbw1wnl.racesnorigins.client;

import com.tbw1wnl.racesnorigins.network.TraitSummary;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * A single selectable row in {@link RaceClassSelectionScreen}: icon, name, description. Selection
 * state is owned by the screen (via {@code selected}) so only one entry in a list is highlighted at
 * a time.
 */
public class TraitEntryWidget extends AbstractWidget {

    private static final int ICON_SIZE = 16;
    private static final int PADDING = 6;

    private final TraitSummary summary;
    private final BooleanSupplier selected;
    private final Consumer<TraitSummary> onClick;
    private final ItemStack icon;

    public TraitEntryWidget(int x, int y, int width, int height, TraitSummary summary,
                             BooleanSupplier selected, Consumer<TraitSummary> onClick) {
        super(x, y, width, height, Component.literal(summary.name()));
        this.summary = summary;
        this.selected = selected;
        this.onClick = onClick;
        this.icon = new ItemStack(BuiltInRegistries.ITEM.getValue(summary.icon()));
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        onClick.accept(summary);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        int background = selected.getAsBoolean() ? 0xC02D5A8C : isHovered() ? 0xC0505050 : 0xC02A2A2A;
        graphics.fill(x, y, x + getWidth(), y + getHeight(), background);
        if (selected.getAsBoolean()) {
            graphics.fill(x, y, x + 2, y + getHeight(), 0xFF33AAFF);
        }

        graphics.item(icon, x + PADDING, y + (getHeight() - ICON_SIZE) / 2);

        int textX = x + PADDING + ICON_SIZE + PADDING;
        graphics.text(net.minecraft.client.Minecraft.getInstance().font, summary.name(), textX, y + 4, 0xFFFFFFFF);
        graphics.text(net.minecraft.client.Minecraft.getInstance().font, summary.description(), textX, y + 15, 0xFFAAAAAA);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, Component.literal(summary.name() + " - " + summary.description()));
    }

    public TraitSummary summary() {
        return summary;
    }
}
