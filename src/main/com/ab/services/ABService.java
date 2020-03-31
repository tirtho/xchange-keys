/**
 * 
 */
package com.ab.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tibarar
 *
 */
public class ABService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        String url = "jdbc:postgresql://tbdemopostgres.postgres.database.azure.com:5432/postgres?sslmode=require";
        String user = "tbarari@tbdemopostgres";
        String password = "P@ssw0rd2020";
        try (Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT name FROM AUTHORS")) {

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

            con.close();
            
        } catch (SQLException ex) {     

        	Logger lgr = Logger.getLogger(ABService.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
	}

}
