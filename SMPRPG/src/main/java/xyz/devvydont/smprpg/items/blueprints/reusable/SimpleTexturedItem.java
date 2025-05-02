package xyz.devvydont.smprpg.items.blueprints.reusable;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured;
import xyz.devvydont.smprpg.services.ItemService;

public class SimpleTexturedItem extends CustomItemBlueprint implements ICustomTextured {

    public SimpleTexturedItem(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public String getTextureUrl() {
        return resolveTexture();
    }

    /**
     * Provides a very fast and simple way to associate textures with custom items if you do not want to create an
     * entirely new class for it if it doesn't serve a big purpose.
     * @return A texture URL based on the item type. If one wasn't found, an exception will throw.
     */
    private String resolveTexture() {
        return switch (getCustomItemType()) {
            case BURGER -> "545440bd8a551aea344d81bf398c9f7cfbaaad582b184785abf0ac1d1d78bb26";
            default -> throw new IllegalStateException("Unexpected custom item type: " + getCustomItemType() + ". Please add a texture in SimpleTexturedItem");
        };
    }
}
