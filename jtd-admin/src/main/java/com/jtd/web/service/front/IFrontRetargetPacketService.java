package com.jtd.web.service.front;

import com.jtd.web.po.RetargetPacket;
import com.jtd.web.service.IBaseService;

import java.util.List;

/**
 * Created by duber on 16/12/20.
 */
public interface IFrontRetargetPacketService extends IBaseService<RetargetPacket> {

    public List<RetargetPacket> listByPartnerId(long partnerId);
}
