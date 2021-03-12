/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 */
object Library {
    const val BINTRAY_REPO = ""
    const val BINTRAY_NAME = ""

    const val LIBRARY_NAME = ""
    const val GROUP_ID = "com.enchainte.sdk"
    const val ARTIFACT_NAME = "enchainte-sdk"
    const val VERSION = "0.1.0"

    const val DESCRIPTION = ""
    const val SITE_URL = ""
    const val GIT_URL = ""
    const val LICENSE = ""
}

object DependencyVersions {
    const val KOIN_VERSION = "3.0.1-beta-1"
    const val APACHE_COMMONS_VERSION = "1.14"
    const val KT_COROUTINES_RX_VERSION = "1.4.2"
    const val KTOR_VERSION = "1.5.0"
    const val WEB3_VERSION = "4.7.0"
    const val BLAKE2B_VERSION = "1.0.0"
    const val RXJAVA_VERSION = "3.0.9"

    const val JUNIT_VERSION = "5.7.0"
    const val MOCKITO = "2.2.0"
}

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("org.unbroken-dome.test-sets") version "3.0.1"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.jetbrains.dokka") version "1.4.20"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    signing
}

sourceSets {
    create("integrationTest") {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/integrationTest/kotlin")
            java.srcDir("src/integrationTest/java")
            resources.srcDir("src/integrationTest/resources")
            compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
            runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
        }
    }
}

task<Test>("integrationTest") {
    description = "Runs the integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    outputs.upToDateWhen { false }
    mustRunAfter(tasks["test"])
    useJUnitPlatform()
}

tasks.dokkaGfm.configure {
    outputDirectory.set(rootDir)
    moduleName.set("docs")
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Hashing
    implementation("com.rfksystems:blake2b:${DependencyVersions.BLAKE2B_VERSION}")

    // Utils
    implementation("io.insert-koin:koin-core:${DependencyVersions.KOIN_VERSION}")

    implementation("commons-codec:commons-codec:${DependencyVersions.APACHE_COMMONS_VERSION}")
    implementation("io.reactivex.rxjava3:rxjava:${DependencyVersions.RXJAVA_VERSION}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:${DependencyVersions.KT_COROUTINES_RX_VERSION}")

    // Networking
    implementation("io.ktor:ktor-client-core:${DependencyVersions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-cio:${DependencyVersions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-gson:${DependencyVersions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-logging:${DependencyVersions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-auth:${DependencyVersions.KTOR_VERSION}")

    // Blockchain
    implementation("org.web3j:core:${DependencyVersions.WEB3_VERSION}")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.insert-koin:koin-test:${DependencyVersions.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test-junit5:${DependencyVersions.KOIN_VERSION}")
    testImplementation("io.ktor:ktor-client-mock:${DependencyVersions.KTOR_VERSION}")
    testImplementation("org.web3j:web3j-unit:${DependencyVersions.WEB3_VERSION}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${DependencyVersions.JUNIT_VERSION}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${DependencyVersions.JUNIT_VERSION}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${DependencyVersions.JUNIT_VERSION}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${DependencyVersions.MOCKITO}")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}


val MAVEN_UPLOAD_USER: String by project
val MAVEN_UPLOAD_PWD: String by project
publishing {
    repositories {
        maven {
            name = "MavenCentral"
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = MAVEN_UPLOAD_USER
                password = MAVEN_UPLOAD_PWD
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("Enchainté SDK")
                description.set("Enchainté SDK for Java / Kotlin")
                url.set("https://www.enchainte.com")
                licenses {
                    license {
                        name.set("Licence Name")
                        url.set("http://link.to/full/license/text")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/enchainte/enchainte-sdk-kotlin.git")
                    developerConnection.set("scm:git:https://github.com/enchainte/enchainte-sdk-kotlin.git")
                    url.set("https://www.enchainte.com")
                }

            }
        }
    }
}

signing {
    val PGP_SIGNING_KEY: String? by project
    val PGP_SIGNING_PASSWORD: String? by project
    useInMemoryPgpKeys(PGP_SIGNING_KEY, PGP_SIGNING_PASSWORD)
    sign(publishing.publications["mavenJava"])
}