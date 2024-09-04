package xyz.devvydont.smprpg.gui.sample;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Arrays;

public final class SampleMenu extends MenuBase {
    private int pageIndex;
    private final Material[][] pages = generatePages();

    public SampleMenu(@NotNull Player player) {
        super(player, 3);
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Sample Menu", NamedTextColor.BLACK));
        render();
    }

    private void render() {
        this.clear();
        this.setBorderFull();

        // Render page counter
        this.setSlot(4, createNamedItem(Material.CHERRY_SIGN, String.format("Page %s/%s", pageIndex + 1, pages.length)));

        // Render the item buttons
        var currentPage = this.pages[this.pageIndex];
        for (int i = 0; i < currentPage.length; i++) {
            var material = currentPage[i];
            this.setButton(10 + i, new ItemStack(material), (e) -> {
                this.openSubMenu(new SampleSubMenu(this.player, this, material));
            });
        }

        // Render the navigation buttons
        this.setButton(21, BUTTON_PAGE_PREVIOUS, (e) -> {
            this.pageIndex--;
            if (this.pageIndex < 0) {
                this.pageIndex = this.pages.length - 1;
            }
            this.sounds.playPagePrevious();
            this.render();
        });
        this.setButton(22, BUTTON_EXIT, (e) -> {
            this.closeMenu();
        });
        this.setButton(23, BUTTON_PAGE_NEXT, (e) -> {
            this.pageIndex++;
            if (this.pageIndex >= this.pages.length) {
                this.pageIndex = 0;
            }
            this.sounds.playPageNext();
            this.render();
        });
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        var clickedItem = this.getItem(event.getSlot());
        if (clickedItem == null || BORDER_NORMAL.equals(clickedItem)) {
            this.playInvalidAnimation();
        }
    }

    private static Material[][] generatePages() {
        var chunkSize = 7;
        var valuesToChunk = Arrays.stream(Material.values()).skip(0).toArray(Material[]::new);
        var numOfChunks = (int)Math.ceil((double)valuesToChunk.length / chunkSize);
        var output = new Material[numOfChunks][];
        for(int i = 0; i < numOfChunks; ++i) {
            var start = i * chunkSize;
            var length = Math.min(valuesToChunk.length - start, chunkSize);
            var temp = new Material[length];
            System.arraycopy(valuesToChunk, start, temp, 0, length);
            output[i] = temp;
        }
        return output;
    }
}
