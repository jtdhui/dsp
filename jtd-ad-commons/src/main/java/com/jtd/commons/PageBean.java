package com.jtd.commons;

import java.util.List;

/**
 * 处理翻页后，产生的翻页bean，记录了所有翻页和查询结果的信息。
 */
public class PageBean<T> {
	/**
	 * 当前页号
	 */
	private int currentPage;
	/**
	 * 每页记录数
	 */
	private int length;
	/**
	 * 总页数
	 */
	private long totalPages;
	/**
	 * 总记录数
	 */
	private long totalRecords;
	/**
	 * 结果
	 */
	private List<T> results;

	public PageBean() {
		super();
	}

	public PageBean(int currentPage, int length) {
		this.currentPage = currentPage;
		this.length = length;
	}

	public PageBean(int length) {
		this.length = length;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long count) {
		totalRecords = count;
		if (length != 0) {
			totalPages = totalRecords / length;
			if (totalRecords % length != 0) {
				totalPages++;
			}

			if (totalPages != 0) {
				if (currentPage < 1) {
					currentPage = 1;
				}
			} else {
				currentPage = 0;
			}
		}

	}

	/**
	 * @return
	 */
	public boolean canToFirst() {
		return currentPage > 1;
	}

	/**
	 * @return
	 */
	public boolean canToLast() {
		return currentPage < totalPages;
	}

	/**
	 * @return
	 */
	public boolean canToNext() {
		return currentPage < totalPages;
	}

	/**
	 * @return
	 */
	public boolean canToPre() {
		return currentPage > 1;
	}

	/**
	 * 起始记录号。
	 * 
	 * @return
	 */
	public long getRsFirstNumber() {
		if (currentPage == 0) {
			return 1;
		}
		return (currentPage - 1) * length + 1;
	}

	public long getRsLastNumber() {
		long rsLastNumber = currentPage * length;
		if (results != null && rsLastNumber > totalRecords) {
			rsLastNumber = totalRecords;
		}
		return rsLastNumber;
	}

	public long getFirstPage() {
		if (this.getTotalPages() <= 0) {
			return 0;
		}
		int displayNum = 5;
		int index = this.getCurrentPage();
		int startPage = 0;
		long totalPage = this.getTotalPages();
		if (index - displayNum / 2 < 1) {
			startPage = 1;
		} else if (index + displayNum / 2 > totalPage) {
			int n = (int) (totalPage - displayNum + 1);
			startPage = n > 0 ? n : 1;
		} else {
			startPage = index - displayNum / 2;
		}
		return startPage;
	}

	public long getLastPage() {
		if (this.getTotalPages() <= 0) {
			return 0;
		}
		int displayNum = 5;
		int index = this.getCurrentPage();
		int startPage = 0, endPage = 0;
		long totalPage = this.getTotalPages();
		if (index - displayNum / 2 < 1) {
			endPage = (int) (displayNum > totalPage ? totalPage : displayNum);
		} else if (index + displayNum / 2 > totalPage) {
			endPage = (int) totalPage;
		} else {
			startPage = index - displayNum / 2;
			endPage = startPage + displayNum - 1;
		}
		return endPage;
	}
}
