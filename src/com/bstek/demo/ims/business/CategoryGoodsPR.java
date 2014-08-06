package com.bstek.demo.ims.business;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bstek.demo.ims.dao.CategoryDao;
import com.bstek.demo.ims.dao.GoodsDao;
import com.bstek.demo.ims.entity.Category;
import com.bstek.demo.ims.entity.Goods;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

@Component
public class CategoryGoodsPR {
	@Resource
	CategoryDao categoryDao;
	@Resource
	GoodsDao goodsDao;
	
	@DataProvider
	public List<Category> getAllCategories(){
		return categoryDao.find("from Category");
	}
	
	@DataProvider
	public void getGoods(Page<Goods> page,int categoryId){
		DetachedCriteria dc = DetachedCriteria.forClass(Goods.class);
		dc.add(Restrictions.eq("categoryId", categoryId));
		goodsDao.find(page,dc.getExecutableCriteria(goodsDao.getSession()));
	}
	@Expose
	public String validatorCategory(String name){
		if(StringUtils.hasText(name)){
			if(categoryDao.findUnique("from Category where name =?", new Object[]{name}) != null){
				return "客户名称不能重复";
			}
		}
		return null;
	}
	
	@DataResolver
	@Transactional
	public void updateAll(List<Category> categorys){
		categoryDao.persistEntities(categorys);
		for (Category category : categorys) {
			Collection<Goods> goods = category.getGoodses();
			if(goods != null){
				for (Goods singleGoods  : goods) {
					singleGoods.setCategoryId(category.getId());
					singleGoods.setCategory(category);
				}
				goodsDao.persistEntities(goods);
			}
		}
	}
}
