package org.de.jmg.showips;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;


//import com.mysql.*;

public class ShowIPs implements ClipboardOwner, ActionListener
{
	public static Frame frame;
	public static Button button;
	public static String filename;
	public static JMenuBar mb;
	public static JMenu mnudefault;
	public static JMenuItem mnuParseMSNM;
	public static JFileChooser fc = new JFileChooser();
	public static JTable listview;
	public static DefaultTableModel model = new DefaultTableModel(new Object[] {
			"IP", "Host", "Type", "Process", "Log", "Count" }, 0);
	public enum msnmFields
	{
		Frame, Time_Date, Time_Offset, Process, Source, Destination, Protocol_Name, Description, Conv_Id
	}
	
	public static ActionListener ActionLMenu = new ActionListener()
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				dbIps.cleardb();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	};
	public static ActionListener ActionL = new ActionListener()
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			fc.setDialogTitle("Select log file");
			int returnVal = fc.showOpenDialog(button);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				filename = file.getName();
				ArrayList<Entry<String, parser.foundIP>> iplist = null;
				try 
				{
					if (e.getSource() == button)
					{
						iplist = parser.parsefile(file);
					}
					else if ( e.getSource()== mnuParseMSNM)
					{
						iplist = parser.parsefilemsnm(file);
					}
				} 
				catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, e1.getMessage());;
				}
				String output = parser.join(iplist, "\n");
				StringSelection stringSelection = new StringSelection(output);
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				clipboard.setContents(stringSelection, new ClipboardOwner()
				{

					@Override
					public void lostOwnership(Clipboard clipboard,
							Transferable contents)
					{
						// TODO Auto-generated method stub

					}
				});
			}
			else
			{
			}
		}
		
	};
	public static WindowListener WinLi = new WindowListener()
	{

		@Override
		public void windowOpened(WindowEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			// TODO Auto-generated method stub
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e)
		{
			// TODO Auto-generated method stub
			if(dbIps.conn != null)
			{
				try
				{
					dbIps.conn.close();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dbIps.conn = null;
			}
		}

		@Override
		public void windowActivated(WindowEvent e)
		{
			// TODO Auto-generated method stub

		}
	};
	
	
	
	public static void main(String[] args)
	{
		// Create a file chooser
		// Create frame with specific title
		frame = new Frame("ShowIPs");
		frame.setLayout(new BorderLayout());
		// Create a component to add to the frame; in this case a text area with
		// sample text
		button = new Button("Click Me!!");
		button.addActionListener(ActionL);
		listview = new JTable(model);
		listview.getColumnModel().getColumn(0)
				.setCellRenderer(new StatusColumnCellRenderer());
		mb = new JMenuBar();
		mnuParseMSNM = new JMenuItem("msnm");
		mnuParseMSNM.addActionListener(ActionL);
		mb.add(mnuParseMSNM);
		JScrollPane pane = new JScrollPane(listview);
		frame.add(mb, BorderLayout.NORTH);
		frame.add(pane, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
		int width = 300;
		int height = 300;
		frame.setSize(width, height);
		frame.addWindowListener(WinLi);
		frame.setVisible(true);
		/*try 
		{
			dbIps.connecttodatabase();
		} 
		catch (SQLException e2) 
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(frame, e2.getMessage());
		}*/
		listview.addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseMoved(MouseEvent evt)
			{
				int row = listview.rowAtPoint(evt.getPoint());
				int col = listview.columnAtPoint(evt.getPoint());
				DefaultTableModel tableModel = (DefaultTableModel) listview
						.getModel();
				String text = (String) tableModel.getValueAt(row, col);
				listview.setToolTipText(text);
			}

			@Override
			public void mouseDragged(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		listview.addMouseListener(new java.awt.event.MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent evt)
			{
				final int row = listview.rowAtPoint(evt.getPoint());
				final int col = listview.columnAtPoint(evt.getPoint());
				final int Button = evt.getButton();
				Integer timerinterval = (Integer) Toolkit.getDefaultToolkit()
						.getDesktopProperty("awt.multiClickInterval");
				if (evt.getClickCount() == 1)
				{
					doubleclick = false;
					Timer timer = new Timer(timerinterval, new ActionListener()
					{

						@Override
						public void actionPerformed(ActionEvent ae)
						{
							// TODO Auto-generated method stub
							DefaultTableModel tableModel = (DefaultTableModel) listview
									.getModel();
							if (col == 0 || col == 1)
							{
								String ip = (String) tableModel.getValueAt(row,
										0);
								if (ip != null)
								{
									if (!doubleclick)
									{
										if (Button == MouseEvent.BUTTON3)
										{
											try
											{
												JTextArea textArea = new JTextArea(
														30, 75);
												textArea.setText(shell("whois "
														+ ip));
												textArea.setEditable(false);

												// wrap a scrollpane
												// around it
												JScrollPane scrollPane = new JScrollPane(
														textArea);
												JOptionPane.showMessageDialog(
														listview, scrollPane);
											}
											catch (HeadlessException
													| IOException
													| InterruptedException e)
											{
												// TODO Auto-generated
												// catch block
												e.printStackTrace();
											}
										}
										else if (Button == MouseEvent.BUTTON2)
										{
											try
											{
												JTextArea textArea = new JTextArea(
														30, 75);
												String domain = null;
												String host = (String) tableModel
														.getValueAt(row, 1);
												if (!host.equalsIgnoreCase(ip))
												{
													domain = new URI("//"
															+ host).getHost();
													if (domain
															.equalsIgnoreCase(host))
													{
														domain = domain
																.substring(1 + domain
																		.lastIndexOf(
																				".",
																				domain.lastIndexOf(".") - 1));
													}
												}
												if (parser.critical.containsKey(ip))
												{
													textArea.setText(join(
															parser.critical.get(ip),
															"\n"));
												}
												else if (parser.critical
														.containsKey(domain))
												{
													textArea.setText(join(
															parser.critical.get(domain),
															"\n"));
												}
												else if (parser.critical
														.containsKey(host))
												{
													textArea.setText(join(
															parser.critical.get(host),
															"\n"));
												}
												else
												{
													if (!host
															.equalsIgnoreCase(ip))
													{
														int i = -1;
														while ((i = host
																.indexOf('.',
																		i + 1)) >= 0)
														{
															domain = host
																	.substring(i + 1);
															if (parser.critical
																	.containsKey(domain))
															{
																textArea.setText(join(
																		parser.critical.get(domain),
																		"\n"));
																break;
															}
														}
													}
												}
												textArea.setEditable(false);

												// wrap a scrollpane
												// around it
												JScrollPane scrollPane = new JScrollPane(
														textArea);
												JOptionPane.showMessageDialog(
														listview, scrollPane);
											}
											catch (HeadlessException
													| URISyntaxException e)
											{
												// TODO Auto-generated
												// catch block
												e.printStackTrace();
											}
										}
										else
										{
											try
											{
												openWebpage(new URL(
														"http://www.ipvoid.com/scan/"
																+ ip + "/"));
											}
											catch (MalformedURLException e)
											{
												// TODO Auto-generated
												// catch block
												e.printStackTrace();
											}
										}
									}
									else if (doubleclick)
									{
										// PBDemo Terminal = new PBDemo(
										// "blcheck", ip);
										if (Button == MouseEvent.BUTTON1)
										{
											if (OSValidator.isWindows())
											{
												JOptionPane
														.showMessageDialog(
																listview,
																"This function is not available \non Windows!");
											}
											else
											{
												int exitCode;
												ProcessBuilder pb = new ProcessBuilder(
														"xterm", "-hold", "-e",
														"bash", "blcheck", ip);
												// pb.redirectError();
												try
												{
													Process pro = pb.start();
													exitCode = pro.waitFor();

												}
												catch (Exception e)
												{
													System.out.println("sorry" + e);
													JOptionPane.showMessageDialog(
															listview,
															e.getMessage());

												}
											}
										}
										else
										{
											try
											{
												openWebpage(new URL(
														"https://www.virustotal.com/en/ip-address/" 
																+ ip + "/information/"));
											}
											catch (MalformedURLException e)
											{
												// TODO Auto-generated
												// catch block
												e.printStackTrace();
											}
										}
									}

								}
							}
							else if (col == 4)
							{
								String log = (String) tableModel.getValueAt(
										row, 4);
								JTextArea textArea = new JTextArea(30, 75);
								textArea.setText(log);
								textArea.setEditable(false);
								JScrollPane scrollPane = new JScrollPane(
										textArea);
								JOptionPane.showMessageDialog(listview,
										scrollPane);
							}

							if (doubleclick)
							{
								doubleclick = false;
							}
						}

					});
					timer.setRepeats(false);
					timer.start();

				}
				else if (evt.getClickCount() > 1)
				{
					doubleclick = true;
				}

			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				int row = listview.rowAtPoint(evt.getPoint());
				int col = listview.columnAtPoint(evt.getPoint());
				DefaultTableModel tableModel = (DefaultTableModel) listview
						.getModel();
				String text = (String) tableModel.getValueAt(row, col);
				listview.setToolTipText(text);
			}

			boolean doubleclick;

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{

			}
		});
		parser.loadCritical();
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}

	public static String shell(String cmd) throws IOException,
			InterruptedException
	{
		Process p = Runtime.getRuntime().exec(cmd);
		p.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null)
		{
			sb.append(line + "\n");
		}
		return sb.toString();

	}

	public static void openWebpage(URI uri)
	{
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
		{
			try
			{
				desktop.browse(uri);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URL url)
	{
		try
		{
			openWebpage(url.toURI());
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	public static String join(String[] list, String conjunction)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : list)
		{
			if (first) first = false;
			else sb.append(conjunction);
			String line = item;
			if (line != null) sb.append(line);
		}
		return sb.toString();
	}

}
