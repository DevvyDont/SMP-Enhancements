### SMPRPG

This plugin aims to completely transform the progression of an SMP, but still stay true
to what makes vanilla Minecraft special. The core gameplay is still the same, but
the progression is completely reworked to add more depth to all aspects of the game
in the form of skills and additional resources, loot, and gear. This allows more aspects
of the game to have a reason to be played, so that players are encouraged to put in more
time into things such as farming Elder Guardians, hypermaxing a fishing setup, and even having
a reason to use expensive maxed out armor to give them an edge in lategame content.

## Setup

This plugin is quite intrusive to core Minecraft, and as such, requires a few settings in 
server configs to be set to work correctly. Before continuing, **double check that you are
running a Paper server.** This plugin is specifically designed to only work on Paper.

### spigot.yml
- Change the `settings.attribute` options (excluding movement speed) to allow values of precisely `2.0E9`.
By default, vanilla Minecraft only allows entities to have a max of 2048 HP and deal 2048 max damage. This isn't high enough.

### paper-world-defaults.yml
- Change `entities.behavior.disable-player-crits` to `true`. Critical hits are managed manually by the plugin since crit
behavior cannot be modified via either the Paper, Spigot, or Bukkit API.
- I would recommend turning on `anticheat.anti-xray`. I have never run a server where nobody has attempted to xray, and the
free built in anti-cheat with paper for this comes in handy.

### Misc notes
It is also important to note that any player data is stored using persistent data containers. This means that
any progression data such as skills are stored in your `world/playerdata/` folder. 
