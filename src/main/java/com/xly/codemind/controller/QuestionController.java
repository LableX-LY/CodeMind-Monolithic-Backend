package com.xly.codemind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.xly.codemind.common.BaseResponse;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.exception.BusinessException;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.model.bean.User;
import com.xly.codemind.model.dto.question.*;
import com.xly.codemind.model.vo.AdminQuestionVO;
import com.xly.codemind.model.vo.UserQuestionVO;
import com.xly.codemind.service.QuestionService;
import com.xly.codemind.service.QuestionSubmitService;
import com.xly.codemind.service.UserService;
import com.xly.codemind.utils.ActionResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author X-LY。
 * @version 1.0
 * @datetime 2024/12/4 19:48
 * @description 题目Controller
 **/

@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    //格式化JSON字符串
    private final static Gson GSON = new Gson();

    @PostMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request",required = true,value = "HttpServletRequest请求")
    })
    @ApiOperation(value = "添加题目接口",notes = "添加题目接口")
    public BaseResponse<Long> addQuestion(@RequestBody AddQuestionRequest addQuestionRequest, HttpServletRequest request) {
        if (addQuestionRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"参数为空!");
        }
        String questionTitle = addQuestionRequest.getQuestionTitle();
        String questionContent = addQuestionRequest.getQuestionContent();
        List<String> questionTags = addQuestionRequest.getQuestionTags();
        if (questionTags == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目标签为空!");
        }
        List<JudgeCase> judgeCase = addQuestionRequest.getJudgeCase();
        if (judgeCase == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"判题用例为空!");
        }
        JudgeConfig judgeConfig = addQuestionRequest.getJudgeConfig();
        if (judgeConfig == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"判题配置为空!");
        }
        String questionTagsJsonString = GSON.toJson(questionTags);
        String judgeCaseObjectJsonString = GSON.toJson(judgeCase);
        String judgeConfigObjectJsonString = GSON.toJson(judgeConfig);
        String questionAnswer = addQuestionRequest.getQuestionAnswer();
        int questionDifficulty = addQuestionRequest.getQuestionDifficulty();
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        Long questionId = questionService.addQuestion(questionTitle, questionContent, questionAnswer, questionTagsJsonString, judgeCaseObjectJsonString, judgeConfigObjectJsonString, questionDifficulty, request);
        return ActionResultUtil.success(questionId);
    }

    @PostMapping("/delete")
    @ApiImplicitParam(name = "questionId",required = true,value = "题目id")
    @ApiOperation(value = "删除题目接口",notes = "删除题目接口")
    public BaseResponse<Boolean> deleteQuestion(long questionId) {
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目id为空!");
        }
        return ActionResultUtil.success(questionService.deleteQuestion(questionId));
    }

    @PostMapping("/edit")
    @ApiOperation(value = "修改题目信息",notes = "修改题目信息")
    public BaseResponse<Boolean> editQuestion(@RequestBody EditQuestionRequest editQuestionRequest, HttpServletRequest request) {
        if (editQuestionRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"参数为空!");
        }
        long questionId = editQuestionRequest.getQuestionId();
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目id有误!");
        }
        String questionTitle = editQuestionRequest.getQuestionTitle();
        String questionContent = editQuestionRequest.getQuestionContent();
        List<String> questionTags = editQuestionRequest.getQuestionTags();
        List<JudgeCase> judgeCase = editQuestionRequest.getJudgeCase();
        JudgeConfig judgeConfig = editQuestionRequest.getJudgeConfig();
        if (questionTags == null || judgeCase == null || judgeConfig == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        String questionTagsJsonString = GSON.toJson(questionTags);
        String judgeCaseObjectJsonString = GSON.toJson(judgeCase);
        String judgeConfigObjectJsonString = GSON.toJson(judgeConfig);
        String questionAnswer = editQuestionRequest.getQuestionAnswer();
        Integer questionStatus = editQuestionRequest.getQuestionStatus();
        int questionDifficulty = editQuestionRequest.getQuestionDifficulty();
        if (StringUtils.isAnyBlank(questionTitle,questionContent,questionAnswer,questionTagsJsonString,judgeCaseObjectJsonString,judgeConfigObjectJsonString)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"题目信息设置有误!");
        }
        if (questionDifficulty < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目难度设置错误!");
        }
        if (questionStatus == null || questionStatus < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目状态设置有误!");
        }
        Boolean result = questionService.editQuestion(questionId, questionTitle, questionContent, questionAnswer, questionTagsJsonString, judgeCaseObjectJsonString, judgeConfigObjectJsonString, questionDifficulty, questionStatus, request);
        return ActionResultUtil.success(result);
    }

    //普通用户分页查询题目
    @PostMapping("/user/list/page")
    @ApiOperation(value = "普通用户分页查询题目",notes = "普通用户分页查询题目")
    public BaseResponse<Page<UserQuestionVO>> getUserQuestionVOByPage(@RequestBody UserQueryQuestionRequest userQueryQuestionRequest) {
        if (userQueryQuestionRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数错误!");
        }
        long current = userQueryQuestionRequest.getCurrent();
        long size = userQueryQuestionRequest.getPageSize();
        Page<Question> userQuestionPageQueryWrapper = questionService.page(new Page<>(current, size),
                questionService.getUserQueryWrapper(userQueryQuestionRequest));
        Page<UserQuestionVO> questionVOByPage = questionService.getUserQuestionVOByPage(userQuestionPageQueryWrapper);
        return ActionResultUtil.success(questionVOByPage);
    }

    //管理员分页查询题目
    @PostMapping("/admin/list/page")
    @ApiOperation(value = "管理员分页查询题目",notes = "管理员分页查询题目")
    public BaseResponse<Page<AdminQuestionVO>> getAdminQuestionByPage(@RequestBody AdminQueryQuestionRequest adminQueryQuestionRequest) {
        if (adminQueryQuestionRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数错误!");
        }
        long current = adminQueryQuestionRequest.getCurrent();
        long size = adminQueryQuestionRequest.getPageSize();
        Page<Question> adminQuestionPageQueryWrapper = questionService.page(new Page<>(current, size),
                questionService.getAdminQueryWrapper(adminQueryQuestionRequest));
        Page<AdminQuestionVO> adminQuestionVOByPage = questionService.getAdminQuestionVOByPage(adminQuestionPageQueryWrapper);
        return ActionResultUtil.success(adminQuestionVOByPage);
    }

    @PostMapping("/submit")
    @ApiOperation(value = "用户提交代码,发起判题请求", notes = "用户提交代码,发起判题请求")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数错误!");
        }
        //只有在登录的前提下才可以答题
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录,请先登录!");
        }
        Long questionId = questionSubmitAddRequest.getId();
        String questionCode = questionSubmitAddRequest.getQuestionCode();
        String questionLanguage = questionSubmitAddRequest.getQuestionLanguage();
        if (ObjectUtils.isEmpty(questionId) || questionId < 0 ||StringUtils.isAnyBlank(questionCode,questionLanguage)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"请求参数错误!");
        }
        Long questionSubmitedId = questionSubmitService.doQuestionSubmit(questionId,questionCode,questionLanguage,loginUser);
        return ActionResultUtil.success(questionSubmitedId);
    }

}
