package com.websystique.springmvc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.websystique.springmvc.dao.AbstractDao;
import com.websystique.springmvc.dao.ContentPlayingNowDao;
import com.websystique.springmvc.model.ContentPlayingNow;

@Repository("contentPlayingNowDao")
public class ContentPlayingNowDaoImpl extends AbstractDao<Integer, ContentPlayingNow> implements ContentPlayingNowDao{

	@SuppressWarnings("unchecked")
	public List<ContentPlayingNow> findByDeviceId(int deviceId){
		Criteria crit = createEntityCriteria();
		crit.setTimeout(50000);
		Criteria deviceCrit = crit.add(Restrictions.ne("isDeleted",1)).createCriteria("device");
		deviceCrit.add(Restrictions.eq("id", deviceId));
		//Criteria userDocCrit = crit.createCriteria("userDocument");
		//userDocCrit.setProjection(Projections.groupProperty("playGroup"));
		crit.addOrder(Order.asc("startTime"));
		return (List<ContentPlayingNow>)crit.list();
	}

}
