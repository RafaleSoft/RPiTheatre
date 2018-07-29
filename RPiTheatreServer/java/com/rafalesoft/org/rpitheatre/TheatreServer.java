package com.rafalesoft.org.rpitheatre;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TheatreServer
{
	private String mc_play = "PLAY";
	private String mc_cmd = "COMMAND";
	private String mc_stop = "STOP";
	private String mc_pause = "PAUSE";
	private String mc_next = "NEXT";
	private String mc_prev = "PREV";
	private String mc_details = "DETAILS";
	private String mc_catalog = "CATALOG";
	private String mc_quit = "QUIT";
	private String mc_off = "OFF";
	private String mc_volume = "VOLUME";

	private Logger logger = Logger.getLogger("TheatreServer");
	
	private Moviestore moviestore = null;
	private Player m_player = null;
	
	public TheatreServer(Moviestore moviestore)
	{
		this.moviestore = moviestore;
	}
	
	public Boolean init()
	{
	    return true;
	}

	private void sendString(OutputStream output,String string)
	{
		try
		{
			String stringSize = String.format("%04d", string.length());
			
			output.write(stringSize.getBytes());
			logger.log(Level.FINE, "String size:["+stringSize+"]");
			
			output.write(string.getBytes());
			logger.log(Level.FINE, "String sent to client:["+string+"]");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void sendFile(OutputStream output, File img)
	{
		try
		{
			if (img.canRead())
			{
				String fileSize = String.format("%08d", img.length());
				output.write(fileSize.getBytes());
				logger.log(Level.FINE, "File size:["+fileSize+"]");
				
				byte [] data = new byte[(int) img.length()];
				FileInputStream fis = new FileInputStream(img);
				int bytesRead = fis.read(data);
				output.write(data);
				fis.close();
				
				logger.log(Level.FINE, "File ["+img.getName()+"] bytes sent to client:" + bytesRead);
			}
			else
			{
				String fileSize = String.format("%08d", 0);
				output.write(fileSize.getBytes());
				logger.log(Level.INFO, "File size:["+fileSize+"]");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run(String ipaddr,int port)
	{
		try
		{
			ServerSocket s = new ServerSocket(port,0,InetAddress.getByName(ipaddr));
			s.setReuseAddress(true);
			
			System.out.println("Server address: "+s.getLocalSocketAddress().toString());
			
			boolean terminateServer = false;
			while (!terminateServer)
			{
				System.out.println("Waiting client connection ...");
				terminateServer = processNextClient(s);
			}
			
			System.out.println("Server terminating ...");
			s.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void processQuit(OutputStream output,String msg)
	{
		String cmd = msg.substring(mc_quit.length());
		System.out.println("Command received: ["+cmd+"]");

		if (m_player != null) {
			System.out.println("Stopping movie: "+m_player.getMovie());
			m_player.stop();
		}
		
		if (0 == cmd.compareTo(mc_quit))
		{
			System.out.println("Exiting, bye.");
			sendString(output,"RPi Theater Server is stopping.");
		}
		else if (0 == cmd.compareTo(mc_off))
		{
			System.out.println("Exiting and power off, bye.");
			sendString(output,"RPi Theater Server is stopping and poweroff.");
		}
		else
			System.out.println("unknown quit sub-command");
	}
	private void processStop(OutputStream output)
	{
		if (m_player != null) {
			logger.log(Level.INFO, "Stopping movie: "+m_player.getMovie());
			m_player.stop();
			
			String stop = "Stopped " + m_player.getMovie();
			sendString(output,stop);
		}
		else
			logger.log(Level.WARNING, "Movie not started.");
	}
	private void processPause(OutputStream output)
	{
		if (m_player != null) {
			logger.log(Level.INFO, "Pausing movie: "+m_player.getMovie());
			m_player.pause();
			
			String pause = "Paused " + m_player.getMovie();
			sendString(output,pause);
		}
		else
			logger.log(Level.WARNING, "Movie not started.");
	}
	private void processNext(OutputStream output)
	{
		if (m_player != null) {
			logger.log(Level.INFO, "Next chapter in movie: "+m_player.getMovie());
			m_player.next();
			
			String next = "Next " + m_player.getMovie();
			sendString(output,next);
		}
		else
			logger.log(Level.WARNING, "Movie not started.");
	}
	private void processPrev(OutputStream output)
	{
		if (m_player != null) {
			logger.log(Level.INFO, "Prev chapter in movie: "+m_player.getMovie());
			m_player.prev();
			
			String prev = "Prev " + m_player.getMovie();
			sendString(output,prev);
		}
		else
			logger.log(Level.WARNING, "Movie not started.");
	}
	private void processVolume(String volume, OutputStream output)
	{
		if (m_player != null) {
			String v = volume.substring(mc_volume.length());
			int vol = Integer.parseInt(v);
			
			logger.log(Level.INFO, "Volume of movie: "+m_player.getVolume());
			m_player.volume(vol);
			
			v = "Volume " + m_player.getVolume();
			sendString(output,v);
		}
		else
			logger.log(Level.WARNING, "Movie not started.");
	}
	private void processPlay(String movie,OutputStream output)
	{
		String pm = movie.substring(mc_play.length());
		logger.log(Level.INFO, "Playing movie: "+pm);
		
		Movie m = moviestore.getMovieFromTitle(pm);
		if (null != m) {
			m_player = new Player(m.getFile(),moviestore.getPlayer(),moviestore.getLocation());
			Thread t = new Thread(m_player);
			t.start();
			
			String play = "Playing " + pm;
			sendString(output,play);
		}
		else
			logger.log(Level.WARNING, "Movie not found in catalog.");
	}
	
	private void processDetails(String movie,OutputStream output)
	{
		String mm = movie.substring(mc_details.length());
		logger.log(Level.INFO, "Collecting details for: "+mm);
	
		Movie m = moviestore.getMovieFromTitle(mm);
		if (null != m)
		{
			String style = m.getStyle();
			String date = m.getDate();
			sendString(output,style+" - "+date);
			
			String author = m.getAuthor();
			sendString(output,author);
			
			String actors = "";
			for (String actor:m.getActors())
				actors += actor + ", ";
			sendString(output,actors);
			
			String duration = m.getDuration();
			sendString(output,duration);
			
			String resume = m.getSummary();
			sendString(output,resume);
			
			String image = m.getImage();
			sendString(output,image);
			File img = new File(moviestore.getLocation()+"/"+image);
			sendFile(output,img);
		}
		else
			logger.log(Level.WARNING, "Movie not found in catalog.");
	}
	
	private void processCatalog(String msg,OutputStream output)
	{
		try
		{
			String order = msg.substring(mc_details.length());
			logger.log(Level.INFO, "Querying catalog order: "+order);
			
			moviestore.sort(order);
		
			String catalogSize = String.format("%04d", moviestore.getNbMovies());
			output.write(catalogSize.getBytes());
			logger.log(Level.INFO, "Catalog size:["+catalogSize+"]");
			
			for (int i=0;i<moviestore.getNbMovies();i++)
			{
				Movie movie = moviestore.getMovieFromIndex(i);
				
				String title = movie.getTitle();
				sendString(output,title);

				String date_style_duration = movie.getStyle();
				date_style_duration += " - " + movie.getDate();
				date_style_duration += " - approx. " + movie.getDuration();
				sendString(output,date_style_duration);
				
				String author = movie.getAuthor();
				sendString(output,author);
				
				String image = movie.getImage();
				sendString(output,image);
				File img = new File(moviestore.getLocation()+"/"+image);
				sendFile(output,img);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	

	private boolean processNextClient(ServerSocket s)
	{
		boolean quitServer = false;
		
		try
		{
			Socket client = s.accept();
			logger.log(Level.FINE, "Incoming connection from: " + client.getRemoteSocketAddress().toString());
			InputStream input = client.getInputStream();
			
			ByteBuffer buffer = ByteBuffer.allocate(256);
			boolean exit = false;
			while (!exit)
			{	
				int len = input.read(buffer.array());
				if (len < 0)
				{
					logger.log(Level.FINE, "Read client returned:" + len);
					logger.log(Level.INFO, "Terminating client, bye.");
					exit = true;
				}
				else
				{
					String msg = new String(buffer.array(),0,len);
					logger.log(Level.INFO, "Received command: ["+msg+"]");
					
					if (msg.startsWith(mc_quit))
					{
						processQuit(client.getOutputStream(),msg);
						quitServer = true;
						exit = true;
					}
					else if (msg.startsWith(mc_catalog))
					{
						processCatalog(msg,client.getOutputStream());
					}
					else if (msg.startsWith(mc_details))
					{
						processDetails(msg,client.getOutputStream());
					}
					else if (msg.startsWith(mc_play))
					{
						processPlay(msg,client.getOutputStream());
					}
					else if (msg.startsWith(mc_cmd))
					{
						String cmd = msg.substring(mc_cmd.length());
						logger.log(Level.INFO, "Command received: ["+cmd+"]");

						if (0 == cmd.compareTo(mc_stop))
							processStop(client.getOutputStream());
						else if (0 == cmd.compareTo(mc_pause))
							processPause(client.getOutputStream());
						else if (0 == cmd.compareTo(mc_next))
							processNext(client.getOutputStream());
						else if (0 == cmd.compareTo(mc_prev))
							processPrev(client.getOutputStream());
						else if (cmd.startsWith(mc_volume))
							processVolume(cmd,client.getOutputStream());
						else
							logger.log(Level.WARNING, "Unknown sub-command !");
					}
					else
					{
						logger.log(Level.WARNING, "Unknown command");
					}
				}
			}
			
			client.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return quitServer;
	}
}
