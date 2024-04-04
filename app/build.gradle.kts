import org.jetbrains.kotlin.tooling.core.closure

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    jacoco
}


android {
    namespace = "com.example.sawitprotest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sawitprotest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isTestCoverageEnabled
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(platform("com.google.firebase:firebase-bom:29.0.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation(libs.core.ktx)



    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("io.mockk:mockk:1.13.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.robolectric:robolectric:4.7.3")


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required = true
        html.required = true
    }

    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.run{
        setFrom(
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/domain"),
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/data"),
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/base/extensions/Extensions.kt"),
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/base/base_entity"),
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/base/base_classes"),
            files("build/tmp/kotlin-classes/debug/com/example/sawitprotest/feature/weighbridge/viewmodel"),
        )
    }
    executionData.setFrom(files("build/jacoco/testDebugUnitTest.exec"))
}