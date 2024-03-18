package com.example.utils

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import java.time.Clock

object JwtUtils {
  val secretKey = "your_secret_key" // Store this securely
  val algorithm = JwtAlgorithm.HS256
  
  // Provide an implicit Clock instance
  implicit val clock: Clock = Clock.systemUTC

  def createToken(userId: String): String = {
    val claim = JwtClaim(subject = Some(userId))
      .issuedNow
      .expiresIn(3600) // Expires in 1 hour
    Jwt.encode(claim, secretKey, algorithm)
  }

  def isValidToken(token: String): Boolean = Jwt.isValid(token, secretKey, Seq(algorithm))
}
