package org.tju.HFDemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tju.HFDemo.web.model.User;
import org.tju.HFDemo.web.service.UserService;

/**
 * Created by shaohan.yin on 05/05/2017.
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public User userLogIn(@RequestBody User user) {
        logger.info("[Login]User:{}", user.getUserId());
        return userService.login(user);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void userLogOut(@RequestBody User user) {
        logger.info("[Logout]User:{}", user.getUserId());
        userService.logout(user);
    }
}
