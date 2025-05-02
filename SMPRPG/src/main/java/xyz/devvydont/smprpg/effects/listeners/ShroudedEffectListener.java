package xyz.devvydont.smprpg.effects.listeners;

import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.effects.tasks.ShroudedEffect;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/*
 * Responsible for the application of the "Shrouded" special effect.
 * This is in its own class since the application of special effects cannot be contained in its class.
 */
public class ShroudedEffectListener implements Listener {

    private final String[] MESSAGES = {
            "Death has touched you… but not taken you. For now, you walk unseen.",
            "You awaken between worlds — the veil shields you, so long as you do not disturb the balance.",
            "The shroud clings to you… but it is thin, and the living world is easily stirred.",
            "Returned, but not restored. The spirits grant you passage, briefly.",
            "The boundary blurs — you walk hidden, neither living nor lost.",
            "Returned, yet unseen — but the world watches closely when touched.",
            "You are hidden from fate's eye… until your actions call it back.",
            "For now, you walk unnoticed — but the world knows when you act.",
            "A quiet return… but all silence breaks with sound.",
            "The world does not yet see you. But it will, should you make it.",
            "The shroud is not protection — it is patience.",
            "Unseen, unheard — but not forgotten."
    };

    private final SpecialEffectService service;

    public ShroudedEffectListener(SpecialEffectService service) {
        this.service = service;
    }

    private String getRandomMessage() {
        return MESSAGES[(int) (Math.random() * MESSAGES.length)];
    }

    /*
     * When a player respawns, give them the shrouded effect.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        var effect = new ShroudedEffect(service, event.getPlayer(), 60*5);
        var msg = ComponentUtils.merge(
                ComponentUtils.create("From "),
                ComponentUtils.create("???", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                ComponentUtils.create(": "),
                ComponentUtils.create("\"" + getRandomMessage() + "\"", NamedTextColor.AQUA)
        ).hoverEvent(HoverEvent.showText(ComponentUtils.merge(
                ComponentUtils.create("It seems like you "),
                ComponentUtils.create("died", NamedTextColor.DARK_RED),
                ComponentUtils.create(".\nDon't worry! Your lost items will take "),
                ComponentUtils.create("MUCH LONGER", NamedTextColor.GOLD),
                ComponentUtils.create(" to despawn than usual.\nTo aid in the retrieval of your items, you have been given a "),
                ComponentUtils.create("Death Certificate", NamedTextColor.RED),
                ComponentUtils.create(" containing information regarding your death and the "),
                effect.getNameComponent(),
                ComponentUtils.create(" ailment to be temporarily invisible to hostile enemies.\nKeep in mind, "),
                ComponentUtils.create("certain interactions", NamedTextColor.RED),
                ComponentUtils.create(" are capable of making it expire!")
        )));
        event.getPlayer().sendMessage(msg);
        service.giveEffect(event.getPlayer(), new ShroudedEffect(service, event.getPlayer(),  60*5));
    }

}
