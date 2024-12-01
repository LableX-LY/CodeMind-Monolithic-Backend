package com.xly.codemind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xly.codemind.mapper.QuestionMapper;
import com.xly.codemind.model.bean.Question;
import com.xly.codemind.service.QuestionService;
import org.springframework.stereotype.Service;

/**
* @author x-ly
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2024-12-01 16:36:16
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




