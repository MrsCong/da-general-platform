package com.dgp.core.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgp.core.web.mapper.BizMapper;

/**
 * 可以按业务做前置或后置操作
 *
 * @param <M>
 * @param <T>
 */
public abstract class BaseBiz<M extends BizMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * insert
     *
     * @param entity
     * @return
     */
    public int insert(T entity) {
        //前置操作
        return super.baseMapper.insert(entity);
    }

    /**
     * 前置操作
     * @param entity
     * @return
     */
    public boolean updateById(T entity) {
        //前置操作
        return super.updateById(entity);
    }

}
