plugins {
    id("dev.kikugie.stonecutter")
    kotlin("jvm") version "2.0.0" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") apply false
    id("systems.manifold.manifold-gradle-plugin") version "0.0.2-alpha" apply false
}

stonecutter active "1.21.1-neoforge" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledRunClient", stonecutter.chiseled) {
    group = "project"
    ofTask("runClient")
}

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledBuildAndCollect", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}

stonecutter.automaticPlatformConstants = true
