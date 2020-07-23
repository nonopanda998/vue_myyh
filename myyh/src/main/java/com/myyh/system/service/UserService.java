package com.myyh.system.service;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.myyh.system.config.SecurityProperties;
import com.myyh.system.config.TokenProvider;
import com.myyh.system.dao.OrganizationDao;
import com.myyh.system.dao.UserDao;
import com.myyh.system.exception.BadRequestException;
import com.myyh.system.pojo.Organization;
import com.myyh.system.pojo.Role;
import com.myyh.system.pojo.vo.*;
import com.myyh.system.pojo.User;
import com.myyh.system.util.*;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${loginCode.expiration}")
    private Long expiration;

    @Value("${rsa.private_key}")
    private String privateKey;

    /**
     * 验证码图片生成
     *
     * @return
     */
    public Object getCode() {
        //生成验证码 算术类型 https://gitee.com/whvse/EasyCaptcha
        SpecCaptcha specCaptcha = new SpecCaptcha(111, 36, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //放入缓存
        cacheManagerUtil.put("CAPTCHA", uuid, verCode);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", specCaptcha.toBase64());
            put("uuid", uuid);
        }};
        return imgResult;
    }


    /**
     * 登录
     *
     * @param loginUser
     * @param request
     * @return
     */
    public Map login(User loginUser, HttpServletRequest request) {
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        log.debug("默认字符集：" + Charset.defaultCharset());
        String password = new String(rsa.decrypt(loginUser.getPassword(), KeyType.PrivateKey), Charset.defaultCharset());
        //读取验证码
        String code = (String) cacheManagerUtil.get("CAPTCHA", loginUser.getUuid());
        // 清除验证码
        cacheManagerUtil.del("CAPTCHA", loginUser.getUuid());

        if (MyStringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (MyStringUtils.isBlank(loginUser.getCode()) || !loginUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 保存在线用户到Redis,key：online-key+token
        try {
             savetoCache(jwtUser, token, request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("保存在线信息失败！" + e.getMessage());
        }
        User byUsername = userDao.findByUsername(loginUser.getUsername());
        byUsername.setLastLoginTime(new Date());
        String ip = MyStringUtils.getIp(request);
        byUsername.setLastLoginIp(ip);
        //更新最后一次登陆时间
        saveUser(byUsername);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        return authInfo;
    }


    /**
     * 保存用户
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    public  void saveUser(User user) {
        userDao.save(user);
    }


    /**
     * 退出登陆
     *
     * @param request
     */
    public boolean logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        String key = properties.getOnlineKey() + token;
        log.debug("退出登录删除缓存的key为:" + key);
        cacheManagerUtil.del("ONLINEUSER",key);
        return true;
    }


    /**
     * 分页查询用户
     *
     * @param pageRequest
     * @return
     */
    public Page<User> findAll(PageParameter pageRequest) {
        UserQuery searchKey = (UserQuery) pageRequest.getSearchKey(UserQuery.class);
        return userDao.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, searchKey, criteriaBuilder)
                , pageRequest.toPageable());
    }


    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {
        User userSelect = userDao.findByUsername(user.getUsername());
        Assert.isNull(userSelect, "用户已存在！");
        List<String> names = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<Role> roleSet = roleService.findByRoles(names);
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(user.getPassword(), KeyType.PrivateKey), Charset.defaultCharset());
        String encodePassWord = passwordEncoder.encode(password);
        user.setPassword(encodePassWord);
        user.setCreateTime(new Date());
        user.setEnabled(user.getEnabled());
        user.setRoles(roleSet);
        userDao.save(user);
        return true;
    }


    /**
     * 获取认证用户信息
     *
     * @return
     */
    public JwtUser getJwtUser() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return jwtUser;
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(User user) {
        User byUsername = userDao.findByUsername(user.getUsername());
        if(byUsername!=null && !byUsername.getId().equals(user.getId())){
            //修改了用户名，但用户名已存在
            Assert.isNull(byUsername,"用户名:"+user.getUsername()+"已存在！");
        }
        List<String> names = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<Role> newRoleList = roleService.findByRoles(names);
        User daoUser = userDao.getOne(user.getId());
        daoUser.setUsername(user.getUsername());
        daoUser.setPhone(user.getPhone());
        daoUser.setEmail(user.getEmail());
        daoUser.setNickName(user.getNickName());
        daoUser.setEnabled(user.getEnabled());
        daoUser.setOid(user.getOid());
        daoUser.setRoles(newRoleList);
        daoUser.setUpdateTime(new Date());
        //更新用户
        User updateUser = userDao.save(daoUser);
        return !ObjectUtils.isEmpty(updateUser);
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        userDao.deleteById(id);
        return true;
    }


    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    public User findByName(String username) {
        return userDao.findByUsername(username);
    }


    /**
     * 保存用户到缓存
     * @param jwtUser
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    public boolean savetoCache(JwtUser jwtUser, String token, HttpServletRequest request) throws Exception {
        String ip = MyStringUtils.getIp(request);
        String browser = MyStringUtils.getBrowser(request);
        OnlineUser onlineUser = new OnlineUser(jwtUser.getUsername(), browser, ip, EncryptUtils.desEncrypt(token), new Date());
        cacheManagerUtil.put("ONLINEUSER",token,onlineUser);
        return true;
    }

    /**
     * 获取首页数据
     * @param request
     * @return
     */
    public Map dashboardDate(HttpServletRequest request) {
        Map resoutMap = new HashMap();
        String token = TokenUtil.getToken(request);
        OnlineUser onlineUser = (OnlineUser) cacheManagerUtil.get("ONLINEUSER",token);
        User byUsername = userDao.findByUsername(onlineUser.getUserName());
        if(byUsername!=null){
            Organization org = organizationDao.getOne(byUsername.getOid());
            // 日期格式化
            DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
            resoutMap.put("userName",byUsername.getUsername());
            resoutMap.put("nickName",byUsername.getNickName());
            resoutMap.put("orgName",org.getOrgName());
            resoutMap.put("lastLoginIp",byUsername.getLastLoginIp());
            resoutMap.put("lastLogintime",dateTimeInstance.format(byUsername.getLastLoginTime()));
            resoutMap.put("phone",byUsername.getPhone());
            resoutMap.put("email",byUsername.getEmail());
        }
        return resoutMap;
    }

    /**
     * 首页个人资料修改
     * @param user
     * @param request
     * @return
     */
    public Boolean center(User user, HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        OnlineUser onlineUser = (OnlineUser) cacheManagerUtil.get("ONLINEUSER",token);
        User byUsername = userDao.findByUsername(onlineUser.getUserName());
        byUsername.setNickName(user.getNickName());
        byUsername.setEmail(user.getEmail());
        byUsername.setPhone(user.getPhone());
        userDao.save(byUsername);
        return true;
    }

    public Boolean updatePass(UserPassVo passVo) {
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String oldPass = new String(rsa.decrypt(passVo.getOldPass(), KeyType.PrivateKey));
        String newPass = new String(rsa.decrypt(passVo.getNewPass(), KeyType.PrivateKey));
        User user = userDao.findByUsername(SecurityUtils.getUsername());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("旧密码错误!");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        user.setPassword(passwordEncoder.encode(newPass));
        userDao.save(user);
        return true;
    }

    public Boolean resetPassword(String newPass,Integer id) {
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(newPass, KeyType.PrivateKey));
        User user = userDao.getOne(id);
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
        return true;
    }
}
