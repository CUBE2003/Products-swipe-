plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")


}

android {
    namespace = "com.example.swipe"
    compileSdk = 34



    defaultConfig {
        applicationId = "com.example.swipe"
        minSdk = 30
        targetSdk = 34
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0")

    // retrofit
    implementation(libs.retrofit)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // for logging retrofit response
    implementation(libs.okhttp.client)
    implementation(libs.okhttp.logging.interceptor)

    implementation("androidx.databinding:databinding-runtime:4.3.1")


    // koin
    implementation("io.insert-koin:koin-android:3.4.0")
    implementation("io.insert-koin:koin-core:3.4.0")

    // coil
    implementation("io.coil-kt:coil-compose:2.6.0")



}