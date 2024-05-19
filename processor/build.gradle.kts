import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

dependencies {
    implementation(libs.ksp.symbolProcessingApi)
    implementation(libs.kotlinPoet)
}

val dokkaHtml by tasks.getting(DokkaTask::class)

tasks.dokkaHtml.configure {
    outputDirectory.set(project.layout.buildDirectory.file("docs").get().asFile)
}

tasks.withType<DokkaTask> {
    dokkaSourceSets {
        configureEach {
            documentedVisibilities = setOf(Visibility.PUBLIC)
        }
    }
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

publishing {
    val properties = Properties().apply {
        val credentialFile = project.layout.projectDirectory.file("credentials.properties").asFile
        if (credentialFile.exists()) {
            credentialFile.inputStream().use { load(it) }
        }
    }
    val packagingUser = properties["gpr.user"] as? String ?: System.getenv("PACKAGING_USER")
    val packagingToken = properties["gpr.token"] as? String ?: System.getenv("PACKAGING_TOKEN")

    publications {
        register<MavenPublication>("gpr") {
            groupId = "com.crakac"
            artifactId = "parsableenum"
            version = System.getenv("RELEASE_VERSION") ?: "0.1.0-SNAPSHOT"
            from(components["kotlin"])
            artifact(tasks.kotlinSourcesJar)
            artifact(javadocJar)
        }
    }

    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/crakac/ParsableEnum")
            credentials {
                username = packagingUser
                password = packagingToken
            }
        }
    }
}