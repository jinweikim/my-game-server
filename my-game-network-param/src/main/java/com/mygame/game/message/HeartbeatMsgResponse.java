package com.mygame.game.message;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
import com.mygame.game.message.HeartbeatMsgResponse.ResponseBody;
@GameMessageMetadata(messageId=2,messageType=EnumMesasageType.RESPONSE,serviceId=1)
public class HeartbeatMsgResponse extends AbstractJsonGameMessage<ResponseBody>{

    public static class ResponseBody{
        private long serverTime;

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }
        
    }

    @Override
    protected Class<ResponseBody> getBodyObjClass() {
        return ResponseBody.class;
    }
}
