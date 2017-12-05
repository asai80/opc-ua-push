package com.s_ap.www.opc.ua.pointpool;

import com.s_ap.www.opc.ua.core.Point;
import com.s_ap.www.opc.ua.core.PointPool;
import com.s_ap.www.opc.ua.core.PointType.PointTypeEnum;
import com.s_ap.www.opc.ua.global.Constant;
import com.s_ap.www.opc.ua.global.Global;

/**
 * 数据点池
 * 
 * @author zihaozhu
 * @date 2017-12-05 11:30:08 AM
 */
public class DataPointPool extends PointPool {
	private static final String GROUP_NAME = Constant.GROUP_DATA;

	@Override
	public void createPoints() {
		String tag = Constant.TAG3;
		Point newpoint = new Point(GROUP_NAME, tag, PointTypeEnum.STRING);
		add(newpoint);

		tag = Constant.TAG4;
		newpoint = new Point(GROUP_NAME, tag, PointTypeEnum.STRING);
		add(newpoint);

		/** 加入全局变量 */
		if (null != Global.getOpcProcess().getGroups().get(GROUP_NAME)) {
			Global.getOpcProcess().getGroups().get(GROUP_NAME).addItems(this.getPoints());
		}
	}

	@Override
	protected void onDataChange(Point p) {
		logger.info("-->DataPointPool:" + p + "");
	}

}
