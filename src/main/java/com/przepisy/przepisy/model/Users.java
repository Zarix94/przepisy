package com.przepisy.przepisy.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity

public class Users {
    @Id
    @Column(unique = true)
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String login;
    @Column(unique = true)
    private String email;
    private String password;
    private int type = 0;

    @Transient
    private Result result;

    public Users() {
        result = new Result();
        result.setResult(true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +

                '}';
    }

    private boolean isUniqueLogin() {
        boolean result = true;
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        String hql = "SELECT count(*) FROM Users u WHERE login = '" + login + "'";
        Query query = session.createQuery(hql);
        List results = query.getResultList();

        if ((long) results.get(0) != 0)
            result = false;

        factory.close();
        return result;
    }

    private boolean isUniqueMail() {
        boolean result = true;
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        String hql = "SELECT count(*) FROM Users u WHERE email = '" + email + "'";
        Query query = session.createQuery(hql);
        List results = query.getResultList();

        if ((long) results.get(0) != 0)
            result = false;

        factory.close();
        return result;
    }

    public void isValid() {
        if (!isUniqueLogin()) {
            Error error = new Error("login", "Podany login jest zajęty");
            result.setError(error);
        }

        if (!isUniqueMail()) {
            Error error = new Error("email", "Podany adres e-mail jest zajęty");
            result.setError(error);
        }
    }

    public String hashPassword(String password) {
        MessageDigest digest = null;
        String hashString = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
                hashString = hexString.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashString;

/*        BCryptPasswordEncoder crypter = new BCryptPasswordEncoder();
        return crypter.encode(password);*/
    }


    public Result registerUser() {
        isValid();

        if (result.getResult()) {
            password = hashPassword(password);
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session session = factory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(this);
            tx.commit();
            factory.close();
        }

        return result;
    }

    private long getUserId() {
        long userId = 0;
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        String hql = "SELECT id FROM Users u WHERE login = '" + login + "' AND password = '" + password + "'";
        System.out.println(hql);

        Query query = session.createQuery(hql);
        List results = query.getResultList();

        if (results.size() > 0)
            userId = (long) results.get(0);

        factory.close();

        return userId;
    }

    public static Users login(Users user) {
        user.setPassword(user.hashPassword(user.getPassword()));
        long userId = user.getUserId();
        if (userId > 0) {

            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            Session session = factory.openSession();
            user = session.get(Users.class, userId);
        } else
            user = null;

        return user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
