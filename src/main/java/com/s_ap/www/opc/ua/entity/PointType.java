package com.s_ap.www.opc.ua.entity;

import java.text.ParseException;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @author zihaozhu
 * @date 2017-11-27 2:02:55 PM
 */
public class PointType {

	public enum PointTypeEnum {
		BOOLEAN, BYTE, UBYTE, SHORT, USHORT, INTEGER, UINTEGER, LONG, ULONG, FLOAT, DOUBLE, STRING, DATETIME, NULL
	}

	public static Object getValueType(PointTypeEnum objectType, String objectValue) throws ParseException {
		Object object = null;
		switch (objectType) {
		case BOOLEAN:
			object = new Boolean(objectValue);
			break;
		case BYTE:
			object = Byte.valueOf(objectValue);
			break;
		case UBYTE:
			object = UByte.valueOf(objectValue);
			break;
		case SHORT:
			object = Short.valueOf(objectValue);
			break;
		case USHORT:
			object = UShort.valueOf(objectValue);
			break;
		case INTEGER:
			object = Integer.valueOf(objectValue);
			break;
		case UINTEGER:
			object = UInteger.valueOf(objectValue);
			break;
		case LONG:
			object = Long.valueOf(objectValue);
			break;
		case ULONG:
			object = ULong.valueOf(objectValue);
			break;
		case FLOAT:
			object = Float.valueOf(objectValue);
			break;
		case DOUBLE:
			object = Double.valueOf(objectValue);
			break;
		case STRING:
			object = new String(objectValue);
			break;
		case DATETIME:
			final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final java.util.Date date = sdf.parse(objectValue);
			object = new DateTime(date);
			break;
		case NULL:
			break;
		default:
			break;
		}
		return object;
	}
}
