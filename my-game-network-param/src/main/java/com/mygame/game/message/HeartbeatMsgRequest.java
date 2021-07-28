package com.mygame.game.message;

import com.mygame.game.common.AbstractJsonGameMessage;
import com.mygame.game.common.EnumMesasageType;
import com.mygame.game.common.GameMessageMetadata;
@GameMessageMetadata(messageId=2,messageType=EnumMesasageType.REQUEST,serviceId=1)
public class HeartbeatMsgRequest extends AbstractJsonGameMessage<Void>{

    @Override
    protected Class<Void> getBodyObjClass() {
        return null;
    }

}
