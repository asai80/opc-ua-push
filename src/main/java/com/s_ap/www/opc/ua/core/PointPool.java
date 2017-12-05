package com.s_ap.www.opc.ua.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author zihaozhu
 * @date 2017-12-05 11:27:20 AM
 */
public abstract class PointPool {
	protected static Logger logger = (Logger) LoggerFactory.getLogger(PointPool.class);

	/** 创建点池对象,监控的点的源头 */
	private List<Point> points = new ArrayList<Point>();

	protected abstract void onDataChange(Point p);

	public abstract void createPoints();

	public List<Point> getPoints() {
		return points;
	}

	public void monitorPoints() {
		getPoints().forEach((p) -> {
			/** 注册事件并处理 */
			p.registerPointChangeHandler(point -> {
				onDataChange(point);
			});
		});
	}

	public void add(Point p) {
		if (!this.getPoints().contains(p)) {
			this.getPoints().add(p);
		}
	}
}
