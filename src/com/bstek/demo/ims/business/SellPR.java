package com.bstek.demo.ims.business;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.demo.ims.dao.GoodsDao;
import com.bstek.demo.ims.dao.SellDao;
import com.bstek.demo.ims.entity.Goods;
import com.bstek.demo.ims.entity.Sell;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@Component
public class SellPR {
	@Resource
	SellDao sellDao;
	@Resource
	GoodsDao goodsDao;

	@DataProvider
	public void getSell(Page<Sell> Page, Map<String, Object> params) {

		if (params == null)

			params = new HashMap<String, Object>();

		StringBuffer sql = new StringBuffer("from Sell where  1=1 ");

		List<Object> paramValues = new ArrayList<Object>();

		Iterator<String> iterator = params.keySet().iterator();

		while (iterator.hasNext()) {

			String key = iterator.next();

			Object value = params.get(key);

			if ("name".equals(key)) {

				sql.append(" and goods.name like ?");

				paramValues.add("%" + value + "%");

			} else if ("shortName".equals(key)) {

				sql.append(" and goods.shortName like ?");

				paramValues.add("%" + value + "%");

			} else if ("birthplace".equals(key)) {

				sql.append(" and goods.birthplace like ?");

				paramValues.add("%" + value + "%");

			} else if ("packaging".equals(key)) {

				sql.append(" and goods.packaging like ?");

				paramValues.add("%" + value + "%");

			} else if ("customer".equals(key) && value != null) {

				sql.append(" and customer.id=?");

				paramValues.add(value);

			} else if ("beginTime".equals(key) && value != null) {

				sql.append(" and storeTime>=?");

				paramValues.add(value);

			} else if ("endTime".equals(key) && value != null) {

				sql.append(" and storeTime<=?");

				paramValues.add(value);

			}

		}

		// ִ�о���Ĳ�ѯ

		sellDao.find(Page, sql.toString(), paramValues.toArray());

	}

	@DataResolver
	@Transactional
	public void updateAll(List<Sell> sell) throws Exception {

		sellDao.persistEntities(sell);

		for (Sell c : sell) {

			doAdjustingStorage(c);
		}
	}

	// ������
	private void doAdjustingStorage(Sell sell) throws Exception {
		Goods goods = sell.getGoods();

		Float oldTotalnumber = (Float) EntityUtils.getOldFloat(sell,
				"totalnumber");
		Float newTotalnumber = Float
				.valueOf(sell.getTotalnumber() == null ? "0" : sell
						.getTotalnumber().toString());

		// ���ƫ��
		Float storeDeviation = Float.valueOf(0);

		// ����ƫ��
		Float salesDeviation = Float.valueOf(0);

		if (EntityState.DELETED.equals(EntityUtils.getState(sell))) {
			salesDeviation = newTotalnumber;
			storeDeviation = newTotalnumber;
		} else if (EntityState.MODIFIED.equals(EntityUtils.getState(sell))) {
			salesDeviation = newTotalnumber - oldTotalnumber;
			storeDeviation -= salesDeviation;
		} else if (EntityState.NEW.equals(EntityUtils.getState(sell))) {
			salesDeviation = newTotalnumber;
			storeDeviation -= salesDeviation;
		}
		Float storage = Float.valueOf(goods.getStorage() == null ? "0" : goods
				.getStorage().toString());
		if (storage == null || storeDeviation + storage < 0) {
			throw new Exception("�����С�ڳ�����.");
		}
		Float sales = Float.valueOf(goods.getSales() == null ? "0" : goods
				.getSales().toString());
		if (sales == null)
			sales = Float.valueOf(0);
		goods.setStorage((int) (storage + storeDeviation));
		goods.setSales((int) (sales + salesDeviation));
		goodsDao.save(goods);

	}

}
