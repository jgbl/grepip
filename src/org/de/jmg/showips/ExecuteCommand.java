package org.de.jmg.showips;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ExecuteCommand
{

	public String cmd;
	public String args;

	public ExecuteCommand(final String cmd, final String args)
	{
		this.cmd = cmd;
		this.args = args;
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				}
				catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex)
				{
				}

				JFrame frame = new JFrame(cmd);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public class TestPane extends JPanel
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TestPane()
		{
			setLayout(new BorderLayout());
			JTextArea ta = new JTextArea(40, 80);
			add(new JScrollPane(ta));

			new ProcessWorker(ta).execute();
		}

		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(400, 400);
		}
	}

	public interface Consumer
	{
		public void consume(String value);
	}

	public class ProcessWorker extends SwingWorker<Integer, String> implements
			Consumer
	{

		private JTextArea textArea;

		public ProcessWorker(JTextArea textArea)
		{
			this.textArea = textArea;
		}

		@Override
		protected void process(List<String> chunks)
		{
			for (String value : chunks)
			{
				textArea.append(value);
			}
		}

		@Override
		protected Integer doInBackground() throws Exception
		{
			// Forced delay to allow the screen to update
			Thread.sleep(5000);
			publish("Starting...\n");
			int exitCode = 0;
			ProcessBuilder pb = new ProcessBuilder("bash", cmd, args);
			// pb.directory(new
			// File("C:\\DevWork\\personal\\java\\projects\\wip\\StackOverflow\\HelloWorld\\dist"));
			pb.redirectError();
			try
			{
				Process pro = pb.start();
				InputConsumer ic = new InputConsumer(pro.getInputStream(), this);
				System.out.println("...Waiting");
				exitCode = pro.waitFor();

				ic.join();

				System.out.println("Process exited with " + exitCode + "\n");

			}
			catch (Exception e)
			{
				System.out.println("sorry" + e);
			}
			publish("Process exited with " + exitCode);
			return exitCode;
		}

		@Override
		public void consume(String value)
		{
			publish(value);
		}
	}

	public static class InputConsumer extends Thread
	{

		private InputStream is;
		private Consumer consumer;

		public InputConsumer(InputStream is, Consumer consumer)
		{
			this.is = is;
			this.consumer = consumer;
			start();
		}

		@Override
		public void run()
		{
			try
			{
				int in = -1;
				while ((in = is.read()) != -1)
				{
					// System.out.print((char) in);
					consumer.consume(Character.toString((char) in));
				}
			}
			catch (IOException exp)
			{
				exp.printStackTrace();
			}
		}
	}
}