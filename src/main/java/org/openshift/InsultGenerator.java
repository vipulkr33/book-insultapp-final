package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsultGenerator {
	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		
		try {
			String databaseURL = "jdbc:mysql://";
			databaseURL += System.getenv("MYSQL_SERVICE_HOST");
			databaseURL += "/" + System.getenv("MYSQL_DATABASE");
			databaseUrl += "?useSSL=false";

			String username = System.getenv("MYSQL_USER");
			String password = System.getenv("MYSQL_PASSWORD");
			Class.forName ("com.mysql.cj.jdbc.Driver").newInstance ();
			Connection connection = DriverManager.getConnection(databaseURL, username, password);

			if (connection != null) {
				String SQL = "select a.string AS first, b.string AS second, c.string AS noun from short_adjective a , long_adjective b, noun c ORDER BY random() limit 1";
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				while (rs.next()) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
						article = "a";
					}
					theInsult =  String.format("Thou art %s %s %s %s!", article, rs.getString("first"),
							rs.getString("second"), rs.getString("noun"));
				}
				rs.close();
				connection.close();
			}
		} catch (Exception e) {
			return "Database connection problem!";
		}
		return theInsult;
	}
}
