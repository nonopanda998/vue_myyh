package com.myyh.system.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.myyh.system.annotation.Query;
import com.myyh.system.pojo.Log;
import com.myyh.system.pojo.vo.LogQuery;
import com.myyh.system.pojo.vo.PageQuery;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Slf4j
@SuppressWarnings({"unchecked", "all"})
public class QueryHelp {

    /**
     *  JPA 构建 Specification
     *
     * @param root  查询实体类
     * @param query 查询对象
     * @param cb    查询构造器
     * @param <R>
     * @param <Q>
     * @return
     */
    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<>();

        if (query == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);
                    if (ObjectUtil.isEmpty(val) || "".equals(val)) {
                        continue;
                    }


                    Join join = null;
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(root.get(s)
                                    .as(String.class), "%" + val.toString() + "%"));
                        }
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(cb.or(orPredicate.toArray(p)));
                        continue;
                    }
                    if (ObjectUtil.isNotEmpty(joinName)) {
                        String[] joinNames = joinName.split(">");
                        for (String name : joinNames) {
                            switch (q.join()) {
                                case LEFT:
                                    if (ObjectUtil.isNotNull(join)) {
                                        join = join.join(name, JoinType.LEFT);
                                    } else {
                                        join = root.join(name, JoinType.LEFT);
                                    }
                                    break;
                                case RIGHT:
                                    if (ObjectUtil.isNotNull(join)) {
                                        join = join.join(name, JoinType.RIGHT);
                                    } else {
                                        join = root.join(name, JoinType.RIGHT);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), val));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(cb.lessThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_NQ:
                            list.add(cb.lessThan(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), val.toString() + "%"));
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Long>) val)) {
                                list.add(getExpression(attributeName, join, root).in((Collection<Long>) val));
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(cb.notEqual(getExpression(attributeName, join, root), val));
                            break;
                        case NOT_NULL:
                            list.add(cb.isNotNull(getExpression(attributeName, join, root)));
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            list.add(cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                    (Comparable) between.get(0), (Comparable) between.get(1)));
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int size = list.size();
        return cb.and(list.toArray(new Predicate[size]));
    }

    @SuppressWarnings("unchecked")
    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtil.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }


    /**
     *     MyBatisPlus 构建QueryWrapper
     *     不支持JOIN 关联查询
     *
     * @param query      查询对象
     * @param pageQuery  查询参数
     * @param classzz    查询对象字节码
     * @param <Q>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Q, T> Wrapper<Q> getQuery(T query, PageQuery<T, Q> pageQuery, Class classzz) {
        QueryWrapper<Q> wp = new QueryWrapper<Q>();
        //排序设置
        String sortField = pageQuery.getSortField();
        String sqlSortField = null;
        String sort = pageQuery.getSort();
        if (!ObjectUtil.isEmpty(sortField)) {
            //获取实体类字段
            Field[] fields = classzz.getDeclaredFields();
            Field sqlField = Arrays.stream(fields).filter(field -> field.getName().equalsIgnoreCase(sortField)).findAny().get();
            boolean accessible = sqlField.isAccessible();
            sqlField.setAccessible(true);
            TableField annotation = sqlField.getAnnotation(TableField.class);
            sqlSortField = annotation.value();
            sqlField.setAccessible(accessible);

            if (sort.toLowerCase().startsWith("desc")) {
                wp.orderByDesc(sqlSortField);
            } else {
                wp.orderByAsc(sqlSortField);
            }
        }
        //查询条件为空则返回空类
        if (query == null) {
            return wp;
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);
                    if (ObjectUtil.isEmpty(val) || "".equals(val)) {
                        continue;
                    }
                    // 模糊多字段  ，mybatis 不做支持
//                    if (ObjectUtil.isNotEmpty(blurry)) {
//                        String[] blurrys = blurry.split(",");
//                        List<Predicate> orPredicate = new ArrayList<>();
//                        for (String s : blurrys) {
//                            int i = 0;
//                            wp.like(attributeName, val.toString().split(",")[i]);
//                            i++;
//                        }
//                        continue;
//                    }

                    switch (q.type()) {
                        case EQUAL:
                            wp.eq(attributeName, val);
                            break;
                        case GREATER_THAN:
                            wp.ge(attributeName, val);
                            break;
                        case LESS_THAN:
                            wp.le(attributeName, val);
                            break;
                        case LESS_THAN_NQ:
                            wp.lt(attributeName, val);
                            break;
                        case GREATER_THAN_NQ:
                            wp.gt(attributeName, val);
                            break;
                        case INNER_LIKE:
                            wp.like(attributeName, val);
                            break;
                        case LEFT_LIKE:
                            wp.likeLeft(attributeName, val);
                            break;
                        case RIGHT_LIKE:
                            wp.likeRight(attributeName, val);
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Long>) val)) {
                                wp.in(attributeName, val);
                            }
                            break;
                        case NOT_EQUAL:
                            wp.ne(attributeName, val);
                            break;
                        case NOT_NULL:
                            wp.isNotNull(attributeName);
                            break;

                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            wp.between(attributeName, between.get(0), between.get(1));
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return wp;
    }
}
