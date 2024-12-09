package org.shift.dao;

import org.shift.entity.Person;
import org.shift.exception.DaoException;
import org.shift.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PersonDao {

    private static final PersonDao INSTANCE = new PersonDao();

    private static final String DELETE_PERSON_SQL = """
            DELETE FROM person WHERE id = ?
            """;

    private static final String SAVE_PERSON_SQL = """
            insert into person (first_name, last_name, address, birth_date) values (?, ?, ?, ?)
            """;

    private static final String UPDATE_PERSON_SQL = """
            update person
            set first_name = ?,
                last_name = ?,
                address = ?,
                birth_date = ?
            where id = ?
            """;

    private static final String FIND_BY_ID = """
            select id,
                   first_name,
                   last_name,
                   address,
                   birth_date
            from person where id = ?
            """;

    private static final String FIND_ALL = """
            select id,
                   first_name,
                   last_name,
                   address,
                   birth_date
            from person
            """;

    private PersonDao() {}

    public static PersonDao getInstance() {
        return INSTANCE;
    }

    public List<Person> findAll(){
        List<Person> persons = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
        ) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setAddress(resultSet.getString("address"));
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                persons.add(person);
            }

            return persons;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Person> findById(int id) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setFirstName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setAddress(resultSet.getString("address"));
                person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                return Optional.of(person);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Person person) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSON_SQL);
        ) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getAddress());
            preparedStatement.setDate(4, Date.valueOf(person.getBirthDate()));
            preparedStatement.setInt(5, person.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }



    public Person save(Person person) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        SAVE_PERSON_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getAddress());
            preparedStatement.setDate(4, Date.valueOf(person.getBirthDate()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getInt(1));
            }

            return person;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
















    public boolean delete(int id) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSON_SQL)
        ) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


}
