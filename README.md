Simple Spigot library for checking compatibility with the current server version.

### Maven
VersionUtil is available on [Maven Central](https://central.sonatype.com/artifact/com.cjcrafter/weaponmechanics).
To use it, add the following to your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.cjcrafter</groupId>
        <artifactId>versionutil</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle
Add the following into your `build.gradle.kts`:
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.cjcrafter:versionutil:1.0.0")
}
```

### Usage
> > Checking if we need to remap the CraftBukkit package for Paper servers
```java
    // In 1.20.6+ paper servers, the CraftBukkit package has been remapped
    String craftBukkitPackage;
    boolean isPaper = ServerVersions.isPaper();
    if (isPaper && MinecraftVersions.TRAILS_AND_TAILS.get(5).isAtLeast()) {
        craftBukkitPackage = "org.bukkit.craftbukkit.";
    } else {
        craftBukkitPackage = "org.bukkit.craftbukkit." + versionString + '.';
    }
```

> > Parsing a potion effect from a string
```java
    String potion = "speed";
    PotionEffectType potion = null;
    if (MinecraftVersions.WILD_UPDATE.isAtLeast())
        potion = PotionEffectType.getByKey(NamespacedKey.minecraft(potion.toLowerCase(Locale.ROOT)));
    if (potion == null)
        potion = PotionEffectType.getByName(potion.toUpperCase(Locale.ROOT));
    if (potion == null)
        throw new IllegalArgumentException("Invalid potion effect type: " + potion);
```