package com.ead.authuser;

import static com.ead.authuser.helpers.JsonMatchers.isBrDateTime;
import static com.ead.authuser.helpers.JsonMatchers.isUuid;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIT {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  protected <T> ResultActions postRequest(final String url, T body) throws Exception {
    return mockMvc.perform(
        post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)));
  }

  protected void expectBadRequestWithMessages(ResultActions action, String... expectedMessages)
      throws Exception {
    action
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.message", Matchers.containsInAnyOrder(expectedMessages)));
  }

  protected <T> T readBody(MvcResult result, Class<T> type) throws Exception {
    return objectMapper.readValue(result.getResponse().getContentAsString(), type);
  }

  protected static ResultActions assertAuditFields(ResultActions actions, String basePath)
      throws Exception {
    return actions
        .andExpect(jsonPath(basePath + ".id").value(isUuid()))
        .andExpect(jsonPath(basePath + ".created").value(isBrDateTime()))
        .andExpect(jsonPath(basePath + ".updated").value(isBrDateTime()));
  }
}
