package com.s_ap.www.opc.ua.core;

import java.util.List;

/**
 * @author zihaozhu
 * @date 2017-11-27 2:39:22 PM
 */
public class Group {
	private String name;
	private List<Point> points;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public Group(String name) {
		this.name = name;
	}

	public void addItems(List<Point> points) {
		if (null == this.points) {
			this.points = points;

		} else {
			points.forEach((point) -> {
				if (!this.points.contains(point)) {
					this.points.add(point);
				}
			});
		}
	}
}
