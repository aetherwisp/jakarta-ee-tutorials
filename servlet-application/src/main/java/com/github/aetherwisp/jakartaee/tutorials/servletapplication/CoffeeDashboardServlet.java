package com.github.aetherwisp.jakartaee.tutorials.servletapplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/coffeeDashboard")
public class CoffeeDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Map<String, String> COFFEE_DESCRIPTIONS = new HashMap<>();
    static {
        COFFEE_DESCRIPTIONS.put("Black", """
                    <p>Black coffee has a robust flavor, perfect for those who prefer a coffee with some bite.</p>
                    <p>Try brewing methods like French Press or Aeropress for an enjoyable black coffee experience.</p>
                """);
        COFFEE_DESCRIPTIONS.put("Latte", """
                    <p>A latte is a creamy delight, suitable for people who enjoy a smoother and less harsh flavor.</p>
                    <p>Experimenting with various syrups and sweeteners can elevate your latte experience.</p>
                """);
        COFFEE_DESCRIPTIONS.put("Cold Brew",
                """
                            <p>Cold brew coffee tends to be smoother and less acidic. It's perfect for those hot summer days.</p>
                            <p>Try brewing a batch in the fridge overnight for a refreshing morning pick-me-up.</p>
                        """);
    }

    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _res) throws ServletException, IOException {
        final HttpSession session = _req.getSession();
        final String[] coffeeTypes = (String[]) session.getAttribute("userCoffeeTypes");

        if (null == coffeeTypes || 0 == coffeeTypes.length) {
            this.handleNoCoffeeTypes(_res);
            return;
        }

        final PrintWriter out = _res.getWriter();
        out.println("""
                    <html>
                    <body>
                    <h1>Your Personalized Coffee Dashboard</h1>
                """);

        for (final String coffeeType : coffeeTypes) {
            final String additionalInfo = COFFEE_DESCRIPTIONS.get(coffeeType);
            out.println("""
                        <h2>Recommended %s</h2>
                        <p>Here are some %s blends you might enjoy.</p>
                        %s
                    """.formatted(coffeeType, coffeeType, additionalInfo));
        }

        out.println("""
                    </body>
                </html>
                """);
    }

    private void handleNoCoffeeTypes(HttpServletResponse _res) throws IOException {
        final PrintWriter out = _res.getWriter();
        out.println("""
                <html>
                    <body>
                        <h1>No Coffee Types Found</h1>
                        <p>Please select at least one type of coffee.</p>
                    </body>
                </html>
                """);
    }
}
