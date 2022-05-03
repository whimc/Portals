# WHIMC-Portals
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/whimc/Portals?label=download&logo=github)](https://github.com/whimc/Portals/releases/latest)

Portals is a Minecraft plugin that allows for the creation of custom portals with user-defined destinations.

---
## Building
Build the source with Maven:
```
$ mvn install
```

---
## Commands

Note: `/destination purge` and `/portal purge` require [specific keywords defined below these commands](#definitions).

| Command                                                     | Description                                                        |
|-------------------------------------------------------------|--------------------------------------------------------------------|
|`/destination change <destination_name>`                     | Sets the location of a destination to the user's current position. |
|`/destination clear <portal_name>`                           | Removes the destination of a portal.                               |
|`/destination create <destination_name>`                     | Creates a new destination at the user's current position.          |
|`/destination info <destination_name>`                       | Displays information about the current destination.                |
|`/destination list`                                          | Displays a list of all the existing destinations.                  |
|`/destination purge <'invalid'/'no-portals'/'both'>`         | Purges any unused and / or invalid destinations.                   |
|`/destination remove <destination_name>`                     | Removes a destination.                                             |
|`/destination set <portal_name> <destination_name>`          | Sets the destination of a portal.                                  |
|`/destination sethere <portal_name>`                         | Sets the destination of the portal to the user's current location. |
|`/destination teleport <destination_name>`                   | Teleports the user to the provided destination.                    |
|`/portal create <portal_name>`                               | Creates a permissionless portal using the selected area.           |
|`/portal debug`                                              | Displays information about the portal being entered (no teleport). |
|`/portal info <portal_name>`                                 | Displays information about a portal.                               |
|`/portal list`                                               | Lists all existing portals.                                        |
|`/portal permission whimc-portals.entry.<permission_suffix>` | Sets or removes portal permissions.                                |
|`/portal purge <'invalid'/'no-destination'/'both'>`          | Purges any unused and / or invalid portals.                        |
|`/portal refill <portal_name>`                               | Regenerates the filler of a portal.                                |
|`/portal remove <portal_name>`                               | Removes a portal.                                                  |
|`/portal reshape <portal_name>`                              | Reshape a portal to your current selection.                        |
|`/portal setfiller <portal_name> <block_type>`               | Sets the filler of a portal.                                       |
|`/portal teleport <portal_name>`                             | Teleports the user to the provided portal.                         |
|`/portal tool`                                               | Gives user the portal selector tool.                               |

### Definitions
- `invalid` means that the destination/portal is in a non-existent world (used in `/destination purge` and `/portal purge`)
- `no-portals` means that the destination has no linked portals (used in `/destination purge`)
- `no-destination` means that the portal has no linked destinations (used in `/portal purge`)

### How to Create a Portal

To set up a new portal you'll want to do `/portal tool` to get the portal selection tool. Then left click to select your first position and right click to select your second. These two blocks will be the bounding corners of a cuboid [like WorldEdit](https://worldedit.enginehub.org/en/latest/usage/regions/selections/). Then do:

```
/portal create <portal_name>
/portal setfiller <portal_name> <block_type>
/portal permission <permission_suffix>
````

Note that *only* air blocks will be replaced with the filler block.

After this, you'll want to configure a destination.

### How to Create and Set a Destination

Destinations are independent of portals, so you can have many portals link to a single destination. To create a destination, stand where you'd like it to be and run:

```
/destination create <destination_name>
````

You can then set a portal to link to it with:

```
/destination set <portal_name> <destination_name>
````

Note that `/destination` has many other sub-commands, including:

![image](https://user-images.githubusercontent.com/5846359/136828198-ab093daf-1b08-481e-a352-f391a269c497.png)

---
## Configuration

### portalData.yml

*You shouldn't have to edit this file directly; everything can be done with commands.*

Includes a listing of each portal with world, position info, destination and permissions as well as a listing of all destinations with worlds and positions.

#### Example
The file will look something like this:
```yaml
Portals:
  PortalName:
    world: WorldName
    pos1:
      x: 0
      y: 0
      z: 0
    pos2:
      x: 3
      y: 3
      z: 0
    destination: DestinationName
```