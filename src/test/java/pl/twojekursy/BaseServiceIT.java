package pl.twojekursy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.twojekursy.test.helper.UserCreator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class BaseServiceIT {
    @Autowired
    protected EntityManager entityManager;

    @Autowired
    private DBCleaner dbCleaner;

    @Autowired
    protected UserCreator userCreator;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();
    }
}
