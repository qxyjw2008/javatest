package com.bstek.demo.ims.business;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bstek.demo.ims.dao.ProducerDao;
import com.bstek.demo.ims.entity.Producer;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.hibernate.HibernateUtils;
@Component
public class ProducherPR {
	@Resource
	ProducerDao producerDao;
	
	@DataProvider
	public void getProducer(Page<Producer> page,Criteria criteria) throws Exception{
		DetachedCriteria dc = DetachedCriteria.forClass(Producer.class);
		if(criteria != null){
			HibernateUtils.createFilter(dc, criteria);
		}
		producerDao.find(page,dc.getExecutableCriteria(producerDao.getSession()));
		
	}
	@DataProvider
	public List<Producer> getAllProducer(){
		return producerDao.find("from Producer");
	}
	@Expose
	public String validatorCompanyName(String name){
		if(StringUtils.hasText(name)){
			if(producerDao.findUnique("from Producer where companyName=?", new Object[]{name}) != null){
				return "��Ӧ�������ظ���";
			}
		}
		return null;
	}
	@DataResolver
	@Transactional
	public void saveAll(List<Producer> producers){
		producerDao.persistEntities(producers);
	}
}
