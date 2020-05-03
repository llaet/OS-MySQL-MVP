/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.dal;

import java.sql.*;

/**
 *
 * @author Lucas Laet
 */
public class ConnectionModule {

    /*
     * establishes the database connection
     *
     * @return Connection with database
     */
    public static Connection connector() {
        java.sql.Connection connection = null;
        //import the database driver
        String driver = "com.mysql.jdbc.Driver";
        //store database infos
        String url = "jdbc:mysql://localhost:3306/dbInfoX";
        String user = "root";
        String password = "12345";
        //database connection establishment
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            //error description
            //System.out.println(e);
            return null;
        }
    }
}
