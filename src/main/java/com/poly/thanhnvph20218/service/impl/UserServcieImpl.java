package com.poly.thanhnvph20218.service.impl;

import com.poly.thanhnvph20218.entity.Role;
import com.poly.thanhnvph20218.entity.User;
import com.poly.thanhnvph20218.reponsitory.RoleRepository;
import com.poly.thanhnvph20218.reponsitory.UserRepository;
import com.poly.thanhnvph20218.service.UserService;
import com.poly.thanhnvph20218.viewModel.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServcieImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPass(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRole();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user)).collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRole() {
        Role role = new Role();
        role.setName("ADMIN");
        return roleRepository.save(role);
    }
}
