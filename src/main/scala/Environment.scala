package com.nostalgia

import com.comcast.ip4s.{Host, Port}

final case class Environment(host: Host, port: Port)
object Environment {

  val host = "HOST" // value of 0.0.0.0
  val port = "PORT"

  def toEnvironment: Either[Throwable, Environment] = {
    val maybeHost = Option(System.getProperty(host))
    val maybePort = Option(System.getProperty(port))
    parse(maybeHost, maybePort)
  }

  private def parse(maybeHost: Option[String],
                    maybeHttpPort: Option[String]): Either[Throwable, Environment] =
    for {
      maybeHost: Host <- maybeHost.flatMap(Host.fromString).toRight(new IllegalArgumentException(s"service host is missing or an invalid format, ${maybeHost.getOrElse("")}"))
      portError = new IllegalArgumentException(s"service http port not a number or missing, ${maybeHttpPort.getOrElse("")}")
      portStr  <- maybeHttpPort.toRight(portError)
      portInt  <- portStr.toIntOption.toRight(portError)
      maybePort <- Port.fromInt(portInt).toRight(portError)
    } yield Environment(maybeHost, maybePort)
}