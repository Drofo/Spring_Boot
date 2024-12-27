package web.crud_spring_hiber.dao;

import org.springframework.stereotype.Repository;
import web.crud_spring_hiber.model.User;

import java.util.*;

@Repository
public interface UserDao {
    List<User> allUsers();
    void add(User user);
    void delete(User user);
    void edit(User user);
    User getById(int id);
}
