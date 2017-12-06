package com.s_ap.www.opc.ua.pointpool;

import com.s_ap.www.opc.ua.entity.PointBean;

/**
 * 回调接口
 * 
 * @author zihaozhu
 * @date 2017-12-04 3:17:28 PM
 */
public interface OnPointChange {
	void pointChange(PointBean p);
}
