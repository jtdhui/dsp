package com.jtd.web.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.Channel;
import com.jtd.web.service.admin.IAdminChannelService;

@Controller
@RequestMapping("/admin/channel")
public class AdminChannelController extends BaseController {

	public static final String PAGE_DIRECTORY = "/admin/channel/";
	public static final String ACTION_PATH = "redirect:/admin/camp/";

	@Autowired
	private IAdminChannelService adminChannelService;

	@RequestMapping("/list")
	public String list(Model model, Integer pageNo, Integer pageSize,
			HttpServletRequest request) {
		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		Pagination<Map<String, Object>> page = adminChannelService
				.listChannelBy(paraMap, pageNo, pageSize);
		model.addAttribute("page", page);
		return PAGE_DIRECTORY + "admin_channel_list";

	}

	@RequestMapping(value = "/addChannel", method = RequestMethod.GET)
	public String addChannel() {

		return PAGE_DIRECTORY + "admin_add_channel";
	}

	@RequestMapping(value = "/addChannel", method = RequestMethod.POST)
	public String postChannel(HttpServletRequest request, Channel channel,
			MultipartFile file, ModelMap model) {

		if (file.getSize() > 0) {
			String fileName = file.getOriginalFilename();
			System.out.println("原始文件名:" + fileName);
			String newFileName = UUID.randomUUID() + fileName;
			ServletContext sc = request.getSession().getServletContext();
			String path = sc.getRealPath("/images/front/temp/") + "\\"; // 设定文件保存的目录
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			if (!file.isEmpty()) {
				try {
					FileOutputStream fos = new FileOutputStream(path
							+ newFileName);
					InputStream in = file.getInputStream();
					int b = 0;
					while ((b = in.read()) != -1) {
						fos.write(b);
					}
					fos.close();
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("上传图片到:" + "images/front/temp/" + newFileName);
			channel.setLogo("images/front/temp/" + newFileName);
		}
		// 保存文件地址，用于JSP页面回显
		// model.addAttribute("fileUrl", path + newFileName);
		if (channel.getId() != null && !"".equals(channel.getId())) {
			adminChannelService.update(channel);
		} else {
			channel.setCreateTime(new Date());
			adminChannelService.insert(channel);
		}

		return "redirect:list.action";
	}

	@RequestMapping(value = "/updateChannel", method = RequestMethod.GET)
	public String updateChannel(Model model, Integer id) {
		Channel channel = adminChannelService.getById(id);
		model.addAttribute("channel", channel);

		return PAGE_DIRECTORY + "admin_update_channel";
	}

	@RequestMapping(value = "/deleteChannel", method = RequestMethod.GET)
	public String deleteChannel(Model model, Integer id) {
		Channel channel = adminChannelService.getById(id);
		if (channel.getDeleteStatus() == null || channel.getDeleteStatus() == 0) {
			channel.setDeleteStatus(1);
			adminChannelService.update(channel);
		}
		return "redirect:list.action";
	}

}
