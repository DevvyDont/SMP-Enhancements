package xyz.devvydont.smprpg.items.reforges.interfaces;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.items.ItemRarity;

import java.util.Map;

public interface HarvestReforgeable {

    public Map<Attribute, AttributeModifier> getHarvesterModifiers(ItemRarity rarity);

}
