package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

public abstract class ChargedItemBlueprint extends CustomItemBlueprint {

    public ChargedItemBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public abstract int maxCharges(ItemMeta meta);

    public Sound getBreakSound() {
        return Sound.ENTITY_ITEM_BREAK;
    }

    public void breakItem(Player player, ItemStack item) {
        item.setAmount(0);
        player.playSound(player.getLocation(), getBreakSound(), 1, 1);
    }

    public void useCharge(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        int chargesUsed = getChargesUsed(meta);
        chargesUsed++;
        setChargesUsed(meta, chargesUsed);
        item.setItemMeta(meta);

        chargesUsed = getChargesUsed(meta);

        if (getChargesLeft(meta) <= 0)
            breakItem(player, item);
    }

    public void setChargesUsed(ItemMeta meta, int charges) {

        if (!(meta instanceof Damageable damageable))
            return;

        damageable.setDamage(charges);
        updateMeta(meta);
    }

    public int getChargesUsed(ItemMeta meta) {
        if (!(meta instanceof Damageable damageable))
            return 0;

        return damageable.getDamage();
    }

    public int getChargesLeft(ItemMeta meta) {

        if (!(meta instanceof Damageable damageable))
            return 0;

        return damageable.getMaxDamage() - damageable.getDamage();
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        if (meta instanceof Damageable damageable)
            damageable.setMaxDamage(maxCharges(meta));

        super.updateMeta(meta);

        meta.setUnbreakable(false);
    }

    @Override
    public List<Component> getFooterComponent(ItemMeta meta) {
        return List.of(
                Component.empty(),
                ComponentUtils.getDefaultText("Charges left: ").append(ComponentUtils.getColoredComponent(getChargesLeft(meta) + "", NamedTextColor.GREEN))
        );
    }
}
