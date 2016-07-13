package org.de.jmg.showips;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.de.jmg.showips.ShowIPs.msnmFields;

public class parser 
{
	public static LinkedHashMap<String, String[]> critical = new LinkedHashMap<>();
	
	public static class foundIP
	{
		public foundIP(String ip, String line)
		{
			this.ip = ip;
			this.line = line;
			count += 1;
		}

		public String ip = null;
		public String host = null;
		public String line = null;
		public String process = null;

		public int count;
		public int ID;
		
		@Override
		public String toString()
		{
			return ip;

		}
	}

	public static ArrayList<Entry<String, foundIP>> parsefileSQL(File file) throws SQLException
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
		ShowIPs.frame.setTitle("searching ips");
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
								if (r.first()) dbIps.InsertText(r.getInt("ID"),line);
								r.close();
							}
							else
							{
								dbIps.InsertText(r.getInt("ID"),line);
								r.updateInt("count", r.getInt("count") + 1);
								r.updateRow();
								r.close();
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
								if (r.first()) dbIps.InsertText(r.getInt("ID"),line);
								r.close();
							}
							else
							{
								dbIps.InsertText(r.getInt("ID"),line);
								r.updateInt("count", r.getInt("count") + 1);
								r.updateRow();
								r.close();
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
								if (r.first()) dbIps.InsertText(r.getInt("ID"),line);
								r.close();
							}
							else
							{
								dbIps.InsertText(r.getInt("ID"),line);
								r.updateInt("count", r.getInt("count") + 1);
								r.updateRow();
								r.close();
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
								if (r.first()) dbIps.InsertText(r.getInt("ID"),line);
								r.close();
							}
							else
							{
								dbIps.InsertText(r.getInt("ID"),line);
								r.updateInt("count", r.getInt("count") + 1);
								r.updateRow();
								r.close();
							}
						}
					}
					if (ii % 100 == 0)
					{
						ShowIPs.frame.setTitle("read " + ii);
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
			JOptionPane.showMessageDialog(ShowIPs.frame, ex.getMessage());
		}
		
		LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
		ResultSet r = dbIps.getAllIps();
		while (r.next())
		{
			String address = r.getString("address");
			foundIP ip = new foundIP(address, "");
			//foundIP ip = new foundIP("","");//(r.getString("address"),"");
			ip.ID = r.getInt("ID");
			ip.count = r.getInt("count");
			ips.put(r.getString("address"), ip);
		}
		r.close();
		ArrayList<Entry<String, foundIP>> iplist = new ArrayList<>(
				ips.entrySet());
		
		return iplist;
		
	}

	public static ArrayList<Entry<String, foundIP>> parsefile(File file)
	{
		final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		final String IP6PatternStd = "((?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4})";
		final String IP6PatternCompr = "(((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?))";
		final String IP6PatternAlt = "(?<![[:alnum:]]|[[:alnum:]]:)(?:(?:[a-f0-9]{1,4}:){7}[a-f0-9]{1,4}|(?:[a-f0-9]{1,4}:){1,6}:(?:[a-f0-9]{1,4}:){0,5}[a-f0-9]{1,4})(?![[:alnum:]]:?)";
		//final String IP6PatternAll = "(((?<= )[A-Za-z,-]+?_){0,1}[0-9A-Fa-f]{1,4}:?[0-9A-Fa-f]{1,4}:?[0-9A-Fa-f]{0,4}:?[0-9A-Fa-f]{0,4}:?[0-9A-Fa-f]{0,4}:?[0-9A-Fa-f]{0,4}:[0-9A-Fa-f]{0,4})";//"(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
		//final String IP6PatternAll = "(((?<= )[0-9A-Za-z,-]+?_){0,1}([0-9A-Fa-f]{0,4}::?){1,7}[0-9A-Fa-f]{1,4})";
		//final String ValidHostnameRegex = "(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])";
		final Pattern patternip4 = Pattern.compile(IPADDRESS_PATTERN);
		final Pattern patternip6std = Pattern.compile(IP6PatternStd);
		final Pattern patternip6compr = Pattern.compile(IP6PatternCompr);
		final Pattern patternip6alt = Pattern.compile(IP6PatternAlt);
		//final Pattern patternip6all = Pattern.compile(IP6PatternAll);
		//final Pattern patternhostname = Pattern.compile(ValidHostnameRegex);
		
		LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
		ShowIPs.frame.setTitle("searching ips");
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
								//ips.get(foundIP).line += "\n" + line;
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
								//ips.get(foundIP).line += "\n" + line;
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
								//ips.get(foundIP).line += "\n" + line;
								ips.get(foundIP).count += 1;
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
								//ips.get(foundIP).line += "\n" + line;
								ips.get(foundIP).count += 1;
							}
						}
					}
					/*
					matcher = patternip6all.matcher(line);
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
								//ips.get(foundIP).line += "\n" + line;
								ips.get(foundIP).count += 1;
							}
						}
					}
					*/
					/*
					matcher = patternhostname.matcher(line);
					while (matcher.find())
					{
						if (!foundips.contains(matcher.group()))
						{
							foundIP = matcher.group();
							foundips.add(foundIP);
							
							if (!ips.containsKey(foundIP))
							{
								foundIP f = new foundIP(foundIP,line);
								f.host = foundIP;
								ips.put(foundIP, new foundIP(foundIP, line));
							}
							else
							{
								//ips.get(foundIP).line += "\n" + line;
								ips.get(foundIP).count += 1;
							}
							
							
						}
					}
					*/
					if (ii % 100 == 0)
					{
						ShowIPs.frame.setTitle("read " + ii);
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
	
	
	public static ArrayList<Entry<String, foundIP>> parsefilemsnm(File file)
	{
		msnmFields flds = msnmFields.Frame;
		LinkedHashMap<String, foundIP> ips = new LinkedHashMap<>();
		ShowIPs.frame.setTitle("searching ips");
		int ii = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line;
			String fields[];
			//Frame, Time Date, Time Offset, Process, Source, Destination, Protocol Name, Description, Conv Id
			
			while ((line = br.readLine()) != null)
			{
				if (line.length() > 3)
				{
					// process the line.
					ii++;
					fields = line.split("\t");
					flds = msnmFields.Destination;
					if (fields.length >= flds.ordinal() +1 )
					{
						for (int i = 0;i < 2; i++)
						{
							String foundIP = "";
							if (i == 0)
							{
								flds = msnmFields.Source;
								foundIP = fields[flds.ordinal()];
							}
							else
							{
								flds = msnmFields.Destination;
								foundIP = fields[flds.ordinal()];
							}
							if (foundIP != "" && !ips.containsKey(foundIP))
							{
								foundIP F = new foundIP(foundIP, line);
								flds = msnmFields.Process;
								F.process = fields[flds.ordinal()];
								ips.put(foundIP, F);
							}
							else
							{
								//ips.get(foundIP).line += "\n" + line;
								foundIP F = ips.get(foundIP);
								F.count += 1;
								if (F.process == null || F.process.trim().equalsIgnoreCase("") || F.process.length()< 3)
								{
									flds = msnmFields.Process;
									F.process = fields[flds.ordinal()];
								}
							}
						}
					}
					if (ii % 100 == 0)
					{
						ShowIPs.frame.setTitle("read " + ii);
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


	public static String join(List<Entry<String, foundIP>> list, String conjunction)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		ShowIPs.model.setRowCount(0);
		int ii = 0;
		int count = list.size();
		for (Entry<String, foundIP> item : list)
		{
			ii++;
			if (first) first = false;
			else sb.append(conjunction);
			
			InetAddress addr = null;
			String line = null;
			String key = item.getKey();
			try
			{
				String domain = null;
				try
				{
					addr = InetAddress.getByName(key);
				}
				catch (Exception ex)
				{
					try
					{
						NetworkInterface network = NetworkInterface.getByName(key);
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						addr = addresses.nextElement();
					}
					catch (SocketException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String host = null;
				item.getValue().ip = addr.getHostAddress();
				item.getValue().host = addr.getHostName();
				host = item.getValue().host;
				/*
				if (item.getValue().host != null)
				{
					host = item.getValue().host;
					item.getValue().ip = addr.getHostAddress();
				}
				else
				{
					host = addr.getHostName();
					item.getValue().host = host;
				}
				*/
				ShowIPs.frame.setTitle("found host:" + host + " " + ii + "("
						+ count + ")");
				if (!host.equalsIgnoreCase(item.getValue().ip))
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
				line = item.getValue().ip + " " + host + " "
						+ item.getValue().line;
				if (item.getValue().line.contains("DROP"))
				{
					type += " drop";
				}
				if (critical.containsKey(item.getValue().ip)
						|| critical.containsKey(domain)
						|| critical.containsKey(host))
				{
					type += " critical";
				}
				else
				{
					if (!host.equalsIgnoreCase(item.getValue().ip))
					{
						int i = -1;
						while ((i = host.indexOf('.', i + 1)) >= 0)
						{
							domain = host.substring(i + 1);
							if (domain.indexOf('.') < 1) break;
							if (critical.containsKey(domain) || (critical.containsKey("www." + domain)))
							{
								type += " critical";
								break;
							}
						}
					}
				}
				ShowIPs.model.addRow(new Object[] { item.getValue().ip, host, type,
						item.getValue().process, item.getValue().line, item.getValue().count });
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				line = item.getValue().ip + " " + " " + item.getValue().line;
				ShowIPs.model.addRow(new Object[] { item.getValue().ip, "invalid", "",
						item.getValue().process, item.getValue().line, item.getValue().count });

			}
			if (line != null) sb.append(line);
		}
		ShowIPs.listview.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		ShowIPs.listview.doLayout();
		ShowIPs.frame.setTitle(ShowIPs.filename);
		return sb.toString();
	}

	public static void loadCritical() {
		File data = new File(
				"/opt/critical-stack/frameworks/intel/master-public.bro.dat");
		if (!data.exists())
		{
			data = new File(
					"//192.168.2.25/critical-stack/frameworks/intel/master-public.bro.dat");
		}
		if (!data.exists())
		{
			ShowIPs.fc.setDialogTitle("Select BRO database");
			int returnVal = ShowIPs.fc.showOpenDialog(ShowIPs.listview);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				data = ShowIPs.fc.getSelectedFile();
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
						String key = fields[0];
						String domain = null;
						try
						{
							if (fields[1].equalsIgnoreCase("Intel::URL"))
							{
								domain = new URL("http://" + key).getHost();
								//getting hostname for URL's
								if (!domain.equalsIgnoreCase(key))
								{
									key = domain;
								}
							}
							
						}
						catch (Exception ex)
						{
							
						}
						if (!critical.containsKey(key))
						{
							critical.put(key, fields);
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

}
