package com.przepisy.przepisy.model;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Session {

    public void startSession(HttpServletRequest request){
        request.getSession();
    }

    public void setToSession(HttpServletRequest request, String key, Object value){
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public Object getFromSession(HttpServletRequest request, String key){
        HttpSession session = request.getSession();
        return session.getAttribute(key);
    }

    public void destroySession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }
}
