import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.moa.salary.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.moa.salary.app"
        minSdk = 26
        targetSdk = 36
        versionCode = property("APP_VERSION_CODE").toString().toInt()
        versionName = property("APP_VERSION_NAME").toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${localProperties.getProperty("KAKAO_NATIVE_APP_KEY")}\""
        )
        buildConfigField(
            "String",
            "POSTHOG_KEY",
            "\"${localProperties.getProperty("POSTHOG_KEY")}\""
        )
        buildConfigField(
            "String",
            "POSTHOG_URL",
            "\"${localProperties.getProperty("POSTHOG_URL")}\""
        )

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] =
            localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "Moa-debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "Moa")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":data"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Posthog
    implementation(libs.posthog)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    // Kakao
    implementation(libs.kakao.user)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}