# BackSlot

One more equipment slot, on your back. Fabric, Minecraft 26.2, no dependencies.

## Using it

Drag something into the slot in your inventory, just above the shield slot. Or press **G**
to swap it with whatever you're currently holding; the key is rebindable under
Controls → Inventory, sitting next to vanilla's own offhand swap.

The slot takes any item and holds a full stack, exactly like the offhand does. There's no
restriction on what goes in it. A sword, a shulker box, a stack of dirt, whatever you want
on your back.

## How it behaves

Nothing here is new to learn, because the back slot follows the same rules as the rest of
your inventory:

- drops on death, and stays with you when keepInventory is on
- survives logging out, respawning, and coming back through the end portal
- destroyed by Curse of Vanishing, the same as any other equipment
- reachable in creative, in the survival inventory tab

Since other players see your back item, **the mod has to be on the server too**, not only on
the clients.

## No dependencies

Not even Fabric API. Drop the jar into `/mods` alongside Fabric Loader.

| | |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 or newer |
| Java | 25 |

## Building

Needs JDK 25 and Gradle. Loom fetches Minecraft itself, so there's nothing else to set up.

```bash
gradle build
```

The jar lands in `build/libs/`. Two other tasks worth knowing:

```bash
gradle runClient
```

```bash
gradle genSources
```

`runClient` launches the game against your working copy; `genSources` decompiles Minecraft
so your IDE can resolve it. The decompile takes a few minutes and only needs doing once.

## How it works

Everything is mixins, there are no entrypoints and no registered content.

The stack lives in the player's synched entity data rather than a container of its own,
because the item has to be drawn on *other* people's backs, so every client tracking a player
needs it. That also means the mod carries no networking code.

The slot itself is a real `Slot` at index 46 of `InventoryMenu`, which is what lets vanilla
drive it. Shift-clicking, creative clicks and the G keybind are all ordinary container
interactions; the keybind sends a plain `SWAP` click rather than anything custom, since that
click type already means "trade this slot with that hotbar index".

## Translations

The keybind label is a single string in `BackSlotKeys`, handed to the game by
`ClientLanguageMixin`. The mod ships no language files and can't. Registering a mod's assets
with the resource manager is Fabric API's job, and there's no Fabric API here, so adding a
language means extending that lookup rather than dropping in a JSON file. Open an issue or a
PR with the language code and the string and it can go in.
