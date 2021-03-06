package com.ares.app.dao;

import org.springframework.stereotype.Repository;

import com.ares.app.domain.Do.AdminDO;
import com.ares.framework.dao.mongodb.CollectionName;
import com.ares.framework.dao.mongodb.MongoDBDAO;


@Repository
@CollectionName("admin")
public class AdminDAO extends MongoDBDAO<AdminDO>{

	public AdminDAO() {
		super(AdminDO.class);
	}
}
