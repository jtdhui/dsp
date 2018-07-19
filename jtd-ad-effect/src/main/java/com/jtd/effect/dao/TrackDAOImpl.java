package com.jtd.effect.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.po.Click;
import com.jtd.effect.po.OrderDetail;
import com.jtd.effect.po.Param;
import com.jtd.effect.po.Track;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class TrackDAOImpl implements TrackDAO {
	
	private static final Log log = LogFactory.getLog(TrackDAOImpl.class);

	private DataSource dataSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.doddata.tracker.dao.TrackDAO#saveTrack(net.doddata.tracker.po.Track)
	 */
	@Override
	public void saveTrack(Track track) {
		if(track.getType() <= 2) {
			//保存订单数据
			saveOrder(track);
		} else {
			//保存效果跟踪数据
			saveEffect(track);
		}
	}

	/**
	 * 保存订单数据
	 * @param track
	 * @return void
	 */
	private void saveOrder(Track track) {
		
		Click c = track.getClick();
		Param p = c.getParam();
		
		String orderid = track.getTrackParam("orderid");
		if (track.getType() == 1 && StringUtils.isEmpty(orderid)) {
			log.error("没有订单ID: " + JSON.toJSONString(track));
			return;
		}

		if (exist(p.getP(), orderid)) {
			log.error("订单ID已经存在: " + JSON.toJSONString(track));
			return;
		}

		Integer goodsnum = track.getTrackParam("goodsnum");
		if (goodsnum == null) {
			log.error("没有商品数: " + JSON.toJSONString(track));
			return;
		}
		Integer totalamount = track.getTrackParam("totalamount");
		if (totalamount == null) {
			log.error("没有总金额: " + JSON.toJSONString(track));
			return;
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement insertPst = conn.prepareStatement("insert into orders (partner_id,group_id,camp_id,camp_type,ad_type,trans_type,cregrp_id,creative_id,channel_id,host,ad_id,cookie_id,ip,city_id,ord_type,order_id,sku_num,amount,click_time,ret_time,rd,date,hour,date_hour) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			insertPst.setLong(1, p.getP());
			insertPst.setLong(2, p.getG());
			insertPst.setLong(3, p.getCp());
			insertPst.setInt(4, p.getCpt());
			insertPst.setInt(5, p.getAt());
			insertPst.setInt(6, p.getTs());
			insertPst.setLong(7, p.getAg());
			insertPst.setLong(8, p.getCr());
			insertPst.setInt(9, p.getCh());

			String pageUrl = p.getUrl();
			if (!StringUtils.isEmpty(pageUrl)) {
				String host = "未知";
				try {
					URL u = new URL(pageUrl);
					host = u.getHost();
				} catch (Exception e) {}
				insertPst.setString(10, host);
			} else {
				String appp = p.getAppp();
				if (!StringUtils.isEmpty(appp)) {
					insertPst.setString(10, appp);
				} else {
					insertPst.setString(10, "未知");
				}
			}
			
			insertPst.setLong(11, p.getA());
			insertPst.setString(12, track.getCookieid());
			insertPst.setString(13, track.getUserip());
			insertPst.setInt(14, p.getCt() == null ? 0 : p.getCt());

			insertPst.setInt(15, track.getType());
			if(orderid != null) {
				insertPst.setString(16, orderid);
			} else {
				insertPst.setNull(16, Types.VARCHAR);
			}
			insertPst.setInt(17, goodsnum);
			insertPst.setInt(18, totalamount);
			long now = System.currentTimeMillis();
			insertPst.setTimestamp(19, new Timestamp(c.getTime()));
			insertPst.setTimestamp(20, new Timestamp(now));
			int rd = (int)((now - c.getTime()) / 86400000);
			insertPst.setInt(21, rd);
			String yyyyMMddHH = new SimpleDateFormat("yyyyMMddHH").format(now);
			insertPst.setInt(22, Integer.parseInt(yyyyMMddHH.substring(0, 8)));
			insertPst.setInt(23, Integer.parseInt(yyyyMMddHH.substring(8)));
			insertPst.setInt(24, Integer.parseInt(yyyyMMddHH));
			insertPst.executeUpdate();
			ResultSet rs = insertPst.getGeneratedKeys();
			long oid = 0;
			if(rs.next()) oid = rs.getLong(1);
			List<OrderDetail> ds = track.getTrackParam("details");
			if(ds != null) {
				PreparedStatement detailInsertPst = conn.prepareStatement("insert into order_detalls (order_tab_id,order_id,sku_id,sku_num,price,sku_url,sku_pic_url) values(?,?,?,?,?,?,?)");
				for(OrderDetail d : ds) {
					detailInsertPst.setLong(1, oid);
					if(orderid != null) {
						detailInsertPst.setString(2, orderid);
					} else {
						detailInsertPst.setNull(2, Types.VARCHAR);
					}
					detailInsertPst.setString(3, d.getGoodsid());
					detailInsertPst.setInt(4, d.getGoodsnum());
					detailInsertPst.setInt(5, d.getPrice());
					if(!StringUtils.isEmpty(d.getPageurl())) {
						detailInsertPst.setString(6, d.getPageurl());
					} else {
						detailInsertPst.setNull(6, Types.VARCHAR);
					}
					if(!StringUtils.isEmpty(d.getImgurl())) {
						detailInsertPst.setString(7, d.getImgurl());
					} else {
						detailInsertPst.setNull(7, Types.VARCHAR);
					}
					detailInsertPst.addBatch();
				}
				detailInsertPst.executeBatch();
			}
			conn.commit();
		} catch (Exception e) {
			try { conn.rollback(); } catch (Exception e1) {}
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception e) {}
			}
		}
	}
	
	/**
	 * 从数据中查看，订单是否存在
	 * @param partnerid
	 * @param orderid
	 * @return
	 * @return boolean
	 */
	private boolean exist(long partnerid, String orderid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement pst = conn.prepareStatement("select count(0) from orders where partner_id = ? and order_id = ?");
			pst.setLong(1, partnerid);
			pst.setString(2, orderid);
			ResultSet rs = pst.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		} catch (Exception e) {
			try { conn.rollback(); } catch (Exception e1) {}
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception e) {}
			}
		}
		return false;
	}

	/**
	 * 保存效果数据
	 * @param track
	 * @return void
	 */
	private void saveEffect(Track track) {

		Click c = track.getClick();
		Param p = c.getParam();

		int type = track.getType();
		String effectid = null;
		switch(type) {
		case 4:
		case 8:
		case 9:
			effectid = track.getTrackParam("userid");
			break;
			
		case 5:
			effectid = track.getTrackParam("shareid");
			break;

		case 6:
			effectid = track.getTrackParam("favid");
			break;

		case 7:
			effectid = track.getTrackParam("askid");
			break;
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement insertPst = conn.prepareStatement("insert into effects (partner_id,group_id,camp_id,camp_type,ad_type,trans_type,cregrp_id,creative_id,channel_id,host,ad_id,cookie_id,ip,city_id,ord_type,order_id,click_time,ret_time,rd,date,hour,date_hour) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			insertPst.setLong(1, p.getP());
			insertPst.setLong(2, p.getG());
			insertPst.setLong(3, p.getCp());
			insertPst.setInt(4, p.getCpt());
			insertPst.setInt(5, p.getAt());
			insertPst.setInt(6, p.getTs());
			insertPst.setLong(7, p.getAg());
			insertPst.setLong(8, p.getCr());
			insertPst.setInt(9, p.getCh());

			String pageUrl = p.getUrl();
			if (!StringUtils.isEmpty(pageUrl)) {
				String host = "未知";
				try {
					URL u = new URL(pageUrl);
					host = u.getHost();
				} catch (Exception e) {}
				insertPst.setString(10, host);
			} else {
				String appp = p.getAppp();
				if (!StringUtils.isEmpty(appp)) {
					insertPst.setString(10, appp);
				} else {
					insertPst.setString(10, "未知");
				}
			}
			
			insertPst.setLong(11, p.getA());
			insertPst.setString(12, track.getCookieid());
			insertPst.setString(13, track.getUserip());
			insertPst.setInt(14, p.getCt() == null ? 0 : p.getCt());

			insertPst.setInt(15, track.getType());
			if(effectid != null) {
				insertPst.setString(16, effectid);
			} else {
				insertPst.setNull(16, Types.VARCHAR);
			}
			long now = System.currentTimeMillis();
			insertPst.setTimestamp(17, new Timestamp(c.getTime()));
			insertPst.setTimestamp(18, new Timestamp(now));
			int rd = (int)((now - c.getTime()) / 86400000);
			insertPst.setInt(19, rd);
			String yyyyMMddHH = new SimpleDateFormat("yyyyMMddHH").format(now);
			insertPst.setInt(20, Integer.parseInt(yyyyMMddHH.substring(0, 8)));
			insertPst.setInt(21, Integer.parseInt(yyyyMMddHH.substring(8)));
			insertPst.setInt(22, Integer.parseInt(yyyyMMddHH));
			insertPst.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			log.error("保存效果发生错误", e);
			try { conn.rollback(); } catch (Exception e1) {}
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception e) {}
			}
		}
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
