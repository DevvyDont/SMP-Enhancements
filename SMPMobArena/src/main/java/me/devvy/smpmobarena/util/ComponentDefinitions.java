package me.devvy.smpmobarena.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ComponentDefinitions {

    public static final Component PREFIX = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("!", NamedTextColor.GOLD))
            .append(Component.text("] ", NamedTextColor.GRAY));

}
