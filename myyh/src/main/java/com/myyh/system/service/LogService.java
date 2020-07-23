package com.myyh.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myyh.system.mapper.LogMapper;
import com.myyh.system.pojo.Log;
import com.myyh.system.pojo.vo.LogQuery;
import com.myyh.system.pojo.vo.PageQuery;
import com.myyh.system.util.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LogService {

    @Autowired
    private LogMapper logDao;


    /**
     * 分页查询日志
     * @return
     * @param pageQuery
     */
    public IPage list(PageQuery<LogQuery,Log> pageQuery) {
        LogQuery searchKey = pageQuery.getSearchKey(LogQuery.class);
        Wrapper<Log> query = QueryHelp.getQuery(searchKey,new Log());
        Page<Log> logPage = logDao.selectPage(pageQuery.getPage(), query);
        Page<Log> sortPage = pageQuery.getSortPage(logPage);
        return sortPage;
    }


    /**
     * 保存日志
     * @param log
     */
    @Transactional
    public void save(Log log) {
        logDao.insert(log);
    }
}
