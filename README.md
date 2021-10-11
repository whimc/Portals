# WHIMC-Portals
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/whimc/Portals?label=download&logo=github)](https://github.com/whimc/Portals/releases/latest)

Create portals for player teleportation

## Building
Build the source with Maven:
```
$ mvn install
```
## Dependencies
* [multiverse-portals](https://dev.bukkit.org/projects/multiverse-portals)

## Commands

### `/portal`

To setup a new portal you'll want to use the `wooden sword` tool to `left` and then `right` click two blocks to select an ``empty`` area. Then do:

```yaml
/portal create <portalname>
/portal setfiller <portalname> <blocktype>
/portal permission <luckpermsgroup>
````

After this you'll want to configure a destination.

### `/destination`

Destinations are independent of portals, so you can have many portals link to a single destination. To create a destination to stand in the place you want it and do:

```yaml
/destination create <destinationname>
````

You can then set a portal to link to it with:

```yaml
/destination set <portalname> <destinationname>
````

Note that /destination has many other sub-commands, including:

![image](https://user-images.githubusercontent.com/5846359/136828198-ab093daf-1b08-481e-a352-f391a269c497.png)

## Config

### portalData.yml

Includes a listing of each portal with world, position info, destination and permissions as well as a listing of all destinations with worlds and positions. 
