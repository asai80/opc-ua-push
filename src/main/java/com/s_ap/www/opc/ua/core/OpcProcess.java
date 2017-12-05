package com.s_ap.www.opc.ua.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OpcProcess {
	/** 定义全局组 */
	private Map<String, Group> groups = new HashMap<String, Group>();

	/** 定义全局点 */
	private List<Point> points;

	public abstract boolean connect();

	public abstract void disconn();

	public abstract Object read(Point p);

	public abstract boolean write(Point p, String value);

	public abstract Map<String, Object> read(Group g);

	public abstract boolean write(Group g, List<String> value);

	public abstract List<String> browserNode(String nodeId);

	/**
	 * 一个订阅管理器
	 * 
	 * @param p
	 */
	public abstract void subscribe(Point p);

	/**
	 * 一个组对应一个订阅管理器
	 * 
	 * @param g
	 */
	public abstract void subscribeOneSubscription(Group g);

	/**
	 * 一个点对应一个订阅管理器
	 * 
	 * @param g
	 */
	public abstract void subscribeMoreSubscription(Group g);

	public abstract void cancelSubscribe(Point p);

	public abstract void cancelSubscribe(Group g);

	public abstract boolean checkPointExists(Point p);

	public abstract void addFaultListener();

	public Map<String, Group> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
