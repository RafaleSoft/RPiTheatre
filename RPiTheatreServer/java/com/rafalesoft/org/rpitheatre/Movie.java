package com.rafalesoft.org.rpitheatre;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"file", "image", "title", "duration", "style", "author", "date", "summary", "actor"})
public class Movie
{
	@XmlElement
	private String file;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	@XmlElement
	private String image;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@XmlElement
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@XmlElement
	private String duration;
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@XmlElement
	private String style;
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}

	@XmlElement
	private String author;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@XmlElement
	private String date;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	@XmlElement
	private String summary;
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@XmlElementWrapper(name = "actors")
	private ArrayList<String>	actor = new ArrayList<String>();
	public ArrayList<String> getActors() {
		return actor;
	}
	public void setActors(ArrayList<String> actors) {
		this.actor = actors;
	}
}

