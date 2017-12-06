package com.s_ap.www.opc.ua.pointpool;

import com.s_ap.www.opc.ua.entity.PointBean;
import com.s_ap.www.opc.ua.entity.PointType.PointTypeEnum;
import com.s_ap.www.opc.ua.global.Constant;
import com.s_ap.www.opc.ua.global.Global;

/**
 * 信息点池
 * 
 * @author zihaozhu
 * @date 2017-12-05 11:30:08 AM
 */
public class MessagePointPool extends PointPool {
	private static final String GROUP_NAME = Constant.GROUP_MESSAGE;

	@Override
	public void createPoints() {
		String tag = Constant.TAG1;
		PointBean newpoint = new PointBean(GROUP_NAME, tag, PointTypeEnum.USHORT);
		add(newpoint);

		tag = Constant.TAG2;
		newpoint = new PointBean(GROUP_NAME, tag, PointTypeEnum.USHORT);
		add(newpoint);

		/** 加入全局变量 */
		if (null != Global.getOpcProcess().getGroups().get(GROUP_NAME)) {
			Global.getOpcProcess().getGroups().get(GROUP_NAME).addItems(this.getPoints());
		}
	}

	@Override
	protected void onDataChange(PointBean p) {
		logger.info("-->MessagePointPool:" + p + "");
	}

}
