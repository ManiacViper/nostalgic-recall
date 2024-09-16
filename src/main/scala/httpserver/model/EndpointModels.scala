package com.nostalgia
package httpserver.model

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class LoginResponse(name: String)
object LoginResponse {
  implicit val decoder: Decoder[LoginResponse] = deriveDecoder
}
final case class UserDetailsRequest(name: String)

final case class ErrorResponse(message: String)
object ErrorResponse {
  implicit val decoder: Decoder[ErrorResponse] = deriveDecoder
}
