package com.poly.thanhnvph20218.service;

import com.poly.thanhnvph20218.entity.User;
import com.poly.thanhnvph20218.viewModel.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUser();
}
