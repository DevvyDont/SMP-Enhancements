# SMP-Enhancements
 A collection of spigot/paper plugins that I use for my Minecraft SMP servers I host

## AdvancementCompition
A plugin that allows you to create a competition for advancements. It will track the advancements of all players and 
display them on the leaderboard. The leaderboard is toggleable with `/togglescoreboard`

Also as a small feature, the Ender Dragon egg is obtainable by defeating the Ender Dragon multiple times and acquiring
"Dragon Shards" which can then be used to craft into an alternate type of Dragon Egg. This allows for all players
to fairly obtain the achievement requiring players to hold the dragon egg.

## CustomItems
Adds custom items that can be obtained in game through various means (fishing, mining, defeating mobs). These items
are typically unobtainable in vanilla play containing impossible stats and overenchants, with functionality of having
custom abilities.

As a bonus, a command is included (`/treasure`) that lets any player view every registered custom item, 
the items' possible stat ranges, every possible way to obtain items with respective drop rates, and a trade-up
system to trade in lower quality items for higher quality items.

As an administator, you can also use `/givetreasure` to cheat in these custom items.

## DeathDrops
Protects items from despawning when a player dies. This is useful for when a player dies in lava, or in a place
where they cannot normally retrieve their items. When a player dies their items will never despawn, glow, and be
invincible to all types of damage. Items that also fall in the void in The End will float just above the void.
Players will also be given a "Death Certificate" that tells them when and where they died.

## DynamicDifficulty
Lets players open a GUI using `/difficulty` to vote on a difficulty for the server to run on. The difficulty is chosen
based on the majority vote (tiebreakers are settled from easier difficulties first).
As an incentive to player on higher difficulties, NORMAL and HARD give players bonuses such as XP multipliers and an 
increased Luck stat.

## SMPBuildWorld (Unfinished)
Creates a world that is used for building competitions. Players can claim plots and build in them. 

## SMPDuels
Allows players to duel each other in a 1v1 setting. Players can use `/duel <player>` to challenge 
another player to a duel. The challenged player can then accept or deny the duel. The winner of the duel will 
receive their head displaying their W/L record against that player.

## SMPEnhancements (Deprecated)
Old paper version of DeathDrops that functionally does the same thing, was backported to spigot for compatibility.

## SMPEvents
A plugin that is meant to be the middleman for my plugins that have functionality that may interfere with an SMP
environment. This allows a server to define an "event area" where players must go if they want to participate in
any minigames or activities a plugin may offer. 

The most significant feature/purpose of this plugin is to allow [Dodgebolt](https://github.com/DevvyDont/Dodgebolt) to
function on an SMP server without players having to worry about inventory loss or death.

## SMPMobArena (Unfinished)
A Plugin that creates an experience similar to old school mob arenas. Players can cooperatively (or solely) fight
waves of mobs in an arena, gain experience, and upgrade their stats and gear. This plugin will also have a leaderboard
that will display the players that have reached the highest wave and the time that it took.

## SMPParkour
Allows player to teleport to a "parkour world" (basically just a parkour map I downloaded) where players can compete
for the fastest time to complete the map. The plugin will keep track of the fastest times and display them on a
holographic leaderboard at the spawn of the parkour world.

As a bonus, a practice mode is available for players to practice the parkour map with a custom checkpoint system.

Adding new maps is currently hard-coded and requires source code access to add new maps.

## Stimmys
Adds an item to the game that is specifically used as a currency for a GUI shop using `/stimmy`. The shop lets
players purchase items useful for an SMP server. 

As an administrator, you can also use `/stimmy <add/set> <player>` to give players this currency. They do not have to
be online to receive it and can redeem them whenever they log on.

## WhatAmIHolding
An extremely simple plugin that allows players to use `/whatamiholding` to display the name of the item they are
holding in chat for other players to hover over and look at.
