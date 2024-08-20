package xyz.devvydont.smprpg.items.blueprints.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.ArrayList;
import java.util.List;

public class InfernoArrow extends CustomItemBlueprint implements Sellable, Listener {

    public InfernoArrow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> components = new ArrayList<>(super.getDescriptionComponent(meta));
        components.add(ComponentUtil.getDefaultText("Used to summon an ")
                .append(ComponentUtil.getColoredComponent("Infernal Phoenix", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.OBFUSCATED)));
        components.add(ComponentUtil.getDefaultText("when ")
                .append(ComponentUtil.getColoredComponent("shot in lava", NamedTextColor.GOLD).decorate(TextDecoration.OBFUSCATED))
                .append(ComponentUtil.getDefaultText(" in the "))
                .append(ComponentUtil.getColoredComponent("Nether", NamedTextColor.RED)));
        return components;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setFireResistant(true);
        updateLore(meta);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public int getWorth() {
        return 20_000;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return 20_000;
    }

    /*
     * Decide what to do when this arrow is initially shot. For now, we just add a cute little glow :p
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onShootInfernoArrow(EntityShootBowEvent event) {

        // Is the projectile our arrow?
        if (!(event.getProjectile() instanceof SpectralArrow arrow))
            return;

        if (!isItemOfType(arrow.getItemStack()))
            return;

        ItemStack item = arrow.getItemStack();
        item.editMeta( meta -> {
            if (event.getEntity() instanceof Player player)
                SMPRPG.getInstance().getDropsService().setOwner(meta, player);
        });
        arrow.setItemStack(item);
        SMPRPG.getInstance().getDropsService().getTeam(ItemRarity.LEGENDARY).addEntity(arrow);  // give it a glow :p
        arrow.setGlowing(true);
        arrow.setHitSound(Sound.BLOCK_AMETHYST_BLOCK_BREAK);
        arrow.setInvulnerable(true);
    }

    /*
     * We need to disallow our inferno arrow from being lost to being shot at enemies + in lava to burn
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInfernoArrowCollideWithEntity(ProjectileHitEvent event) {


        // Is the projectile our arrow?
        if (!(event.getEntity() instanceof SpectralArrow arrow))
            return;

        if (!isItemOfType(arrow.getItemStack()))
            return;

        // This arrow is an inferno arrow. Decide what to do what an inferno arrow collides with something.
        // If we hit an entity, this interaction is completely nullified.
        if (event.getHitEntity() != null) {
            event.setCancelled(true);
            arrow.setVelocity(arrow.getVelocity().multiply(-1).multiply(.5));
            arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1.5f);
            return;
        }

        // What about when we hit a block?
        // Delete the arrow entity and spawn the corresponding item stack so they don't lose it.
        Block block = event.getHitBlock();
        if (block != null) {
            Item drop = block.getWorld().dropItemNaturally(arrow.getLocation(), arrow.getItemStack());
            drop.setVelocity(arrow.getVelocity().multiply(-1).multiply(.2));
            arrow.remove();
        }

    }
}
