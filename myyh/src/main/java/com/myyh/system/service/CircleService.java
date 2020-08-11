package com.myyh.system.service;

import cn.hutool.core.lang.Assert;
import com.myyh.system.dao.CircleDao;
import com.myyh.system.pojo.Circle;
import com.myyh.system.pojo.vo.CircleQuery;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.util.QueryHelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CircleService {

	@Autowired
	private CircleDao circleDao;

	public List<Circle> getAll(PageParameter pageParameter) {
		CircleQuery searchKey =(CircleQuery) pageParameter.getSearchKey(CircleQuery.class);
		Specification<Circle> spec = new Specification<Circle>() {
			@Override
			public Predicate toPredicate(Root<Circle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = QueryHelp.getPredicate(root, searchKey, cb);
				return predicate;
			}
		};
		//根据查询条件，获取所有符合条件圈子
		List<Circle> organizationList = circleDao.findAll(spec);
		//为机构的子节点赋值
		List<Circle> resoutList = new ArrayList<>();
		//ids应对搜索时，搜索结果无根节点情况；
		Set<Integer> ids = new HashSet<>();
		for (Circle orgOut : organizationList) {
			if(orgOut.getPid() == 0 ){
				resoutList.add(orgOut);
			}
			for (Circle orgIn : organizationList) {
				if(orgOut.getId().equals(orgIn.getPid())){
					if(orgOut.getChildren() == null){
						orgOut.setChildren(new ArrayList<>());
					}
					orgOut.getChildren().add(orgIn);
					ids.add(orgIn.getId());
				}
			}
		}
		//搜索结果无根节点时
		if(resoutList.size() == 0){
			resoutList = organizationList.stream()
					.filter(org -> !ids.contains(org.getId()))
					.collect(Collectors.toList());
		}
		return resoutList;
	}

	/**
	 * 更新
	 * @param updateOrg
	 * @return
	 */
	public boolean update(Circle updateOrg) {
		Circle org = circleDao.getOne(updateOrg.getId());
		org.setName(updateOrg.getName());
		org.setEnabled(updateOrg.getEnabled());
		org.setPid(updateOrg.getPid());
		circleDao.save(org);
		return true;
	}

	/**
	 * 获取圈子层级结构
	 * @return
	 */
	public List<Map<String, Object>> tree() {
		//获取所有一级圈子
		List<Circle> byPid = circleDao.findByPid(0);
		//过滤掉禁用的圈子
		List<Circle> resoutList = byPid.stream().filter(org -> org.getEnabled()).collect(Collectors.toList());
		return getOrgTree(resoutList);
	}

	/**
	 * 递归获取子圈子map集合
	 * @param rootOrg
	 * @return
	 */
	private List<Map<String,Object>> getOrgTree(List<Circle> rootOrg) {
		List<Map<String,Object>> list = new LinkedList<>();
		rootOrg.forEach(org -> {
					if (org!=null){
						List<Circle> orgList = circleDao.findByPid(org.getId());
						List<Circle> collect = orgList.stream().filter(item -> item.getEnabled()).collect(Collectors.toList());
						Map<String,Object> map = new HashMap<>(16);
						map.put("id",org.getId());
						map.put("value",org.getId());
						map.put("label",org.getName());
						if(collect!=null && collect.size()!=0){
							map.put("children",getOrgTree(collect));
						}
						list.add(map);
					}
				}
		);
		return list;
	}

	/**
	 * 新增圈子
	 * @param organization
	 * @return
	 */
	public Boolean addOrganization(Circle organization) {
		organization.setCreateTime(new Date());
		circleDao.save(organization);
		return true;
	}

	/**
	 * 删除机构及子圈子
	 * @param id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean del(Integer id) {
		Circle org = circleDao.getOne(id);
		Assert.notNull(org,"删除圈子不存在");
		List<Circle> byPidList = circleDao.findByPid(id);
		HashSet set = new HashSet();
		HashSet<Circle> delSet = getDelOrg(byPidList,set);
		delSet.add(org);
		for (Circle delorg : delSet) {
			circleDao.deleteById(delorg.getId());
		}
		return true;
	}

	/**
	 * 递归获取需删除圈子
	 * @param byPidList
	 * @param resoutSet
	 * @return
	 */
	private HashSet<Circle> getDelOrg(List<Circle> byPidList,HashSet<Circle> resoutSet) {
		for (Circle org : byPidList) {
			resoutSet.add(org);
			List<Circle> byPid = circleDao.findByPid(org.getId());
			if(byPid.size()>0){
				getDelOrg(byPid,resoutSet);
			}
		}
		return resoutSet;
	}

	/**
	 * 查询所有启用状态的圈子
	 * @return
	 */
	public List<Map<String, Object>> getallEnabledOrgs() {
		List<Map<String,Object>> resoutList = new LinkedList<>();
		List<Circle> byEnabled = circleDao.findByEnabled(true);
		for (Circle org : byEnabled) {
			Map<String,Object> map = new HashMap<>();
			map.put("value",org.getId());
			map.put("label",org.getName());
			resoutList.add(map);
		}
		return resoutList;
	}
}
