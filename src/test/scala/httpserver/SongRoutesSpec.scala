package com.nostalgia
package httpserver

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.nostalgia.httpserver.SongRoutesSpec.runLoginRoute
import com.nostalgia.httpserver.model.{ErrorResponse, LoginResponse, UserDetailsRequest}
import io.circe.generic.auto.exportEncoder
import io.circe.syntax.EncoderOps
import org.http4s.circe.{jsonEncoder, jsonOf}
import org.http4s.{EntityDecoder, HttpRoutes, MediaType, Method, Request, Response, Status}
import org.http4s.implicits.http4sLiteralsSyntax
import org.scalatest.Assertion
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import SongRoutesSpec._
import org.http4s.headers.`Content-Type`
import io.circe.parser._

class SongRoutesSpec extends AnyWordSpec with Matchers {

  implicit val ioRuntime: IORuntime = IORuntime.global

  "SongRoutes.login" should {
    "return Ok" when {
      "name is present" in {
        val requestBody = UserDetailsRequest(name = "kenneth")
        runLoginRoute(requestBody, SongRoutes.songRoutes[IO]()){ response: Response[IO] =>
          val responseBody = response.as[LoginResponse].unsafeRunSync().asJson
          val Right(expectedBody) = parse("""
                                     |{
                                     | "name": "kenneth"
                                     |}
                                     |""".stripMargin)
          response.status mustBe Status.Ok
          responseBody mustBe expectedBody
        }

      }
    }

    "return BadRequest" when {
      "there is no name" in {
        val emptyName = UserDetailsRequest(name = "")
        runLoginRoute(emptyName, SongRoutes.songRoutes[IO]()){ response: Response[IO] =>
          val responseBody = response.as[ErrorResponse].unsafeRunSync().asJson
          val Right(expectedBody) = parse("""
                                            |{
                                            | "message": "name is empty"
                                            |}
                                            |""".stripMargin)
          response.status mustBe Status.BadRequest
          responseBody mustBe expectedBody
        }
      }
    }
  }

}

object SongRoutesSpec {
  implicit val userDetailsDecoder: EntityDecoder[IO, LoginResponse] = jsonOf[IO, LoginResponse]
  implicit val errorResponseDecoder: EntityDecoder[IO, ErrorResponse] = jsonOf[IO, ErrorResponse]

  def runLoginRoute(request: UserDetailsRequest, routes: HttpRoutes[IO])(testFn: Response[IO] => Assertion)(implicit ioRuntime: IORuntime): Assertion = {

    routes
      .orNotFound
      .run(
        Request[IO](method = Method.POST)
          .withUri(uri"/login")
          .withEntity(request.asJson)
          .withContentType(`Content-Type`(MediaType.application.json))
      )
      .map(testFn)
      .unsafeRunSync()
  }
}
