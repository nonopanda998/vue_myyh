package com.myyh.system.controller;

import com.myyh.system.annotation.Anonymous;
import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Article;
import com.myyh.system.pojo.vo.ArticleQuery;
import com.myyh.system.pojo.vo.PageQuery;
import com.myyh.system.pojo.vo.QPageResult;
import com.myyh.system.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/article")
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Anonymous
    @LogInfo("分页查询文章")
    @GetMapping("/pagelist")
    public QPageResult<Article> list(PageQuery<ArticleQuery,Article> pageQuery){
        return  new QPageResult<Article>(articleService.list(pageQuery));
    }


}
