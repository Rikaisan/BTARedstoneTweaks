# Redstone Tweaks

A mod to improve BTA! redstone :)

## Tweaks

### Changes
- Redstone wire is no longer redirected by diagonal power sources (e.g. lever at the side of the block the wire is on)
- Redstone Jack o' lanterns behaves as a solid block, allowing the block to be powered
- Redstone Jack o' lanterns isolate the front face from the rest of the redstone going though it
- Redstone Jack o' lanterns can be now used as a semi-target block
- Redstone blocks no longer hard power adjacent blocks (toggleable with the gamerule `redstoneBlockHardPower`)
- Activator block now allows using left click to lock/unlock slots
- Activator block now allows unlocking slots while holding an item
- Activator block now allows locking/unlocking all unused slots with middle click
- Redstone ore now redirects redstone dust

### Fixes
- Redstone wire now properly handles redstone redirection
- Redstone wire no longer visually connects to things it isn't logically connected to
- Repeaters now properly connect to redstone dust
- Repeaters now properly soft power some blocks and redstone components
- Repeaters now send updates when removed

## To-do list

### Defined
- Allow some sort for compact downwards wiring, just like upwards glass. The groundwork is already implemented, just need to choose a block :) maybe slabs or another glass type?
- Add a config menu to allow enabling/disabling some tweaks
- Allow a slot to be used twice on an activator (e.g. bucket in a sequence of steps would be able to be handled properly)
- Fix trapdoors closing when adjacent blocks are updated
- Fix directionality (monostable circuits suck in BTA!)
- Fix positional inconsistencies
- Fix update queue being reset on dimension change and world reload

### Maybe
- Add observer functionality to motion sensor
- Make activator be able to retake some items like discs from jukeboxes?
- Make activator be able to plant seeds from below farmland
- Activator rail
- Minecart with basket, going over mesh blocks drops items in order and over golden mesh blocks only a certain item.

## Afterword

I made this mod out of pure love for BTA, I want to be able to play the game and don't let jank ruin the experience, feel free to contribute or use the code as you see fit :)
Licence is CC0, but credit is always appreciated!
