package com.github.teamverdeingsis.permission

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@SpringBootTest
@AutoConfigureMockMvc
class PermissionsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "testuser", authorities = ["manage:permissions"])
    fun `should return 200 OK for authorized user`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/permissions"))  // Correct URL
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "testuser", authorities = ["other:permission"])
    fun `should return 403 Forbidden for unauthorized user`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/permissions"))  // Correct URL
            .andExpect(status().isForbidden)
    }

}
