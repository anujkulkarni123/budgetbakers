package com.exavalu.services;

import com.exavalu.entities.Card;
import com.exavalu.pojos.PropertyValues;
import com.exavalu.utilities.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardService {

    public static List<Card> getCards(PropertyValues propertyValues) {
    	System.out.println("REACHED CARDS FUNCTION");
        DbConnectionProvider dbConnectionProvider = DbConnectionProvider.getInstance();
        Connection conn = dbConnectionProvider.getDbConnection(propertyValues);
        ArrayList<Card> cards = new ArrayList<>();

        String sql = "SELECT * FROM Cards";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Card card = new Card();
                card.setType(rs.getString("type"));
                card.setJson(rs.getString("json"));
                card.setName(rs.getString("name"));
                card.setIsDefault(rs.getBoolean("default"));
                cards.add(card);
            }
            System.out.println("Cards retrieval success");
            return cards;
        } catch (SQLException e) {
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database access error encountered during card retrieval", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
