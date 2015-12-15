package org.de.jmg.showips;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StatusColumnCellRenderer extends DefaultTableCellRenderer {
	  @Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

	    //Cells are by default rendered as a JLabel.
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	    if (col == 0)
	    {
	    	DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		    String type = tableModel.getValueAt(row, 2).toString(); 
	    	if (type.equalsIgnoreCase("extern")) 
		    {
		      l.setBackground(Color.RED);
		    } 
	    	else if (type.equalsIgnoreCase("local")) 
	    	{
		      l.setBackground(Color.GREEN);
		    }
	    	else if (type.equalsIgnoreCase("multi")) 
	    	{
		      l.setBackground(Color.YELLOW);
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
	    	
	    }
	    //Get the status for the current row.
	    

	  //Return the JLabel which renders the cell.
	  return l;

	}
}