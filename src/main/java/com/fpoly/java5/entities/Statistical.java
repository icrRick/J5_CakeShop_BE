package com.fpoly.java5.entities;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class Statistical {
	private Date date;
	private long total;
	private String description;
	
	public Statistical(Date date, long total) {
		super();
		this.date = date;
		this.total = total;
	}
}
