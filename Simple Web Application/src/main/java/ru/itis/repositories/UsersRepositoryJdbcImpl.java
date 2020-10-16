package ru.itis.repositories;

import ru.itis.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private Connection connection;

    //language=SQL
    private  static final String SQL_SELECT_ALL_FROM_USERS = "select * from registration";
    //language=SQL
    private static final String SQL_SELECT_ALL_BY_EMAIL_PASSWORD =
            "select * from registration where email = ? and password = ?";
    //language=SQL
    private static final String SQL_UPDATE_COOKIE_BY_EMAIL = "update registration set uuid = ? where email = ?;";

    public UsersRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    public void save(User entity) {
        try {
            PreparedStatement prepareStatement = connection.prepareStatement("insert into registration " +
                    "(firstName, lastName, email, username, password) values (?,?,?,?,?);");
            prepareStatement.setString(1, entity.getFirstName());
            prepareStatement.setString(2, entity.getLastName());
            prepareStatement.setString(3, entity.getEmail());
            prepareStatement.setString(4, entity.getUsername());
            prepareStatement.setString(5, entity.getPassword());
            prepareStatement.executeUpdate();
            prepareStatement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findByLogin() {
        return null;
    }

    @Override
    public List<User> findAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_SELECT_ALL_FROM_USERS);
            List<User> users = new ArrayList<>();
            while (result.next()) {
                users.add(User.builder()
                        .firstName(result.getString("firstName"))
                        .lastName(result.getString("lastName"))
                        .email(result.getString("email"))
                        .username(result.getString("username"))
                        .password(result.getString("password"))
                        .build());
            }
            return users;
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public Boolean findUserByLoginAndPass(String email, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_BY_EMAIL_PASSWORD);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void addCookie(String email, String uuid) {
        try {
            Class.forName("org.postgresql.Driver");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_COOKIE_BY_EMAIL);
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

