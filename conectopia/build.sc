import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object conectopia extends PlayModule with SingleModule {

  def scalaVersion = "2.13.13"
  def playVersion = "3.0.1"
  def twirlVersion = "2.0.1"

  object test extends PlayTests
}
