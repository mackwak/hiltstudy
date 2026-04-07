import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.hilt)
    id("com.google.gms.google-services")
}


// 1. Initialize Properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

// Add reading local Shopify token for BuildConfig
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val shopifyTokenFromLocal: String = localProps.getProperty("SHOPIFY_ACCESS_TOKEN", "")

// --- Gradle project 속성에서 읽기 (안전하게 toString 사용) ---
val shopifyTokenFromProject: String? = project.findProperty("SHOPIFY_ACCESS_TOKEN")?.toString()

// --- 우선순위: project 속성 > local.properties > 빈 문자열 ---
val shopifyTokenFinal: String = shopifyTokenFromProject?.takeIf { it.isNotBlank() } ?: shopifyTokenFromLocal

android {
    namespace = "com.example.uppmanageapp1"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
        viewBinding = true// This MUST be true for variant constants to work
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("developmentExtra") {
            dimension = "environment"
            applicationIdSuffix = ".developmentextra"
            resValue("string", "app_name", "\"Dev Extra\"")
            buildConfigField("String", "BASE_URL", "\"https://sdaz1b-r2.myshopify.com/\"")
        }
        // 추가된 플레이버: productionExtra (운영용 별도 앱)
        create("productionExtra") {
            dimension = "environment"
            applicationIdSuffix = ".productionextra"
            resValue("string", "app_name", "\"Prod Extra\"")
            buildConfigField("String", "BASE_URL", "\"https://sdaz1b-r2.myshopify.com/\"")
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

    val injectedVersionCode = project.findProperty("android.injected.version.code")?.toString()?.toInt() ?: 2
    defaultConfig {
        applicationId = "com.example.uppmanageapp1"
        minSdk = 25
        targetSdk = 36
        versionCode = injectedVersionCode
        versionName = "1.0"

        buildConfigField("String", "SHOPIFY_ACCESS_TOKEN", "\"${shopifyTokenFinal}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    sourceSets {
        // test 소스셋에 kotlin 폴더를 포함
        getByName("test") {
            java.srcDirs("src/test/java", "src/test/kotlin")
        }
        // androidTest 도 필요한 경우 동일하게 추가
        getByName("androidTest") {
            java.srcDirs("src/androidTest/java", "src/androidTest/kotlin")
        }
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
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    val composeBom = platform("androidx.compose:compose-bom:2025.02.00") // Use a recent BOM version
    implementation(composeBom)
    androidTestImplementation(composeBom)

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
    // Retrofit & Moshi for Shopify API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    testImplementation(libs.mockito.kotlin) // Optional but recommended for Kotlin
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Retrofit & Moshi for Shopify API
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
}