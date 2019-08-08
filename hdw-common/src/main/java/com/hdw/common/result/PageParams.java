package com.hdw.common.result;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.hdw.common.constants.CommonConstants;
import com.hdw.common.utils.BeanConvertUtils;
import com.hdw.common.utils.JacksonUtils;
import com.hdw.common.xss.SQLFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @Description 分页参数
 * @Author TuMinglong
 * @Date 2018/6/11 19:41
 */
public class PageParams extends Page implements Serializable {
    private static final long serialVersionUID = 1L;

    //当前页数
    private long currPage;
    //每页记录数
    private long pageSize;
    //排序字段
    private String sort;
    //排序方向
    private String order;
    //参数
    private Map<String, Object> requestMap = Maps.newHashMap();

    //总记录数
    private long totalCount;
    //列表数据
    private List list;


    public PageParams() {
        requestMap = Maps.newHashMap();
    }

    /**
     * 分页
     */
    public PageParams(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
    }

    public PageParams(Map<String, Object> params) {
        if (params == null) {
            params = Maps.newHashMap();
        }
        this.currPage = Long.valueOf(params.getOrDefault(CommonConstants.PAGE_KEY, CommonConstants.DEFAULT_PAGE).toString());
        this.pageSize = Long.valueOf(params.getOrDefault(CommonConstants.PAGE_LIMIT_KEY, CommonConstants.DEFAULT_LIMIT).toString());
        //防止SQL注入
        //排序方向（asc,desc）
        if (params.containsKey("order")) {
            this.order = SQLFilter.sqlInject((String) params.get("order"));
        }
        //排序字段
        if (params.containsKey("sort")) {
            this.sort = SQLFilter.sqlInject((String) params.get("sort"));
        }
        super.setCurrent(currPage);
        super.setSize(pageSize);

        params.remove(CommonConstants.PAGE_KEY);
        params.remove(CommonConstants.PAGE_LIMIT_KEY);
        params.remove(CommonConstants.PAGE_ORDER_KEY);
        params.remove(CommonConstants.PAGE_SORT_KEY);
        requestMap.putAll(params);

    }

    public long getCurrPage() {
        if (currPage <= CommonConstants.MIN_PAGE) {
            currPage = 1;
        }
        return currPage;
    }

    public void setCurrPage(long currPage) {
        this.currPage = currPage;
    }

    public long getPageSize() {
        if (pageSize >= CommonConstants.MAX_LIMIT) {
            pageSize = CommonConstants.MAX_LIMIT;
        }
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public <T> T mapToObject(Class<T> clazz) {
        return BeanConvertUtils.mapToObject(this.getRequestMap(), clazz);
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}
