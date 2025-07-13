package xyz.devvydont.smprpg.gui.base;

import org.bukkit.Material;

/**
 * Represents an item that may be displayed by vanilla Minecraft GUIs. All an entity needs to be displayable
 * by a GUI by default, is a material to show up as. Any other specific logic such as button name,
 * enchant glow, or special lore text should be handled by the UI.
 */
public interface IMenuDisplayable {

    /**
     * Get the desired material to make this button appear as in a Minecraft GUI.
     * @return A vanilla Minecraft material.
     */
    Material getDisplayMaterial();

}
