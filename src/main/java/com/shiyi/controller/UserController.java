package com.shiyi.controller;

import com.shiyi.domain.User;
import com.shiyi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, String> json) throws
            ServletException {
        if(json.get("username") == null || json.get("password") ==null) {
            throw new ServletException("Please fill in username and password");
        }

        String username = json.get("username");
        String password = json.get("password");

        JSONObject msg = new JSONObject();

        User user = userService.findByUsername(username);
        if (user ==null) {
            msg.put("status", "failure");
            msg.put("message", "User name not found");
            throw new ServletException("User name not found.");
        }

        String pwd = user.getPassword();

        if(!password.equals(pwd)) {
            msg.put("status", "failure");
            msg.put("message", "Login failed, please check your name and password");
            throw new ServletException("Invalid login. Please check your name and password");
        }

        msg.put("status", "success");
        msg.put("username", user.getUsername());
        msg.put("name", user.getFirstName() + " " +  user.getLastName());
        msg.put("token", Jwts.builder().setSubject(username).claim("roles", "user").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
        return msg.toString();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registerUser(@RequestBody User user) {
        return userService.save(user);
    }


}
