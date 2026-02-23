plugins {
    kotlin("jvm") version "2.3.0"
}

group = "fr.belic.drift"
version = "2026.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("fr.belic.drift:drift-hir:2026.0")

    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}