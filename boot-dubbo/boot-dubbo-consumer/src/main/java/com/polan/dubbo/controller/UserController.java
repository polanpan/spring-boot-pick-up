package com.polan.dubbo.controller;

import com.polan.dubbo.entity.User;
import com.polan.dubbo.service.UserRequestService;
import com.polan.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 用户Controller</p>
 * @author youq  2019/4/27 15:03
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRequestService userRequestService;

    @GetMapping("/save")
    public ResultData<?> save() {
        User save = userRequestService.save();
        return ResultData.success(save);
    }

}
