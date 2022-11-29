package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.Resource;
import de.fraunhofer.iais.eis.ResourceUpdateMessage;
import de.fraunhofer.iais.eis.ResourceUpdateMessageImpl;
import de.fraunhofer.iais.eis.ids.broker.core.common.impl.ResourcePersistenceAdapter;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import ids.messaging.handler.message.SupportedMessageType;
import ids.messaging.response.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
@Component
@SupportedMessageType(ResourceUpdateMessageImpl.class)
public class ResourceUpdateMessageHandler extends AbstractMessageHandler<ResourceUpdateMessage> {

    @Autowired
    ResourcePersistenceAdapter resourcePersistenceAdapter;

    @Autowired
    Serializer serializer;

    @Override
    @SneakyThrows
    public MessageResponse handleMessage(ResourceUpdateMessage message, String payload) {
        Resource resource = serializer.deserialize(payload, Resource.class);
        resourcePersistenceAdapter.updated(resource, message.getIssuerConnector());
        return messageProcessed(message);
    }

}