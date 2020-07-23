package com.myyh.system.dao;


import com.myyh.system.pojo.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrganizationDao extends JpaRepository<Organization,Integer>, JpaSpecificationExecutor<Organization> {

	List<Organization> findByPid(Integer id);

	List<Organization> findByEnabled(Boolean enabled);
}
