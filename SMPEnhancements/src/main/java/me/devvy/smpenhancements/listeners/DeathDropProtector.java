package me.devvy.smpenhancements.listeners;

import me.devvy.smpenhancements.SMPEnhancements;
import me.devvy.smpenhancements.events.AddDeathCertificateEvent;
import me.devvy.smpenhancements.events.ProtectDeathDropsEvent;
import me.devvy.smpenhancements.items.ItemUtil;
import me.devvy.smpenhancements.tasks.VoidProtectionTask;

import org.bukkit.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class DeathDropProtector implements Listener {



    public DeathDropProtector() {

        // Have a loop check all items in the end twice a second, if any of them are in the end, not floating, and below y=0 then protect them
        VoidProtectionTask voidProtectionTask = new VoidProtectionTask();
        voidProtectionTask.runTaskTimer(SMPEnhancements.getInstance(), 1, 10);

    }

    @EventHandler
    public void onEntityPickupAnyItem(EntityPickupItemEvent event) {
        // Stop protecting this item
        ItemUtil.setItemStackFlag(event.getItem().getItemStack(), false, null);
    }

    @EventHandler
    public void onProtectedItemEntitySpawned(ItemSpawnEvent event) {

        // When an item is spawned, if it has the dead drop flag, protect it
        if (!event.getEntity().getItemStack().hasItemMeta())
            return;

        if (!ItemUtil.isDeadDrop(event.getEntity().getItemStack()))
            return;

        ItemUtil.addItemEntityAttributes(event.getEntity());

        // If this item spawned in the void in the end, lets make them float at y=1
        VoidProtectionTask.checkAndFloatAboveVoid(event.getEntity());
    }

    @EventHandler
    public void onInventoryAttemptPickupProtectedItem(InventoryPickupItemEvent event) {

        // Do not allow hoppers/inventories to pickup a dead drop at all
        if (ItemUtil.isDeadDrop(event.getItem().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryAttemptMoveProtectedItem(InventoryMoveItemEvent event) {

        // Do not allow hoppers/inventories to pickup a dead drop at all
        if (ItemUtil.isDeadDrop(event.getItem()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAttemptPickupProtectedItem(PlayerAttemptPickupItemEvent event) {

        if (!ItemUtil.isDeadDrop(event.getItem().getItemStack()))
            return;

        // Ignore people in creative mode for cleanup reasons
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;

        // Only cancel the event if someone else is trying to pickup the item
        if (!event.getPlayer().getName().equalsIgnoreCase(ItemUtil.getOwnerName(event.getItem().getItemStack())))
            event.setCancelled(true);

    }

    @EventHandler
    public void onProtectedItemDespawn(ItemDespawnEvent event) {

        // Is this a dead drop?
        if (!ItemUtil.isDeadDrop(event.getEntity().getItemStack()))
            return;

        // Under no circumstance let a deaddrop item despawn
        event.setCancelled(true);
    }

    ////////////////////////////////////
    // CUSTOM EVENT CALLING
    ////////////////////////////////////

    @EventHandler
    public void onPlayerAttemptReceiveDeathCertificate(PlayerRespawnEvent event) {

        // If this wasnt a death respawn then ignore it
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH))
            return;

        // Make a bukkit event and if it gets canceled by another plugin cancel it
        AddDeathCertificateEvent deathCertEvent = new AddDeathCertificateEvent(event.getPlayer());
        deathCertEvent.callEvent();
        if (deathCertEvent.isCancelled())
            return;

        event.getPlayer().getInventory().addItem(ItemUtil.generateDeathPaper(event.getPlayer()));

    }

    @EventHandler
    public void onPlayerAttemptDropProtectedItems(PlayerDeathEvent event) {

        // If the player didn't die with anything ignore
        if (event.getDrops().isEmpty() || event.getKeepInventory())
            return;

        // We are going to protect these drops, if another plugin decides to cancel it then we don't
        ProtectDeathDropsEvent dropProtEvent = new ProtectDeathDropsEvent(event);
        dropProtEvent.callEvent();
        if (dropProtEvent.isCancelled())
            return;

        // When someone dies, mark all their items as dead drops
        for (ItemStack item : event.getDrops())
            ItemUtil.setItemStackFlag(item, true, event.getPlayer().getName());
    }



}
