package club.hoy.weixin.events;

import club.hoy.base.exception.CodedException;
import club.hoy.base.exception.ErrorCode;
import club.hoy.base.exception.Exceptions;
import club.hoy.weixin.EVENT_TYPE;
import club.hoy.weixin.model.EventMessage;

public abstract interface EventHandler {
    public abstract EVENT_TYPE[] eventTypes();

    public abstract String handle(EventMessage paramEventMessage, EVENT_TYPE paramEVENT_TYPE) throws CodedException;

    public static class ErrorCodes {
        private static final String MODULE_CODE = "weixin.event.EventHandler";
        public static final ErrorCode CAN_NOT_FOUND_MATHED_EVENT_HANDLER = Exceptions
            .errorCode("weixin.event.EventHandler", "CAN_NOT_FOUND_MATHED_EVENT_HANDLER");
    }
}