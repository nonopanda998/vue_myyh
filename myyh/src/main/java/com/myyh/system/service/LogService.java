package com.myyh.system.service;

import com.myyh.system.dao.LogDao;
import com.myyh.system.pojo.Log;
import com.myyh.system.pojo.vo.LogQuery;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.util.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LogService {

    @Autowired
    private LogDao logDao;


    /**
     * 分页查询日志
     * @return
     * @param pageRequest
     */
    public Page<Log> list(PageParameter pageRequest) {
        LogQuery searchKey = (LogQuery)pageRequest.getSearchKey(LogQuery.class);
        Page<Log> all = logDao.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, searchKey, criteriaBuilder)
                , pageRequest.toPageable());
        return  all;
    }


    /**
     * 保存日志
     * @param log
     */
    @Transactional
    public void save(Log log) {
        logDao.save(log);
    }
}
