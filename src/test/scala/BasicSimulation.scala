import java.util.Random

import de.m3y.gatling.knox.KnoxProtocol
import de.m3y.gatling.knox.Predef._
import io.gatling.core.Predef._
import org.apache.knox.gateway.shell.hdfs.Hdfs
import org.apache.knox.gateway.shell.{Hadoop, HadoopException}

import scala.concurrent.duration._

class BasicSimulation extends Simulation {
  val knoxConf = KnoxProtocol("https://localhost/knox-gateway", "some user", "some pwd")
  val random = new Random()


  // Cleanup and initialize a tmp. HDFS base directory
  val hdfsBenchmarkDir = "/tmp/benchmark-gatling"
  try {
    if (Hdfs.status(knoxConf.hadoop).file(hdfsBenchmarkDir).now().exists()) {
      Hdfs.rm(knoxConf.hadoop).file(hdfsBenchmarkDir).recursive().now()
    }
  } catch {
    case _: HadoopException => println("No cleanup, base dir does not exist")
  }
  Hdfs.mkdir(knoxConf.hadoop).dir(hdfsBenchmarkDir).now()


  // Scenario
  val scn = scenario("Knox Test")
    .exec(
      knox("upload 100KiB file") { hadoop: Hadoop =>
        Hdfs.put(hadoop)
          .file("src/test/resources/data/100KiB.blob")
          .to(hdfsBenchmarkDir + "/100KiB-" + random.nextInt(Integer.MAX_VALUE) + ".blob")
      })

  setUp(
    scn
      .inject(constantUsersPerSec(10) during (60 seconds)))
    .protocols(knoxConf)
}
