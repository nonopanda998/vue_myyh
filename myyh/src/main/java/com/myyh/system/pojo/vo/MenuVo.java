package com.myyh.system.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {

    private String name;

    private String path;

    private Boolean hidden ;

    private String redirect;

    private String component;

    private MenuMetaVo meta;

    private List<MenuVo> children;
    //TODO
    private Boolean alwaysShow;

}