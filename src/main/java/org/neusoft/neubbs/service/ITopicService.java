package org.neusoft.neubbs.service;

import java.util.List;
import java.util.Map;

/**
 * 话题业务接口
 *      - 增删查改顺序
 *
 * @author Suvan
 */
public interface ITopicService {

    /**
     * 保存话题
     *
     * @param userId 用户id
     * @param categoryNick 话题分类昵称
     * @param title 话题标题
     * @param topicContent 话题内容
     * @return int 新增的话题id
     */
    int saveTopic(int userId, String categoryNick, String title, String topicContent);

    /**
     * 保存回复
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @param replyContent 话题内容
     * @return int 新增的回复id
     */
    int saveReply(int userId, int topicId, String replyContent);

    /**
     * 保存话题分类
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param categoryName 话题分类名称（中文）
     * @return Map 话题分类Map（包含 id（原 nick，英文），name）
     */
    Map<String, Object> saveCategory(String categoryNick, String categoryName);

    /**
     * 删除话题
     *
     * @param topicId 要删除的话题id
     */
    void removeTopic(int topicId);

    /**
     * 删除回复
     *
     * @param replyId 回复id
     */
    void removeReply(int replyId);

    /**
     * 删除话题分类
     *
     * @param categoryNick 话题分类昵称（英文）
     */
    void removeCategory(String categoryNick);

    /**
     * 统计话题总数
     *
     * @return int 话题总数
     */
    int countTopicTotals();

    /**
     * 统计话题回复总数
     *
     * @return int 回复总数
     */
    int countReplyTotals();

    /**
     * 统计话题总页数
     *
     * @param limit 每页限制多少条
     * @param categoryNick 话题分类昵称
     * @param username 用户名
     * @return int 总页数
     */
    int countTopicTotalPages(int limit, String categoryNick, String username);

    /**
     * 获取话题内容点赞数
     *
     * @param topicId 话题id
     * @return int 当前话题点赞数量
     */
    int countTopicContentLike(int topicId);

    /**
     * 获取话题内容页面 Map
     *
     * @param topicId 话题id
     * @return Map 话题内容页信息
     */
    Map<String, Object> getTopicContentPageModelMap(int topicId);

    /**
     * 获取话题回复页面Map
     *      - 单条回复
     *
     * @param replyId 回复id
     * @return Map 回复信息
     */
    Map<String, Object> getReplyPageModelMap(int replyId);

    /**
     * 获取热门话题
     *      - 每天回复数最高
     *      - 默认 10 条（不足 10 条，则补充前一天热议话题）
     *
     * @return List<Map> 话题列表
     */
    List<Map<String, Object>> listHotTalkTopics();

    /**
     * 获取话题列表
     *      - 包含话题基本信息，内容
     *      - 话题用户信息
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param categoryNick 话题分类昵称
     * @param username 用户名
     * @return List<Map> 话题列表
     */
    List<Map<String, Object>> listTopics(int limit, int page, String categoryNick, String username);

    /**
     * 获取所有话题分类
     *      - map 包含（id(过滤后)，name）
     *
     * @return List<String> 话题分类列表
     */
    List<Map<String, Object>> listAllTopicCategorys();

    /**
     * 修改话题内容
     *
     * @param topicId 话题id
     * @param categoryNick 话题分类昵称
     * @param newTitle 新标题
     * @param newTopicContent 新话题内容
     */
    void alterTopicContent(int topicId, String categoryNick, String newTitle, String newTopicContent);

    /**
     * 修改回复内容
     *
     * @param replyId 回复id
     * @param newReplyContent 新回复内容
     */
    void alterReplyContent(int replyId, String newReplyContent);

    /**
     * 修改话题回复数（+1）
     *
     * @param topicId 话题id
     */
    void alterTopicReadAddOne(int topicId);

    /**
     * （通过指令）修改话题喜欢人数（+1 or -1）
     *      - user service 从中获取用户当前用户是否已经点击过喜欢按钮
     *      - param check service 将会检查指令是否为 'inc' or 'dec'
     *
     * @param isCurrentUserLikeTopic 当前用户是否喜欢该话题（是否已经点赞）
     * @param topicId 话题id
     * @param command 操作指令（INC-自增1，DEC-自减-）
     * @return int 当前话题点赞数
     */
    int alterTopicLikeByInstruction(boolean isCurrentUserLikeTopic, int topicId, String command);

    /**
     * 修改话题分类描述
     *
     * @param categoryNick 话题分类昵称（英文）
     * @param newDescription 新的话题分类描述
     */
    void alterTopicCategoryDescription(String categoryNick, String newDescription);

    /**
     * 是否喜欢话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return boolean 判断结果（true-已喜欢，false-未喜欢）
     */
    boolean isLikeTopic(int userId, int topicId);

    /**
     * 判断用户是否收藏话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return boolean 判断结果（true-已收藏，false-未关注）
     */
    boolean isCollectTopic(int userId, int topicId);

    /**
     * 判断用户是否关注话题
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return boolean 判断结果（true-已关注，false-未关注）
     */
    boolean isAttentionTopic(int userId, int topicId);

    /**
     * 操作喜欢话题
     *      - 操作取反（已喜欢 -> 未喜欢，未喜欢 -> 已喜欢）
     *
     * @param userId　用户id
     * @param topicId 话题id
     * @return List 用户目前喜欢话题id列表
     */
    List<Integer> operateLikeTopic(int userId, int topicId);

    /**
     * 操作收藏话题
     *      - 操作取反（已收藏 -> 未收藏，未收藏 -> 已收藏）
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return List 用户目前收藏话题id列表
     */
    List<Integer> operateCollectTopic(int userId, int topicId);

    /**
     * 操作关注话题
     *      - 操作取反（已关注 -> 未关注， 未关注 -> 已关注）
     *
     * @param userId 用户id
     * @param topicId 话题id
     * @return List 用户目前关注话题id列表
     */
    List<Integer> operateAttentionTopic(int userId, int topicId);
}
