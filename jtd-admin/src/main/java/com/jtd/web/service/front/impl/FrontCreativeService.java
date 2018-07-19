package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.FileUtil;
import com.jtd.utils.UpYunUtil;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.Constants;
import com.jtd.web.dao.*;
import com.jtd.web.po.*;
import com.jtd.web.service.front.IFrontAdCategoryService;
import com.jtd.web.service.front.IFrontCreativeService;
import com.jtd.web.service.impl.BaseService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FrontCreativeService extends BaseService<Creative> implements IFrontCreativeService {

	@Autowired
	private ICreativeDao creativeDao;

	@Autowired
	private ICreativeGroupDao creativeGroupDao;
	
	@Autowired
	private IAdDao adDao;

	@Autowired
	private IAdAuditStatusDao adAuditStatusDao;

	@Autowired
	private IFrontAdCategoryService frontAdCategoryService;

	@Autowired
	private ICreativeSizeDao creativeSizeDao;

	@Override
	protected BaseDao<Creative> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeDao;
	}

	/**
	 * 上传并保存素材
	 */
	@Override
	public String uploadFileAndSave(HttpServletRequest request, Campaign camp, long partner_id, String uploadDir) {
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		//允许上传的文件类型
		String fileType = "jpg";
		//允许上传的文件最大大小(100M,单位为byte) 例: 1024*1024*100 100M
		int maxSize = 150*1024; //上传素材最大为150K

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

		String retStr = "";

		if (multipartResolver.isMultipart(request)) {

			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			// 遍历所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			for (Iterator<String> iterator = iter; iterator.hasNext();) {

				String fileName = (String) iterator.next();
				// 得到上传的文件
				List<MultipartFile> listFile = multiRequest.getFiles(fileName);
				if(listFile.size()>0){
					for(MultipartFile file : listFile){

						String orginFileName = file.getOriginalFilename();

						// start try-catch
						if (!StringUtils.isEmpty(orginFileName)) {
							try {

								// 生成新文件名
								String extend = FileUtil.getExtendName(orginFileName);

								//判断是否为允许上传的文件类型
								if ( !Arrays.<String> asList(fileType.split(",")).contains(extend))
								{
									retStr += "<br/>创意名称["+orginFileName+"]文件类型不正确，必须为" + fileType + "的文件！";
									continue;
								}

                                if (file.getSize() > maxSize)
                                {
                                    retStr += "<br/>创意名称["+orginFileName+"]文件大小超过限制！应小于" + maxSize/1024+"K";
                                    continue;
                                }

								long currentTime = System.currentTimeMillis();
								String fileNewName = currentTime + "." + extend;

								// 上传目录
								File localFile = new File(uploadDir + "/" + fileNewName);
								// 将上传文件保存到上传目录
								//file.transferTo(localFile);
								// 上传
								FileCopyUtils.copy(file.getBytes(), localFile);

								// 根据上传图片解析图片的长和宽
								BufferedImage bufferedImage = ImageIO.read(localFile);
								int width = bufferedImage.getWidth();
								int height = bufferedImage.getHeight();
								String size = width + "x" + height;

                                Subject subject = SecurityUtils.getSubject();
                                Session session = subject.getSession();
                                Set<String> sizeSet = (Set<String>) session.getAttribute("creativeSize");
                                if(sizeSet.contains(size) == false){
                                    retStr += "<br/>创意尺寸["+size+"]上传失败:尺寸不对,请按规格上传!";
                                    continue;
                                }

//								CreativeSize creativeSize =  creativeSizeDao.getSizeByName(size);
//								if(creativeSize == null){
//									retStr += "<br/>创意尺寸["+size+"]上传失败:尺寸不对,请按规格上传!";
//									continue;
//								}

								Map<String, Object> map = new HashMap<String, Object>();
								map.put("campaign_id", camp.getId());
								map.put("size", size);
								Map<String,Object> retMap = creativeDao.getAdsBy(map);
								//根据活动ID和尺寸判断该素材是否存在
								if (retMap != null) {

									// 删除文件
									if(localFile.exists()){
										localFile.delete();
									}

									// 逻辑保存广告信息
									retStr += logicSaveAd(file,size, retMap,extend);

								} else {

									// 新增素材组,素材,广告及广告的行业类别
									insertAd(partner_id, camp, currentTime, size, fileNewName, localFile,extend);
									retStr += "<br/>创意尺寸["+size+"]上传成功!";
								}

							} catch (IllegalStateException | IOException e) {
								e.printStackTrace();
							}
						}
						// end try-catch
					}
				}

			}
		}

		return retStr;
	}

	/**
	 * 逻辑保存广告信息
	 * @param file
	 * @param size
	 * @param map
	 * @return
	 */
	private String logicSaveAd(MultipartFile file,String size, Map<String,Object> map,String suffix) {

		String retStr = "";

		//广告素材渠道审核状态
		AdAuditStatus adAuditStatus = new AdAuditStatus();
		String adId = map.get("ad_id").toString();
		adAuditStatus.setAdId(Long.parseLong(adId));
		List<AdAuditStatus> list = adAuditStatusDao.listBy(adAuditStatus);
		boolean channel_audit_flag = true; // 广告上的渠道全部审核通过标志
		if(list.size()>0) {
			for (AdAuditStatus aas : list) {
				if (aas.getStatus() != AuditStatus.STATUS_SUCCESS) {
					channel_audit_flag = false;
					break;
				}
			}
		}else{
			channel_audit_flag = false;
		}

		if(channel_audit_flag){  //如果全部审核通过,提示这个尺寸的创意上传失败,原因是这个创意已经审核通过,不能更新.
			retStr += "<br/>创意尺寸["+size+"]已经全部审核通过,不能重新上传!";
		}else{

            String creative_url = map.get("local_creative_url").toString();
            //更新创意
            String extend = "";
            if(!map.containsKey("suffix") || map.get("suffix") == null || map.get("suffix") == ""){
                extend = FileUtil.getExtendName(creative_url);
            }else {
                extend = map.get("suffix").toString();
            }
            if(!extend.equals(suffix)){ //后辍不同则更新素材信息
                creative_url = FileUtil.getPreName(creative_url) + "." + suffix;  //后辍名更新
            }
			//重新上传素材,但是素材的名称,路径等都不变,相当于更新操作
			String uploadPath = Constants.DATA_DIR_PATH+"/"+creative_url;
			// 上传目录
			File uploadFile = new File(uploadPath);
			// 将上传文件保存到上传目录
			try {

				// 删除文件
				if(uploadFile.exists()){
					uploadFile.delete();
				}
				file.transferTo(uploadFile);
				String upYunImgUrl = UpYunUtil.uploadFile(creative_url, uploadFile);

                Creative creative = new Creative();
                Date now = new Date();
                creative.setId(Long.parseLong(map.get("id").toString()));
                creative.setSuffix(suffix);
                creative.setCreativeUrl(upYunImgUrl);
                creative.setLocalCreativeUrl(creative_url);
                creative.setUpdateTime(now);
                creativeDao.update(creative);

				retStr += "<br/>创意尺寸["+size+"]重新上传成功!";

			} catch (IOException e) {
				e.printStackTrace();
			}

			// 重置广告的内部审核状态为0:待审核
			Ad ad = new Ad();
			ad.setId(Long.parseLong(adId));
			ad.setInternalAudit(0);
			adDao.update(ad);
		}

		return retStr;
	}

	/**
	 * 新增广告信息
	 * @param partner_id
	 * @param camp
	 * @param currentTime
	 * @param size
	 * @param fileNewName
	 * @param localFile
	 */
	private void insertAd(long partner_id, Campaign camp, long currentTime, String size, String fileNewName, File localFile,String extend) {

		Date now = new Date();

		// 素材分组保存
		CreativeGroup cg = new CreativeGroup();
		cg.setPartnerId(partner_id);
		cg.setGroupName(camp.getCampaignName() + "_" + size + "_" + currentTime);
		cg.setDeleteStatus(0);
		long cg_id = creativeGroupDao.insert(cg);

		// 素材数据保存
		Creative ct = new Creative();
		ct.setPartnerId(partner_id);
		ct.setGroupId(cg_id);
		ct.setSize(size); // 尺寸
		ct.setCreativeName(camp.getCampaignName() + "_" + size + "_" + currentTime);
        ct.setSuffix(extend);
		ct.setDeleteStatus(0);
		ct.setCreativeType(0);
		ct.setPubStatus(0);
		ct.setCreateTime(now);
		ct.setUpdateTime(now);

		String creative_url = "creative/" + camp.getId() + "/" + fileNewName;
		//上传到图片服务器
		String upYunImgUrl = UpYunUtil.uploadFile(creative_url, localFile);
		ct.setCreativeUrl(upYunImgUrl);

        ct.setLocalCreativeUrl(creative_url);

		long ct_id = creativeDao.insert(ct);

		// 广告数据保存
		Ad ad = new Ad();
		ad.setCampaignId(camp.getId());
		ad.setCreativeId(ct_id);
		ad.setDeleteStatus(0);
		ad.setCreateTime(now);
		ad.setClickUrl(camp.getClickUrl());
		ad.setLandingPage(camp.getLandingPage());
		ad.setPvUrls(camp.getPvUrls());
		long ad_id = adDao.insert(ad);
		ad.setId(ad_id);
		//广告行业类型保存
		frontAdCategoryService.saveAdCategory(ad);
	}

}
