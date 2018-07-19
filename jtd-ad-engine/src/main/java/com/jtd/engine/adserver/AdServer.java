package com.jtd.engine.adserver;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-engine
 * @描述 Engine服务的入口程序，负责启动、停止engine程序
 */
@SuppressWarnings("restriction")
public class AdServer {
	static {
//		 配置log4j
		// DOMConfigurator.configure("log4j2.xml");

	}
	private static final Log log = LogFactory.getLog(AdServer.class);

	//配置文件路径
	private static final String CONFIG_LOCATION = "spring.xml";

	// PID文件的位置
	private static String PID_FILE = "server.pid";

	// spring 上下文环境
	private static ConfigurableApplicationContext applicationContext;

	// acceptor的代理类
	private static com.jtd.engine.adserver.AdServerAcceptor adServerAcceptor;

	/**
	 * 启动引擎服务器
	 * @throws Exception
	 * @return void
	 */
	private static void startup() throws Exception {
		//加载spring配置文件
		applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
		//获取AdServerAcceptor(广告服务适配器)实例
		adServerAcceptor = (com.jtd.engine.adserver.AdServerAcceptor) applicationContext.getBean("adServerAcceptor");
		//调用构建方法
		adServerAcceptor.bind();
	}

	/**
	 * 关闭引擎服务器
	 * @throws Exception
	 * @return void
	 */
	private static void shutdown() throws Exception {
		adServerAcceptor.destroy();
		applicationContext.close();
		new File(PID_FILE).delete();
	}

	/**
	 * 根据PID关闭服务器
	 */
	private static void shutdownByPid() {

		try {
			com.jtd.engine.utils.PIDAwareUtil.setPidFilePath(PID_FILE);
			int pid = com.jtd.engine.utils.PIDAwareUtil.readFromFile();
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
			com.jtd.engine.utils.PIDAwareUtil.setPidFilePath(PID_FILE);
			com.jtd.engine.utils.PIDAwareUtil.savePID2File();
			log.info("服务器启动成功, PID: " + com.jtd.engine.utils.PIDAwareUtil.getPid());
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

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月28日
	 * @项目名称 dsp-engine
	 * @描述 <p></p>
	 */
	private static class ExitSignalHandler implements SignalHandler {

		/**
		 * (non-Javadoc)
		 * 
		 * @see sun.misc.SignalHandler#handle(sun.misc.Signal)
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
