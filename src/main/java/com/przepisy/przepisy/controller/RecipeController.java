package com.przepisy.przepisy.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.przepisy.przepisy.model.Recipe;
import com.przepisy.przepisy.model.Result;
import com.przepisy.przepisy.model.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class RecipeController {

    private String getAuhtor(long authorId) {
        String author;

        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Users user = session.get(Users.class, authorId);

        return user.getLogin();
    }

    private String generateTableHtml(List<Recipe> receipts) {
        String html = "";
        html += "<table id=\"tblRecipe\" class=\"display\" style=\"width: 100%\">";
        html += "  <thead>";
        html += "       <tr>";
        html += "          <th>Nazwa</th>";
        html += "          <th>Poziom trudności</th>";
        html += "          <th>Autor</th>";
        html += "          <th>Zobacz</th>";
        html += "       </tr>";
        html += "   </thead>";
        html += "   <tbody>";
        for (Recipe recipe : receipts) {
            html += "<tr>";
            html += "<td>" + recipe.getName() + "</td>";
            html += "<td>" + String.valueOf(recipe.getDiff()) + "</td>";
            html += "<td>" + getAuhtor(recipe.getAuthor_id()) + "</td>";
            html += "<td><a href=\"showRecipe?id=" + String.valueOf(recipe.getId()) + "\"><img src=\"/img/ico-search.png\" style=\"width: 25px;\"/></a></td>";

            html += " </tr>";
        }
        html += "   </tbody>";
        html += "</table>";

        return html;
    }

    @PostMapping("/getRecipeList")
    public String getRecipeList() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        String hql = "FROM Recipe ";
        Query query = session.createQuery(hql);
        List results = query.getResultList();

        return generateTableHtml(results);
    }

    @PostMapping("/saveRecipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipeObj, BindingResult result, HttpSession session) {
        recipeObj.setAuthor_id((long) session.getAttribute("userId"));
        Result resultObj = recipeObj.addRecipe();
        return resultObj.toJson();
    }




}

