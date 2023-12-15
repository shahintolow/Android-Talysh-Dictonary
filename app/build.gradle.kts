
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.com.google.service)
    alias(libs.plugins.com.crash)
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.com.talyshdictionary"
        minSdk = 19
        targetSdk = 34
        versionCode = 11
        versionName = "1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles (getDefaultProguardFile ("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }

    namespace  = "com.com.talyshdictionary"

}

dependencies {

    implementation (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation (libs.core.ktx)
    implementation (libs.androidx.appcompat.v110)
    implementation (libs.androidx.core.ktx.v131)
    implementation (libs.androidx.constraintlayout.v113)
    implementation (libs.material.v110)
    implementation (libs.flexbox)
    implementation (libs.androidx.room.runtime)
    implementation (libs.firebase.firestore.ktx.v2150)
    implementation (libs.androidx.multidex)
    implementation (libs.billing.ktx)
    implementation (libs.play.services.auth)
    ksp(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    testImplementation (libs.junit.v412)
    androidTestImplementation (libs.androidx.junit.v111)
    androidTestImplementation (libs.androidx.espresso.core.v320)

}
