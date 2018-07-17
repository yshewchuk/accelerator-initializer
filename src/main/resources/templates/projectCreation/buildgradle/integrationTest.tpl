

facets {
    acceptanceTest {
        parentSourceSet = 'test'
        testTaskName = 'acceptanceTest'
        includeInCheckLifecycle = false
    }
}

sourceSets {
    acceptanceTest {
        resources {
            srcDirs 'src/acceptanceTest/resources'
        }
        java {
            srcDirs 'src/acceptanceTest/java'
        }
    }
}

acceptanceTest {
    jacoco {
        destinationFile = file("$buildDir/jacoco/acceptanceTest.exec")
        classDumpDir = file("$buildDir/classes/acceptanceTest")
    }

}

//should be in shared library
cleanAcceptanceTest {
    delete reportsDir
}