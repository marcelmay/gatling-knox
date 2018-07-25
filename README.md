GatlingKnox
===========

Gatling DSL support for Apache Knox

Features
--------

GatlingKnox provides a DSL for load testing Apache Knox via [Knox Shell](https://cwiki.apache.org/confluence/display/KNOX/Client+Usage).

Here is a simple example:
```
class de.m3y.gatling.knox.BasicSimulation extends Simulation {
  val knoxConf = KnoxProtocol("https://localhost/gateway/default", "some user", "some pwd")
  val random = new Random()

  // Cleanup and initialize a tmp. HDFS base directory
  val hdfsBenchmarkDir = "/tmp/benchmark-gatling"
  ...

  // Define load test scenario
  val scn = scenario("Knox Load Test")
    .exec(
      knox("upload 100KiB file") { hadoop: Hadoop =>
        // Use any Knox Shell command ...
        Hdfs.put(hadoop)
          .file("src/test/resources/data/100KiB.blob")
          .to(hdfsBenchmarkDir + "/100KiB-" + random.nextInt(Integer.MAX_VALUE) + ".blob")
      })

  setUp( scn.inject( constantUsersPerSec(10) during (60 seconds) ) )
    .protocols(knoxConf)
}
```

## Building

```mvn clean install```

Requires Maven 3.5.x for building.

## License

This software is released under the Apache License 2.0, see [LICENSE](https://github.com/marcelmay/gatling-knox/blob/master/LICENSE).

```
Copyright 2018 Marcel May

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
