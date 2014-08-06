package com.bstek.demo.ims.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.demo.ims.dao.GoodsDao;
import com.bstek.demo.ims.dao.PurchasingDao;
import com.bstek.demo.ims.entity.Goods;
import com.bstek.demo.ims.entity.Purchasing;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@Component
public class PurchasingPR {
	@Resource
	PurchasingDao purchasingDao;
	@Resource
	GoodsDao goodsDao;

	@DataProvider
	public void getPurchasing(Page<Purchasing> Page, Map<String, Object> params) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
			StringBuffer sql = new StringBuffer("from Purchasing where 1=1 ");

			List<Object> paramValues = new ArrayList<Object>();
			Iterator<String> iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Object value = params.get(key);
				if ("name".equals(key)) {
					sql.append(" and goods.name like '%" + value + "%'");
				} else if ("shortName".equals(key)) {
					sql.append(" and goods.shortName like '%" + value + "%'");
				} else if ("birthplace".equals(key)) {
					sql.append(" and goods.birthplace like '%" + value + "%'");
				} else if ("packaging".equals(key)) {
					sql.append(" and goods.packaging like '%" + value + "%'");
				} else if ("producer".equals(key) && value != null) {
					Map<String, Object> val = (Map<String, Object>) value;
					sql.append(" and producer.id='"+val.get("id")+"'");
				} else if ("beginTime".equals(key) && value != null) {
					sql.append(" and sellTime>='"+value+"'");
				} else if ("endTime".equals(key) && value != null) {
					sql.append(" and sellTime<='"+value+"'");
				}
			}
			  // 执行具体的查询
	        purchasingDao.find(Page, sql.toString());
	}
		@DataResolver
		@Transactional
		    public void updateAll(List<Purchasing> purchasing) {
		        purchasingDao.persistEntities(purchasing);
		        for (Purchasing p : purchasing) {
		            doAdjustingStorage(p);
		        }
		    }

		    // 处理库存
		    private void doAdjustingStorage(Purchasing purchasing) {
		        Goods goods = purchasing.getGoods();
		        Float oldTotalnumber = EntityUtils.getOldFloat(purchasing,
		                "totalnumber");
		        Float newTotalnumber =  Float.valueOf(purchasing.getTotalnumber()==null?"0":purchasing.getTotalnumber().toString());
		        // 入库偏差
		        Float storeDeviation = Float.valueOf(0);

		        if (EntityState.DELETED.equals(EntityUtils.getState(purchasing))) {
		            storeDeviation -= newTotalnumber;
		        } else if (EntityState.MODIFIED
		                .equals(EntityUtils.getState(purchasing))) {
		            storeDeviation = newTotalnumber - oldTotalnumber;
		        } else if (EntityState.NEW.equals(EntityUtils.getState(purchasing))) {
		            storeDeviation = newTotalnumber;
		        }
		        Float storage = Float.valueOf(goods.getStorage()==null?"0":goods.getStorage().toString());
		        if (storage == null)
		            storage = Float.valueOf(0);
		        Float totalStorage = Float.valueOf(goods.getTotalStorage()==null?"0":goods.getTotalStorage().toString());
		        if (totalStorage == null)
		            totalStorage = Float.valueOf(0);
		        goods.setTotalStorage((int)(totalStorage + storeDeviation));
		        goods.setStorage((int)(storage + storeDeviation));
		        goodsDao.save(goods);
		    }
}
