plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    
    // ================================
    // PLUGINS PROJET DEEZER
    // ================================
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.diiage.projet_rattrapage"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.diiage.projet_rattrapage"
        minSdk = 24  // Réduit pour une meilleure compatibilité
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        
        // Génération de BuildConfig
        buildConfigField("String", "VERSION_NAME", "\"1.0\"")
        buildConfigField("int", "VERSION_CODE", "1")
        buildConfigField("boolean", "DEBUG", "true")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "DEBUG", "false")
        }
        debug {
            buildConfigField("boolean", "DEBUG", "true")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
        buildConfig = true  // Active la génération de BuildConfig
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ================================
    // DEPENDENCIES CORE ANDROID
    // ================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // ================================
    // COMPOSE BOM ET UI (Material 3 uniquement)
    // ================================
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    
    
    // ================================
    // NAVIGATION ET ARCHITECTURE
    // ================================
    implementation(libs.bundles.navigation)
    
    // ================================
    // KOIN DEPENDENCY INJECTION
    // ================================
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    
    // ================================
    // RÉSEAU ET API
    // ================================
    implementation(libs.bundles.networking)
    
    // ================================
    // COROUTINES
    // ================================
    implementation(libs.kotlinx.coroutines.android)
    
    // ================================
    // LOGGING
    // ================================
    implementation(libs.timber)
    
    // ================================
    // IMAGES
    // ================================
    implementation(libs.coil.compose)
    
    // ================================
    // TESTS UNITAIRES
    // ================================
    testImplementation(libs.bundles.testing)
    
    // ================================
    // TESTS ANDROID
    // ================================
    androidTestImplementation(libs.bundles.android.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    
    // ================================
    // DEBUG TOOLS
    // ================================
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}