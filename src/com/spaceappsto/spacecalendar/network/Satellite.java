package com.spaceappsto.spacecalendar.network;

import java.util.ArrayList;
import java.util.Date;

public class Satellite {

	public String url;
	public boolean active;
	public String name;
	public int id;

	public ArrayList<Date> dates;

	@Override
	public String toString() {
			return name + " " + super.toString();
	}
}
