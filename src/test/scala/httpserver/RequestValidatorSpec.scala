package com.nostalgia
package httpserver

import com.nostalgia.domain.RequestValidationError
import com.nostalgia.httpserver.model.UserDetailsRequest
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RequestValidatorSpec extends AnyWordSpec with Matchers {

  "RequestValidator.validate" should {
    "return success" when {
      "name is not empty in request" in {
        val validator = RequestValidator()
        val Right(result) = validator.validate(UserDetailsRequest(name = "john"))
        result mustBe ()
      }
    }

    "return failure" when {
      "name is empty" in {
        val validator = RequestValidator()
        val emptyName = ""
        val Left(result) = validator.validate(UserDetailsRequest(name = emptyName))
        result mustBe RequestValidationError("name is empty")
      }
    }
  }

}
