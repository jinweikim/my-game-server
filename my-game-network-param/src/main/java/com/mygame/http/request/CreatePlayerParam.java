package com.mygame.http.request;

import com.mygame.error.GameCenterError;
import com.mygame.http.request.AbstractHttpRequestParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class CreatePlayerParam extends AbstractHttpRequestParam {
    private String zoneId; // 游戏分区的 id
    private String nickName;

    @Override
    protected void haveError() {
        if (StringUtils.isEmpty(zoneId)) {
            this.error = GameCenterError.ZONE_ID_IS_EMPTY;
        } else if (StringUtils.isEmpty(nickName)) {
            this.error = GameCenterError.NICKNAME_IS_EMPTY;
        } else {
            int len = nickName.length();
            if (len < 2 || len > 10) {
                this.error = GameCenterError.NICKNAME_LEN_ERROR;
            }
        }
    }
}
