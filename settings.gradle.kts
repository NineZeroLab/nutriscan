pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "nutriscan"
include(":app")
include(":core")
include(":client-openfoodfacts")
include(":client-firebase")
include(":feature-register")
include(":common")
include(":feature-login")
include(":feature-home")
include(":feature-profile")
include(":feature-product-details")
include(":feature-analytics")
include(":feature-scan")
include(":feature-history")
