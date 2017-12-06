package com.s_ap.www.opc.ua.global;

import com.s_ap.www.opc.ua.service.OpcServiceImpl;

/**
 * 
 * @author zihaozhu
 * @date 2017-11-30 3:32:50 PM
 */
public class Global {
	private static OpcServiceImpl opcProcess = new OpcServiceImpl();

	public static OpcServiceImpl getOpcProcess() {
		return opcProcess;
	}

	public static void setOpcProcess(OpcServiceImpl opcProcess) {
		Global.opcProcess = opcProcess;
	}

}
