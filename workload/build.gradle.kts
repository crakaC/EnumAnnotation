plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

dependencies {
    testImplementation(libs.junit)
    implementation(projects.processor)
    ksp(projects.processor)
}