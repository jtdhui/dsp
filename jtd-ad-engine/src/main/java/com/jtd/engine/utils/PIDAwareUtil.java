package com.jtd.engine.utils;

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
 * @创建日期 2016年8月22日
 * @项目名称 dsp-engine
 * @描述
 */
public class PIDAwareUtil {
	// PID文件地址
	private static String PID_FILE;

	// 当前进程的PID
	private static final int PID;

	
	static {
		/**
		 * RuntimeMXBean是Java 虚拟机的运行时系统的管理接口。使用它可以获取正在运行的 Java 虚拟机等信息，包括获取PID
		 * 4499@MacBook-Pro.local
		 */
		String mBeanName = ManagementFactory.getRuntimeMXBean().getName();
		PID = Integer.parseInt(mBeanName.substring(0, mBeanName.indexOf("@")));
	}

	/**
	 * 获取PID
	 * @return
	 * @return int
	 */
		public static final int getPid() {
			return PID;
		}
	

	/**
	 * 将pid保存到文件中
	 * @throws IOException
	 * @return void
	 */
	public static final void savePID2File() throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(PID_FILE);
			writer.write(String.valueOf(PID));
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 从文件中读取pid
	 * @return
	 * @throws IOException
	 * @return int
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
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				// Nothing to do
			}
		}
	}

	/**
	 * 设置存储pid文件的地址
	 * @param pidFile
	 * @return void
	 */
	public static final void setPidFilePath(String pidFile) {
		PID_FILE = pidFile;
	}
}
