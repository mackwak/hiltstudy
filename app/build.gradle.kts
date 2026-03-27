import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // Kotlin Android plugin via version catalog
    alias(libs.plugins.kotlin.android)
    // Compose Kotlin plugin via version catalog
    alias(libs.plugins.kotlinCompose)
    // Kapt via version catalog
    alias(libs.plugins.kotlinKapt)
    // Hilt Gradle plugin
    alias(libs.plugins.hilt)
}


// 1. Initialize Properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}


android {
    namespace = "com.example.uppmanageapp1"
    compileSdk = 36

    buildFeatures {
        buildConfig = true // This MUST be true for variant constants to work
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("developmentExtra") {
            dimension = "environment"
            applicationIdSuffix = ".developmentextra"
            resValue("string", "app_name", "\"UppManage - Dev Extra\"")
            buildConfigField("String", "BASE_URL", "\"https://dev-extra.api.com\"")
        }
        // 추가된 플레이버: productionExtra (운영용 별도 앱)
        create("productionExtra") {
            dimension = "environment"
            applicationIdSuffix = ".productionextra"
            resValue("string", "app_name", "\"UppManage - Prod Extra\"")
            buildConfigField("String", "BASE_URL", "\"https://api-extra.com\"")
        }

    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    defaultConfig {
        applicationId = "com.example.uppmanageapp1"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Jetpack Compose dependencies
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}