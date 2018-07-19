package com.jtd.web.service;

import java.util.List;
import java.util.Map;

import com.jtd.commons.page.Pagination;

public interface IBaseService<T> {

	
	/**
	 * 根据ID查找记录.
	 * 
	 * @param id
	 *            .
	 * @return entity .
	 */
	T getById(long id);
	
	/**
	 * 根据条件获取列表
	 * @param <T>
	 * @param t
	 * @return
	 */
	public List<T> listBy(T t);
	
	/**
	 * 获取列表
	 * @return
	 */
	public List<T> listAll();
	
	/**
	 * 根据实体对象新增记录.
	 * 
	 * @param entity
	 *            .
	 * @return id .
	 */
	long insert(T entity);
	
	/**
	 * 批量保存对象
	 * @return
	 */
	long insertBatch(List<T> list);

	/**
	 * 更新实体对应的记录.
	 * 
	 * @param entity
	 *            .
	 * @return
	 */
	long update(T entity);

	/**
	 * 批量更新对象.
	 * 
	 * @param entity
	 *            .
	 * @return int .
	 */
	long update(List<T> list);

	/**
	 * 根据ID删除记录.
	 * 
	 * @param id
	 *            .
	 * @return
	 */
	long deleteById(long id);
	
	/**
	 * 批量删除数据
	 * @param list
	 * @return
	 */
	long delete(List<T> list);
	
	/**
	 * 单表分页
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<T> findPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize);
	
	/**
	 * 多表分页
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<Map<String, Object>> findMapPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 单表分页(前台)
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<T> findFrontPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize);
	
	/**
	 * 多表分页(前台)
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return 实体对象的集合
	 */
	public Pagination<Map<String, Object>> findMapFrontPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize);
	
}
