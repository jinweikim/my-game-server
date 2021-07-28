package com.mygame.game.message.im;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
import com.mygame.game.message.im.SendIMMsgRequest.SendIMMsgBody;

@GameMessageMetadata(messageId = 311, messageType = EnumMesasageType.REQUEST, serviceId = 101)
public class SendIMMsgRequest  extends AbstractJsonGameMessage<SendIMMsgBody>{
    
    public static class SendIMMsgBody {
        private String chat;
        
        public String getChat() {
            return chat;
        }

        public void setChat(String chat) {
            this.chat = chat;
        }
    }

    @Override
    protected Class<SendIMMsgBody> getBodyObjClass() {
        return SendIMMsgBody.class;
    }
}
