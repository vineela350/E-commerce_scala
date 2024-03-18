package com.example.models

final case class UserCredentials(username: String, email: String, password: String)


// Used for sending a JWT token back to the user upon successful authentication
final case class AuthToken(token: String)
