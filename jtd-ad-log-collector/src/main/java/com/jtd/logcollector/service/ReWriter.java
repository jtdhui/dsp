package com.jtd.logcollector.service;

import com.alibaba.fastjson.JSON;
import com.jtd.logcollector.po.Click;
import com.jtd.logcollector.po.Landing;
import com.jtd.logcollector.po.PV;
import com.jtd.logcollector.po.Param;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 把顺序写的日志文件按广告主拆分
 * @作者 Amos Xu
 * @版本 V1.0
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p>
 * 		把顺序写的日志文件按广告主拆分<br/>
 * 			1、"目标目录destDir/广告主id/pv/yyyyMMddHH.txt"
 * 			2、"目标目录destDir/广告主id/click/yyyyMMddHH.txt"
 * 			3、"目标目录destDir/广告主id/landing/yyyyMMddHH.txt"
 * 		
 * 		</p>
 */
public class ReWriter {

	private static final Log log = LogFactory.getLog(ReWriter.class);
	//未捕获异常，在线程中出现未捕获异常时，都会被这个EH的方法捕获，EH的作用是为了是线程不间断的运转下去
	private static final UncaughtExceptionHandler EH = new UncaughtExceptionHandler(){
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			log.error("线程" + t.getName() + "发生异常", e);
		}
	};

	// 源目录和目标目录
	private File rawDir;
	private File destDir;
	private Thread thread;
	
	private LinkedBlockingQueue<Task> taskq = new LinkedBlockingQueue<Task>();
	
	/**
	 * 
	 * @param rawDir				源目录
	 * @param destDir				目标目录
	 */
	public ReWriter(File rawDir, File destDir) {
		this.rawDir = rawDir;
		this.destDir = destDir;
	}

	public void rewrite(Task task) {
		//将任务添加到队列中
		taskq.offer(task);
		//如果thread 为null，或thread为非激活状态，则thread重新指向新的WriteThread对象，并启动线程。
		if (thread == null || !thread.isAlive()) {
			//写入线程，接收装有任务的队列、文件源目录，目标目录
			thread = new WriteThread(taskq, rawDir,destDir);
			thread.setUncaughtExceptionHandler(EH);
			thread.start();
		}
	}
	
	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月12日
	 * @项目名称 dsp-log-collector
	 * @描述 <p>任务类</p>
	 */
	public static class Task {
		//当前日期和小时
		private int datehour;
		
		private Runnable callback;
		public Task(int datehour) {
			this(datehour, null);
		}
		/**
		 * 
		 * @param datehour					当前日期小时
		 * @param callback					
		 */
		public Task(int datehour, Runnable callback) {
			this.datehour = datehour;
			this.callback = callback;
		}
	}

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月12日
	 * @项目名称 dsp-log-collector
	 * @描述 <p>写文件线程，从task队列中取需要拆分的小时日期</p>
	 */
	public static class WriteThread extends Thread {
		private LinkedBlockingQueue<Task> taskq;
		Map<Long, BufferedWriter> writers = new HashMap<Long, BufferedWriter>();
		private File rawDir;
		private File destDir;
		/**
		 * 
		 * @param taskq				任务队列，队列中的Tash封装的是，当前时间的 yyyyMMddHH; 和callBack线程，callBack线程中的run方法中
		 * @param rawDir
		 * @param destDir
		 */
		public WriteThread(LinkedBlockingQueue<Task> taskq, File rawDir, File destDir){
			super("WriteThread");
			this.taskq = taskq;
			this.rawDir = rawDir;
			this.destDir = destDir;
		}
		public void run() {
			log.info("WriteThread启动");
			//如果线程没有被阻断则一直循环下去
			for (; !Thread.interrupted();) {
				Task task = null;
				try {
					//返回队列中首位的任务对象，如果10秒钟取不到，则直接返回null
					task = taskq.poll(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					break;
				}
				//如果从队列中渠道的task为null，则退出循环
				if (task == null) break;
				//拿到文件名前缀，这里是task中的datehour,例如：yyyyMMddHH
				final String prefix = String.valueOf(task.datehour);
				//文件过滤器
				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File f) {
						//f是文件，并且，文件明是一，yyyyMMddHH开头的返回true
						return f.isFile() && f.getName().startsWith(prefix);
					}
				};
				//构建一个目录，rawDir目录名称，pv为子目录名称
				File d = new File(rawDir, "pv");
				if(d != null) {
					//获取pv目录下所有 yyyyMMddHH开头的文件
					File[] fs = d.listFiles(filter);
					//以广告主id，将原始日志文件拆分成 "目标目录/广告主id/pv/yyyyMMddHH.txt"
					if(fs != null) for(File f : fs) rewrite(f, 0);
				}
				//关闭 writers 集合中所有的 BufferWriters对象
				closeAll();
				//目录:"源目录/click"
				d = new File(rawDir, "click");
				if(d != null) {
					//获取"源目录/click"下所有 yyyyMMddHH开头的文件
					File[] fs = d.listFiles(filter);
					//以广告主id，将原始日志文件拆分成 "目标目录/广告主id/click/yyyyMMddHH.txt"
					if(fs != null) for(File f : fs) rewrite(f, 1);
				}
				closeAll();
				
				d = new File(rawDir, "landing");
				if(d != null) {
					File[] fs = d.listFiles(filter);
					//以广告主id，将原始日志文件拆分成 "目标目录/广告主id/landing/yyyyMMddHH.txt"
					if(fs != null) for(File f : fs) rewrite(f, 2);
				}
				closeAll();
				
				//拆分完成后，调用callback；callback是一个线程，这里直接调用run方法。
				if(task.callback != null) {
					try {
						task.callback.run();
					}catch(Exception e) {
						log.error("运行回调发生错误", e);
					}
				}
			}
			log.info("WriteThread退出");
		}
		
		/**
		 * 写入文件
		 * @param src				文件对象
		 * @param type				要操作的文件类型，0为pv日志，1为click日志，2为landing日志。
		 * @return void
		 */
		private void rewrite(File src, int type) {
			//读取文件缓冲对象
			BufferedReader r = null;

			try {
				//读取文件
				r = new BufferedReader(new InputStreamReader(new FileInputStream(src), "UTF-8"));
				switch(type) {
				case 0:
					//处理pv日志文件。一行一行的读取日志数据，每一行都是一个pv对象
					for (String line = r.readLine(); line != null; line = r.readLine()) {
						try {
							//将日志转换成pv对象
							PV p = JSON.parseObject(line, PV.class);
							//从pv对象中获取参数对象
							Param param = p.getParam();
							//如果参数对象不为null
							if(param != null) {
								//获取广告主对象
								long pid = param.getP();
								//根据广告主id，从writes中获取写文件的缓冲对象
								BufferedWriter w = writers.get(pid);
								//如果writes中没有这个广告主对应的，BufferedWriter对象
								if(w == null) {
									//在目标目录中创建一个以广告主id为名称的目录
									File pdir = new File(destDir, String.valueOf(pid));
									//如果目标目录不存在，则创建一个目录
									if(!pdir.exists()){
										pdir.mkdirs();
									}
									//在广告主目录下创建，pv目录
									File pvdir = new File(pdir, "pv");
									if(!pvdir.exists()) pvdir.mkdirs();
									//在"目标目录/广告主id/pv/"目录下创建文件名为 "yyyyMMddHH.txt"的文件 
									File destFile = new File(pvdir, src.getName().substring(0, 10) + ".txt");
									//为文件 destFile 创建一个写入缓冲区对象
									w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
									//将缓冲区对象添加到 writers map中
									writers.put(pid, w);
								}//如果能够获取 BufferedWriter对象，则直接将日志文件中这行数据写入到缓冲区中。
								
								//将原始日志中取出的这个广告主，的pv数据写入到新创建的文件中
								w.write(line + "\r\n");
							}//如果参数对象为null，则不做任何操作
						} catch (Exception e) {
							log.error("重写记录[" + line + "]发生错误", e);
						}
					}
					break;

				case 1:
					//处理click日志文件。一行一行的读取日志数据，每一行都是一个click对象
					for (String line = r.readLine(); line != null; line = r.readLine()) {
						try {
							//将日志转换成点击对象，并取出参数对象
							Click c = JSON.parseObject(line, Click.class);
							Param param = c.getParam();
							if(param != null) {
								long pid = param.getP();
								//如果writers中没有 广告主id 对应的 Bufferwrite，则创建一个新的BufferWriters对象
								BufferedWriter w = writers.get(pid);
								if(w == null) {
									File pdir = new File(destDir, String.valueOf(pid));
									if(!pdir.exists()) pdir.mkdirs();
									File pvdir = new File(pdir, "click");
									if(!pvdir.exists()) pvdir.mkdirs();
									//路径 "目标目录/广告主id/click/yyyyMMddHH.txt"
									File destFile = new File(pvdir, src.getName().substring(0, 10) + ".txt");
									w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
									writers.put(pid, w);
								}
								w.write(line + "\r\n");
							}//如果参数对象，为null，则不写日志
						} catch (Exception e) {
							log.error("重写记录[" + line + "]发生错误", e);
						}
					}
					break;

				case 2:
					//处理landing日志文件。一行一行的读取日志数据，每一行都是一个Landing对象
					for (String line = r.readLine(); line != null; line = r.readLine()) {
						try {
							//从landing中取出参数队形，如果参数对象为null，则不做任何处理；否则写入相应的数据
							Landing l = JSON.parseObject(line, Landing.class);
							Param param = l.getClick().getParam();
							if(param != null) {
								//拿到广告主id，如果 writers中没有广告主对应的 BufferWriters 对象，创建一个；
								//最后写入landing数据
								long pid = param.getP();
								BufferedWriter w = writers.get(pid);
								if(w == null) {
									File pdir = new File(destDir, String.valueOf(pid));
									if(!pdir.exists()) pdir.mkdirs();
									File pvdir = new File(pdir, "landing");
									if(!pvdir.exists()) pvdir.mkdirs();
									File destFile = new File(pvdir, src.getName().substring(0, 10) + ".txt");
									//文件目录 "目标目录/广告主id/landing/yyyyMMddHH.txt"
									w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
									writers.put(pid, w);
								}
								w.write(line + "\r\n");
							}
						} catch (Exception e) {
							log.error("重写记录[" + line + "]发生错误", e);
						}
					}
					break;
				}//switch结束
				//将BufferWriters中的数据全部刷新到文件中
				flushAll();
				//src.getAbsolutePath,拿到源文件的绝对路径
				log.info("重写文件[" + src.getAbsolutePath() + "]完成");
				//删除源文件
				if(src.delete()) log.info("文件[" + src.getAbsolutePath() + "]已删除");
			} catch(Exception e) {
				log.error("重写文件[" + src.getAbsolutePath() + "]发生错误", e);
			} finally {
				if(r != null) {
					try {
						r.close();
					} catch (IOException e) {
					}
				}
			}
		}
		/**
		 * 将 writers 集合中所有的 BufferWriters对象中的内容，刷到文件中。
		 * 
		 * @return void
		 */
		private void flushAll() {
			for(BufferedWriter w : writers.values()) {
				try { w.flush(); }catch(Exception e) {}
			}
		}
		/**
		 * 关闭 writers 集合中所有的 BufferWriters对象
		 * 
		 * @return void
		 */
		private void closeAll() {
			for(BufferedWriter w : writers.values()) {
				try { w.close(); }catch(Exception e) {}
			}
			writers.clear();
		}
	}
}
