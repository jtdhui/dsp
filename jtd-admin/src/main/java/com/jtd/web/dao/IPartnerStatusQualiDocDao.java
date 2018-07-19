package com.jtd.web.dao;

import java.util.HashMap;
import java.util.List;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.PartnerStatusQualiDoc;

public interface IPartnerStatusQualiDocDao extends
		BaseDao<PartnerStatusQualiDoc> {

	public List<PartnerStatusQualiDoc> listByMap(HashMap<String, Object> params);

	/**
	 * 在渠道送审的时候，查询partner是否曾提交过相应类型的资质
	 * 
	 * @param partnerId
	 * @param docTypeId
	 * @param channelId
	 * @return
	 */
	public PartnerStatusQualiDoc findWhenChannelAudit(long partnerId,
                                                      long docTypeId, long channelId);
	
	/**
	 * 按照partnerId，docTypeId，channelId为条件，修改qualiDocId，submitUserId
	 * 
	 * @param qualiDocId
	 * @param partnerId
	 * @param docTypeId
	 * @param channelId
	 * @return
	 */
	public long updateWhenChannelAudit(long qualiDocId, long submitUserId, long partnerId,
                                       long docTypeId, long channelId);

	// /**
	// * 当广告主在前台删除资质时，将该类资质送审的记录删掉，因为向渠道提交资质时，只会提交没有送审记录的资质类别
	// *
	// * @param partnerId
	// * @param docTypeId
	// * @par
	// * @return
	// */
	// public long deleteBy(Long partnerId, Long docTypeId , Long channelId);
}
