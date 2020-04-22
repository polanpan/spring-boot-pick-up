package com.polan.feign.response.service;

import com.polan.feign.response.entity.User;
import com.polan.feign.response.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户Service</p>
 * @author youq  2019/4/12 10:51
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

}
