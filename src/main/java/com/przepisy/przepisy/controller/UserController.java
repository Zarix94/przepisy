package com.przepisy.przepisy.controller;

import com.przepisy.przepisy.model.Result;
import com.przepisy.przepisy.model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.IOException;
import java.util.Enumeration;

@RestController
public class UserController {

/*    @GetMapping("/registerUser")
    @RequestMapping(value = "/registerUser", method = {RequestMethod.POST})
    public String register(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("email") String email) {
       // Users.registerUser(login, password, email);
        return "registerUser";
    }*/

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") Users userObject, BindingResult result) {
        Result resultObj = userObject.registerUser();
        return resultObj.toJson();
    }

    @ModelAttribute
    @PostMapping("/loginUser")
    public void loginUser(@ModelAttribute("user") Users userObject, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        session = request.getSession();

        Users user = Users.login(userObject);

        try {
            if (user != null) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("loginFailed", false);
                response.sendRedirect("/index");
            } else {
                session.setAttribute("loginFailed", true);
                response.sendRedirect("/login");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

