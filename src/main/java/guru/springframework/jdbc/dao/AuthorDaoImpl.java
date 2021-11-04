package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by jt on 8/20/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource source;

    /**
     * @param source Spring boot realiza el autowired sin la necesidad de especifirca el tag @Autowired
     */
    //@Autowired
    public AuthorDaoImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public Author getById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM author where id = ?")) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {

        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM author where first_name = ? and last_name = ?")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {

        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO author (first_name, last_name) values (?, ?)");
             Statement statement = connection.createStatement();) {

            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.execute();

            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");

            if (resultSet.next()) {
                Long savedId = resultSet.getLong(1);
                return this.getById(savedId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE author set first_name = ?, last_name = ? where author.id = ?")) {
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setLong(3, author.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE from author where id = ?")) {
            ps.setLong(1, id);
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Author getAuthorFromRS(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
    }
}













