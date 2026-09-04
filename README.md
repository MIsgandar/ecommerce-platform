## JWT Authentication

This project uses JWT authentication with Spring Security.

### Authentication Flow ###
 
1. User logs in with email and password.
2. Spring Security authenticates the user.
3. A JWT is generated.
4. The client sends the JWT in the Authorization header.
5. JwtAuthenticationFilter validates the token.
6. Protected endpoints become accessible.

Example 

Authorization: Bearer <jwt-token>
