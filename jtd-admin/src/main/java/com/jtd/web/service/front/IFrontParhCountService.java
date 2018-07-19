package com.jtd.web.service.front;

import java.util.List;
import java.util.Map;

import com.jtd.commons.page.Pagination;
import com.jtd.web.po.count.Parh;
import com.jtd.web.service.IBaseService;

public interface IFrontParhCountService extends IBaseService<Parh> {

	public List<Parh> listByMap(Map<String, Object> phMap, boolean isAllDay);

    public Pagination<Parh> findFullPageBy(Map<String, Object> phMap, Integer pageNo, Integer pageSize, boolean isAllDay);
}
