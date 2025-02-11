# Redstone Tweaks

A mod to improve BTA! redstone :)

## Tweaks

### Changes
- Redstone wire is no longer redirected by diagonal power sources (e.g. lever at the side of the block the wire is on)  [This is NOT related to QC]
- Redstone wire now uses the [Alternate Current](<https://www.curseforge.com/minecraft/mc-mods/alternate-current>) efficient and non-locational redstone dust implementation (toggleable with the `useAlternateCurrent` gamerule)
- Redstone Jack o' lanterns behaves as a solid block, allowing the block to be powered
- Redstone Jack o' lanterns isolate the front face from the rest of the redstone going though it
- Redstone blocks no longer hard power adjacent blocks (toggleable with the gamerule `redstoneBlockHardPower`)
- Activator block now allows using left click to lock/unlock slots
- Activator block now allows unlocking slots while holding an item
- Activator block now allows locking/unlocking all unused slots with middle click
- Activator block now allows using seeds directly on farmland
- Redstone ore now redirects redstone dust
- Levers are now placed parallel to the player's view instead of perpendicularly

### Fixes
- Redstone wire now properly handles redstone redirection
- Redstone wire now properly sends updates when its direction changes
- Redstone wire no longer visually connects to things it isn't logically connected to
- Redstone wire now uses the same checks to visually connect diagonally downwards than upwards
- Repeaters now properly connect to redstone dust
- Repeaters now properly soft power some blocks and redstone components
- Repeaters now send updates when removed
- Repeaters no longer send a 1 tick pulse when placed next to a powered block (breaks repeater auto-powering with `/setblock`, toggleable with the gamerule `removeInitialRepeaterUpdate`)
- Redstone Jack o' lanterns no longer redirect redstone on all sides
- Trapdoors no longer close when adjacent blocks are updated
- TNT and door types now properly handle valid signals to activate/deactivate

## To-do list

### Defined
- Allow some sort for compact downwards wiring, just like upwards glass. The groundwork is already implemented, just need to choose a block :) maybe slabs or another glass type?
- Fix update queue being reset on dimension change and world reload
- Allow fence gates to be affected by redstone

### Maybe
- Add observer functionality to motion sensor
- Make activator be able to retake some items like discs from jukeboxes ot items from golden meshes
- Allow a slot to be used twice on an activator (e.g. bucket in a sequence of steps would be able to be handled properly)
- Activator rail
- Minecart with basket, going over mesh blocks drops items in order and over golden mesh blocks only a certain item.

## Afterword

I made this mod out of pure love for BTA, I want to be able to play the game and don't let jank ruin the experience, feel free to contribute or use the code as you see fit :)
Licence is CC0, but credit is always appreciated!
