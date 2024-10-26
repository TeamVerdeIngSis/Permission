package com.github.teamverdeingsis.snippetsPermission

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.mockito.Mockito.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import com.fasterxml.jackson.databind.ObjectMapper  // Import ObjectMapper


@SpringBootTest
@AutoConfigureMockMvc
class Auth0IntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jwtDecoder: JwtDecoder

    @Autowired  // Autowire ObjectMapper
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        val jwt: Jwt = mock(Jwt::class.java)
        `when`(jwt.subject).thenReturn("auth0|123456789")
        `when`(jwt.getClaimAsString("scope")).thenReturn("manage:permissions")  // Ensure the correct authority is set
        `when`(jwt.audience).thenReturn(listOf("dev-ppmfishyt4u8fel3"))  // Ensure the correct audience
        `when`(jwtDecoder.decode(anyString())).thenReturn(jwt)
    }


    @Test
    fun `should allow access with valid JWT`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/permissions")
            .header("Authorization", "Bearer valid-jwt-token"))
            .andExpect(status().isOk)  // Ensure that this matches the authority required in SecurityConfig
    }


    @Test
    fun `should reject access with invalid JWT`() {
        `when`(jwtDecoder.decode(anyString())).thenThrow(RuntimeException("Invalid token"))

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")  // Correct URL
            .header("Authorization", "Bearer invalid-jwt-token"))
            .andExpect(status().isUnauthorized)  // Expecting 401 Unauthorized for invalid JWT
    }
}
