package com.jtd.web.service.impl;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.service.IBaseService;

public abstract class BaseService<T> implements IBaseService<T>{

	protected abstract BaseDao<T> getDao();
	
	/**
	 * 根据ID查找记录.
	 * @param id
	 * @return entity .
	 */ 
	public T getById(long id){
		return (T) this.getDao().getById(id);
	}
	
	/**
	 * 根据条件获取列表
	 * @param <T>
	 * @param t
	 * @return
	 */
	public List<T> listBy(T t){
		return this.getDao().listBy(t);
	}
	
	/**
	 * 获取列表
	 * @return
	 */
	public List<T> listAll(){
		return this.getDao().listAll();
	}
	
	/**
	 * 根据实体对象新增记录.
	 * 
	 * @param entity
	 *            .
	 * @return id .
	 */
	public long insert(T entity){
		long ret = getDao().insert(entity);
		return ret;
	}
	
	/**
	 * 批量保存对象
	 * @return
	 */
	public long insertBatch(List<T> list){
		long ret = getDao().insert(list);
		return ret;
	}

	/**
	 * 更新实体对应的记录.
	 * 
	 * @param entity
	 *            .
	 * @return
	 */
	public long update(T entity){
		long ret = getDao().update(entity);
		return ret;
	}

	/**
	 * 批量更新对象.
	 * 
	 * @param entity
	 *            .
	 * @return int .
	 */
	public long update(List<T> list){
		long ret = getDao().update(list);
		return ret;
	}

	/**
	 * 根据ID删除记录.
	 * 
	 * @param id
	 *            .
	 * @return
	 */
	public long deleteById(long id){
		long ret = getDao().deleteById(id);
		return ret;
	}
	
	/**
	 * 批量删除数据
	 * @param list
	 * @return
	 */
	public long delete(List<T> list){
		long ret = getDao().delete(list);
		return ret;
	}
	
	/**
	 * 单表分页
	 * @param paramMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<T> findPageBy(Map<String, Object> paramMap, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 20 : pageSize;
		
		Pagination<T> page = new Pagination<T>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCondition(paramMap);
		
		List<T> list = this.getDao().getPageBy(page);
		page.setList(list);
		return page;
	}
	
	/**
	 * 多表分页
	 * @param paramMap
	 * @param pageNo
	 * @param pageSize
	 * @return map对象的集合
	 */
	public Pagination<Map<String, Object>> findMapPageBy(Map<String, Object> paramMap, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		
		Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCondition(paramMap);
		
		List<Map<String, Object>> listMap = this.getDao().getMapPageBy(page);
		page.setListMap(listMap);
		return page;
	}
	
	/**
	 * 单表分页
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<T> findFrontPageBy(Map<String, Object> paramMap, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		
		Pagination<T> page = new Pagination<T>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCondition(paramMap);
		
		List<T> list = this.getDao().getFrontPageBy(page);
		page.setList(list);
		return page;
	}
	
	/**
	 * 多表分页(前台查询)
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return map对象的集合
	 */
	public Pagination<Map<String, Object>> findMapFrontPageBy(Map<String, Object> paramMap, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		
		Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCondition(paramMap);
		
		List<Map<String, Object>> listMap = this.getDao().getMapFrontPageBy(page);
		page.setListMap(listMap);
		return page;
	}
}
