# Permissions

This document contains the various permissions that can be utilized in a permission plugin
like LuckPerms. Server operators implicitly inherit any permissions that are checked against
in the plugin.

### General

`smprpg.receiveopmessages` - Having this permission means the player will receive messages regarding critical plugin information intended for server operators to see.

### Commands

`smprpg.command.reforge` - Allows usage of `/reforge` anywhere.

`smprpg.command.reforge.admin` - Allows `/reforge` to take an argument to force apply a reforge to an item.

`smprpg.command.give` - Allows the usage of `/give`, which mimics vanilla's counterpart with the benefit of getting custom items.

`smprpg.command.summon` - Allows the usage of `/summon`, which mimics vanilla's counterpart with the benefit of summoning custom entities.

`smprpg.command.attribute` - Allows the usage of `/attribute`, which mimics vanilla's counterpart with the benefit of tweaking custom attributes.

`smprpg.command.eco` - Allows the usage of `/eco`, where the server economy can be managed.

`smprpg.command.skill.admin` - Allows manually modifying skill level/experience values using `/skill`.

`smprpg.command.simulatefishing` - Allows the ability to simulate fishing rolls. This is considered an admin command.

### GUIs

`smprpg.difficulty.ignorerestrictions` - Allows difficulty to be changed with no restrictions.

### Items

`smprpg.items.spawneditor.view` - Allows a player to view GUIs related to spawner entities when using the "Spawner Editor Wand".

`smprpg.items.spawneditor.modify` - Allows a player to make modifications to spawner entities.