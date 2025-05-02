package xyz.devvydont.smprpg.items.interfaces;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface ICustomTextured {

    /**
     * Retrieve the URL to use for the custom head texture of this item.
     * The link that is set here should follow the following format:
     * Let's say you have the following link to a skin;
     * <a href="https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a">...</a>
     * You should only use the very last component of the URL, as the backend will fill in the rest.
     * Meaning we would end up using: "18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a"
     * @return The URL to the skin.
     */
    String getTextureUrl();

    // Caching mechanism for storing profiles that we retrieved. This allows us to build custom items quickly as if
    // they weren't pulling textures from the internet no matter how fast we create them.
    Map<String, PlayerProfile> profileCache = new HashMap<>();

    /**
     * Helper method to retrieve and convert a URL to a skin texture to a player profile to be used on skulls.
     * @param url The full URL to retrieve.
     * @return A PlayerProfile instance that can be applied to SkullMeta.
     */
    static PlayerProfile getProfile(String url) {

        // If we already resolved a profile, use that
        var profile = profileCache.get(url);
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
        profileCache.put(url, profile);
        return profile;
    }

    /**
     * Used if you just want to quickly create a skull, no custom blueprint required.
     * For this method, you must pass in the full URL.
     * @param url The FULL URL to the texture. (https://textures.minecraft.net/texture/......)
     * @return An item with a custom texture applied to it.
     */
    static ItemStack createTexturedSkull(String url) {
        var item = ItemStack.of(Material.PLAYER_HEAD);
        var meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(getProfile(url));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * The method that should be called externally to modify the meta of the item to reflect the custom skull.
     * Keep in mind the passed in item NEEDS to be a Material.PLAYER_HEAD, otherwise the returned item
     * will not reflect the same item that was passed in, and it will not be updated. A warning will spit out
     * in case you are abusing this behavior.
     * @param textured A blueprint instance that implements this interface.
     * @return The same item stack passed in.
     */
    static ItemStack update(ItemStack item, ICustomTextured textured) {

        // Ensure this is a player skull.
        if (!item.getType().equals(Material.PLAYER_HEAD))
            throw new IllegalArgumentException("The item parameter NEEDS to be a player head item! It is currently: " + item.getType());

        // Apply the item component to make this skull render correctly.
        var url = String.format("https://textures.minecraft.net/texture/%s", textured.getTextureUrl());
        var profile = getProfile(url);
        var resolvedProfile = ResolvableProfile.resolvableProfile(profile);
        item.setData(DataComponentTypes.PROFILE, resolvedProfile);

        // Remove the ability to equip this item by default.
        // If you want to be able to equip it, the item should also implement the IEquippableOverride interface.
        // That interface will have logic in place to allow it to be equippable again.
        item.unsetData(DataComponentTypes.EQUIPPABLE);

        return item;
    }



}
