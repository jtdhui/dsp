package com.jtd.utils;

import java.io.File;
import java.io.Serializable;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>视频信息</p>
 */
public class VideoInfo implements Serializable {
	private static final long serialVersionUID = -970306566688971546L;
	// 宽
	private String width;
	// 高
	private String height;
	// 时长
	private Long duration;
	
	// 文件大小
	private String fileSize;
	private String bitRate;
	//文件名称
    private File videoImageFile;
	
	public String getBitRate() {
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public File getVideoImageFile() {
		return videoImageFile;
	}

	public void setVideoImageFile(File videoImageFile) {
		this.videoImageFile = videoImageFile;
	}
}
