package com.nostalgia
package httpserver

import domain.AppError
import httpserver.model.{ErrorResponse, LoginResponse, UserDetailsRequest}

import cats.effect.Concurrent
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Response}

object SongRoutes {

  def songRoutes[F[_] : Concurrent](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def handleRequest(userDetailsRequest: UserDetailsRequest, maybeValidRequest: Either[AppError, Unit]): F[Response[F]] = {
      maybeValidRequest match {
        case Right(_) =>
          Ok(LoginResponse(userDetailsRequest.name).asJson)
        case Left(error) =>
          BadRequest(ErrorResponse(error.message).asJson)
      }
    }

    HttpRoutes.of[F] {
      case req @ POST -> Root / "login"  =>
        import cats.syntax.flatMap._
        import cats.syntax.functor._

        implicit val userDetailsDecoder: EntityDecoder[F, UserDetailsRequest] = jsonOf[F, UserDetailsRequest]

        for {
          userDetailsRequest: UserDetailsRequest <- req.as[UserDetailsRequest]
          validatedRequest <- Concurrent[F].pure(RequestValidator().validate(userDetailsRequest))
          response <- handleRequest(userDetailsRequest, validatedRequest)
        } yield response
    }
  }

}
