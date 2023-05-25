package pl.twojekursy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.twojekursy.test.helper.CommentCreator;
import pl.twojekursy.test.helper.PostCreator;

@ActiveProfiles("test")
@DataJpaTest
@Import({DBCleaner.class, PostCreator.class, CommentCreator.class})
public class BaseRespositoryIT {
    @Autowired
    protected EntityManager entityManager;

    @Autowired
    private DBCleaner dbCleaner;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();
    }
}
