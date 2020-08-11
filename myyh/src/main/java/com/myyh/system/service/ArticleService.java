package com.myyh.system.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.myyh.system.mapper.ArticleMapper;
import com.myyh.system.pojo.Article;
import com.myyh.system.pojo.vo.ArticleQuery;
import com.myyh.system.pojo.vo.PageQuery;
import com.myyh.system.util.QueryHelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleMapper articleDao;


    //分页查询文章
    public IPage list(PageQuery<ArticleQuery, Article> pageQuery) {
        ArticleQuery searchKey = pageQuery.getSearchKey(ArticleQuery.class);
        Wrapper<Article> query = QueryHelp.getQuery(searchKey,pageQuery,Article.class);
        return articleDao.selectPage(pageQuery.getPage(), query);
    }



}
