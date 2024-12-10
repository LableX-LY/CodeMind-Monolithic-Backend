package com.xly.codemind.utils;

import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.common.IdWorker;
import com.xly.codemind.exception.BusinessException;

/**
 * @author X-LY。
 * @version 1.0
 * @createtime 2024/12/10 20:44
 * @description 使用IdWorker生成分布式ID工具类
 **/

public class IdWorkerUtil {

    public static long generateId() {
        long generatedId = IdWorker.getInstance().nextId();
        if (generatedId < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统内部异常,ID生成失败!");
        }
        return generatedId;
    }

}
