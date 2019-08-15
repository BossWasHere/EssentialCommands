# EssentialCommands v1.0.0
#### By BackwardsNode/BossWasHere
A simple and lightweight command plugin, tested for Spigot and Paper servers.
Intuitive commands such as /fly, /heal etc. with a custom API developed for making command registration as simple as possible.

This is a minor side project to implement compatibility after development for [Essentials](https://dev.bukkit.org/projects/essentials) ceased before 1.8, originally made before the modern alternative [EssentialsX](https://dev.bukkit.org/projects/essentialsx) was founded.

You can find the active Github repository for EssentialsX [here](https://github.com/EssentialsX/Essentials).
This project now serves as proof-of-concept, to which there are better alternatives to support and updates which this project provides.

### Building
EssentialCommands uses the Spigot-API for building, and Spigot/CraftBukkit libraries for testing (to be introduced later).
[BackwardsAPI](https://github.com/BossWasHere/BackwardsAPI) is also required and can be added through a local repository.

To compile, it is recommended that you use  [BuildTools](https://www.spigotmc.org/wiki/buildtools) first.
Using Maven, you can build the plugin using:
```
mvn clean package
```