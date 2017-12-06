package com.s_ap.www.opc.ua.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.s_ap.www.opc.ua.entity.GroupBean;
import com.s_ap.www.opc.ua.entity.PointBean;

public abstract class OpcService {
	/** 定义全局组 */
	private Map<String, GroupBean> groups = new HashMap<String, GroupBean>();

	/** 定义全局点 */
	private List<PointBean> points;

	public abstract boolean connect();

	public abstract void disconn();

	public abstract Object read(PointBean p);

	public abstract boolean write(PointBean p, String value);

	public abstract Map<String, Object> read(GroupBean g);

	public abstract boolean write(GroupBean g, List<String> value);

	public abstract List<String> browserNode(String nodeId);

	/**
	 * 一个订阅管理器
	 * 
	 * @param p
	 */
	public abstract void subscribe(PointBean p);

	/**
	 * 一个组对应一个订阅管理器
	 * 
	 * @param g
	 */
	public abstract void subscribeOneSubscription(GroupBean g);

	/**
	 * 一个点对应一个订阅管理器
	 * 
	 * @param g
	 */
	public abstract void subscribeMoreSubscription(GroupBean g);

	public abstract void cancelSubscribe(PointBean p);

	public abstract void cancelSubscribe(GroupBean g);

	public abstract boolean checkPointExists(PointBean p);

	public abstract void addFaultListener();

	public Map<String, GroupBean> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, GroupBean> groups) {
		this.groups = groups;
	}

	public List<PointBean> getPoints() {
		return points;
	}

	public void setPoints(List<PointBean> points) {
		this.points = points;
	}

}
