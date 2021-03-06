package com.mygame.game.message.im;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
import com.mygame.game.message.im.SendIMMsgeResponse.IMMsgBody;

@GameMessageMetadata(messageId = 311, messageType = EnumMesasageType.RESPONSE, serviceId = 101)
public class SendIMMsgeResponse extends AbstractJsonGameMessage<IMMsgBody>{
    public static class IMMsgBody {
        private String chat;
        private String sender;//消息发送者，这里测试，使用昵称，也可以添加一些其它的信息，比如头像，等级等。
        
        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getChat() {
            return chat;
        }

        public void setChat(String chat) {
            this.chat = chat;
        }
    }
    @Override
    protected Class<IMMsgBody> getBodyObjClass() {
        return IMMsgBody.class;
    }

}
