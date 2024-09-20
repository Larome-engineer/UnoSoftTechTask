plugins {
    id("java")
}

group = "unosoft"
version = "1"

tasks.jar {
    manifest {
        attributes("Main-Class" to "UniqueGroupMain")
    }
}