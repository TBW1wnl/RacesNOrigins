package com.tbw1wnl.racesnorigins.client;

import com.tbw1wnl.racesnorigins.network.TraitSummary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/**
 * Scrollable race/class picker (see {@link RaceClassSelectionScreen}) - a vanilla
 * {@link ObjectSelectionList} rather than a fixed stack of rows, since the list of races/classes is
 * datapack-driven and unbounded (fixed rows only fit 4-5 entries before running off the bottom of
 * the screen). Scrollbar, scissoring, and background are handled entirely by the vanilla base class.
 */
public class TraitSelectionList extends ObjectSelectionList<TraitSelectionList.Entry> {

    private static final int ROW_WIDTH = 280;

    public TraitSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight,
                               List<TraitSummary> entries, Consumer<TraitSummary> onSelect) {
        super(minecraft, width, height, y, itemHeight);
        for (TraitSummary summary : entries) {
            addEntry(new Entry(summary, onSelect));
        }
    }

    @Override
    public int getRowWidth() {
        return ROW_WIDTH;
    }

    public TraitSummary getSelectedSummary() {
        Entry selected = getSelected();
        return selected == null ? null : selected.summary;
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {

        private static final int ICON_SIZE = 16;
        private static final int PADDING = 6;

        private final TraitSummary summary;
        private final Consumer<TraitSummary> onSelect;
        private final ItemStack icon;

        Entry(TraitSummary summary, Consumer<TraitSummary> onSelect) {
            this.summary = summary;
            this.onSelect = onSelect;
            this.icon = new ItemStack(BuiltInRegistries.ITEM.getValue(summary.icon()));
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            int x = getX();
            int y = getY();
            int background = hovered ? 0xC0505050 : 0xC02A2A2A;
            graphics.fill(x, y, x + getWidth(), y + getHeight(), background);

            graphics.item(icon, x + PADDING, y + (getHeight() - ICON_SIZE) / 2);

            int textX = x + PADDING + ICON_SIZE + PADDING;
            graphics.text(Minecraft.getInstance().font, summary.name(), textX, y + 4, 0xFFFFFFFF);
            graphics.text(Minecraft.getInstance().font, summary.description(), textX, y + 15, 0xFFAAAAAA);
        }

        @Override
        public Component getNarration() {
            return Component.literal(summary.name() + " - " + summary.description());
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            onSelect.accept(summary);
            return super.mouseClicked(event, doubleClick);
        }
    }
}
