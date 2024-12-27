package web.crud_spring_hiber.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.crud_spring_hiber.dao.UserDao;
import web.crud_spring_hiber.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Transactional
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Transactional
    public void delete(User user) {
        if (!entityManager.contains(user)) {
            user = entityManager.merge(user);
        }
        entityManager.remove(user);
    }

    @Transactional
    @Override
    public void edit(User user) {
        userDao.edit(user);
    }

    @Transactional
    @Override
    public User getById(int id) {
        return userDao.getById(id);
    }
}
