package Dao;

import Models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Stats {
    public static final String SELECT_CATEGORIES_SQL = "SELECT c.id, c.name, c.color, SUM(t.number*t.unit_price) as total_price FROM transactions t JOIN expenses e ON e.id = t.expense_id JOIN names n ON n.id = e.name_id JOIN categories c ON c.id=n.category_id WHERE t.date BETWEEN <start> AND <end> GROUP BY c.name ORDER BY total_price";
    private String url = null;

    public Stats(String url) {
        this.url=url;
    }

    public HashSet<Category> getCategories(long start, long end) {
        HashSet<Category> categories = new HashSet<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            Statement stmt = conn.createStatement();
            String query = SELECT_CATEGORIES_SQL;
            query = query.replaceAll("<start>",""+start);
            query = query.replaceAll("<end>",""+end);
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                categories.add(new Category(id,name,color));
            }
            conn.close();
            return categories;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
