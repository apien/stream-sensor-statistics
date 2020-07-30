# stream-sensor-statistics

## Getting started

Application calculate statistics based on all csv files from the given directory.

It does not include invalid rows (it does not contain two columns) to the calculations.
It treats a measurement out of range `<0,100>` like `NaN`.   

### Prerequisites

* Scala SBT [here](https://www.scala-sbt.org/)
* OpenJdk 1.8


##### Run  
Run prepared `run.sh` which takes single argument - a directory path with csv files  i.e: 
```
cd /bin/dev
./run.sh /home/john.doe/csv_reports
```

Other option is to run application directly through sbt. 

## Application configuration

|env variable|system property|description|default value|example value|
|---|---|---|---|---|
| SENSOR_STATISTICS_PARALLELISM | parallelism | Parallelism level during processing of a files | 2 | 99 |

### Example

#### Content of input files

leader-1.csv
```
sensor-id,humidity
s1,10
s2,88
s1,NaN
```

leader-2.csv
```
sensor-id,humidity
s2,80
s3,NaN
s2,78
s1,98
```

#### Output

```
Num of processed files: 2
Num of processed measurements: 7
Num of failed measurements: 2

Sensors with highest avg humidity:

sensor-id,min,avg,max
s2,78,82,88
s1,10,54,98
s3,NaN,NaN,NaN