package com.myyh.system.dao;


import com.myyh.system.pojo.Circle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CircleDao extends JpaRepository<Circle,Integer>, JpaSpecificationExecutor<Circle> {

	List<Circle> findByPid(Integer id);

	List<Circle> findByEnabled(Boolean enabled);
}
