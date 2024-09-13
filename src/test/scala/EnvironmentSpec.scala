package com.nostalgia

import cats.effect.unsafe.implicits.global
import com.comcast.ip4s.{Host, Port}
import com.nostalgia.EnvironmentSpec.{DefaultHost, DefaultPort, withEnvironmentSetup}
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Properties

class EnvironmentSpec extends AnyWordSpec with Matchers {

  "EnvironmentSpec.toEnvironment" should {
    "return the default environment" when {
      "host and port are both set" in {
        withEnvironmentSetup(DefaultHost, DefaultPort) {
          val Right(result) = Environment.toEnvironment
          result.host mustBe Host.fromString(DefaultHost).get
          result.port mustBe Port.fromString(DefaultPort).get
        }
      }
    }
    "return error" when {
      "host is not set" in {
        withEnvironmentSetup("", DefaultPort) {
          val Left(result: IllegalArgumentException) = Environment.toEnvironment
          result.getMessage mustBe "service host is missing or an invalid format, "
        }
      }

      "port is not set" in {
        withEnvironmentSetup(DefaultHost, "") {
          val Left(result: IllegalArgumentException) = Environment.toEnvironment
          result.getMessage mustBe "service http port not a number or missing, "
        }
      }

      "port is not a number" in {
        val invalidPort = "some port"
        withEnvironmentSetup(DefaultHost, invalidPort) {
          val Left(result: IllegalArgumentException) = Environment.toEnvironment
          result.getMessage mustBe s"service http port not a number or missing, $invalidPort"
        }
      }
    }
  }
}

object EnvironmentSpec {
  val DefaultHost: String = "0.0.0.0"
  val DefaultPort: String = "8080"

  def withEnvironmentSetup(host: String, port: String)
                          (testFn: => Assertion): Unit = {
    cleanup()

    System.setProperty(Environment.host, host)
    System.setProperty(Environment.port, port)

    println(s"Host: ${System.getProperty(Environment.host)}")
    println(s"Port: ${System.getProperty(Environment.port)}")

    testFn
  }

  private def cleanup() = {
    Properties.clearProp(Environment.host)
    Properties.clearProp(Environment.port)
    ()
  }

}
