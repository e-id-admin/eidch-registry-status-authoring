/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.it;

import ch.admin.bit.eid.status_registry.authoring_service.security.TestWebSecurityConfig;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestWebSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DataStoreControllerTest {

    private static final String ENTRY_BASE_URL = "/api/v1/entry/";

    @Autowired
    protected MockMvc mvc;

    String createDatastoreEntry() throws Exception {
        var datastoreEntryResponse = mvc
                .perform(post(ENTRY_BASE_URL))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("SETUP"))
                .andExpect(jsonPath("$.files").isNotEmpty())
                .andExpect(jsonPath("$.files", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("$.files.TokenStatusListJWT").exists())
                .andExpect(jsonPath("$.files.TokenStatusListJWT.isConfigured").value(Boolean.FALSE))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String datastoreEntryId = JsonPath.parse(datastoreEntryResponse).read("$.id");
        return datastoreEntryId;
    }

    @Test
    void testCreateEntry_response() throws Exception {
        createDatastoreEntry();
    }

    @Test
    void testCheckEntry_statusIsdisabled_response() throws Exception {
        var datastoreEntryId = createDatastoreEntry();

        mvc
                .perform(
                        patch(ENTRY_BASE_URL + datastoreEntryId)
                                .contentType("application/json")
                                .content(
                                        """
                                                {
                                                  "status": "DISABLED"
                                                }
                                                """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DISABLED"));

        mvc
                .perform(get(ENTRY_BASE_URL + datastoreEntryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DISABLED"));
    }

    @Test
    void testCheckEntry_statusIsdeactivated_response() throws Exception {
        var datastoreEntryId = createDatastoreEntry();

        mvc
                .perform(
                        patch(ENTRY_BASE_URL + datastoreEntryId)
                                .contentType("application/json")
                                .content(
                                        """
                                                {
                                                  "status": "DEACTIVATED"
                                                }
                                                """
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DEACTIVATED"));

        mvc
                .perform(get(ENTRY_BASE_URL + datastoreEntryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("DEACTIVATED"));
    }
}
