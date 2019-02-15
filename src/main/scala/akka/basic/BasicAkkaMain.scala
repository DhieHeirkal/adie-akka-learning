package akka.basic

/**
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-scala
 * 
 * runMain akka.basic.BasicAkkaMainSc
 */
object BasicAkkaMainSc {
  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[HelloBasicAkkaSc].getName))
  }
}