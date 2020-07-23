package com.myyh.system.dao;

import com.myyh.system.pojo.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitsDao extends JpaRepository<Visits,Long> {

    /**
     * findByDate
     * @param date 日期
     * @return Visits
     */
    Visits findByDate(String date);

    /**
     * 获得一个时间段的记录
     * @param date1 日期1
     * @param date2 日期2
     * @return List
     */
    @Query(value = "select * FROM visits where create_time between ?1 and ?2",nativeQuery = true)
    List<Visits> findAllVisits(String date1, String date2);
}
