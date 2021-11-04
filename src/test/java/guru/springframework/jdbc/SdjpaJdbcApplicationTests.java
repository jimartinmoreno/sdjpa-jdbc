package guru.springframework.jdbc;

import guru.springframework.jdbc.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@SpringBootTest
class SdjpaJdbcApplicationTests {

	@Autowired
	BookRepository bookRepository;

	@Test
	void contextLoads() {
		assertThat(bookRepository).isNotNull();
	}

}
