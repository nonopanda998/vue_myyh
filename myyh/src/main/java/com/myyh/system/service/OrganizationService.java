package com.myyh.system.service;

import cn.hutool.core.lang.Assert;
import com.myyh.system.dao.OrganizationDao;
import com.myyh.system.pojo.Organization;
import com.myyh.system.pojo.vo.OrganizationQuery;
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
public class OrganizationService {

	@Autowired
	private OrganizationDao organizationDao;

	public List<Organization> getAll(PageParameter pageParameter) {
		OrganizationQuery searchKey =(OrganizationQuery) pageParameter.getSearchKey(OrganizationQuery.class);
		Specification<Organization> spec = new Specification<Organization>() {
			@Override
			public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = QueryHelp.getPredicate(root, searchKey, cb);
				return predicate;
			}
		};
		//根据查询条件，获取所有符合条件机构
		List<Organization> organizationList = organizationDao.findAll(spec);
		//为机构的子节点赋值
		List<Organization> resoutList = new ArrayList<>();
		//ids应对搜索时，搜索结果无根节点情况；
		Set<Integer> ids = new HashSet<>();
		for (Organization orgOut : organizationList) {
			if(orgOut.getPid() == 0 ){
				resoutList.add(orgOut);
			}
			for (Organization orgIn : organizationList) {
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
	public boolean update(Organization updateOrg) {
		Organization org = organizationDao.getOne(updateOrg.getId());
		org.setOrgName(updateOrg.getOrgName());
		org.setBankCode(updateOrg.getBankCode());
		org.setEnabled(updateOrg.getEnabled());
		org.setPid(updateOrg.getPid());
		organizationDao.save(org);
		return true;
	}

	/**
	 * 获取机构层级结构
	 * @return
	 */
	public List<Map<String, Object>> tree() {
		//获取所有一级机构
		List<Organization> byPid = organizationDao.findByPid(0);
		//过滤掉禁用的机构
		List<Organization> resoutList = byPid.stream().filter(org -> org.getEnabled()).collect(Collectors.toList());
		return getOrgTree(resoutList);
	}

	/**
	 * 递归获取子机构map集合
	 * @param rootOrg
	 * @return
	 */
	private List<Map<String,Object>> getOrgTree(List<Organization> rootOrg) {
		List<Map<String,Object>> list = new LinkedList<>();
		rootOrg.forEach(org -> {
					if (org!=null){
						List<Organization> orgList = organizationDao.findByPid(org.getId());
						List<Organization> collect = orgList.stream().filter(item -> item.getEnabled()).collect(Collectors.toList());
						Map<String,Object> map = new HashMap<>(16);
						map.put("id",org.getId());
						map.put("value",org.getId());
						map.put("label",org.getOrgName());
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
	 * 新增机构
	 * @param organization
	 * @return
	 */
	public Boolean addOrganization(Organization organization) {
		organization.setCreateTime(new Date());
		organizationDao.save(organization);
		return true;
	}

	/**
	 * 删除机构及子机构
	 * @param id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean del(Integer id) {
		Organization org = organizationDao.getOne(id);
		Assert.notNull(org,"删除机构不存在");
		List<Organization> byPidList = organizationDao.findByPid(id);
		HashSet set = new HashSet();
		HashSet<Organization> delSet = getDelOrg(byPidList,set);
		delSet.add(org);
		for (Organization delorg : delSet) {
			organizationDao.deleteById(delorg.getId());
		}
		return true;
	}

	/**
	 * 递归获取需删除机构
	 * @param byPidList
	 * @param resoutSet
	 * @return
	 */
	private HashSet<Organization> getDelOrg(List<Organization> byPidList,HashSet<Organization> resoutSet) {
		for (Organization org : byPidList) {
			resoutSet.add(org);
			List<Organization> byPid = organizationDao.findByPid(org.getId());
			if(byPid.size()>0){
				getDelOrg(byPid,resoutSet);
			}
		}
		return resoutSet;
	}

	/**
	 * 查询所有启用状态的机构
	 * @return
	 */
	public List<Map<String, Object>> getallEnabledOrgs() {
		List<Map<String,Object>> resoutList = new LinkedList<>();
		List<Organization> byEnabled = organizationDao.findByEnabled(true);
		for (Organization org : byEnabled) {
			Map<String,Object> map = new HashMap<>();
			map.put("value",org.getId());
			map.put("label",org.getOrgName());
			resoutList.add(map);
		}
		return resoutList;
	}
}
