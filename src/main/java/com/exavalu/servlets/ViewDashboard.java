package com.exavalu.servlets;

import com.exavalu.services.CardService;
import com.exavalu.entities.Card;
import com.exavalu.pojos.PropertyValues;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

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
        request.getRequestDispatcher("pages/dashboard.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        // Retrieve all cards
        List<Card> cards = CardService.getCards(propertyValues);
        Map<Boolean, List<Card>> groupedByDefault = cards.stream()
            .collect(Collectors.partitioningBy(Card::isDefault));

        List<Card> defaultCards = groupedByDefault.get(true);
        List<Card> nonDefaultCards = groupedByDefault.get(false);

        Map<String, List<Card>> nonDefaultCardsByType = nonDefaultCards.stream()
            .collect(Collectors.groupingBy(Card::getType));

        // Create JSON response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("defaultCards", new JSONArray(defaultCards)); // Assuming Card has a suitable toJSON method
        jsonResponse.put("nonDefaultCardsByType", new JSONObject(nonDefaultCardsByType));

        // Send JSON as response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
        response.getWriter().close();
    }
}
