import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Leaderboard extends JPanel {
    private final int tailleCase = 36;
    private JTable table;

    public Leaderboard(JFrame frame) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:results.db");
            PreparedStatement ps = con.prepareStatement("SELECT name, score FROM scores ORDER BY score DESC LIMIT 5");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Username");
            model.addColumn("Score");

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"), rs.getInt("score")});
            }

            table = new JTable(model);
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(324, tailleCase * 4));

            add(new JScrollPane(table), BorderLayout.CENTER);

            rs.close();
            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            add(new JLabel("Could not load leaderboard"));
        }
    }
}