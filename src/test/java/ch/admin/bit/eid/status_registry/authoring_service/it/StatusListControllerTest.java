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
class StatusListControllerTest {

    private static final String ENTRY_BASE_URL = "/api/v1/entry/";
    private static final String STATUSLIST_BASE_URL = "/api/v1/statuslist/";

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
                .andExpect(jsonPath("$.files.TokenStatusListJWT.readUri", Matchers.containsString("TEST.DATAURL/")))
                .andReturn()
                .getResponse()
                .getContentAsString();
        var parsed = JsonPath.parse(datastoreEntryResponse);
        String datastoreEntryId = parsed.read("$.id");
        String readUri = parsed.read("$.files.TokenStatusListJWT.readUri");
        assert readUri.compareTo("TEST.DATAURL/" + datastoreEntryId + ".jwt") == 0;
        return datastoreEntryId;
    }


    @Test
    void testPutEntry_validTS_response() throws Exception {
        var datastoreEntryId = createDatastoreEntry();

        var tokenStatusListJWT =
                "HEADER.ewogICJleHAiOiAyMjkxNzIwMTcwLAogICJpYXQiOiAxNjg2OTIwMTcwLAogICJpc3MiOiAiZGlkOnRkdzowMDAwOmV4YW1wbGU6IiwKICAic3RhdHVzX2xpc3QiOiB7CiAgICAiYml0cyI6IDEsCiAgICAibHN0IjogImVOcmJ1UmdBQWhjQlhRIgogIH0sCiAgInN1YiI6ICJodHRwczovL2V4YW1wbGUuY29tL3N0YXR1c2xpc3RzLzEuand0Igp9.SIGNATURE";
        mvc
                .perform(put(STATUSLIST_BASE_URL + datastoreEntryId + ".jwt").content(tokenStatusListJWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.files").isNotEmpty())
                .andExpect(jsonPath("$.files", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("$.files.TokenStatusListJWT").exists())
                .andExpect(jsonPath("$.files.TokenStatusListJWT.isConfigured").value(Boolean.TRUE));

        mvc
                .perform(get(ENTRY_BASE_URL + datastoreEntryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.files").isNotEmpty())
                .andExpect(jsonPath("$.files", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("$.files.TokenStatusListJWT").exists())
                .andExpect(jsonPath("$.files.TokenStatusListJWT.isConfigured").value(Boolean.TRUE));
    }
}
