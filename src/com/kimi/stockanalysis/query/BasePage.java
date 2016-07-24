/**
 * 
 */
package com.kimi.stockanalysis.query;

/**
 * @author kimi
 *
 */
public class BasePage {
	/**
	 * 每页数据条数，用于分页查询，默认为10
	 */
	private Integer pageSize = 10;
	
	/**
	 * 页码，用于分页查询，默认为第一页（行数从0开始）
	 */
	private Integer pageIndex = 0;

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageIndex
	 */
	public Integer getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex
	 *            the pageIndex to set
	 */
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
}
