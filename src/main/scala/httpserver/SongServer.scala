package com.nostalgia
package httpserver

import cats.effect.{IO, Resource}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.middleware.Logger

object SongServer {

  def setup(environment: Environment): Resource[IO, Server] = {
    val httpApp = SongRoutes.songRoutes[IO]().orNotFound
    val withLogger = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

    EmberServerBuilder
      .default[IO]
      .withHost(environment.host)
      .withPort(environment.port)
      .withHttpApp(withLogger)
      .build
  }
}
