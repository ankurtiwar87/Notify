pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add the following
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Notify"
include(":app")
 