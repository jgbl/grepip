package org.de.jmg.showips;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
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
		catch(MySQLSyntaxErrorException eex)
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

	
}
