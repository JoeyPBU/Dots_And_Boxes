plugins {
    id("org.jetbrains.kotlin.jvm")
}

val kotlin_version: String by project
val junitJupiterVersion: String by project

dependencies {
    api(project(":lib"))
    testImplementation(project(":testlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junitJupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
//    testRuntime "org.junit.vintage:junit-vintage-engine:5.5.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

kotlin.target.compilations.all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
