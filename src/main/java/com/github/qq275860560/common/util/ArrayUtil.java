package com.github.qq275860560.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangyuanlin@163.com
 * 
 * 
 *
 */
public class ArrayUtil {

	// @param ids 逗号区分的整型字符串，格式如"1,2,3,4"  返回整型数组
	  
	public static Integer[] convert(String ids) {
		if(StringUtils.isBlank(ids)) return new Integer[]{};
		String[] idsArr = ids.split(",");
		Integer[] intIds = new Integer[idsArr.length];
		for (int i = 0; i < idsArr.length; ++i) {
			intIds[i] = Integer.valueOf(idsArr[i]);
		}
		return intIds;
	}
	
	public static String getHexString(byte b ) {
		return getHexString(new byte[]{b} );
	}
	public static String getHexString(byte[] bytes) {
		
	 
		
		StringBuilder sb = new StringBuilder();
		for(byte b:bytes){
			/*
			String hex=Integer.toHexString(b  & 0xFF);
			if(hex.length()==1) {
				hex="0"+hex;
			}
			*/
			String  hex = String.format("%02x", b);
			//String hex=  String.format("%#04x", b);
			sb.append(hex).append(" ");
		}
		return sb.toString();
		 
	}
	
	public static String getBinaryString(byte b){
		return getBinaryString(new byte[]{b} );
	}
      public static String getBinaryString(byte[] bytes) {	
		StringBuilder sb = new StringBuilder();
		for(byte b:bytes){
			String hex  = String.format("%08d", Integer.valueOf(Integer.toBinaryString(b & 0xff)));
			sb.append(hex).append(" ");
		}
		return sb.toString();
		 
	}

}












