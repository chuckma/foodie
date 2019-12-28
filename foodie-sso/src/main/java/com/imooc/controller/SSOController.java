package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UserVO;
import com.imooc.service.UserService;
import com.imooc.utils.JSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author mcg
 * @create 2019-11-14-17:39
 */
@Controller
public class SSOController {


    public static final String REDIS_USER_TOKEN = "redis_user_token";
    private static final String REDIS_USER_TICKET = "redis_user_ticket";
    private static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    private static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);


        // 1  获取 userTicket ，如果 cookie 可以获取到，此时签发一个临时的票据，并回跳
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        boolean isVerified = verifyUserTicket(userTicket);
        if (isVerified) {
            // 验证通过
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
        }
        // 2 用户从未登录，则统一跳转到 cas 的统一登录页面。
        return "login";
    }

    /**
     * 校验 cas 用户全局门票
     * @param userTicket
     * @return
     */
    private boolean verifyUserTicket(String userTicket) {
        // ticket 是否为 空
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }
        // ticket是否有效

        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return false;
        }

        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return false;
        }
        return true;
    }


    /**
     * CAS 的统一登录接口  目的有 3
     *  1. 登录后创建用户的全局会话 -> uniqueToken
     *  2. 创建用户全局门票 ，用以表示 在 CAS 端是否登录 -> userTicket
     *  3. 创建用户的临时票据，用户回传跳转 -> tmpTicket
     * @param username
     * @param password
     * @param returnUrl
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        model.addAttribute("returnUrl", returnUrl);



        // 0 用户名和密码不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }


        // 1 实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            model.addAttribute("errmsg", "用户名或者密码不正确");
            return "login";
        }

        // 2 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), JsonUtils.objectToJson(userVO));

        // 3 生成 ticket 门票，全局门票，用以表示 用户在 CAS 登录过
        String userTicket = UUID.randomUUID().toString().trim();

        // 3.1 用户全局门票需要放入 cas 端的 cookie 中。
        setCookie(COOKIE_USER_TICKET,userTicket,response);

        // 4 userTicket 关联用户ID，存入 redis，表示这个用户有门票了，可以自由穿梭
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, userResult.getId());

        // 5 临时票据，回调到调用端网站，是有 cas 签发的 一个一次性的 ticket
        String tmpTicket = createTmpTicket();


//        return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }


    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public JSONResult verifyTmpTicket(String tmpTicket,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        // 使用临时一次性票据验证用户是否登录，如果登录过，把用户信息返回站点，并销毁临时票据

        String tmpTicketValue = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isBlank(tmpTicketValue)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 0 如果临时票据可用，则需要销毁，并且拿到cas 端 cookie 中全局的 userTicket,以此获取用户会话
        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return JSONResult.errorUserTicket("用户票据异常");
        } else {
            // 销毁临时票据
            redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);
        }

        // 验证并且获取用户的 userTicket
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 验证成功，返回用户会话
        return JSONResult.ok(JsonUtils.jsonToPojo(userRedis, UserVO.class));
    }



    @PostMapping("/logout")
    @ResponseBody
    public JSONResult logout(String userId,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // 获取 cas 中的 用户门票
        String userTicket = getCookie(request, COOKIE_USER_TICKET);

        // 清除 redis、cookie 中 的ticket
        deleteCoolie(COOKIE_USER_TICKET, response);
        redisOperator.del(REDIS_USER_TICKET + ":" + userTicket);
        // 清除用户分布式会话
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        // 验证成功，返回用户会话
        return JSONResult.ok();
    }



    private String createTmpTicket(){
        String tmpTicket = UUID.randomUUID().toString().trim();
        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {

        }
        return tmpTicket;
    }

    private void deleteCoolie(String key, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    private void setCookie(String key, String val, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    private String  getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(key)) {
            return null;
        }
        String cookieValue = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }

}
