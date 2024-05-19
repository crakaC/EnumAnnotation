import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
}

dependencies {
    implementation(libs.ksp.symbolProcessingApi)
    implementation(libs.kotlinPoet)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootProject.layout.buildDirectory.file("docs").get().asFile)
}

tasks.withType<DokkaTask> {
    dokkaSourceSets {
        configureEach {
            documentedVisibilities = setOf(Visibility.PUBLIC)
        }
    }
}