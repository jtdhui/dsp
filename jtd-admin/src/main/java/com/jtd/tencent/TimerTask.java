package com.jtd.tencent;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.jtd.tencent.service.AuthService;
import com.jtd.tencent.service.impl.AuthServiceImpl;

public class TimerTask extends java.util.TimerTask{
	
	private static Logger log = Logger.getLogger(TimerTask.class);

	/** token超时时间 */
	private Integer tokenExpiresIn;
	
	/** refreshToken超时时间 */
	private Integer refreshTokenExpiresIn;
	
	private Timer timer;
	
	public TimerTask() {
		super();
	}

	public TimerTask(Integer tokenExpiresIn,Integer refreshTokenExpiresIn) {
		super();
		this.tokenExpiresIn = tokenExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

	@Override
	public void run() 
	{
		/** 判断token超时时间 */
		if(tokenExpiresIn <= 1){
			Config.TOKEN = null;
			Config.TOKEN_STATUS = false;
			
			AuthService authService = new AuthServiceImpl();
			authService.getToken(2, null);
		}
		tokenExpiresIn --;
		
		/** 判断refreshToken超时时间 */
		if(refreshTokenExpiresIn <= 1)
		{
			Config.REFRESH_TOKEN = null;
			Config.REFRESH_TOKEN_STATUS = false;
			this.timer.cancel();
			log.info("REFRESH_TOKEN 已超时！" + Config.TOKEN_STATUS + "  " + Config.TOKEN + "," + Config.REFRESH_TOKEN_STATUS + " " + Config.REFRESH_TOKEN);
		}
		refreshTokenExpiresIn --;
	}
	
	public void excute(TimerTask task)
	{
		Calendar  calendar= Calendar.getInstance();    
        Date firstTime = calendar.getTime();
        long period = 1000;    //每秒一次
        Timer timer = new Timer();  
        task.setTimer(timer);
        timer.schedule(task, firstTime, period);
	}
	
	public Integer getTokenExpiresIn() {
		return tokenExpiresIn;
	}
	public void setTokenExpiresIn(Integer tokenExpiresIn) {
		this.tokenExpiresIn = tokenExpiresIn;
	}
	public Integer getRefreshTokenExpiresIn() {
		return refreshTokenExpiresIn;
	}
	public void setRefreshTokenExpiresIn(Integer refreshTokenExpiresIn) {
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public static void main(String[] args) 
	{
		Config.TOKEN_STATUS = true;
		Config.TOKEN = "token";
		Config.REFRESH_TOKEN = "ref_token";
		Config.REFRESH_TOKEN_STATUS = true;
		TimerTask task = new TimerTask(5,10);  
		task.excute(task);
    }
}
