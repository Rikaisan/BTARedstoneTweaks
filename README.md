# Redstone Tweaks

A mod to improve BTA! redstone :)

## Tweaks

### Changes
- Redstone blocks no longer hard power adjacent blocks (toggleable with the gamerule `redstoneBlockHardPower`)
- Activator block now allows using left click to lock/unlock slots
- Activator block now allows unlocking slots while holding an item
- Activator block now allows locking/unlocking all unused slots with middle click
- Redstone ore now redirects redstone dust
- Levers are now placed parallel to the player's view instead of perpendicularly
- Fence gates can now be affected by redstone
- Mesh Blocks and Gold Mesh Blocks can now be powered to block items from going through them

### Fixes
- Repeaters now properly soft power some blocks and redstone components
- Repeaters now send updates when removed
- Repeaters no longer send a 1 tick pulse when placed next to a powered block (breaks repeater auto-powering with `/setblock`, toggleable with the gamerule `removeInitialRepeaterUpdate`)
- TNT now properly handles valid signals to activate

## Merged to BTA!
Some of our changes were merged into base BTA! and therefore are no longer included in our mod, here's a list of changes no longer included in Redstone Tweaks:

### Merged in BTA! 8.0+

- Redstone wire now uses the [Alternate Current](<https://www.curseforge.com/minecraft/mc-mods/alternate-current>) efficient and non-locational redstone dust implementation (toggleable with the `useAlternateCurrent` gamerule)
- Redstone Jack o' lanterns behaves as a solid block, allowing the block to be powered
- Redstone Jack o' lanterns isolate the front face from the rest of the redstone going though it
- Redstone wire is no longer redirected by diagonal power sources (e.g. lever at the side of the block the wire is on)  [This is NOT related to QC]
- Redstone wire no longer visually connects to things it isn't logically connected to
- Redstone wire now uses the same checks to visually connect diagonally downwards than upwards
- Repeaters now properly connect to redstone dust
- Redstone Jack o' lanterns no longer redirect redstone on all sides
- Redstone wire now properly handles redstone redirection
- Redstone wire now properly sends updates when its direction changes
- Activator block now allows using seeds directly on farmland


## To-do list

### Defined
- Allow some sort for compact downwards wiring, just like upwards glass. The groundwork is already implemented, just need to choose a block :) maybe slabs or another glass type?

### Maybe
- Make activator be able to retake some items like discs from jukeboxes or items from golden meshes
- Allow a slot to be used twice on an activator (e.g. bucket in a sequence of steps would be able to be handled properly)
- Activator rail
- Minecart with basket, going over mesh blocks drops items in order and over golden mesh blocks only a certain item.

## Afterword

I made this mod out of pure love for BTA, I want to be able to play the game and don't let jank ruin the experience, feel free to contribute or use the code as you see fit :)
Licence is CC0, but credit is always appreciated!
