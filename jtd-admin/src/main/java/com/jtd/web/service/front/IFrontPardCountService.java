package com.jtd.web.service.front;

import java.util.Map;

import com.jtd.web.po.count.Pard;
import com.jtd.web.service.IBaseService;

public interface IFrontPardCountService extends IBaseService<Pard> {

	public Pard getByMap(Map<String, Object> pdMap);

}
