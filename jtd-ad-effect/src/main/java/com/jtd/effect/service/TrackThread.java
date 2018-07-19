package com.jtd.effect.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.dao.ClickDAO;
import com.jtd.effect.dao.CookieDataDAO;
import com.jtd.effect.dao.TrackDAO;
import com.jtd.effect.po.Track;
import com.jtd.effect.util.SystemTime;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>处理track对象的线程，将track对象计入到redis，和将track对象写入日志。</p>
 */
class TrackThread  extends Thread {

	private static final Log log = LogFactory.getLog(TrackThread.class);
	//过期时间为60天
	private static final long EXPIRE = 86400000l * 60;
	//存放Track对象的队列
	private LinkedBlockingQueue<Track> queue;
	//存放cookieData数据的访问对象，reids集群A，索引15
	private CookieDataDAO cookieDataDAO;
	//redis集群C访问层
	private ClickDAO clickDAO;
	//数据库访问层
	private TrackDAO trackDAO;
	private SystemTime systemTime;
	//日志存放的目录
	private File logDir;

	private volatile boolean write = true;

	private int yyyyMMdd;
	private int threadId;

	private BufferedWriter writer;

	TrackThread(int id, LinkedBlockingQueue<Track> queue, CookieDataDAO cookieDataDAO, ClickDAO clickDAO, TrackDAO trackDAO, SystemTime sysTime, String logDir) {
		super("TrackThread-" + id);
		threadId = id;
		this.queue = queue;
		this.cookieDataDAO = cookieDataDAO;
		this.clickDAO = clickDAO;
		this.trackDAO = trackDAO;
		this.systemTime = sysTime;
		File dir = new File(logDir);
		if(!dir.exists()) dir.mkdirs();
		this.logDir = dir;
		yyyyMMdd = systemTime.getYyyyMMdd();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		log.info(Thread.currentThread().getName() + " is running；处理track对象。");
		for (; !Thread.interrupted();) {
			Track o = null;
			try {
				//取出队列首位的对象，如果队列中没有数据，则等待10秒，如果10后还没有数据，返回null，如果失败，跳入到cache中，退出循环。
				o = queue.poll(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				break;
			}
			
			if (o == null) {
				if (writer != null) {
					try {
						//如果writer对象中缓冲区内容，写入到文件中。
						writer.flush();
					} catch (Exception e) {
						log.error("刷新文件发生错误", e);
					}
				}
				continue;
			}
			//o就是Track对象不为null，将对象传给tack方法。
			track(o);
		}
	}

	/**
	 * 将cookie数据写入到reidsA 15中；将除了全站访客找回之外的其他数据，例如：注册找回，登陆找回，订单找回等等，写入到数据库中；
	 * 其中 订单数据分别写入到 ORDERES订单表和ORDERDETAILS订单详情表中；其它或效果数据写入到 EFFECTS 表中。 
	 * @param track
	 * @return void
	 */
	private void track(Track track) {
		//获取cookieid
		String cookieid = track.getCookieid();
		//获取gid，这个gid是cookiegid表中的id
		long gid = track.getCookiegId();
		Map<Long, String[]> d = new HashMap<Long, String[]>();
		d.put(gid, new String[] { String.valueOf(systemTime.getTime() + EXPIRE) });
		//将数据写入到reids格式
		//key=track对象中的cookieid
		//value=hash,field=track中的cookiegId
		//				  value=目前中只有过期时间
		cookieDataDAO.addCookieData(cookieid, d);
		
		//获取后效果跟踪的类型
		int type = track.getType();

		// 效果跟踪，入库类型为0以上的
		if(type > 0) {
			String clickKey = track.getClickKey();
			if(!StringUtils.isEmpty(clickKey)) {
				//根据从客户端取出的clickKey，从reids集群C中取出click对象，并设置到track对象中。
				//这里的clickKey是在统计系统的点击的时候，写入到客户端的cookie中的，并把click对象写入到，reids集群C中。
				track.setClick(clickDAO.getClick(clickKey));
			}
			//如果click对象从reidsC集群总取出来了，这时才将track对象，写入到数据库中
			if(track.getClick() != null){
				trackDAO.saveTrack(track);
			}
		}

		// 写日志
		if(!write && type == 0) return;
		BufferedWriter w = getWriter();
		if (w != null) {
			try {
				w.write(JSON.toJSONString(track) + "\r\n");
			} catch (Exception e) {
				log.error("写Track日志发生错误", e);
			}
		}
	}

	/**
	 * 获取BufferedWriter 对象
	 * @param type 0 pv 1 click 2 landing
	 * @param writers
	 * @param partnerId
	 * @return
	 */
	private BufferedWriter getWriter() {
		int today = systemTime.getYyyyMMdd();
		if(today != yyyyMMdd) {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					// Nothing to do
				}
			}
			File f = new File(logDir, today + "_" + threadId + ".txt");
			BufferedWriter w = null;
			try {
				w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, f.exists()), "UTF-8"));
			} catch (Exception e) {
				try { log.error("创建文件["+f.getCanonicalPath()+"]发生错误"); } catch (Exception e1) {}
			}
			if (w != null) {
				writer = w;
			}
			yyyyMMdd = today;
			return w;
		} else {
			if(writer != null) {
				return writer;
			} else {
				File f = new File(logDir, today + "_" + threadId + ".txt");
				BufferedWriter w = null;
				try {
					w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, f.exists()), "UTF-8"));
				} catch (Exception e) {
					try { log.error("创建文件["+f.getCanonicalPath()+"]发生错误"); } catch (Exception e1) {}
				}
				if (w != null) {
					writer = w;
				}
				return w;
			}
		}
	}

	boolean isWrite() {
		return write;
	}
	void stopWrite() {
		this.write = false;
	}
	void resumeWrite() {
		this.write = true;
	}
}
