package com.s_ap.www.opc.ua.entity;

import java.util.List;

/**
 * @author zihaozhu
 * @date 2017-11-27 2:39:22 PM
 */
public class GroupBean {
	private String name;
	private List<PointBean> points;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PointBean> getPoints() {
		return points;
	}

	public void setPoints(List<PointBean> points) {
		this.points = points;
	}

	public GroupBean(String name) {
		this.name = name;
	}

	public void addItems(List<PointBean> points) {
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
