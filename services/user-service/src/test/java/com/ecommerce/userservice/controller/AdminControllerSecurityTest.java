package com.ecommerce.userservice.controller;



import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AdminController.class)
public class AdminControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void unauthenticatedUserShouldReceive403() throws Exception {

        mockMvc.perform(get("/api/admin/test"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(
            username = "Customer@test.com",
            roles = {"CUSTOMER"}
    )
    void customerShouldNotAccessAdminEndpoint() throws Exception{

        mockMvc.perform(get("/api/admin/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin@shop.com",
            roles = {"ADMIN"}
    )
    void adminShouldAccessAdminEndpoint() throws Exception {

        mockMvc.perform(get("/api/admin/test"))
                .andExpect(status().isOk());
    }


}
