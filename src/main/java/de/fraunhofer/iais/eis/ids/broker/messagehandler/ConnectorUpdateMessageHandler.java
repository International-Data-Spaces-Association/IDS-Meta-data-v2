package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.ConnectorUpdateMessage;
import de.fraunhofer.iais.eis.ConnectorUpdateMessageImpl;
import de.fraunhofer.iais.eis.InfrastructureComponent;
import de.fraunhofer.iais.eis.ids.broker.core.common.impl.SelfDescriptionPersistenceAdapter;
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
@SupportedMessageType(ConnectorUpdateMessageImpl.class)
public class ConnectorUpdateMessageHandler extends AbstractMessageHandler<ConnectorUpdateMessage> {

    @Autowired
    SelfDescriptionPersistenceAdapter connectorIndexing;



    @Autowired
    Serializer serializer;

    @Override
    @SneakyThrows
    public MessageResponse handleMessage(ConnectorUpdateMessage requestMessage, String messagePayload) {
        var connector = serializer.deserialize(messagePayload, InfrastructureComponent.class);
        connectorIndexing.updated(connector);
        return messageProcessed(requestMessage);
    }
}