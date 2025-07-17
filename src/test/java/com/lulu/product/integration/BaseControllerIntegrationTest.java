package com.lulu.product.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public abstract class BaseControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
}
