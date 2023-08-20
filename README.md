----
# Enrich Melbourne Pedestrian Data Using APACHE BEAM

Task
----
This is a timeboxed task that requires you to develop a set of datapipelines / tasks that process pedestrian data form the city of melbourne.
This task will require you to:

1. Create a beam that loads an external street data file (from the pedestrian-counting-system-monthly-counts-per-hour.json)
2. Read each record and enrich the message with the true location names (from the pedestrian-counting-system-sensor-locations.json file)
3. Finally write the enriched data into a new file
4. Have at least one unit test
5. Instructions on how to build you pipelines
---
Main Components
---
1. source-data
   * pedestrian.zip -- data source files
2. output-data -- target directory for the output file
3. src
   * main: _source code_
   * test: _unit-test code_

---
## Build Instructions

---
### For Location Direct Run

Please unzip the source-data/pedestrian.zip before executing the following steps.
#### 1. Gradle
Prerequisite installing gradle [here](https://gradle.org/install/)

Then in terminal, under the project directory, run
```bash
#example :
gradle build
gradle clean execute -DmainClass=melpedestrian.PedestrianDataProcessor
```
#### 2. Maven
Then in terminal, under the project directory, run
```bash
#example :
mvn compile exec:java -Dexec.mainClass=melpedestrian.PedestrianDataProcessor -Pdirect-runner
```
### For Running on A Spark Cluster
Prerequisite require spark version 3.2 and above [here](https://spark.apache.org/downloads.html)
#### Maven
```bash
#example :
mvn package
spark-submit --class melpedestrian.PedestrianDataProcessor target/melbourn-pedestrian-bundled-0.1.jar --runner=SparkRunner
```





