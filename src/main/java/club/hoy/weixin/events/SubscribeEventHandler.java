package club.hoy.weixin.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import club.hoy.base.exception.CodedException;
import club.hoy.base.exception.Exceptions;
import club.hoy.weixin.EVENT_TYPE;
import club.hoy.weixin.model.EventMessage;

/**
 * 关注事件
 * @author ouyanghan
 * @version $Id: V1.0 2017年3月30日 下午1:49:55 Exp $
 */
@Service
public class SubscribeEventHandler implements EventHandler {

    @Value("${weixin.welcome_subscribe}")
    private String WELCOME_SUBSCRIBE;
    
    /** 
     * @see club.hoy.weixin.events.EventHandler#eventTypes()
     */
    @Override
    public EVENT_TYPE[] eventTypes() {
        return new EVENT_TYPE[] { EVENT_TYPE.subscribe };
    }

    /** 
     * @see club.hoy.weixin.events.EventHandler#handle(club.hoy.weixin.model.EventMessage, club.hoy.weixin.EVENT_TYPE)
     */
    @Override
    public String handle(EventMessage paramEventMessage, EVENT_TYPE paramEVENT_TYPE) throws CodedException {
        if (EVENT_TYPE.subscribe == paramEVENT_TYPE) {
            return this.WELCOME_SUBSCRIBE;
        }

        throw Exceptions.except(EventHandler.ErrorCodes.CAN_NOT_FOUND_MATHED_EVENT_HANDLER);
    }

}
