import org.gradle.api.tasks.Exec

plugins {
    `java-library`
}
// ./gradlew :scripts:validateRssFeeds
tasks.register<Exec>("validateRssFeeds") {
    group = "validation"
    description = "Validates RSS feeds and removes outdated ones."

    // Ensure the virtual environment and dependencies are set up
    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py
    """.trimIndent())

    workingDir = project.projectDir
}
