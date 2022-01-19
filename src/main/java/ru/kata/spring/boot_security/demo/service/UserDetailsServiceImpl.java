package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserServiceImpl userServiceImpl;


    @Autowired
    public UserDetailsServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }



//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //метод от UserDetailsService!
//        /* этот класс User - это наш собственный класс. Ищем юзера в нашей базе...*/
//        User user = userServiceImpl.findByUsername(username); // дергаем метод обертку, который уже дергает метод из UserRepository!
//        if (user == null) {   // если такого нет в базе..
//            throw new UsernameNotFoundException("User " + username + " not found");
//        } else {
//            // создаем права на основе ролей.
//            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//            for (Role role : user.getRoles()) {  //для всех ролей юзера
//                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//            }
//            // возвращаем спринговского юзера с нашими деталями (username, password и правами)
//            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
//        }
//    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //метод от UserDetailsService!
        /* этот класс User - это наш собственный класс. Ищем юзера в нашей базе...*/
        User user = userServiceImpl.findByUsername(username); // дергаем метод обертку, который уже дергает метод из UserRepository!
        if (user == null) {   // если такого нет в базе..
            throw new UsernameNotFoundException("User " + username + " not found");
        } else {
            return user;
        }
    }
}
