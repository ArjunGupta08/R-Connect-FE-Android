plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "rconnect.retvens.technologies"
    compileSdk = 33

    defaultConfig {
        applicationId = "rconnect.retvens.technologies"
        minSdk = 23
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding =  true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-graphics-android:1.5.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("com.google.android.libraries.places:places:3.2.0")

    implementation("com.google.android.gms:play-services-maps:16.1.0")
    implementation("com.google.android.gms:play-services-location:10.+")

    //Retrofit
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson
    implementation ("com.squareup.retrofit2:converter-gson:2.1.0")

    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.github.Spikeysanju:MotionToast:1.4")

    //ImageCropper
//    implementation "com.theartofdev.edmodo:android-image-cropper:2.8.0"
//    implementation ("com.github.yalantis:ucrop:2.2.8")
//    implementation ("com.github.yalantis:ucrop:2.2.8-native")
}