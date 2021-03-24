# jreng
Reverse engineering tool for Java

Jreng uses the javaparser library to extract classes, methods, annotations, and their dependencies into a spreadsheet

# Installation 
* This only works on Linux
* Install JDK 8+ and Maven
* Run `./compile.sh`

# Usage
`./run.sh <path to pom.xml/build.gradle> <output file> [-c]`

* `path to pom.xml/build.gradle` : Relative or absolute path of the project to analyze. Must be the path of the pom.xml (if it is a Maven project) or the build.gradle (if it is a Gradle project)
* `output file`: Path of an `.xlsx` file that will store the results
* `-c` (optional): Force clean and install of the project to analyze
