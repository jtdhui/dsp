package com.jtd.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     文件工具类
 *     </p>
 */
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	private static final Map<String, String> CONTENTTYPES = new HashMap<String, String>();
	private static final String DEFAULT_CONTENTTYPE = "text/html;charset=UTF-8";
	static {
		CONTENTTYPES.put("html", "text/html;charset=UTF-8");
		CONTENTTYPES.put("xls", "application/x-msdownload;charset=UTF-8");
		CONTENTTYPES.put("xlsx", "application/x-msdownload;charset=UTF-8");
		CONTENTTYPES.put("csv", "application/csv;charset=UTF-8");
		CONTENTTYPES.put("jpg", "image/jpeg;charset=UTF-8");
		CONTENTTYPES.put("bmp", "image/bmp;charset=UTF-8");
		CONTENTTYPES.put("gif", "image/gif;charset=UTF-8");
	}

	public static String getExtendName(String fileName) {
		int l = fileName.lastIndexOf(".");
		if (l == -1) {
			return "";
		}
		return fileName.substring(l + 1, fileName.length());
	}

    public static String getPreName(String fileName) {
        int l = fileName.lastIndexOf(".");
        if (l == -1) {
            return "";
        }
        return fileName.substring(0, l);
    }

	public static boolean sava(String base, String fileName,
			CommonsMultipartFile commonsMultipartFile) {

		boolean flag = false;
		File dirs = new File(base);
		dirs.mkdirs();

		// 这里的file就是前台页面的name
		if (commonsMultipartFile.isEmpty()) {
			return false;
		}

		// 获取路径，生成完整的文件路径,当然要先创建upload文件夹
		File uploadFile = new File(base + fileName);
		try {
			// 上传
			FileCopyUtils.copy(commonsMultipartFile.getBytes(), uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		flag = true;
		return flag;
	}

	public static void downloadFile(HttpServletResponse response,
			String fileName, File downLoadFile) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(downLoadFile);
		downloadFile(response, fileName, is);
	}

	public static void downloadFile(HttpServletResponse response,
			String fileName, String downLoadPath) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(downLoadPath);
		downloadFile(response, fileName, is);
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 *            下载的文件名
	 * @param downLoadPath
	 *            文件的路径
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletResponse response,
			String fileName, InputStream is) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			String contentType = getContentType(fileName);
			response.setContentType(contentType);
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String((fileName).getBytes("UTF-8"), "ISO8859-1"));
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (UnsupportedEncodingException e) {
			log.error("FileUtil.downloadFile设置编码时错误", e);
		} catch (FileNotFoundException e) {
			log.error("FileUtil.downloadFile下载文件未找到", e);
		} catch (Exception e) {
			log.error("FileUtil.downloadFile下载文件时错误", e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				log.error("FileUtil.downloadFile关闭文件流时出错", e);
			}
		}
	}

	private static String getContentType(String fileName) {
		String extendName = getExtendName(fileName).toLowerCase();
		String contentType = CONTENTTYPES.get(extendName);
		if (contentType == null) {
			contentType = DEFAULT_CONTENTTYPE;
		}
		return contentType;
	}

	/**
	 * 从classpath路径读取文件
	 * 
	 * @param resource
	 * @return
	 */
	public static File getFileInClassPath(String resource) {
		File file = null ;
		try {
			file = ResourceUtils.getFile(FileUtil.class.getClassLoader()
					.getResource(resource));
		} catch (FileNotFoundException e) {
			log.error("FileUtil.getFileInClassPath发生错误",e);
		}
		return file;
	}

	/**
	 * 将文件转为base64字符串
	 * 
	 * @param file
	 * @return
	 */
	public static String readAsBase64String(File file) {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] b = new byte[(int) file.length()];
			fis.read(b);
			
			//BASE64Encoder encoder = new BASE64Encoder();
			//return encoder.encode(b);
			
			return new String(Base64.encodeBase64(b));

		} catch (Exception e) {
			log.error("FileUtil.readAsBase64String发生错误",e);
			
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("FileUtil.readAsBase64String发生错误",e);
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 以字节流读取文件的内容并返回
	 * 
	 * @param charFile
	 * @return
	 */
	public static String readAsString(File charFile){
		
		String fileContent = null ;
		
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(charFile);
			InputStreamReader fr = new InputStreamReader(fis,"UTF-8");
			BufferedReader br = new BufferedReader(fr);
			if(br != null){
				String s = null ;
				while((s = br.readLine()) != null){
					sb.append(s);
				}
			}
			fis.close();
			fr.close();
			br.close();
			
			//fileContent = new String(sb.toString().getBytes("UTF-8"));
			fileContent = sb.toString();
			
		} catch (Exception e) {
			log.error("FileUtil.readAsString发生错误",e);
		}
		
		return fileContent ;
		
	}
}
