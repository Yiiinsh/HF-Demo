package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

/**
 * Created by shaohan.yin on 05/05/2017.
 */
@RestController
@RequestMapping("/console")
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/login")
    public void userLogIn() {

    }

    @RequestMapping("/user/logout")
    public void userLogOut() {

    }

    @RequestMapping("/user/current")
    public void userInfo() {

    }
}
