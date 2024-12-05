package com.xly.codemind.common;

import com.xly.codemind.constant.CommonConstant;
import lombok.Data;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/5 22:02
 * @description
 **/

@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
