package com.myyh.system.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单中的元信息
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

}