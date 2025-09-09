import com.dokar.quickjs.disableUnsupportedPlatformTasks

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm()

    applyDefaultHierarchyTemplate()

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":quickjs"))
                implementation(libs.moshi)
            }
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test.junit)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.dokar.quickjs.converter.moshi"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    ksp(libs.moshi.kotlin.codegen)
}

disableUnsupportedPlatformTasks()

afterEvaluate {
    // Disable Android tests
    tasks.named("testDebugUnitTest").configure {
        enabled = false
    }
    tasks.named("testReleaseUnitTest").configure {
        enabled = false
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/darriousliu/quickjs-kt")
            credentials(PasswordCredentials::class)
        }
    }
}
