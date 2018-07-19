package com.jtd.utils;

import java.util.Random;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月8日
 * @项目名称 dsp-admin
 * @描述 <p>该类主要功能是计算用户密码密文的</p>
 */
public class UserPwd {
	
	/**
	 * 
	 * @param pwd				明文，原始密码 
	 * @param salt				盐，通过使用随机数;就是加密时的干扰字符串
	 * @param hashAlgorithmName 散列算法名称
	 * @param hashIterations	散列的次数，比如散列两次，相当 于md5(md5(''))
	 * @return 加密后的字符串
	 */
	public static String password(String sourcePwd,String salt,String hashAlgorithmName,int hashIterations){
//		Md5Hash md5Hash = new Md5Hash(sourcePwd, salt, hashIterations);
//		String password_md5 =  md5Hash.toString();
//		System.out.println(password_md5);
		SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, sourcePwd, salt, hashIterations);
//		System.out.println(simpleHash.toString());
		return simpleHash.toString();
	}
	
	public static String salt(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }
	
	public static void main(String[] args) {
		System.out.println(salt(5));
		
		System.out.println(password("dddddd","qwerty","md5",2));
	}
}
