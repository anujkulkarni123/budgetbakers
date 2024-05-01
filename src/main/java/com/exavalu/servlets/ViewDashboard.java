package com.exavalu.servlets;

import com.exavalu.services.CardService;
import com.exavalu.entities.Card;
import com.exavalu.pojos.PropertyValues;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewDashboard
 */
@WebServlet("/ViewDashboard")
public class ViewDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewDashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load property values
        ServletContext context = getServletContext();
        Properties properties = new Properties();
        PropertyValues propertyValues = PropertyValues.getInstance();

        try (InputStream input = context.getResourceAsStream("/WEB-INF/config.properties")) {
            if (input == null) {
                throw new IOException("Cannot find configuration file");
            }
            properties.load(input);

            // Log the database name to console (you may want to remove this in production)
            System.out.println("Database name: " + properties.getProperty("dbname"));

            // Set properties in the singleton PropertyValues instance
            propertyValues.setDbname(properties.getProperty("dbname"));
            propertyValues.setPassword(properties.getProperty("password"));
            propertyValues.setUrl(properties.getProperty("url"));
            propertyValues.setUser(properties.getProperty("user"));

        } catch (IOException ex) {
            throw new ServletException("Failed to load configuration properties", ex);
        }

        // Get cards from CardsService

        List<Card> cards = CardService.getCards(propertyValues);
        request.setAttribute("CARDS", cards);

        // Forward to JSP
        request.getRequestDispatcher("pages/dashboard.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delegate to doGet
        doGet(request, response);
    }
}
