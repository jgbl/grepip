package org.de.jmg.showips;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class dbIps 
{
	public static Connection conn;
	
	public static void connecttodatabase() throws SQLException
	{
		MysqlDataSource ds = new MysqlDataSource();
		String user =JOptionPane.showInputDialog("user"); 
		JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        JOptionPane.showMessageDialog(
                null,
                passwordField,
                "Enter password",
                JOptionPane.OK_OPTION);
        String pw = String.valueOf(passwordField.getPassword());
        ds.setPassword(pw);
        String server = JOptionPane.showInputDialog("Server");
		ds.setServerName(server);
		//ds.setUser("'" + user + "'@'" + server + "'");
		ds.setUser(user);
		ds.setPort(3306);
		ds.setDatabaseName("ips");
		//ds.setURL("jdbc:mysql://" + server +":3306/" + "ips");
		try
		{
			conn = ds.getConnection();
		}
		catch(com.mysql.jdbc.exceptions.MySQLSyntaxErrorException eex) //|com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException eex)
		{
			//JOptionPane.showMessageDialog(null, eex.getErrorCode() + eex.getMessage());
			if (eex.getErrorCode() == 1049)
			{
				ds.setDatabaseName("");
				conn = ds.getConnection();
				InputStream is = ShowIPs.class.getResourceAsStream("/resources/file/ips.sql");
				importSQL(conn,is);
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn.close();
				ds.setDatabaseName("ips");
				conn = ds.getConnection();
			}
		}
		catch (SQLException ex)
		{
			throw (ex);
		}
	}
	
	public static void importSQL(Connection conn, InputStream in) throws SQLException
	{
		Scanner s = new Scanner(in);
		s.useDelimiter("(;(\r)?\n)|(--\n)");
		Statement st = null;
		try
		{
			st = conn.createStatement();
			while (s.hasNext())
			{
				String line = s.next();
				if (line.startsWith("/*!") && line.endsWith("*/"))
				{
					int i = line.indexOf(' ');
					line = line.substring(i + 1, line.length() - " */".length());
				}

				if (line.trim().length() > 0)
				{
					st.execute(line);
				}
			}
		}
		finally
		{
			if (st != null) st.close();
		}
	}
    static PreparedStatement findIP;
	public static ResultSet queryIP(String foundIP) throws SQLException {
		// TODO Auto-generated method stub
		if (findIP == null)
		{
			final String sql = "Select * FROM ip WHERE address = ?";
		  	findIP = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		}
		findIP.setString(1, foundIP);
		return findIP.executeQuery();
	}
	static PreparedStatement insertIP;
	public static ResultSet InsertIP(String foundIP, int count, int kind) throws SQLException {
		// TODO Auto-generated method stub
		if (insertIP == null)
		{
			final String sql = "INSERT INTO ip (address,count,kind) VALUES(?,?,?)";
		  	insertIP = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		}
		insertIP.setString(1, foundIP);
		insertIP.setInt(2, count);
		insertIP.setInt(3, kind);
		return insertIP.executeQuery();
	}
	static PreparedStatement insertText;
	public static ResultSet InsertText(int ID, String text) throws SQLException {
		// TODO Auto-generated method stub
		if (insertText == null)
		{
			final String sql = "INSERT INTO text (ipID,text) VALUES(?,?)";
		  	insertText = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		}
		insertText.setString(2, text);
		insertText.setInt(1, ID);
		return insertText.executeQuery();
	}

	
}
