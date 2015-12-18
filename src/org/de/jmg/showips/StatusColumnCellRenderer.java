package org.de.jmg.showips;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StatusColumnCellRenderer extends DefaultTableCellRenderer {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

	    //Cells are by default rendered as a JLabel.
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	    if (col == 0)
	    {
	    	DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		    String type = tableModel.getValueAt(row, 2).toString(); 
	    	l.setForeground(Color.BLACK);
		    if (type.equalsIgnoreCase("extern")) 
		    {
		      l.setBackground(Color.decode("#F0CCCC"));
		      //l.setForeground(Color.WHITE);
		    } 
	    	else if (type.equalsIgnoreCase("local")) 
	    	{
		      l.setBackground(Color.GREEN);
		    }
	    	else if (type.equalsIgnoreCase("multi")) 
	    	{
		      l.setBackground(Color.YELLOW);
		    }
	    	else if (type.equalsIgnoreCase("multi local")) 
	    	{
		      l.setBackground(Color.CYAN);
		    }
	    	else if (type.equalsIgnoreCase("extern drop")) 
		    {
		      l.setBackground(Color.PINK);
		    } 
	    	else if (type.equalsIgnoreCase("local drop")) 
	    	{
		      l.setBackground(Color.GRAY);
		    }
	    	else if (type.equalsIgnoreCase("multi drop")) 
	    	{
		      l.setBackground(Color.ORANGE);
		    }
	    	else if (type.equalsIgnoreCase("multi local drop")) 
	    	{
		      l.setBackground(Color.BLUE);
		      l.setForeground(Color.WHITE);
		    }
	    	if (type.endsWith("critical")) 
	    	{
		      l.setBackground(Color.BLACK);
		      l.setForeground(Color.WHITE);
		    }
	    }
	    //Get the status for the current row.
	    

	  //Return the JLabel which renders the cell.
	  return l;

	}
}