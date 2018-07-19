package com.jtd.utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.jtd.commons.BeanFactory;
import com.jtd.commons.PropertyConfig;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>邮件工具类</p>
 */
public class EmailUtil {
	private static final Log log = LogFactory.getLog(EmailUtil.class);
	private  SimpleEmail instance = null;
	private  String userName;

	/**
	 * 启动发送邮件线程
	 */
    private  void startSendEmail(EmailEntity emailEntity){
				//EmailEntity emailEntity = taskQueue.poll();
    	      if(null == emailEntity){
    	    	  log.debug("未找到邮件信息.emailEntity={"+emailEntity+"}");
    	    	  return;
    	      }
				EmailType emailType = emailEntity.getEmailType();
				if (EmailType.SIMPLEEMAIL.equals(emailType)) {
						sendSimpleEmail(emailEntity);
				log.info("系统发送了邮件："+emailEntity.toString());
			}
	 }
			    
			

	private  void sendSimpleEmail(EmailEntity emailEntity) {
		SimpleEmail instance = getInstance();
		String[] to = emailEntity.getTo();
		String from = emailEntity.getFrom();
		if(StringUtils.isEmpty(from)){
			  from  = userName;
		}
	
		String subject = emailEntity.getSubject();
		String content = emailEntity.getMsg();
		
		try {
			instance.addTo(to);
			instance.setFrom(from);
			instance.setSubject(subject);
			instance.setMsg(content);
			instance.send();
		} catch (EmailException e) {
			log.error(e.getMessage(), e);
		}

	}

	private  SimpleEmail getInstance() {
		if (instance == null) {
			instance = new SimpleEmail();
			synchronized (instance) {
					PropertyConfig propertyConfig = (PropertyConfig) BeanFactory.getBean("propertyConfig");
					String host = propertyConfig.getProperty("email.host");
					userName = propertyConfig.getProperty("email.userName");
					String password = propertyConfig.getProperty("email.password");
					//instance = new SimpleEmail();
					instance.setAuthentication(userName, password);
					instance.setHostName(host);
			}
		}
		return instance;
	}

	/**
	 * 发送简单邮件
	 * 
	 * @param to
	 * @param subject
	 * @param content
	 */
	public  void sendEmail(String[] to, String subject, String content) {
		EmailEntity emailEntity = new EmailEntity(userName, to, subject, content, EmailType.SIMPLEEMAIL);
	   //taskQueue.offer(emailEntity);
        //启动发送邮件
		startSendEmail(emailEntity);
	}

	private  enum EmailType {
		SIMPLEEMAIL;
	}

	/**
	 * 邮件实体类 暂时支持简单邮件的属性
	 * 
	 * @author mwf
	 *
	 */
	private  class EmailEntity {
		private EmailType emailType;
		private String from;
		private String[] to;
		private String subject;
		private String msg;

		private EmailEntity(String from, String[] to, String subject, String msg, EmailType emailType) {
			super();
			this.from = from;
			this.to = to;
			this.subject = subject;
			this.msg = msg;
			this.emailType = emailType;
		}

		public String getFrom() {
			return from;
		}


		public String[] getTo() {
			return to;
		}


		public String getSubject() {
			return subject;
		}


		public String getMsg() {
			return msg;
		}


		public EmailType getEmailType() {
			return emailType;
		}


	}

}