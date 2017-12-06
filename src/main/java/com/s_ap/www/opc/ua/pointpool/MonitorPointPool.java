package com.s_ap.www.opc.ua.pointpool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.s_ap.www.opc.ua.entity.PointBean;
import com.s_ap.www.opc.ua.global.Constant;

/**
 * 
 * @author zihaozhu
 * @date 2017-12-05 11:29:16 AM
 */
public class MonitorPointPool {
	private PointPool messagePointPool;
	private PointPool dataPointPool;
	private Map<String, List<PointBean>> allMonitorPoints = new HashMap<String, List<PointBean>>();

	public MonitorPointPool() {
		createMessagePointPool();
		addinPool();
	}

	private void addinPool() {
		allMonitorPoints.put(Constant.GROUP_MESSAGE, messagePointPool.getPoints());
		allMonitorPoints.put(Constant.GROUP_DATA, dataPointPool.getPoints());
	}

	private void createMessagePointPool() {
		messagePointPool = new MessagePointPool();
		dataPointPool = new DataPointPool();
	}

	public void createPoints() {
		messagePointPool.createPoints();

	}

	public void monitorPoints() {
		messagePointPool.monitorPoints();
	}

	public List<PointBean> getPoints(String groupName) {
		List<PointBean> points = null;
		if (null != this.allMonitorPoints) {
			points = this.allMonitorPoints.get(groupName);
		}
		return points;
	}
}
