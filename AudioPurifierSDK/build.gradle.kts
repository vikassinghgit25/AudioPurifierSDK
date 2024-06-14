plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.audiopurifier.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")  // Add consumer-rules.pro
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))  // Set Java toolchain to use version 17
    }
    sourceCompatibility = JavaVersion.VERSION_17  // Ensure source compatibility with Java 17
    targetCompatibility = JavaVersion.VERSION_17  // Ensure target compatibility with Java 17
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.vikassinghgit25"
            artifactId = "AudioPurifierSDK"
            version = "2.0"
            pom {
                description.set("This library is for audio filter")
            }
        }
    }
    repositories {
        mavenLocal()  // Publish to local Maven repository
    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.arthenica:mobile-ffmpeg-full:4.4")
}

