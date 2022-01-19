package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {                     // класс-обертка над UserRepositories!


    final
    UserRepository userRepository;

    final
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    // найти всех.
    public List<User> allUsers() {                                                          // find all.
        return userRepository.findAll();
    }


    // найти юзера по id.
    public User findUserById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    // сохранить нового юзера.
    public boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        } else {

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }

    // удалить юзера по id.
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    // обновить-изменить юзера.
    public User update(User user) {                                                          //update.
        return userRepository.save(user);
    }
}
