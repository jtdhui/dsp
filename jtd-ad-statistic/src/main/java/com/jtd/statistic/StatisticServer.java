package com.jtd.statistic;


import com.jtd.statistic.util.PIDAwareUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@SuppressWarnings("restriction")
public class StatisticServer {

//	static {
//
//		// 配置log4j
//		// PropertyConfigurator.configure("conf/log4j.properties");
//	}
	private static final Log log = LogFactory.getLog(StatisticServer.class);

	// 配置文件类路径
	private static final String CONFIG_LOCATION = "classpath:applicationContext*.xml";

	// PID文件的位置
	private static String PID_FILE = "server.pid";

	// spring 上下文环境
	private static AbstractApplicationContext applicationContext;

	// 启动器
	private static ServerBootstrap bootstrap;

	/**
	 * 启动服务器
	 */
	@SuppressWarnings("unchecked")
	private static void startup() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
		bootstrap = applicationContext.getBean(ServerBootstrap.class);
		List<InetSocketAddress> address = applicationContext.getBean("serviceAddresses", List.class);
		for (InetSocketAddress addr : address) {
			bootstrap.bind(addr);
			log.info("绑定监听地址[" + addr.toString() + "]");
		}
	}

	/**
	 * 关闭服务器
	 */
	private static void shutdown() throws Exception {
		bootstrap.releaseExternalResources();
		applicationContext.destroy();
		new File(PID_FILE).delete();
	}

	/**
	 * 根据PID关闭服务器
	 */
	private static void shutdownByPid() {

		try {
			PIDAwareUtil.setPidFilePath(PID_FILE);
			int pid = PIDAwareUtil.readFromFile();
			if (pid == -1) {
				log.info("PID文件不存在");
				return;
			}
			String osname = System.getProperty("os.name").toLowerCase();
			String cmd = (osname.indexOf("windows") != -1 ? "ntsd -c q -p " : "kill ") + pid;
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			log.error("关闭服务器发生错误", e);
		}
	}

	/**
	 * 入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// 关闭指令
		if (args.length != 0 && "-shutdown".equals(args[0])) {
			shutdownByPid();
			return;
		}

		// 启动
		try {
			startup();
			PIDAwareUtil.setPidFilePath(PID_FILE);
			PIDAwareUtil.savePID2File();
			log.info("服务器启动成功, PID: " + PIDAwareUtil.getPid());
		} catch (Exception e) {
			log.error("服务器启动失败", e);
			try {
				shutdown();
			} catch (Exception ex) {
				// nothing to do
			}
			return;
		}

		// 安装退出的信号处理器
		SignalHandler handler = new ExitSignalHandler();
		Signal.handle(new Signal("TERM"), handler);
		Signal.handle(new Signal("INT"), handler);
	}

	private static class ExitSignalHandler implements SignalHandler {

		/**
		 * (non-Javadoc)
		 * 
		 * @see SignalHandler#handle(Signal)
		 */
		public void handle(Signal signal) {

			log.info("收到信号:[" + signal.getName() + " : " + signal.getNumber() + "], 服务器开始关闭");

			try {
				shutdown();
				log.info("服务器关闭成功");
			} catch (Exception e) {
				log.error("服务器关闭失败", e);
			}
		}
	}
}
