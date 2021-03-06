package org.neusoft.neubbs.service;

import org.neusoft.neubbs.entity.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 用户业务接口
 *      - 增删查改排序
 *
 * @author Suvan
 */
public interface IUserService {

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 用户密码
     * @param email 用户邮箱
     * @return UserDO 注册成功，获取新用户信息
     */
    UserDO registerUser(String username, String password, String email);

    /**
     * 登录认证
     *
     * @param username 用户名
     * @param password 用户密码
     * @return UserDO 通过登录验证,获取用户信息
     *
     */
    UserDO loginAuthenticate(String username, String password);

    /**
     * （邮箱）确认用户已经激活
     *
     * @param email 用户邮箱
     */
    void confirmUserActivatedByEmail(String email);


    /**
     * 确认用户匹配 Cookie 用户
     *      - 不匹配则抛出异常，无权进行非本用户操作
     *
     * @param inputUsername 输入用户
     * @param cookieUser Cookie用户对象
     */
    void confirmUserMatchCookieUser(String inputUsername, UserDO cookieUser);

    /**
     * 统计用户总数
     *
     * @return int 用户总数
     */
    int countUserTotals();

    /**
     * 统计用户发帖总数
     *
     * @param userId 用户id
     * @return int 用户发帖总数
     */
    int countUserTopicTotals(int userId);

    /**
     * 统计用户回复总数
     *
     * @param userId 用户id
     * @return int 用户回复总数
     */
    int countUserReplyTotals(int userId);

    /**
     * 统计用户喜欢话题总数
     *
     * @param userId 用户id
     * @return int 喜欢话题总数
     */
    int countUserLikeTopicTotals(int userId);

    /**
     * 统计用户收藏话题总数
     *
     * @param userId 用户id
     * @return int 收藏话题总数
     */
    int countUserCollectTopicTotals(int userId);

    /**
     * 统计用户关注话题总数
     *
     * @param userId 用户id
     * @return int 关注话题总数
     */
    int countUserAttentionTopicTotals(int userId);

    /**
     * 统计用户关注人数（主动关注）
     *
     * @param userId 用户id
     * @return int 关注人总数
     */
    int countUserFollowingTotals(int userId);

    /**
     * 统计用户被关注人数（被关注）
     *
     * @param userId 用户id
     * @return int 被关注人总数
     */
    int countUserFollowedTotals(int userId);


    /**
     * 获取用户信息（已经登录状态）
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return Map userInfoMap用户信息键值对
     */
    Map<String, Object> getUserInfoToPageModelMap(String username, String email);

    /**
     * id 获取用户信息
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    UserDO getUserInfoById(int userId);

    /**
     * name 获取用户信息
     *
     * @param username 用户名（支持 name 或 email 类型）
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByName(String username);

    /**
     * email 获取用户信息
     *
     * @param email 用户邮箱
     * @return UserDO 用户对象
     */
    UserDO getUserInfoByEmail(String email);

    /**
     * 获取用户信息 Map
     *
     * @param user 用户对象
     * @return Map 已过滤过的用户信息Map
     */
    Map<String, Object> getUserInfoMapByUser(UserDO user);

    /**
     * 获取所有主动关注人用户信息列表
     *
     * @param userId 用户id
     * @return List 用户主动关注用户信息列表
     */
    List<Map<String, Object>> listAllFollowingUserInfoToPageModelList(int userId);

    /**
     * 获取所有被关注用户信息列表
     *
     * @param userId 用户id
     * @return List 用户被关注用户信息列表
     */
    List<Map<String, Object>> listAllFollowedUserInfoToPageModelList(int userId);

    /**
     * 用户是否存在
     *
     * @param username 用户名
     * @param email 用户邮箱
     * @return boolean 存在结果
     */
    boolean isUserExist(String username, String email);

    /**
     * 获取用户激活状态
     *
     * @param username 用户名
     * @return boolean 激活状态（true-已激活，false-未激活）
     */
    boolean isUserActivatedByName(String username);

    /**
     * 判断用户激活状态
     *
     * @param state 用户激活状态
     * @return boolean 激活状态（true-激活，false-未激活）
     */
    boolean isUserActivatedByState(int state);


    /**
     * 判断用户是否喜欢话题
     *      - 是否已经点击
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return boolean 是点击过喜欢按钮（true-喜欢，false-未点击）
     */
    boolean isUserLikeTopic(int userId, int topicId);

    /**
     * 判断用户是否主动关注用户
     *
     * @param currentUserId 当前用户id
     * @param followingUserId 主动关注用户id
     * @return boolean 判断结果（true-已关注，false-未关注）
     */
    boolean isFollowingUser(int currentUserId, int followingUserId);

    /**
     * 修改用户个人信息
     *      - 返回修改成功，重新查询的用户信息
     *
     * @param username 用户名
     * @param sex 性别（0-女，1-男）
     * @param birthday 出生日期
     * @param position 所在地
     * @param description 个人描述
     * @return Map 用户
     */
    Map<String, Object> alterUserProfile(String username, int sex, String birthday,
                                         String position, String description);

    /**
     * 修改用户密码
     *      - 失败直接抛出异常
     *
     * @param username 用户名
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByName(String username, String newPassword);

    /**
     * 修改用户密码
     *
     * @param email 用户邮箱
     * @param newPassword 用户新密码
     */
    void alterUserPasswordByEmail(String email, String newPassword);

    /**
     * 修改用户邮箱
     *
     * @param username 用户名
     * @param newEmail 用户新邮箱
     */
    void alterUserEmail(String username, String newEmail);

    /**
     * 修改用户头像
     *
     * @param username 用户名
     * @param newImageName 用户新头像名字
     */
    void alterUserAvatorImage(String username, String newImageName);

    /**
     * (Base64 加密 token)修改用户激活状态
     *
     * @param token Base64加密密文
     * @return UserDO 激活完用户重新返回用户数据
     */
    UserDO alterUserActivateStateByToken(String token);

    /**
     * 修改用户行为，喜欢话题 id 列表
     *      - 指令只支持（inc-自增1，dec-自减1）
     *
     * @param userId 用户 id
     * @param topicId 话题 id
     * @param command 操作指令（inc-自增1，dec-自减1）
     */
    void alterUserActionLikeTopicIdArray(int userId, int topicId, String command);

    /**
     * 操作关注用户
     *
     * @param currentUserId 当前用户 id
     * @param followingUserId 关注用户id
     * @return List 用户目前主动关注用户id列表
     */
    List<Integer> operateFollowUser(int currentUserId, int followingUserId);
}
