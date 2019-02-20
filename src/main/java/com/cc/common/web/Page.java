/**
 * 
 */
package com.cc.common.web;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class Page<T> {

	/**
	 * 返回状态
	 */
	private boolean success;
	/**
	 * 返回信息
	 */
	private String message;
	/**
	 * 当前页码
	 */
	private int page;
	/**
	 * 当前页数据条数
	 */
	private int pageSize;
	/**
	 * 总条数
	 */
	private long total;
	/**
	 * 总页数
	 */
	private long pages;
	
	/**
	 * 返回数据信息
	 */
	private List<T> data;
	
	/**
	 * 
	 */
	public Page() {
		this.success = Boolean.FALSE;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * @return the pages
	 */
	public long getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(long pages) {
		this.pages = pages;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}
}
