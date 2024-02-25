package me.devvy.smpevents.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ComponentUtil {

    public static final TextColor GRAY = TextColor.color(200, 200, 200);
    public static final TextColor YELLOW = TextColor.color(200, 200, 0);
    public static final TextColor GREEN = TextColor.color(0, 200, 0);
    public static final TextColor RED = TextColor.color(200, 0, 0);

    public static Component getEventPrefix(TextColor exclColor) {
        return Component.text("[", GRAY)
                .append(Component.text("!", exclColor, TextDecoration.BOLD))
                .append(Component.text("] ", GRAY));
    }

    public static Component getEventPrefix() {
        return getEventPrefix(YELLOW);
    }

}
