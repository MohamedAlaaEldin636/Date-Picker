@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

group = "com.github.MohamedAlaaEldin636"
version = "1.4.0"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.MohamedAlaaEldin636"
            artifactId = "Date-Picker"
            version = "1.4.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

android {
    namespace = "ma.ya.datepicker.core"

    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 33
        }

        resourceConfigurations.add("en")
        resourceConfigurations.add("ar")
    }

    //https://developer.android.com/build/publish-library/configure-pub-variants
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        //compose = true
    }

    /*
    https://developer.android.com/build/publish-library/prep-lib-release

    testFixtures {
        enable = true
    }
     */
}

dependencies {
    // Used for java.time
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")

    // Androidx
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.7")

    // UI ( Material design guidelines )
    implementation("com.google.android.material:material:1.9.0")

    // Gson ( Json Serialization & Deserialization )
    //implementation("com.google.code.gson:gson:2.10.1")

    // Google Services
    //implementation("com.google.android.gms:play-services-location:21.0.1")

    // ---- Testing ---- //

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
