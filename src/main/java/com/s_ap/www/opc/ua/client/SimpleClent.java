package com.s_ap.www.opc.ua.client;

import java.util.Map;

import com.s_ap.www.opc.ua.core.Group;
import com.s_ap.www.opc.ua.core.OpcProcess;
import com.s_ap.www.opc.ua.global.Constant;
import com.s_ap.www.opc.ua.global.Global;
import com.s_ap.www.opc.ua.pointpool.MonitorPointPool;

public class SimpleClent {
	public void monitorPoint() {
		MonitorPointPool monitorPointPool = new MonitorPointPool();
		OpcProcess opcProcess = Global.getOpcProcess();

		/** 创建opc */
		createOpc(opcProcess);

		/** 创建监控点 */
		monitorPointPool.createPoints();

		/** 订阅变化点 */
		Group group = opcProcess.getGroups().get(Constant.GROUP_MESSAGE); 
//		Group group2 = opcProcess.getGroups().get(Constant.GROUP_DATA); 
		opcProcess.setPoints(monitorPointPool.getPoints(group.getName()));
		opcProcess.subscribeMoreSubscription(group);

		/** 监控变化点 */
		monitorPointPool.monitorPoints();

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void createOpc(OpcProcess opcProcess) {
		Map<String, Group> groups = opcProcess.getGroups();

		/** 创建Group */
		Group group = new Group(Constant.GROUP_MESSAGE);
		groups.put(Constant.GROUP_MESSAGE, group);

		opcProcess.connect();
	}
}
