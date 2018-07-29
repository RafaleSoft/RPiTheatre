package com.rafalesoft.org.rpitheatre;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "moviestore")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"location", "player", "movie"})
public class Moviestore
{
	public final static String MOVIESTORE_XML = "com/rpitheatre/Catalog.xml";
	
	@XmlElement
	private String location;
	public String getLocation() {
		return location;
	}
	public void setLocation(String _location) {
		this.location = _location;
	}
	
	@XmlElement
	private String player;
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String _player) {
		this.player = _player;
	}
	
	@XmlElementWrapper(name = "movielist")
	private ArrayList<Movie> movie = new ArrayList<Movie>();
	public ArrayList<Movie> getMovielist() {
	    return movie;
	}
	public void setMovielist(ArrayList<Movie> movielist) {
	    this.movie = movielist;
	}

	
	public int getNbMovies()
	{
		return movie.size();
	}
	
	public void addMovie(Movie mov)
	{
		movie.add(mov);
	}
	
	public Movie getMovieFromIndex(int index)
	{
		if ((index >= 0) && (index < movie.size()))
			return movie.get(index);	
		else
			return null;
	}
	
	public Movie getMovieFromTitle(String title)
	{
		for (Movie m:movie) {
			if (m.getTitle().equals(title))
				return m;
		}
		return null;
	}
	
	public void sort(String order)
	{
		if (0 == order.compareTo("LIST"))
			System.out.println("Catalog sorted by: "+order);
		else
			System.out.println("Unsupported catalog order: " + order);
	}
}
