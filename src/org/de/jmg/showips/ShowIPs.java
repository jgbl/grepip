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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
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
	public static JFileChooser fc = new JFileChooser();
	public static JTable listview;
	public static DefaultTableModel model = new DefaultTableModel(new Object[] {
			"IP", "Host", "Type", "Log", "Count" }, 0);
	public static LinkedHashMap<String, String[]> critical = new LinkedHashMap<>();
	public static ActionListener ActionL = new ActionListener()
	{
		class foundIP
		{
			public foundIP(String ip, String line)
			{
				this.ip = ip;
				this.line = line;
				count += 1;
			}

			public String ip;
			public String line;
			public int count;
			public int ID;

			@Override
			public String toString()
			{
				return ip;

			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			fc.setDialogTitle("Select log file");
			int returnVal = fc.showOpenDialog(button);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				ArrayList<Entry<String, foundIP>> iplist = parsefile(file);
				String output = join(iplist, "\n");
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
		private ArrayList<Entry<String, foundIP>> parsefileSQL(File file) throws SQLException
		{
			final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			final String IP6PatternStd = "(^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4})";
			final String IP6PatternCompr = "(^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?))";
			final String IP6PatternAlt = "(?<![[:alnum:]]|[[:alnum:]]:)(?:(?:[a-f0-9]{1,4}:){7}[a-f0-9]{1,4}|(?:[a-f0-9]{1,4}:){1,6}:(?:[a-f0-9]{1,4}:){0,5}[a-f0-9]{1,4})(?![[:alnum:]]:?)";
			final Pattern patternip4 = Pattern.compile(IPADDRESS_PATTERN);
			final Pattern patternip6std = Pattern.compile(IP6PatternStd);
			final Pattern patternip6compr = Pattern.compile(IP6PatternCompr);
			final Pattern patternip6alt = Pattern.compile(IP6PatternAlt);
			//LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
			frame.setTitle("searching ips");
			int ii = 0;
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				String line;
				ArrayList<String> foundips = new ArrayList<>();
				while ((line = br.readLine()) != null)
				{
					if (line.length() > 3)
					{
						// process the line.
						String foundIP = "";
						foundips.clear();
						Matcher matcher = patternip4.matcher(line);
						ii++;
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								ResultSet r = dbIps.queryIP(foundIP);
								if (r == null || !r.first())
								{
									if(r != null) r.close();
									r = dbIps.InsertIP(foundIP,1,0);
									dbIps.InsertText(r.getInt("ID"),line);
								}
								else
								{
									dbIps.InsertText(r.getInt("ID"),line);
									r.updateInt("count", r.getInt("count") + 1);
									r.updateRow();
								}
							}
						}
						matcher = patternip6std.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								ResultSet r = dbIps.queryIP(foundIP);
								if (r == null || !r.first())
								{
									if(r != null) r.close();
									r = dbIps.InsertIP(foundIP,1,0);
									dbIps.InsertText(r.getInt("ID"),line);
								}
								else
								{
									dbIps.InsertText(r.getInt("ID"),line);
									r.updateInt("count", r.getInt("count") + 1);
									r.updateRow();
								}
							}
						}
						matcher = patternip6compr.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								ResultSet r = dbIps.queryIP(foundIP);
								if (r == null || !r.first())
								{
									if(r != null) r.close();
									r = dbIps.InsertIP(foundIP,1,0);
									dbIps.InsertText(r.getInt("ID"),line);
								}
								else
								{
									dbIps.InsertText(r.getInt("ID"),line);
									r.updateInt("count", r.getInt("count") + 1);
									r.updateRow();
								}
							}
						}
						matcher = patternip6alt.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								ResultSet r = dbIps.queryIP(foundIP);
								if (r == null || !r.first())
								{
									if(r != null) r.close();
									r = dbIps.InsertIP(foundIP,1,0);
									dbIps.InsertText(r.getInt("ID"),line);
								}
								else
								{
									dbIps.InsertText(r.getInt("ID"),line);
									r.updateInt("count", r.getInt("count") + 1);
									r.updateRow();
								}
							}
						}
						if (ii % 100 == 0)
						{
							frame.setTitle("read " + ii);
						}

						
					}
					
				}
			}
			// This is where a real application would open the file.
			catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (SQLException ex)
			{
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			}
			
			LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
			ResultSet r = dbIps.getAllIps();
			while (r.next())
			{
				foundIP ip = new foundIP(r.getString("address"),"");
				ip.ID = r.getInt("ID");
				ips.put(r.getString("address"), ip);
			}
			
			ArrayList<Entry<String, foundIP>> iplist = new ArrayList<>(
					ips.entrySet());
			
			return iplist;
			
		}

		private ArrayList<Entry<String, foundIP>> parsefile(File file)
		{
			final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			final String IP6PatternStd = "(^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4})";
			final String IP6PatternCompr = "(^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?))";
			final String IP6PatternAlt = "(?<![[:alnum:]]|[[:alnum:]]:)(?:(?:[a-f0-9]{1,4}:){7}[a-f0-9]{1,4}|(?:[a-f0-9]{1,4}:){1,6}:(?:[a-f0-9]{1,4}:){0,5}[a-f0-9]{1,4})(?![[:alnum:]]:?)";
			final Pattern patternip4 = Pattern.compile(IPADDRESS_PATTERN);
			final Pattern patternip6std = Pattern.compile(IP6PatternStd);
			final Pattern patternip6compr = Pattern.compile(IP6PatternCompr);
			final Pattern patternip6alt = Pattern.compile(IP6PatternAlt);
			LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
			frame.setTitle("searching ips");
			int ii = 0;
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				String line;
				ArrayList<String> foundips = new ArrayList<>();
				while ((line = br.readLine()) != null)
				{
					if (line.length() > 3)
					{
						// process the line.
						String foundIP = "";
						foundips.clear();
						Matcher matcher = patternip4.matcher(line);
						ii++;
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								if (!ips.containsKey(foundIP))
								{
									ips.put(foundIP, new foundIP(foundIP, line));
								}
								else
								{
									ips.get(foundIP).line += "\n" + line;
									ips.get(foundIP).count += 1;
								}
							}
						}
						matcher = patternip6std.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								if (!ips.containsKey(foundIP))
								{
									ips.put(foundIP, new foundIP(foundIP, line));
								}
								else
								{
									ips.get(foundIP).line += "\n" + line;
									ips.get(foundIP).count += 1;
								}
							}
						}
						matcher = patternip6compr.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								if (!ips.containsKey(foundIP))
								{
									ips.put(foundIP, new foundIP(foundIP, line));
								}
								else
								{
									ips.get(foundIP).line += "\n" + line;
								}
							}
						}
						matcher = patternip6alt.matcher(line);
						while (matcher.find())
						{
							if (!foundips.contains(matcher.group()))
							{
								foundIP = matcher.group();
								foundips.add(foundIP);
								if (!ips.containsKey(foundIP))
								{
									ips.put(foundIP, new foundIP(foundIP, line));
								}
								else
								{
									ips.get(foundIP).line += "\n" + line;
								}
							}
						}
						if (ii % 100 == 0)
						{
							frame.setTitle("read " + ii);
						}

						
					}
					
				}
			}
			// This is where a real application would open the file.
			catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ArrayList<Entry<String, foundIP>> iplist = new ArrayList<>(
					ips.entrySet());
			Collections.sort(iplist, new Comparator<Entry<String, foundIP>>()
			{

				@Override
				public int compare(Entry<String, foundIP> o1,
						Entry<String, foundIP> o2)
				{
					// TODO Auto-generated method stub
					return o1.getKey().compareToIgnoreCase(o2.getKey());
				}
			});
			return iplist;
		}

		String join(List<Entry<String, foundIP>> list, String conjunction)
		{
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			model.setRowCount(0);
			int ii = 0;
			int count = list.size();
			for (Entry<String, foundIP> item : list)
			{
				ii++;
				if (first) first = false;
				else sb.append(conjunction);
				InetAddress addr = null;
				String line = null;
				try
				{
					String domain = null;
					addr = InetAddress.getByName(item.getKey());
					String host = addr.getHostName();
					frame.setTitle("found host:" + host + " " + ii + "("
							+ count + ")");
					if (!host.equalsIgnoreCase(item.getKey()))
					{
						domain = new URI("//" + host).getHost();
						if (domain != null && domain.equalsIgnoreCase(host))
						{
							domain = domain.substring(1 + domain.lastIndexOf(
									".", domain.lastIndexOf(".") - 1));
						}
					}
					String type = "extern";
					if (addr.isAnyLocalAddress() || addr.isLinkLocalAddress()
							|| addr.isLoopbackAddress()
							|| addr.isSiteLocalAddress())
					{
						type = "local";
					}
					else if (addr.isMCLinkLocal() || addr.isMCNodeLocal()
							|| addr.isMCOrgLocal() || addr.isMCSiteLocal())
					{
						type = "multi local";
					}
					else if (addr.isMulticastAddress())
					{
						type = "multi";
					}
					line = item.getKey() + " " + host + " "
							+ item.getValue().line;
					if (item.getValue().line.contains("DROP"))
					{
						type += " drop";
					}
					if (critical.containsKey(item.getKey())
							|| critical.containsKey(domain)
							|| critical.containsKey(host))
					{
						type += " critical";
					}
					else
					{
						if (!host.equalsIgnoreCase(item.getKey()))
						{
							int i = -1;
							while ((i = host.indexOf('.', i + 1)) >= 0)
							{
								domain = host.substring(i + 1);
								if (critical.containsKey(domain))
								{
									type += " critical";
									break;
								}
							}
						}
					}
					model.addRow(new Object[] { item.getKey(), host, type,
							item.getValue().line, item.getValue().count });
				}
				catch (UnknownHostException | URISyntaxException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					line = item.getKey() + " " + " " + item.getValue().line;
					model.addRow(new Object[] { item.getKey(), "invalid", "",
							item.getValue().line });

				}
				if (line != null) sb.append(line);
			}
			listview.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			listview.doLayout();
			return sb.toString();
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
		JScrollPane pane = new JScrollPane(listview);
		frame.add(pane, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
		int width = 300;
		int height = 300;
		frame.setSize(width, height);
		frame.addWindowListener(WinLi);
		frame.setVisible(true);
		try 
		{
			dbIps.connecttodatabase();
		} 
		catch (SQLException e2) 
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(frame, e2.getMessage());
		}
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
												if (critical.containsKey(ip))
												{
													textArea.setText(join(
															critical.get(ip),
															"\n"));
												}
												else if (critical
														.containsKey(domain))
												{
													textArea.setText(join(
															critical.get(domain),
															"\n"));
												}
												else if (critical
														.containsKey(host))
												{
													textArea.setText(join(
															critical.get(host),
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
															if (critical
																	.containsKey(domain))
															{
																textArea.setText(join(
																		critical.get(domain),
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
										if (OSValidator.isWindows())
										{
											JOptionPane
													.showMessageDialog(
															listview,
															"This function is not available \non Windows!");
										}
										else
										{
											int exitCode = 0;
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

								}
							}
							else if (col == 3)
							{
								String log = (String) tableModel.getValueAt(
										row, 3);
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
		File data = new File(
				"/opt/critical-stack/frameworks/intel/master-public.bro.dat");
		if (!data.exists())
		{
			data = new File(
					"//192.168.2.25/critical-stack/frameworks/intel/master-public.bro.dat");
		}
		if (!data.exists())
		{
			fc.setDialogTitle("Select BRO database");
			int returnVal = fc.showOpenDialog(listview);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				data = fc.getSelectedFile();
			}
		}
		if (data.exists())
		{
			try (BufferedReader br = new BufferedReader(new FileReader(data)))
			{
				String line;
				while ((line = br.readLine()) != null)
				{
					// process the line.
					if (!line.startsWith("#field"))
					{
						String[] fields = line.split("\\t");
						if (!critical.containsKey(fields[0]))
						{
							critical.put(fields[0], fields);
						}
					}
				}
			}
			// This is where a real application would open the file.
			catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
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