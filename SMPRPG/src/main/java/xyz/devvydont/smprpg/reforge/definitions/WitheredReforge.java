package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class WitheredReforge extends ReforgeBase implements Listener {

    public static float getDamageBuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .20f;
            case UNCOMMON -> .25f;
            case RARE -> .30f;
            case EPIC -> .35f;
            case LEGENDARY -> .40f;
            case MYTHIC -> .50f;
            case DIVINE -> .55f;
            case TRANSCENDENT -> .60f;
            case SPECIAL -> .70f;
        };
    }

    public WitheredReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides a").append(ComponentUtils.create(" SIGNIFICANT", NamedTextColor.GOLD)).append(ComponentUtils.create(" boost")),
                ComponentUtils.create("in attack damage and attack speed"),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Withered Bonus", NamedTextColor.BLUE),
                ComponentUtils.create("Deal ").append(ComponentUtils.create("2x", NamedTextColor.GREEN)).append(ComponentUtils.create(" damage to enemies who")),
                ComponentUtils.create("have the ").append(ComponentUtils.create("withered", NamedTextColor.DARK_RED).append(ComponentUtils.create(" potion effect")))
        );
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new ScalarAttributeEntry(AttributeWrapperLegacy.STRENGTH, getDamageBuff(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapperLegacy.ATTACK_SPEED, .12f)
        );
    }

    @Override
    public int getPowerRating() {
        return 5;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDealDamageToWitheredEnemies(CustomEntityDamageByEntityEvent event) {

        // Is the damaged entity withered?
        if (!(event.getDamaged() instanceof LivingEntity damaged))
            return;

        if (damaged.getPotionEffect(PotionEffectType.WITHER) == null)
            return;

        if (!(event.getDealer() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        boolean hasWitheredReforge = hasReforge(living.getEquipment().getItemInMainHand()) || hasReforge(living.getEquipment().getItemInOffHand());
        if (!hasWitheredReforge)
            return;

        // We have the withered reforge and the one getting attacked is withered. 2x the damage
        event.multiplyDamage(2);
    }
}
