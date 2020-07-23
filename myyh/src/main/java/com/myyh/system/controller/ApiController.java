package com.myyh.system.controller;

import com.myyh.system.annotation.Anonymous;
import com.myyh.system.annotation.LogInfo;
import com.myyh.system.dao.VisitsDao;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.pojo.Visits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class ApiController {

    @Autowired
	VisitsDao visitsDao;

    @LogInfo("统计信息")
    @Anonymous
    @GetMapping(value = "/chartData")
    public ResultBean charData() {
        HashMap map = new HashMap();
        LocalDate localDate = LocalDate.now();
        List<Visits> list = visitsDao.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return new ResultBean<>(map);
    }


    @LogInfo("查询")
    @Anonymous
    @GetMapping
    public ResultBean get() {
        Map<String,Object> map = new HashMap<>(4);
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsDao.findByDate(localDate.toString());
        List<Visits> list = visitsDao.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        long recentVisits = 0, recentIp = 0;
        for (Visits data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits","");
        map.put("newIp",10L);
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);
        return new ResultBean(map);
    }

    @LogInfo("统计")
    @Anonymous
    @PostMapping
    public ResultBean post() {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsDao.findByDate(localDate.toString());
        visits.setPvCounts(visits.getPvCounts()+1);
        long ipCounts = 10L;
        visits.setIpCounts(ipCounts);
        visitsDao.save(visits);
        return new ResultBean<>();
    }

}
