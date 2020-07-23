package com.myyh.system.dao;

import com.myyh.system.pojo.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogDao extends JpaRepository<Log,Integer>, JpaSpecificationExecutor<Log> {

}
