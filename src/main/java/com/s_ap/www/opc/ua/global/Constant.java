package com.s_ap.www.opc.ua.global;

public interface Constant {
	/** custom server tag */ // work ok
	// public static final String URL = "opc.tcp://localhost:12686/example";
	// public static final String TAG = "HelloWorld/ScalarTypes/Int32";

	/** kepware server tag */ // work not ok
	public static final String URL = "opc.tcp://localhost:49321";
	public static final String TAG1 = "Channel1.Device1.Tag1";
	public static final String TAG2 = "Channel1.Device1.Tag2";
	public static final String TAG3 = "Channel1.Device1.Tag3";
	public static final String TAG4 = "Channel1.Device1.Tag4";
	public static final String TAG_BROWSER = "Channel1.Device1";

	/** uacpp server tag */ // work ok
	// public static final String URL = "opc.tcp://zihao-basic:48010/uacpp";
	// public static final String TAG = "Demo.Static.Scalar.Int32";

	/** cimplicity server tag */
	// public static final String URL = "";
	// public static final String TAG = "";

	/** groups */
	public static final String GROUP_MESSAGE = "MESSAGE";
	public static final String GROUP_DATA = "DATA";

}
