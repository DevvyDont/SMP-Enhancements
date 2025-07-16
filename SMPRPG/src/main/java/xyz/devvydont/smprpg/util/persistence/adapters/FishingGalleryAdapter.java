package xyz.devvydont.smprpg.util.persistence.adapters;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.fishing.utils.FishingGallery;

public class FishingGalleryAdapter implements PersistentDataType<PersistentDataContainer, FishingGallery> {


    /**
     * Returns the primitive data type of this tag.
     * @return the class
     */
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    /**
     * Returns the complex object type the primitive value resembles.
     * @return the class type
     */
    @Override
    public @NotNull Class<FishingGallery> getComplexType() {
        return FishingGallery.class;
    }

    /**
     * Returns the primitive data that resembles the complex object passed to
     * this method.
     * @param complex the complex object instance
     * @param context the context this operation is running in
     * @return the primitive value
     */
    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull FishingGallery complex, @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        for (var entry : complex.entries())
            container.set(entry.getKey(), INTEGER, entry.getValue());
        return container;
    }

    /**
     * Creates a complex object based of the passed primitive value
     * @param primitive the primitive value
     * @param context   the context this operation is running in
     * @return the complex object instance
     */
    @Override
    public @NotNull FishingGallery fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        var gallery = new FishingGallery();
        for (var key : primitive.getKeys())
            gallery.set(key, primitive.getOrDefault(key, INTEGER, 0));
        return gallery;
    }
}
