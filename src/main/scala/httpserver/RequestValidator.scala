package com.nostalgia
package httpserver

import domain.{AppError, RequestValidationError}
import httpserver.model.UserDetailsRequest

trait RequestValidator {
  def validate(userDetailsRequest: UserDetailsRequest): Either[AppError, Unit]
}

object RequestValidator {

  def apply(): RequestValidator = new RequestValidator {

    override def validate(userDetailsRequest: UserDetailsRequest): Either[AppError, Unit] = {
        doesNameExist(userDetailsRequest)
    }

    private def doesNameExist(userDetailsRequest: UserDetailsRequest): Either[AppError, Unit] = {
        val doesNameExist = Option(userDetailsRequest.name).exists(_.nonEmpty)
        if (doesNameExist) {
          Right(())
        } else {
          Left(RequestValidationError(s"name is empty"))
        }
    }

  }

}
