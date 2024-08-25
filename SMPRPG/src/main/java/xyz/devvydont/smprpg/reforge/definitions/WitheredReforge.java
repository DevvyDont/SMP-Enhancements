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
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

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
                ComponentUtil.getDefaultText("Provides a").append(ComponentUtil.getColoredComponent(" SIGNIFICANT", NamedTextColor.GOLD)).append(ComponentUtil.getDefaultText(" boost")),
                ComponentUtil.getDefaultText("in attack damage and movement speed"),
                Component.empty(),
                Component.text("Withered Bonus", NamedTextColor.BLUE),
                ComponentUtil.getDefaultText("Deal ").append(Component.text("2x", NamedTextColor.GREEN)).append(ComponentUtil.getDefaultText(" damage to enemies who")),
                ComponentUtil.getDefaultText("have the ").append(Component.text("withered", NamedTextColor.DARK_RED).append(ComponentUtil.getDefaultText(" potion effect")))
        );
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, getDamageBuff(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .12f)
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