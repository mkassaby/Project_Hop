import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Leaderboard extends JPanel {
    private final int tailleCase =36;
    private JTable table;

    public Leaderboard(JFrame frame) {
        try {
            String tableQuery = "SELECT * FROM scores ORDER BY score";

            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:results.db");
            PreparedStatement ps = con.prepareStatement(tableQuery);
            ResultSet rs = ps.executeQuery();

            // Create a DefaultTableModel
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the model
            model.addColumn("Username");
            model.addColumn("Score");

            // Add rows to the model from the ResultSet
            while (rs.next()) {
                Object[] row = new Object[6];
                row[0] = rs.getString("name");
                row[1] = rs.getInt("score");
                model.addRow(row);
            }

            // Create the JTable with the model
            table = new JTable(model);

            // Add the JTable to a JScrollPane
            frame.setLayout(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(28, 100, 268, tailleCase * 3);
            frame.add(scrollPane);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}