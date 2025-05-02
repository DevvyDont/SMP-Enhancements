package xyz.devvydont.smprpg.items.base;

import io.papermc.paper.datacomponent.item.Equippable;
import org.bukkit.inventory.EquipmentSlot;

public interface IEquippableOverride {

    /**
     * Determines what equipment attributes to override to this item.
     * @return The Equippable properties of this item.
     */
    Equippable getEquipmentOverride();

    static Equippable generateDefault(EquipmentSlot slot) {
        return Equippable.equippable(slot)
                .swappable(true)
                .dispensable(true)
                .damageOnHurt(true)
                .build();
    }

}
