package com.nostalgia
package domain

sealed trait AppError {
  val message: String
}
sealed trait FatalError extends AppError
sealed trait NonFatalError extends AppError

final case class RequestValidationError(message: String) extends FatalError
