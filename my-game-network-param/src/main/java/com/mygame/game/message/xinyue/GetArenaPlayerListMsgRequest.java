package com.mygame.game.message.xinyue;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
import com.mygame.game.message.xinyue.GetArenaPlayerListMsgRequest.RequestBody;

@GameMessageMetadata(messageId = 203, messageType = EnumMesasageType.REQUEST, serviceId = 101)
public class GetArenaPlayerListMsgRequest extends AbstractJsonGameMessage<RequestBody> {
    public static class RequestBody {
        
    }
   

    @Override
    protected Class<RequestBody> getBodyObjClass() {
        return null;
    }
}
