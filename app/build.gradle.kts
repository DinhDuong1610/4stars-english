plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.fourstars.fourstars_english"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fourstars.fourstars_english"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
//    implementation(libs.androidx.navigation.compose)
//    implementation(libs.androidx.storage)

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.espresso.core)
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.firebase:firebase-firestore:24.9.1")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
//    implementation(libs.firebase.crashlytics.buildtools)
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Retrofit API
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Youtube
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

    // Room components
    implementation ("androidx.room:room-runtime:2.6.1")
//    kapt("androidx.room:room-compiler:2.6.1")

    // Kotlin Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.1")

    // UI Support
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // JSON
    implementation ("com.google.code.gson:gson:2.10.1")

    // Coli
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-gif:2.2.2")

    // Flow Row
    implementation("com.google.accompanist:accompanist-flowlayout:0.31.6-rc")
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.room.runtime.android)

    //Image Upload
    implementation("com.cloudinary:cloudinary-android:3.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")


    debugImplementation("androidx.compose.ui:ui-tooling:1.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}