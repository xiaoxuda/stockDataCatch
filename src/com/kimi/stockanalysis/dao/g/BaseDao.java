package com.kimi.stockanalysis.dao.g;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
/**
 * @author kimi
 * @version 0.99
 * @see 数据库操作基类
 * @param T数据实体类，K主键（如果主键有一个以上则K与T相同）
 */
public class BaseDao<T,K> {
	protected String namespace;
	protected SqlSessionTemplate sqlSession;
	/**
	 * @see 返回sqlSession
	 * @return
	 */
	public SqlSessionTemplate getSqlSession(){
		return this.sqlSession;
	}
	/**
	 * @see 用于自动注入
	 * @param sqlSession
	 */
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	/**
	 * 插入操作
	 * @param entity 数据表对应的实体类
	 * @return 
	 */
	public int insert(T entity){
		return sqlSession.insert(this.namespace+".insert", entity);
	};
	/**
	 * @see 根据主键删除对应数据
	 * @param key
	 * @return
	 */
	public int delete(K key){
		return sqlSession.delete(this.namespace+".delete", key);
	};
	/**
	 * 根据主键更新对应数据条目
	 * @param entity
	 * @return
	 */
	public int update(T entity){
		return sqlSession.update(this.namespace+".update", entity);
	};
	/**
	 * 根据主键更新对应数据条目，如果entity数据域为null则不更新对应数据域
	 * @param entity
	 * @return
	 */
	public int updateSelective(T entity){
		return sqlSession.update(this.namespace+".updateSelective", entity);
	};
	/**
	 * 根据主键查询对应数据条目
	 * @param key
	 * @return
	 */
	public T selectOne(K key){
		return sqlSession.selectOne(this.namespace+".selectOne", key);
	};
	/**
	 * 根据查询条件返回相应数据条目，entity中值为null的数据域不作为查询条件
	 * @param entity
	 * @return
	 */
	public List<T> selectList(T entity){
		return sqlSession.selectList(this.namespace+".selectList", entity);
	};
}
