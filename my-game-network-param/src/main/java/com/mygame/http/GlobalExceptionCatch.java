package com.mygame.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mygame.common.error.GameErrorException;
import com.mygame.common.error.IServerError;
import com.mygame.error.GameCenterError;
import com.mygame.http.response.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionCatch {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionCatch.class);

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<JSONObject> exceptionHandler(Throwable ex) {
        IServerError error = null;
        if (ex instanceof GameErrorException) {
            GameErrorException gameError = (GameErrorException) ex;
            error = gameError.getError();
            logger.error("服务器异常", ex);
        } else {
            error = GameCenterError.UNKNOW;
            logger.error("服务器异常", ex);
        }
        JSONObject data = new JSONObject(); // 统一给客户端返回结果
        data.put("errorMsg", ex.getMessage());
        ResponseEntity<JSONObject> response = new ResponseEntity<>(error);
        response.setData(data);
        return response;
    }


}
