package com.jtd.web.service.front;

import com.jtd.web.po.Ad;
import com.jtd.web.po.AdCategory;
import com.jtd.web.service.IBaseService;

import java.util.List;

public interface IFrontAdCategoryService extends IBaseService<AdCategory> {

	public void updateAdCategoryByAdId(AdCategory ac) ;

    /**
     * ad_id campaign_id
     * @param ac
     */
    public List<AdCategory> findAdCategoryBy(AdCategory ac);


    public void saveAdCategory(Ad ad);

    public void deleteAdCategory(AdCategory adCategory);

    public void updateCatgId(AdCategory ac);
}
