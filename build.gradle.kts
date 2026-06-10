import org.gradle.kotlin.dsl.implementation

plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "2.25.0"
    kotlin("jvm")
}

group = "it.unicam.cs.mpgc.rpg125950"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("it.unicam.cs.mpgc.rpg125950.lupusintabula")
    mainClass.set("it.unicam.cs.mpgc.rpg125950.lupusintabula.Main")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics",
        )
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    // H2 database
    implementation("com.h2database:h2:2.2.224")

    // JavaFX
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    //JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
