package com.jtd.commons.dao;

import com.jtd.commons.page.Pagination;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;



/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-common
 * @描述 <p>数据访问层基础支撑接口</p>
 */
public interface BaseDao<T> {
	/**
	 * 根据ID查找记录.
	 * 
	 * @param id
	 *            .
	 * @return entity .
	 */
	T getById(long id);
	
	/**
	 * 获取所有
	 * @return
	 */
	public List<T> listAll();
	
	/**
	 * 根据条件获取列表
	 * @param t
	 * @return
	 */
	public List<T> listBy(T t);
	
	/**
	 * 根据实体对象新增记录.
	 * 
	 * @param entity
	 *            .
	 * @return id .
	 */
	long insert(T entity);

	/**
	 * 批量保存对象.
	 *
	 * @return id .
	 */
	long insert(List<T> list);

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
	 * 单表查询分页
	 * @param page
	 * @return
	 */
	List<T> getPageBy(Pagination<T> page);
	
	/**
	 * 多表关联查询分页
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getMapPageBy(Pagination<Map<String, Object>> page);
	
	/**
	 * 单表查询分页(前台)
	 * @param page
	 * @return
	 */
	List<T> getFrontPageBy(Pagination<T> page);

	/**
	 * 多表关联查询分页(前台)
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getMapFrontPageBy(Pagination<Map<String, Object>> page);
	
	/**
	 * 根据条件查询 listBy: <br/>
	 * 
	 * @param paramMap
	 * @return 返回集合
	 */
//	List<T> listBy(Map<String, Object> paramMap);

//	List<Object> listBy(Map<String, Object> paramMap, String sqlId);

	/**
	 * 根据条件查询 listBy: <br/>
	 *
	 * @param
	 * @return 返回实体
	 */
//	T getBy(Map<String, Object> paramMap);
//
//	Object getBy(Map<String, Object> paramMap, String sqlId);

	SqlSession getSqlSession();

	
}
