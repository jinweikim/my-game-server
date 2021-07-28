package com.mygame.game.message;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
import com.mygame.game.message.ConfirmMesgRequest.ConfirmBody;
@GameMessageMetadata(messageId=1,messageType= EnumMesasageType.REQUEST,serviceId=1)
public class ConfirmMesgRequest extends AbstractJsonGameMessage<ConfirmBody> {
    
    public static class ConfirmBody {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Override
    protected Class<ConfirmBody> getBodyObjClass() {
        return ConfirmBody.class;
    }

}
