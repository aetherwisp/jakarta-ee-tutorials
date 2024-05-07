package com.github.aetherwisp.jakartaee.tutorials.servletapplication;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/storePreferences")
public class StorePreferencesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException {
        _req.getRequestDispatcher("/WEB-INF/coffee_preferences.html").forward(_req, _res);
    }

    @Override
    protected void doPost(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException {
        final String[] coffeeTypes = _req.getParameterValues("coffeeType");
        final HttpSession session = _req.getSession();
        session.setAttribute("userCoffeeTypes", coffeeTypes);
        _res.sendRedirect("coffeeDashboard");
    }
}
