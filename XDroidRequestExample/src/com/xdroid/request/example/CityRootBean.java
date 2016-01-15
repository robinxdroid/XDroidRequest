package com.xdroid.request.example;

import java.io.Serializable;

/**
 * 测试用 城市实体类
 * @author Robin
 * @since 2015-11-11 14:26:36
 *
 */
public class CityRootBean<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String errNum;
	private String retMsg;
	private T retData;

	public static class CityBean implements Serializable{
		private static final long serialVersionUID = 1L;
		private String citylist;

		public String getCitylist() {
			return citylist;
		}

		public void setCitylist(String citylist) {
			this.citylist = citylist;
		}

		@Override
		public String toString() {
			return "CityBean [citylist=" + citylist + "]";
		}

	}

	public String getErrNum() {
		return errNum;
	}

	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public T getRetData() {
		return retData;
	}

	public void setRetData(T retData) {
		this.retData = retData;
	}

	@Override
	public String toString() {
		return "CityRootBean [errNum=" + errNum + ", retMsg=" + retMsg + ", retData=" + retData + "]";
	}

}
