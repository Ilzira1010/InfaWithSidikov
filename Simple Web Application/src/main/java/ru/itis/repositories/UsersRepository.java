package ru.itis.repositories;

import ru.itis.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByLogin();
    void addCookie(String email, String UUID);
    Boolean findUserByLoginAndPass(String email, String password);

}
