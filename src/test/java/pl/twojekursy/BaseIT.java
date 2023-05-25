package pl.twojekursy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseIT {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    private DBCleaner dbCleaner;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();
    }

    protected ResultActions performPost(String url, Object request) throws Exception {
        String content = objectMapper.writeValueAsString(request);
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
    }

    protected ResultActions performPut(String url, Long  id, Object request) throws Exception {
        String content = objectMapper.writeValueAsString(request);
        return mockMvc.perform(put(url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
    }

    protected ResultActions performGet(String url, Long  id) throws Exception {
        return mockMvc.perform(get(url, id)
        );
    }

    protected LocalDateTime parseDateTime(String json, String jsonPath) {
        return LocalDateTime.parse(JsonPath.compile(jsonPath).read(json)
        );
    }
}
