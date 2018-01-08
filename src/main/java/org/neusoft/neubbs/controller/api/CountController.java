package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.IParamCheckService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统计 api
 *      - 论坛基数统计
 *          - 用户总数
 *          - 话题总数
 *          - 回复总数
 *      - 在线统计
 *          - 在线访问人数
 *          - 在线登陆人数
 *      - 用户统计
 *          - 主动关注人数
 *          - 粉丝数
 *          - 喜欢话题数
 *          - 收藏话题数
 *          - 关注话题数量
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/count")
@ResponseBody
public class CountController {

    private final IHttpService httpService;
    private final IUserService userService;
    private final ITopicService topicService;
    private final IParamCheckService paramCheckService;

    @Autowired
    public CountController(IHttpService httpService, IUserService userService,
                           ITopicService topicService, IParamCheckService paramCheckService) {
        this.httpService = httpService;
        this.userService = userService;
        this.topicService = topicService;
        this.paramCheckService = paramCheckService;
    }

    /**
     * 论坛基数统计（CountController 默认访问）
     *      - 用户总数
     *      - 话题总数
     *      - 回复总数
     *
     * @param request http默认请求
     * @return PageJsonDTO 页面传输对象
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public PageJsonDTO count(HttpServletRequest request) {
        Map<String, Object> modelMap = new LinkedHashMap<>(SetConst.SIZE_THREE);
            modelMap.put(ParamConst.USER, userService.countUserTotals());
            modelMap.put(ParamConst.TOPIC, topicService.countTopicTotals());
            modelMap.put(ParamConst.REPLY, topicService.countReplyTotals());
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, modelMap);
    }

    /**
     * 在线统计
     *      - 在线访问人数
     *      - 在线登陆人数
     *
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/online", method = RequestMethod.GET)
    public PageJsonDTO login(HttpServletRequest request) {
        Map<String, Object> onlineCountMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
            onlineCountMap.put(ParamConst.VISIT_USER, httpService.getOnlineVisitUserNumber(request));
            onlineCountMap.put(ParamConst.LOGIN_USER, httpService.getOnlineLoginUserNumber(request));
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, onlineCountMap);
    }

    /**
     * 用户统计
     *      - 用户主动关注人数
     *      - 用户粉丝数
     *      - 用户喜欢话题数
     *      - 用户收藏话题数
     *      - 用户关注话题数
     *      - 用户发帖数
     *      - 用户回复数
     *
     * @param userId 用户id
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public PageJsonDTO user(@RequestParam(value = "userid", required = false) String userId) {
        paramCheckService.check(ParamConst.USER_ID, userId);

        int userIdInt = Integer.valueOf(userId);
        Map<String, Object> userCountMap = new LinkedHashMap<>(SetConst.FIVE);
            userCountMap.put(ParamConst.FOLLOWING, userService.countUserFollowingTotals(userIdInt));
            userCountMap.put(ParamConst.FOLLOWED, userService.countUserFollowedTotals(userIdInt));
            userCountMap.put(ParamConst.LIKE, userService.countUserLikeTopicTotals(userIdInt));
            userCountMap.put(ParamConst.COLLECT, userService.countUserCollectTopicTotals(userIdInt));
            userCountMap.put(ParamConst.ATTENTION, userService.countUserAttentionTopicTotals(userIdInt));
            userCountMap.put(ParamConst.TOPIC, userService.countUserTopicTotals(userIdInt));
            userCountMap.put(ParamConst.REPLY, userService.countUserReplyTotals(userIdInt));
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, userCountMap);
    }
}
