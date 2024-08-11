package xyz.devvydont.smprpg.items.base;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomHeadBlueprint extends CustomItemBlueprint implements Listener {

    protected static Map<String, PlayerProfile> itemKeyToProfile = new HashMap<>();

    public CustomHeadBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);

        if (!type.hasCustomHeadTexture())
            throw new IllegalStateException("You must define a head texture URL in CustomItemType for " + getClass().getSimpleName() + " to use a custom head item type!");
    }

    private PlayerProfile getProfile(String url) {

        // If we already resolved a profile, use that
        PlayerProfile profile = itemKeyToProfile.get(url);
        if (profile != null)
            return profile;

        profile = Bukkit.createProfile(UUID.randomUUID()); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        itemKeyToProfile.put(url, profile);
        return profile;
    }

    public SkullMeta getMeta(ItemMeta meta) {
        return (SkullMeta) meta;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        SkullMeta skullMeta = getMeta(meta);
        skullMeta.setPlayerProfile(getProfile(String.format("https://textures.minecraft.net/texture/%s", getCustomItemType().getUrl())));
    }

    /**
     * Disallow placing of these blocks as we do not want to worry about item persistence
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlaceHead(BlockPlaceEvent event) {

        if (!isItemOfType(event.getItemInHand()))
            return;

        event.setCancelled(true);
    }
}
