import org.gradle.api.tasks.Exec

plugins {
    `java-library`
}

// Helper function to get property or default value
fun getPropertyOrDefault(propertyName: String, defaultValue: String): String {
    return if (project.hasProperty(propertyName)) {
        project.property(propertyName).toString()
    } else {
        defaultValue
    }
}

// Helper function to setup Python environment
fun createPythonTask(taskName: String, description: String, vararg args: String): TaskProvider<Exec> {
    return tasks.register<Exec>(taskName) {
        group = "rss"
        this.description = description

        val pythonArgs = listOf(
            "sh", "-c", """
                if [ ! -d "venv" ]; then
                    python3 -m venv venv
                fi
                source venv/bin/activate
                pip3 install -r requirements.txt
                python3 validate_rss.py ${args.joinToString(" ")}
            """.trimIndent()
        )

        commandLine(pythonArgs)
        workingDir = project.projectDir
    }
}

// 1. Enhanced existing validation task
tasks.register<Exec>("validateRssFeeds") {
    group = "rss"
    description = "Validates existing RSS feeds and generates reports"

    val workers = getPropertyOrDefault("workers", "10")
    val timeout = getPropertyOrDefault("timeout", "15")
    val days = getPropertyOrDefault("days", "7")

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py --workers $workers --timeout $timeout --days $days
    """.trimIndent())

    workingDir = project.projectDir
}

// 2. Discover new RSS feeds for specific countries
tasks.register<Exec>("discoverRssFeeds") {
    group = "rss"
    description = "Discover new RSS feeds for specified countries"

    doFirst {
        val countries = getPropertyOrDefault("countries", "US,GB,CA")
        val categories = getPropertyOrDefault("categories", "news,sports")

        println("Discovering RSS feeds for countries: $countries")
        println("Categories: $categories")

        val countriesList = countries.split(",").joinToString(" ")
        val categoriesList = categories.split(",").joinToString(" ")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --discover $countriesList --categories $categoriesList
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// 3. Discover feeds for all supported countries
tasks.register<Exec>("discoverAllRssFeeds") {
    group = "rss"
    description = "Discover RSS feeds for all supported countries"

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py --discover-all
    """.trimIndent())

    workingDir = project.projectDir
}

// 4. Discover and automatically add to Kotlin file
tasks.register<Exec>("discoverAndAddFeeds") {
    group = "rss"
    description = "Discover new RSS feeds and automatically add them to RssData.kt"

    doFirst {
        val countries = getPropertyOrDefault("countries", "US,GB,CA")
        val categories = getPropertyOrDefault("categories", "news,sports")

        println("Discovering and adding RSS feeds for countries: $countries")
        println("Categories: $categories")

        val countriesList = countries.split(",").joinToString(" ")
        val categoriesList = categories.split(",").joinToString(" ")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --discover $countriesList --categories $categoriesList --add-to-file
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// 5. Remove problematic feeds
tasks.register<Exec>("cleanupRssFeeds") {
    group = "rss"
    description = "Remove problematic RSS feeds from RssData.kt"

    doFirst {
        val removeMode = getPropertyOrDefault("remove", "moderate")

        println("Cleaning up RSS feeds with '$removeMode' criteria")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --remove $removeMode
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// 6. Validate existing feeds and discover new ones
tasks.register<Exec>("validateAndDiscover") {
    group = "rss"
    description = "Validate existing feeds, remove problematic ones, and discover new feeds"

    doFirst {
        val countries = getPropertyOrDefault("countries", "US,GB,CA")
        val removeMode = getPropertyOrDefault("remove", "moderate")
        val categories = getPropertyOrDefault("categories", "news,sports")

        println("Validating feeds with '$removeMode' cleanup")
        println("Discovering new feeds for countries: $countries")

        val countriesList = countries.split(",").joinToString(" ")
        val categoriesList = categories.split(",").joinToString(" ")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --remove $removeMode --discover $countriesList --categories $categoriesList --add-to-file
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// 7. Generate Kotlin code files for discovered feeds
tasks.register<Exec>("generateKotlinFeeds") {
    group = "rss"
    description = "Discover feeds and generate separate Kotlin code files"

    doFirst {
        val countries = getPropertyOrDefault("countries", "US,GB,CA")
        val categories = getPropertyOrDefault("categories", "news,sports")

        println("Generating Kotlin code for countries: $countries")

        val countriesList = countries.split(",").joinToString(" ")
        val categoriesList = categories.split(",").joinToString(" ")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --discover $countriesList --categories $categoriesList --generate-kotlin
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// 8. Quick discovery for major countries
tasks.register<Exec>("discoverMajorCountries") {
    group = "rss"
    description = "Discover and add feeds for major English-speaking countries"

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py --discover US GB CA AU --categories news sports --add-to-file
    """.trimIndent())

    workingDir = project.projectDir
}

// 9. European countries discovery
tasks.register<Exec>("discoverEuropeanFeeds") {
    group = "rss"
    description = "Discover and add feeds for European countries"

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py --discover GB DE FR IT ES NL SE NO DK FI --categories news sports --add-to-file
    """.trimIndent())

    workingDir = project.projectDir
}

// 10. Asian countries discovery
tasks.register<Exec>("discoverAsianFeeds") {
    group = "rss"
    description = "Discover and add feeds for Asian countries"

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        python3 validate_rss.py --discover IN JP CN --categories news sports --add-to-file
    """.trimIndent())

    workingDir = project.projectDir
}

// 11. Full maintenance task
tasks.register<Exec>("fullMaintenanceRss") {
    group = "rss"
    description = "Complete RSS maintenance: validate, cleanup, and discover new feeds"

    commandLine("sh", "-c", """
        if [ ! -d "venv" ]; then
            python3 -m venv venv
        fi
        source venv/bin/activate
        pip3 install -r requirements.txt
        
        echo "🔍 Step 1: Validating existing feeds..."
        python3 validate_rss.py --remove moderate
        
        echo "🌍 Step 2: Discovering new feeds for major countries..."
        python3 validate_rss.py --discover US GB CA AU DE FR --categories news sports --add-to-file
        
        echo "✅ RSS maintenance completed!"
    """.trimIndent())

    workingDir = project.projectDir
}

// 12. Fast discovery (limited scope)
tasks.register<Exec>("fastDiscovery") {
    group = "rss"
    description = "Quick discovery with limited scope for testing"

    doFirst {
        val countries = getPropertyOrDefault("countries", "US,GB")
        val countriesList = countries.split(",").joinToString(" ")

        commandLine("sh", "-c", """
            if [ ! -d "venv" ]; then
                python3 -m venv venv
            fi
            source venv/bin/activate
            pip3 install -r requirements.txt
            python3 validate_rss.py --discover $countriesList --categories news --workers 5 --timeout 8
        """.trimIndent())
    }

    workingDir = project.projectDir
}

// Help task to show all available commands
tasks.register("rssHelp") {
    group = "rss"
    description = "Show all available RSS tasks and their usage"

    doLast {
        println("""
        
🔧 RSS Feed Management Tasks:

📋 VALIDATION & CLEANUP:
  validateRssFeeds          - Validate existing RSS feeds
                             Usage: ./gradlew :scripts:validateRssFeeds -Pworkers=10 -Ptimeout=15 -Pdays=7
                             
  cleanupRssFeeds          - Remove problematic feeds  
                             Usage: ./gradlew :scripts:cleanupRssFeeds -Premove=moderate
                             Options: strict, moderate, loose

🔍 DISCOVERY:
  discoverRssFeeds         - Discover new feeds for specific countries
                             Usage: ./gradlew :scripts:discoverRssFeeds -Pcountries=US,GB,CA -Pcategories=news,sports
                             
  discoverAllRssFeeds      - Discover feeds for all supported countries
                             Usage: ./gradlew :scripts:discoverAllRssFeeds
                             
  discoverAndAddFeeds      - Discover and automatically add to RssData.kt
                             Usage: ./gradlew :scripts:discoverAndAddFeeds -Pcountries=US,GB,CA

🚀 QUICK TASKS:
  discoverMajorCountries   - Discover feeds for US, GB, CA, AU
  discoverEuropeanFeeds    - Discover feeds for European countries  
  discoverAsianFeeds       - Discover feeds for IN, JP, CN
  
🔄 COMBINED OPERATIONS:
  validateAndDiscover      - Validate, cleanup, and discover new feeds
                             Usage: ./gradlew :scripts:validateAndDiscover -Pcountries=US,GB -Premove=moderate
                             
  fullMaintenanceRss       - Complete maintenance workflow
  
📝 UTILITIES:
  generateKotlinFeeds      - Generate separate Kotlin code files
  fastDiscovery            - Quick discovery for testing

💡 EXAMPLES:
  ./gradlew :scripts:validateRssFeeds
  ./gradlew :scripts:discoverAndAddFeeds -Pcountries=US,GB,CA,AU
  ./gradlew :scripts:cleanupRssFeeds -Premove=strict  
  ./gradlew :scripts:validateAndDiscover -Pcountries=US,GB -Premove=moderate
  ./gradlew :scripts:fullMaintenanceRss

        """.trimIndent())
    }
}
