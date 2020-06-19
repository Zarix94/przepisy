package com.przepisy.przepisy.controller;

import com.przepisy.przepisy.model.Recipe;
import com.przepisy.przepisy.model.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        if(session.getAttribute("userId") != null)
            model.addAttribute("userId", session.getAttribute("userId"));
        else
            model.addAttribute("userId", null);
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if(session.getAttribute("loginFailed") != null){
            model.addAttribute("loginFailed", session.getAttribute("loginFailed"));
        } else
            model.addAttribute("loginFailed", false);

        session.removeAttribute("loginFailed");

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/recipes")
    public String recipes(Model model, HttpSession session) {
        if(session.getAttribute("userId") != null)
            model.addAttribute("userId", session.getAttribute("userId"));
        else
            model.addAttribute("userId", null);

        return "recipes";
    }

    @GetMapping("/addRecipe")
    public String addRecipe(Model model,HttpSession session) {
        if(session.getAttribute("userId") != null)
            model.addAttribute("userId", session.getAttribute("userId"));
        else
            model.addAttribute("userId", null);
        if(session.getAttribute("userId") != null)
            return "addRecipe";
        else
            return "recipes";
    }

    @GetMapping("/showRecipe")
    public String showRecipe(@RequestParam(name="id", required=false) long id, HttpSession session, Model model) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session hbSession = factory.openSession();
        Recipe recipe = hbSession.get(Recipe.class, id);

        if(recipe != null)
            model.addAttribute("recipe", recipe);
        else
            model.addAttribute("recipe", null);

        if(session.getAttribute("userId") != null)
            model.addAttribute("userId", session.getAttribute("userId"));
        else
            model.addAttribute("userId", null);




        return "showRecipe";

    }

}

