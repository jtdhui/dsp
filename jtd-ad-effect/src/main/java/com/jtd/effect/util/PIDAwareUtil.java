package com.jtd.effect.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class PIDAwareUtil {

	// PID文件的位置
	private static String PID_FILE;

	// 当前进程的PID
	private static final int PID;
	static {
		String mBeanName = ManagementFactory.getRuntimeMXBean().getName();
		PID = Integer.parseInt(mBeanName.substring(0, mBeanName.indexOf("@")));
	}

	/**
	 * 获取PID
	 * 
	 * @return
	 */
	public static final int getPid() {
		return PID;
	}

	/**
	 * 保存PID到文件中
	 * 
	 * @throws IOException
	 */
	public static final void savePID2File() throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(PID_FILE);
			writer.write(String.valueOf(PID));
		} finally {
			try {
				if (writer != null) writer.close();
			} catch (Exception e) {
				// Nothing to do
			}
		}
	}

	/**
	 * 从文件中读出PID
	 * 
	 * @return
	 * @throws IOException
	 */
	public static final int readFromFile() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(PID_FILE)));
			String pid = reader.readLine();
			return Integer.parseInt(pid);
		} catch (Exception e) {
			return -1;
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				// Nothing to do
			}
		}
	}

	/**
	 * 设置PID文件路径
	 * 
	 * @param pidFile
	 */
	public static final void setPidFilePath(String pidFile) {
		PID_FILE = pidFile;
	}
}
