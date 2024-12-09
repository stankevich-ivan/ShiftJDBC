package org.shift;

import org.shift.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String CREATE_SQL = """
            create table if not exists info (
                id serial primary key,
                data varchar(100) not null
            );
            """;

    private static final String INSERT_SQL = """
            insert into info(data)
                values
                    ('Test1'),
                    ('Test2'),
                    ('Test3'),
                    ('Test4');
            """;

    private static final String UPDATE_SQL = """
            update info
                set data = 'TestTest'
                where id = 1;
            """;

    private static final String SELECT_SQL = """
                select * from info order by id;
            """;

    public static void main(String[] args) throws Exception {
//        createTable();
//        insert();
//        update();
//        select();
//        selectInjectionExample();
//        selectWithPreparedStatement();
    }

    private static void createTable() throws SQLException {
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement();
        ) {
            boolean execute = statement.execute(CREATE_SQL);
            System.out.println(execute);
        }
    }

    private static void insert() throws SQLException {
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement();
        ) {
            int execute = statement.executeUpdate(INSERT_SQL);
            System.out.println(execute);
        }
    }

    private static void update() throws SQLException {
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement();
        ) {
            int execute = statement.executeUpdate(UPDATE_SQL);
            System.out.println(execute);
        }
    }

    private static void select() throws SQLException {
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(SELECT_SQL);
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id"));
                System.out.println(resultSet.getString("data"));
            }
        }
    }

    private static void selectInjectionExample() throws SQLException {
        String id = "1";
        String sql = """
                select data from info where id = %s
                """.formatted(id);
        List<String> result = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSet.getObject("data", String.class));
            }
            System.out.println(result);
        }
    }

    private static void selectWithPreparedStatement() throws SQLException {
        long id = 1L;
        String sql = """
                select data from info where id = ?
                """;
        List<String> result = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            System.out.println(preparedStatement);
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getObject("data", String.class));
            }
            System.out.println(result);
        }
    }
}