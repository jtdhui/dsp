package com.jtd.engine.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.utils.SystemTime;
import com.jtd.engine.utils.Timer;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class OtherDAOImpl extends AbstractRedisDAO implements OtherDAO {

//	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
//	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
//	
	private static final Log log = LogFactory.getLog(OtherDAOImpl.class);
	
	private static final String REQ_NUM_PREFIX = "sum_";
	
	private static final String IS_CUSTOM = "iscustom";
	
	
	// 系统时间
	private SystemTime systemTime;

	// 计时器, 定期清理内存里过期的数据
	private Timer timer;

	

	/**
	 * 初始化时启动定时任务
	 */
	public void init() {
		super.init();

	}

	
	
	@Override
	public int getReq(String key) {
		String reqNum = get(REQ_NUM_PREFIX + key);
		//如果本地cache中没有活动，则直接返回null
		if (reqNum == null) {
			return 0;
		}else{
			try{
				int ret=Integer.parseInt(reqNum);
				return ret;
			}catch(Exception ex){
				return 0;
			}
		}
	}



	@Override
	public boolean setReq(String key, String reqNum) {

		if(key==null){
			return false;
		}
		try{
			set(REQ_NUM_PREFIX + key, reqNum);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	
	
	/**
	 * 从redis中获取是否进行特殊用户匹配标识
	 * @param key
	 * @return
	 */
	@Override
	public int getIsCustom() {
		
		String reqNum = get(IS_CUSTOM);
		//如果本地cache中没有活动，则直接返回null
		if (reqNum == null) {
			return 0;
		}else{
			try{
				int ret=Integer.parseInt(reqNum);
				return ret;
			}catch(Exception ex){
				return 0;
			}
		}
	}


	/**
	 * 向redis中写入是否进行特殊用户匹配标识
	 * @param key
	 */
	@Override
	public void setIsCustom(String value) {
		
		if(value==null){
			return ;
		}
		try{
			set(IS_CUSTOM, value);
		}catch(Exception ex){
			log.error("设置是否进行特殊用户匹配标识错误："+ex.getMessage());
		}
		
	}
	
	


	public SystemTime getSystemTime() {
		return systemTime;
	}



	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}



	public Timer getTimer() {
		return timer;
	}



	public void setTimer(Timer timer) {
		this.timer = timer;
	}



}
