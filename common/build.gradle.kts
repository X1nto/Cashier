plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.xinto.cashier.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 17

        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BACKEND_URL", """"http://10.0.2.2:8080"""")
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.room.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    api(libs.koin.android)
    ksp(libs.room.compiler)
    testImplementation(libs.kotlin.test)
}