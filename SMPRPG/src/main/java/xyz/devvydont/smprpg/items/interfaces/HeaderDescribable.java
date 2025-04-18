package xyz.devvydont.smprpg.items.interfaces;

import net.kyori.adventure.text.Component;

import java.util.List;

/**
 * Represents an Item that can be "described". This item is considered to have some sort of special information
 * attached to it that is unique that should be explained.
 */
public interface HeaderDescribable {

    List<Component> getHeader();

}
