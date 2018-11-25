package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	
	// create record
	ObservableList<Filter> olst = FXCollections.observableArrayList();
	// read db records into a structure
	Model() {
		try {
			Connection c = null;
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			Statement statement = c.createStatement();
			String sql = "SELECT * FROM presets";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println("Brightness val from DB: " + rs.getDouble("brightness"));
				Filter f = new Filter(rs.getString("name"), rs.getDouble("contrast"), rs.getDouble("brightness"),
						rs.getDouble("saturation"), rs.getDouble("shadows"), rs.getDouble("highlights"),
						rs.getDouble("gamma"));
				olst.add(f);
				// create a Filter and add to structure
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	// update record

	public ObservableList<Filter> getLst() {
		return olst;
	}
}
