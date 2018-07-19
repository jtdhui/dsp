package com.jtd.commons.dao;


import com.jtd.commons.entity.BaseEntity;
import com.jtd.commons.page.Pagination;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;



/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-common
 * @描述
 *     <p>
 *     数据访问层基础支撑类
 *     </p>
 */
public abstract class BaseDaoImpl<T extends BaseEntity> extends SqlSessionDaoSupport implements BaseDao<T> {

	public static final String SQL_GET_BY_ID = "getById";
	public static final String SQL_LIST_BY_ALL = "listByAll";
	public static final String SQL_LIST_BY = "listBy";
	public static final String SQL_INSERT = "insert";
	public static final String SQL_BATCH_INSERT = "batchInsert";
	public static final String SQL_UPDATE = "update";
	public static final String SQL_DELETE_BY_ID = "deleteById";

	public static final String SQL_LIST_PAGE = "listPage";
	public static final String SQL_COUNT_BY_PAGE_PARAM = "countByPageParam"; // 根据当前分页参数进行统计

	/**
	 * 注入SqlSessionTemplate实例(要求Spring中进行SqlSessionTemplate的配置).<br/>
	 * 可以调用sessionTemplate完成数据库操作.
	 */
//	@Autowired
//	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
//		super.setSqlSessionFactory(sqlSessionFactory);
//	}
//
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	public SqlSession getSqlSession() {
		return super.getSqlSession();
	}

	/**
	 * 根据id查找
	 */
	public T getById(long id) {
		return getSqlSession().selectOne(getStatement(SQL_GET_BY_ID), id);
	}

	/**
	 * 获取所有数据
	 */
	public List<T> listAll() {
		return getSqlSession().selectList(getStatement(SQL_LIST_BY_ALL));
	}

	/**
	 * 根据条件获取所有数据
	 */
	public List<T> listBy(T t) {
		return getSqlSession().selectList(getStatement(SQL_LIST_BY), t);
	}

	/**
	 * 添加一条数据
	 */
	public long insert(T t) {
		if (t == null)
			throw new RuntimeException("T is null");
		int result = getSqlSession().insert(getStatement(SQL_INSERT), t);
		// if (result <= 0)
		// throw PlatformException.DB_INSERT_RESULT_0;

		if (t != null && t.getId() != null && result > 0)
			return t.getId();
		return result;
	}

	/**
	 * 批量添加数据
	 */
	public long insert(List<T> list) {
		if (list == null || list.size() <= 0)
			return 0;
		int result = getSqlSession().insert(getStatement(SQL_BATCH_INSERT), list);
		// if (result <= 0)
		// throw PlatformException.DB_INSERT_RESULT_0;
		return result;
	}

	/**
	 * 更新一条数据
	 */
	public long update(T t) {
		if (t == null)
			throw new RuntimeException("T is null");
		int result = getSqlSession().update(getStatement(SQL_UPDATE), t);
		System.out.println(result);
		// if (result <= 0)
		// throw PlatformException.DB_UPDATE_RESULT_0;
		return result;
	}

	/**
	 * 批量更新数据
	 */
	public long update(List<T> list) {
		if (list == null || list.size() <= 0)
			return 0;

		int result = 0;

		for (int i = 0; i < list.size(); i++) {
			this.update(list.get(i));
			result += 1;
		}
		// if (result <= 0)
		// throw PlatformException.DB_UPDATE_RESULT_0;
		return result;
	}

	/**
	 * 根据id删除一条数据
	 */
	public long deleteById(long id) {
		return (long) getSqlSession().delete(getStatement(SQL_DELETE_BY_ID), id);
	}

	/**
	 * 批量删除数据
	 */
	public long delete(List<T> list) {
		if (list == null || list.size() <= 0)
			return 0;
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			this.deleteById(list.get(i).getId());
			result += 1;
		}
		return result;
	}
	
	/**
	 * 分页查询
	 * 
	 * @param
	 * @param page
	 * @return
	 */
	public List<T> getPageBy(Pagination<T> page){
		return getSqlSession().selectList(getStatement("getPageBy"),page);
	}

	/**
	 * 多表分页查询也可以单表查询
	 */
	public List<Map<String, Object>> getMapPageBy(Pagination<Map<String, Object>> page){
		return getSqlSession().selectList(getStatement("getMapPageBy"),page);
	}
	
	/**
	 * 分页查询
	 * 
	 * @param
	 * @param page
	 * @return
	 */
	public List<T> getFrontPageBy(Pagination<T> page){
		return getSqlSession().selectList(getStatement("getFrontPageBy"),page);
	}

	/**
	 * 多表分页查询也可以单表查询
	 */
	public List<Map<String, Object>> getMapFrontPageBy(Pagination<Map<String, Object>> page){
		return getSqlSession().selectList(getStatement("getMapFrontPageBy"),page);
	}

	public String getStatement(String sqlId) {

		String name = this.getClass().getName();

		StringBuffer sb = new StringBuffer().append(name).append(".").append(sqlId);

		return sb.toString();
	}

}
