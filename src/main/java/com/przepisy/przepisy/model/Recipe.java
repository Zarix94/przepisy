package com.przepisy.przepisy.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.List;

@Entity

public class Recipe {
    @Id
    @Column(unique = true)
    @GeneratedValue
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public String getAuthor(){
        String author;

        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Users user = session.get(Users.class, author_id);

        return user.getLogin();
    }

    private String name;
    private int diff;
    private String ingredients;
    private String description;
    private long author_id;
    @Transient
    private Result result;

    public Recipe() {
        result = new Result();
        result.setResult(true);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + name + '\'' +
                ", diff=" + diff +
                ", ingredients='" + ingredients + '\'' +
                ", description" + description + '\'' +
                ", author_id=" + author_id +
                '}';
    }

    public Result addRecipe(){
        if (result.getResult()) {
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session session = factory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(this);
            tx.commit();
            factory.close();
        }

        return result;
    }


}
