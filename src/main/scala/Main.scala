package com.nostalgia

import cats.effect.{ExitCode, IO, IOApp}
import com.nostalgia.httpserver.SongServer
import org.http4s.server.Server
import org.log4s.getLogger

object Main extends IOApp {

  private[this] val log = getLogger

  def run(args: List[String]): IO[ExitCode] = {
    Environment.toEnvironment match {
      case Right(environment) =>
        SongServer
          .setup(environment)
          .use { server: Server =>
            log.info(s"App is up ${server.baseUri}")
            IO.never
          }
      case Left(throwable: Throwable) =>
        log.error(throwable)(s"Problem with starting up the app")
        IO.raiseError(throwable)
    }
  }
}
