package com.rafalesoft.org.rpitheatre;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class RPiTheatreServer {

	private static Moviestore moviestore = null;
	private static TheatreServer server = null;
	
	private static byte[] ip = new byte[4];
	private static String ip_str;
	private static int port = 0;
	
	public static void main(String[] args) {
	
		ip[0] = 127;
		ip[1] = 0;
		ip[2] = 0;
		ip[3] = 1;
		ip_str = new String("127.0.0.1");
		port = 2048;
		
		if (!parseCmdLine(args))
		{
			System.out.println("Unable to initialize server! Exiting.");
			return;
		}
		if (!loadConfig())
		{
			System.out.println("Unable to initialize server! Exiting.");
		}
		else
		{
			server = new TheatreServer(moviestore);
			if (!server.init())
			{
				System.out.println("Unable to initialize server! Exiting.");
			}
			else
				server.run(ip_str,port);
		}
	}
	
	private static Boolean parseCmdLine(String[] args)
	{
		for (int i=0;i<args.length - 1;i++) {
			String arg = args[i];
			if (0 == arg.compareTo("-ip")) {
				i++;
				ip_str = args[i];
				// TODO: check it is an ipaddress ?
				// TODO: convert to a byte[4] to use InetAddress.getByName(ip)
			}
			else if (0 == arg.compareTo("-port")) {
				i++;
				port = Integer.parseInt(args[i]);
			}
		}
		
		return true;
	}
	
	private static Boolean loadConfig()
	{
	    // create JAXB context and instantiate unmarshaller
	    JAXBContext context;
		try
		{
			context = JAXBContext.newInstance(Moviestore.class);
		    Unmarshaller um = context.createUnmarshaller();
		    moviestore = (Moviestore) um.unmarshal(new FileReader(System.getProperty("java.class.path")+"/"+Moviestore.MOVIESTORE_XML));
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	    return true;
	}
	
	public static Boolean exportConfig()
	{
	    // create JAXB context and instantiate marshaller
	    JAXBContext context;
		try
		{
			context = JAXBContext.newInstance(Moviestore.class);
			Marshaller m = context.createMarshaller();
			m.marshal(moviestore,  new FileWriter("test.xml")) ;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	    return true;
	}
}
