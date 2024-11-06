plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

android {
    namespace = "com.mdev.feature_scan"
    compileSdk = 34

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":client-openfoodfacts"))
    implementation(project(":client-firebase"))
    implementation(project(":common"))
    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.play.services.code.scanner)
    implementation(project(":client-openfoodfacts"))
    implementation(project(":client-openfoodfacts"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.barcode.scanning)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.9.0")

    // CameraX core library
    implementation("androidx.camera:camera-core:1.3.4")

    // CameraX camera2 library
    implementation("androidx.camera:camera-camera2:1.3.4")

    // CameraX lifecycle library for automatic lifecycle management
    implementation("androidx.camera:camera-lifecycle:1.3.4")

    // CameraX View to display the preview
    implementation("androidx.camera:camera-view:1.3.4")
}