package com.s_ap.www.opc.ua.entity;

import com.s_ap.www.opc.ua.entity.PointType.PointTypeEnum;
import com.s_ap.www.opc.ua.pointpool.OnPointChange;

/**
 * 
 * @author zihaozhu
 * @date 2017-11-27 2:38:01 PM
 */
public class PointBean {
	private String name; // 点名称
	private PointTypeEnum pointType; // 点的数据类型
	private boolean isArray; // True表示该Point是一个数组点；False表示非数组点
	private String groupName; // 点属于的组名称

	private String clientHandle;
	private boolean active; // 点状态
	private boolean quality;
	private Object value; // 点当前值
	private String timestamp; // 点的最后更新时间

	private OnPointChange onPointChange;// 回调接口

	public PointBean(String groupName, String name) {
		this.groupName = groupName;
		this.name = name;
	}

	public PointBean(String groupName, String name, PointTypeEnum pointType) {
		this(groupName, name);
		this.setPointType(pointType);
	}

	public PointBean(String groupName, String name, PointTypeEnum pointType, boolean isArray) {
		this(groupName, name, pointType);
		this.isArray = isArray;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getQuality() {
		return quality;
	}

	public void setQuality(boolean quality) {
		this.quality = quality;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public PointTypeEnum getPointType() {
		return pointType;
	}

	public void setPointType(PointTypeEnum pointType) {
		this.pointType = pointType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + (isArray ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pointType == null) ? 0 : pointType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointBean other = (PointBean) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (isArray != other.isArray)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pointType != other.pointType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [name=" + name + ", pointType=" + pointType + ", isArray=" + isArray + ", groupName=" + groupName
				+ ", clientHandle=" + clientHandle + ", active=" + active + ", quality=" + quality + ", value=" + value
				+ ", timestamp=" + timestamp + "]";
	}

	public String getClientHandle() {
		return clientHandle;
	}

	public void setClientHandle(String clientHandle) {
		this.clientHandle = clientHandle;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 注册点变化事件
	 * 
	 * @param onPointChange
	 */
	public void registerPointChangeHandler(OnPointChange onPointChange) {
		this.onPointChange = onPointChange;
	}

	/**
	 * 触发事件
	 * 
	 * @param p
	 */
	public void triggerPointChange(PointBean p) {
		if (null != this.onPointChange) {
			this.onPointChange.pointChange(p);
		}
	}

}
