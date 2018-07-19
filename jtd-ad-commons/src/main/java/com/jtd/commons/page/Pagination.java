package com.jtd.commons.page;

import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Pagination<T> extends SimplePage implements java.io.Serializable, Paginable {

	public Pagination() {

	}

	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	public Pagination(int pageNo, int pageSize, int totalCount, List<T> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 当前页的数据(主要是实体类的列表集合也就是单表查询)
	 */
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 当前页的数据 Map集合存放列表集合属性需要自定义的情况,多用于多表关联查询的集合,也可以存放单表集合的数据
	 */
	private List<Map<String, Object>> listMap;

	public List<Map<String, Object>> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map<String, Object>> listMap) {
		this.listMap = listMap;
	}

	/** 生成翻页页面 */
	public String getPageHtml() {

		StringBuffer pageHtml = new StringBuffer("");

		pageHtml.append("<div class=\"col-sm-6\">");
		pageHtml.append("第" + pageNo + "/" + this.getTotalPage() + "页，共" + totalCount + "条记录");
		pageHtml.append("</div>");
		pageHtml.append("<div class=\"col-sm-6 dataTables_paginate\">");
		pageHtml.append("<ul class='pagination'>");

		if (this.getPageNo() > 1) {
			if (this.getPageNo() > 5) {
				pageHtml.append("<li><a href='javascript:;' onclick='_submitform(1)'>首页</a></li>");
			}
			pageHtml.append(
					"<li><a href='javascript:;'  onclick='_submitform(" + (this.getPageNo() - 1) + ")'>上一页</a></li>");
		} else {
			pageHtml.append("<li class='disabled'><a href='javascript:;'>上一页</a></li>");
		}
		for (int i = (this.getPageNo() - 2 <= 0 ? 1 : this.getPageNo() - 2), no = 1; i <= this.getTotalPage()
				&& no < 6; i++, no++) {
			if (this.getPageNo() == i) {
				pageHtml.append("<li class='active'><a href='javascript:void(0);' >" + i + "</a></li>");
			} else {
				pageHtml.append("<li><a href='javascript:;' onclick='_submitform(" + i + ")'>" + i + "</a></li>");
			}
		}
		if (this.getPageNo() < this.getTotalPage()) {
			pageHtml.append(
					"<li><a href='javascript:;'  onclick='_submitform(" + (this.getPageNo() + 1) + ")'>下一页</a></li>");
		} else {
			pageHtml.append("<li class='disabled'><a href='javascript:;'>下一页</a></li>");
		}
		pageHtml.append("</ul>\n");

		pageHtml.append("</div>");
		
		pageHtml.append("<script type=\"text/javascript\">\n");
		pageHtml.append("	function _submitform(pageNo){ \n");
		pageHtml.append(" 		if(isNaN(pageNo) == false && pageNo <= " + this.getTotalPage() + "){ \n ");
		pageHtml.append("			$(\"form\").append($(\"<input type='hidden' value='\" + pageNo +\"' name='pageNo'>\")).submit(); \n");
		pageHtml.append(" 		} \n");
		pageHtml.append("	} \n");
		pageHtml.append("</script>\n");

		pageHtml.append("</div>");

		return pageHtml.toString();
	}

	/**
	 * 前台分页模板样式
	 * @return
	 */
	public String getFrontPageHtml() {

		StringBuffer pageHtml = new StringBuffer("");

		pageHtml.append("<div class=\"paging clearfix\">\n");
		pageHtml.append("	<p class=\"paging_lt\">\n");
		
		int rowStart = ((pageNo - 1 ) * pageSize) + 1 ;
		int rowEnd = (pageNo * pageSize) > totalCount ? totalCount : (pageNo * pageSize) ;
		
		pageHtml.append("		<span>当前" + rowStart + "-"+ rowEnd +"条</span> <span>共" + this.getTotalCount()+ "条</span> <span>每页展现\n");
		pageHtml.append("			<select class=\"form-control\" id=\"pageSize-select\" onChange=\"setSelectVal()\" style=\"width:50px;\">");
		pageHtml.append("				<option value=\"10\" " + (pageSize == 10 ? "selected" : "") + ">10</option> ");
		pageHtml.append("				<option value=\"20\" " + (pageSize == 20 ? "selected" : "") + ">20</option> ");
		pageHtml.append("				<option value=\"50\" " + (pageSize == 50 ? "selected" : "") + ">50</option>");
		pageHtml.append("				<option value=\"100\" " + (pageSize == 100 ? "selected" : "") + ">100</option> ");
		pageHtml.append("			</select>条 \n");
		pageHtml.append("		</span>\n");
		pageHtml.append("	</p>\n");
		
		pageHtml.append("	<p class=\"paging_rt\">\n");
		pageHtml.append("		<span>\n");
		if (this.getPageNo() > 1) {
			pageHtml.append( "		<a href='javascript:;'  onclick='_submitform(" + (this.getPageNo() - 1) + ")'>< </a>");
		} 
		pageHtml.append(				pageNo + "/" + this.getTotalPage());
		if (this.getPageNo() < this.getTotalPage()) {
			pageHtml.append( "		<a href='javascript:;'  onclick='_submitform(" + (this.getPageNo() + 1) + ")'> > </a>");
		} 
		pageHtml.append(	" 		共" + this.getTotalPage() + "页</span>\n");
		pageHtml.append("		<span>向第 <input type=\"text\" name='pageNo' id='pageNoId' value=\"" + pageNo + "\">页</span> \n");
		pageHtml.append("		<span class=\"jump_paging\" onclick='goPage()'>跳转</span>\n");
		pageHtml.append("	</p>\n");
		
		pageHtml.append("	<input type='hidden' value=\"" + this.getPageSize() +"\" id='pageSizeId' name='pageSize'>\n");
		
		pageHtml.append("	<script type=\"text/javascript\">\n");
		
		pageHtml.append("		function _submitform(pageNo){ \n");
		pageHtml.append(" 			if(isNaN(pageNo) == false && pageNo <= " + this.getTotalPage() + "){ \n ");
		pageHtml.append("				$(\"#pageNoId\").val(pageNo); \n");
		pageHtml.append("				$(\"form\").submit(); \n");
		pageHtml.append("			} \n");
		pageHtml.append("		} \n");
		
		pageHtml.append("		function goPage(){ \n");
		pageHtml.append("			_submitform($(\"#pageNoId\").val()); \n");
		pageHtml.append("		} \n");
		
		pageHtml.append("		function setSelectVal(){ \n");
		pageHtml.append("			var selectVal = $(\"#pageSize-select\").val(); \n");
		pageHtml.append("			$(\"#pageSizeId\").val(selectVal); \n");
		pageHtml.append("			$(\"form\").submit(); \n ");
		pageHtml.append("		} \n");
		
		pageHtml.append("	</script>\n");
		
		pageHtml.append("</div>");

		return pageHtml.toString();
	}

}
