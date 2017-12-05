package com.s_ap.www.opc.ua.global;

import com.s_ap.www.opc.ua.core.OpcProcessImpl;

/**
 * 
 * @author zihaozhu
 * @date 2017-11-30 3:32:50 PM
 */
public class Global {
	private static OpcProcessImpl opcProcess = new OpcProcessImpl();

	public static OpcProcessImpl getOpcProcess() {
		return opcProcess;
	}

	public static void setOpcProcess(OpcProcessImpl opcProcess) {
		Global.opcProcess = opcProcess;
	}

}
