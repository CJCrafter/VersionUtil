package com.cjcrafter.version

import org.bukkit.Bukkit
import java.lang.IllegalStateException
import java.util.Collections

/**
 * Utility class to check the current Minecraft version.
 *
 * Minecraft updates all follow the `major.minor.patch` format. This utility
 * separates this into two classes: [Update] and [Version]. An [Update] is a
 * "named" update from Minecraft, such as "1.13" ([UPDATE_AQUATIC]). If you
 * need to get more specific, you can use a [Version] object, which is a patch
 * for an [Update], such as "1.13.2".
 *
 * You can get a specific [Version] object calling [Update.get] with the patch
 * number.
 */
object MinecraftVersions {
    private val allUpdates = LinkedHashMap<String, Update>()
    private val allVersions = LinkedHashMap<String, Version>()

    /**
     * Returns an immutable map of all updates.
     *
     * The key is the result of [Update.toString], and the value is the
     * [Update] object. Only updates down to 1.12 are included.
     */
    @JvmStatic
    fun updates(): Map<String, Update> = Collections.unmodifiableMap(allUpdates)

    /**
     * Returns an immutable map of all versions.
     *
     * The key is the result of [Version.toString], and the value is the
     * [Version] object. Only versions down to "1.12.0" are included.
     */
    @JvmStatic
    fun versions(): Map<String, Version> = Collections.unmodifiableMap(allVersions)

    /**
     * The current version of the server.
     */
    @JvmStatic
    val CURRENT: Version by lazy { parseCurrentVersion() }

    @JvmStatic
    internal fun parseCurrentVersion(versionString: String = Bukkit.getVersion()): Version {
        var currentVersion: String? = """\d+\.\d+\.\d+""".toRegex().find(versionString)?.value
        if (currentVersion == null) {
            currentVersion = """\d+\.\d+""".toRegex().find(versionString)?.value
            if (currentVersion != null) {
                currentVersion += ".0"
            }
        }

        return allVersions[currentVersion] ?: throw IllegalStateException("Invalid version: $currentVersion")
    }

    /**
     * 1.12, the colorful blocks update (concrete)
     */
    @JvmField
    val WORLD_OF_COLOR =
        Update(1, 12) {
            version(0, 1) // 1.12
            version(1, 1) // 1.12.1
            version(2, 1) // 1.12.2
        }

    /**
     * 1.13, ocean update (the flattening, waterloggable blocks, sprint swimming, brigadier commands)
     */
    @JvmField
    val UPDATE_AQUATIC =
        Update(1, 13) {
            version(0, 1) // 1.13
            version(1, 2) // 1.13.1
            version(2, 2) // 1.13.2
        }

    /**
     * 1.14, villagers update (sneaking below slabs, new village generation)
     */
    @JvmField
    val VILLAGE_AND_PILLAGE =
        Update(1, 14) {
            version(0, 1) // 1.14
            version(1, 1) // 1.14.1
            version(2, 1) // 1.14.2
            version(3, 1) // 1.14.3
            version(4, 1) // 1.14.4
        }

    /**
     * 1.15, bees update (bug fixes, bees)
     */
    @JvmField
    val BUZZY_BEES =
        Update(1, 15) {
            version(0, 1) // 1.15
            version(1, 1) // 1.15.1
            version(2, 1) // 1.15.2
        }

    /**
     * 1.16, nether update (crimson, fungus, nether generation, biome fogs)
     */
    @JvmField
    val NETHER_UPDATE =
        Update(1, 16) {
            version(0, 1) // 1.16
            version(1, 1) // 1.16.1
            version(2, 2) // 1.16.2
            version(3, 2) // 1.16.3
            version(4, 3) // 1.16.4
            version(5, 3) // 1.16.5
        }

    /**
     * 1.17, caves and cliffs part 1 (tuff, new mobs, new blocks)
     */
    @JvmField
    val CAVES_AND_CLIFFS_1 =
        Update(1, 17) {
            version(0, 1) // 1.17
            version(1, 1) // 1.17.1
        }

    /**
     * 1.18, caves and cliffs part 2 (new generations)
     */
    @JvmField
    val CAVES_AND_CLIFFS_2 =
        Update(1, 18) {
            version(0, 1) // 1.18
            version(1, 1) // 1.18.1
            version(2, 2) // 1.18.2
        }

    /**
     * 1.19, the deep dark update (sculk, warden, mud, mangrove, etc.)
     */
    @JvmField
    val WILD_UPDATE =
        Update(1, 19) {
            version(0, 1) // 1.19
            version(1, 1) // 1.19.1
            version(2, 2) // 1.19.2
            version(3, 3) // 1.19.3
            version(4, 3) // 1.19.4
        }

    /**
     * 1.20, the archaeology update (cherry grove, sniffers, etc.)
     */
    @JvmField
    val TRAILS_AND_TAILS =
        Update(1, 20) {
            version(0, 1) // 1.20
            version(1, 1) // 1.20.1
            version(2, 2) // 1.20.2
            version(3, 3) // 1.20.3
            version(4, 3) // 1.20.4
            version(5, 4) // 1.20.5
            version(6, 4) // 1.20.6
        }

    /**
     * 1.21, the dungeons update (mace, potions, paintings, etc.)
     */
    @JvmField
    val TRICKY_TRIALS =
        Update(1, 21) {
            version(0, 1) // 1.21
        }

    /**
     * Represents a "big" Minecraft update, e.x. 1.13 -> 1.14
     *
     * @property major The major version. Always 1.
     * @property minor The minor version. Always 12 or higher.
     * @property versions List of all patches for this update.
     */
    class Update(
        val major: Int,
        val minor: Int,
        init: Update.() -> Unit,
    ) : Comparable<Update> {
        private val versions = mutableListOf<Version>()
        private var lock: Boolean = false

        init {
            init()
            lock = true
            allUpdates[toString()] = this
            allVersions.putAll(this.versions.associateBy { it.toString() })
        }

        internal fun version(patch: Int, protocol: Int): Version {
            if (lock) {
                throw IllegalStateException("Cannot add versions after initialization")
            }
            val version = Version(this, patch, protocol)
            versions.add(version)
            return version
        }

        /**
         * Returns true if the `server update == this update`.
         */
        fun isCurrent(): Boolean {
            return CURRENT.update == this
        }

        /**
         * Returns true if the `server update > this update`.
         */
        fun isOlder(): Boolean {
            return CURRENT.update < this
        }

        /**
         * Returns true if the `server update >= this update`.
         */
        fun isAtLeast(): Boolean {
            return CURRENT.update >= this
        }

        /**
         * Returns true if the `server update < this update`.
         */
        fun isBelow(): Boolean {
            return CURRENT.update < this
        }

        /**
         * Returns true if the `server update <= this update`.
         */
        fun isAtMost(): Boolean {
            return CURRENT.update <= this
        }

        operator fun get(patch: Int): Version = versions[patch]

        override fun compareTo(other: Update): Int {
            return when {
                major > other.major -> 1
                major < other.major -> -1
                minor > other.minor -> 1
                minor < other.minor -> -1
                else -> 0
            }
        }

        override fun toString(): String {
            return "$major.$minor"
        }
    }

    /**
     * Represents a patch for an [Update]. e.x. 1.16.4 -> 1.16.5
     *
     * @property update The parent update.
     * @property patch The patch number.
     * @property protocol The current protocol version (taken from R1, R2...)
     */
    data class Version(
        val update: Update,
        val patch: Int,
        val protocol: Int,
    ) : Comparable<Version> {
        val major = update.major
        val minor = update.minor

        /**
         * Returns true if the `server version == this version`.
         */
        fun isCurrent(): Boolean {
            return CURRENT == this
        }

        /**
         * Returns true if the `server version > this version`.
         */
        fun isOlder(): Boolean {
            return CURRENT < this
        }

        /**
         * Returns true if the `server version >= this version`.
         */
        fun isAtLeast(): Boolean {
            return CURRENT >= this
        }

        /**
         * Returns true if the `server version < this version`.
         */
        fun isBelow(): Boolean {
            return CURRENT < this
        }

        /**
         * Returns true if the `server version <= this version`.
         */
        fun isAtMost(): Boolean {
            return CURRENT <= this
        }

        /**
         * Returns the version in the protocol format, e.g. `v1_16_R3`.
         */
        fun toProtocolString(): String {
            return "v${major}_${minor}_R$protocol"
        }

        override fun compareTo(other: Version): Int {
            return when {
                major > other.major -> 1
                major < other.major -> -1
                minor > other.minor -> 1
                minor < other.minor -> -1
                patch > other.patch -> 1
                patch < other.patch -> -1
                else -> 0
            }
        }

        override fun toString(): String {
            return "$major.$minor.$patch"
        }
    }
}
