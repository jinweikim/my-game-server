package com.mygame.http.request;

import com.mygame.error.GameCenterError;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import com.mygame.common.utils.CommonField;

@Data
public class LoginParam extends AbstractHttpRequestParam{
    private String openId;
    private String token;
    private String ip;

    @Override
    protected void haveError() {
        if (StringUtils.isEmpty(openId)) {
            this.error = GameCenterError.OPENID_IS_EMPTY;
        } else if (openId.length() > CommonField.OPEN_ID_LENGTH) {
            this.error = GameCenterError.OPENID_LEN_ERROR;
        } else if (StringUtils.isEmpty(token)) {
            this.error = GameCenterError.SDK_TOKEN_ERROR;
        } else if (token.length() > 128) {
            this.error = GameCenterError.SDK_TOKEN_LEN_ERROR;
        }
    }
}
