# WHIMC-Portals
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/whimc/Portals?label=download&logo=github)](https://github.com/whimc/Portals/releases/latest)

Portals is a Minecraft plugin that allows for the creation of custom portals with user-defined destinations. This plugin uses [Multiverse Portals](https://dev.bukkit.org/projects/multiverse-portals).

---
## Building
Build the source with Maven:
```
$ mvn install
```

---
## Commands

| Command                                             | Description                                                        |
|-----------------------------------------------------|--------------------------------------------------------------------|
|`/destination change <destination_name>`             | Sets the location of a destination to the user's current position. |
|`/destination clear <portal_name>`                   | Removes the destination of a portal.                               |
|`/destination create <destination_name>`             | Creates a new destination at the user's current position.          |
|`/destination info <destination_name>`               | Displays information about the current destination.                |
|`/destination list`                                  | Displays a list of all the existing destinations.                  |
|`/destination purge <'invalid'/'no-portals'/'both'>` | Purges any unused and / or invalid destinations.                   |
|`/destination remove <destination_name>`             | Removes a destination.                                             |
|`/destination set <portal_name> <destination_name>`  | Sets the destination of a portal.                                  |
|`/destination sethere <portal_name>`                 | Sets the destination of the portal to the user's current location. |
|`/destination teleport <destination_name>`           | Teleports the user to the provided destination.                    |
|`/portal create <portal_name>`                       | Creates a permissionless portal using the selected location.       |
|`/portal debug`                                      | Displays information about the portal being entered (no teleport). |
|`/portal info <portal_name>`                         | Displays information about a portal.                               |
|`/portal list`                                       | Lists all existing portals.                                        |
|`/portal permission <permission_suffix>`             | Sets or removes portal permissions.                                |
|`/portal purge <'invalid'/'no-destination'/'both'>`  | Purges any unused and / or invalid portals.                        |
|`/portal refill <portal_name>`                       | Regenerates the filler of a portal.                                |
|`/portal remove <portal_name>`                       | Removes a portal.                                                  |
|`/portal reshape <portal_name>`                      | Reshape a portal to your current selection.                        |
|`/portal setfiller <portal_name> <block_type>`       | Sets the filler of a portal.                                       |
|`/portal teleport <portal_name>`                     | Teleports the user to the provided portal.                         |
|`/portal tool`                                       | Gives user the portal selector tool.                               |

### Creating a Portal

To setup a new portal you'll want to use the `wooden sword` tool to `left` and then `right` click two blocks to select an ``empty`` area. Then do:

```yaml
/portal create <portal_name>
/portal setfiller <portal_name> <block_type>
/portal permission <permission_suffix>
````

After this, you'll want to configure a destination.

### Creating and Setting a Destination

Destinations are independent of portals, so you can have many portals link to a single destination. To create a destination to stand in the place you want it and do:

```yaml
/destination create <destination_name>
````

You can then set a portal to link to it with:

```yaml
/destination set <portal_name> <destination_name>
````

Note that `/destination` has many other sub-commands, including:

![image](https://user-images.githubusercontent.com/5846359/136828198-ab093daf-1b08-481e-a352-f391a269c497.png)

---
## Configuration

### portalData.yml

Includes a listing of each portal with world, position info, destination and permissions as well as a listing of all destinations with worlds and positions. 

---
## Dependencies
* [Multiverse Portals](https://dev.bukkit.org/projects/multiverse-portals)