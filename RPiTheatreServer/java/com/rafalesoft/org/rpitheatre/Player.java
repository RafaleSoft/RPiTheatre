package com.rafalesoft.org.rpitheatre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable
{
	private String file;
	private String player;
	private String location;
	private int	   currentVolume = 25;
	private Process process = null;
	private OutputStream control = null;
	
	public Player(String file,String player,String location)
	{
		this.file = file;
		this.player = player;
		this.location = location;
	}
	
	public String getMovie()
	{
		return file;
	}
	
	public int getVolume()
	{
		return currentVolume;
	}
	
	@Override
	public void run()
	{
		List<String> command = new ArrayList<String>();
	    command.add(player);
	    command.add("--key-config");
	    command.add("/home/pi/RPiTheatreServer/omxkeyconfig.txt");
	    //command.add("-i");
	    command.add(location+"/"+file);
	    //command.add("-f");
	    //command.add("mpegts");
	    //command.add("udp://192.168.1.29:2049");
	    
	    // omxplayer udp://192.168.1.15:2049
	    // c:\ffmpeg\bin\ffmpeg.exe -i Z:\CARS\Cars.mp4 -c:v libx264 -f mpegts udp://192.168.1.15:2049

		System.out.println("Command line:" + command.toString());
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream();
		try {
			process = builder.start();
			InputStream is = process.getInputStream();
			control = process.getOutputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr,128);
			String line;
			while ((line = br.readLine()) != null) {
			      System.out.println(line);
			}
			System.out.println("output:" + line);
			System.out.println("Program terminated!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		try {
			control.write('q');
			control.flush();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void pause()
	{
		try {
			control.write('p');
			control.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void next()
	{
		try {
			control.write('b');
			control.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prev()
	{
		try {
			control.write('a');
			control.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void volume(int vol)
	{
		try {
			while (vol > currentVolume) {
				control.write('+');
				currentVolume++;
				control.flush();
				Thread.sleep(100);
			}
			while (vol < currentVolume) {
				control.write('-');
				currentVolume--;
				control.flush();
				Thread.sleep(100);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
