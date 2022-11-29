package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.ResourceUnavailableMessage;
import de.fraunhofer.iais.eis.ResourceUnavailableMessageImpl;
import de.fraunhofer.iais.eis.ids.broker.core.common.impl.ResourcePersistenceAdapter;
import ids.messaging.handler.message.SupportedMessageType;
import ids.messaging.response.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
@Component
@SupportedMessageType(ResourceUnavailableMessageImpl.class)
public class ResourceUnavailableMessageHandler extends AbstractMessageHandler<ResourceUnavailableMessage> {

    @Autowired
    ResourcePersistenceAdapter resourcePersistenceAdapter;

    @Override
    @SneakyThrows
    public MessageResponse handleMessage(ResourceUnavailableMessage message, String payload) {
        resourcePersistenceAdapter.unavailable(message.getAffectedResource(), message.getIssuerConnector());
        return messageProcessed(message);
    }
}