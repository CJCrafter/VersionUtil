package com.cjcrafter.version

/**
 * Utility class to determine the fork type of the server.
 */
object ServerVersions {

    /**
     * Returns true if the server is a Paper server.
     */
    val isPaper: Boolean by lazy {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    /**
     * Returns true if the server is a Folia server.
     */
    val isFolia: Boolean by lazy {
        try {
            Class.forName("io.papermc.paper.threadedregion.RegionizedServer")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}