package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.ConnectorUnavailableMessage;
import de.fraunhofer.iais.eis.ConnectorUnavailableMessageImpl;
import de.fraunhofer.iais.eis.ids.broker.core.common.impl.SelfDescriptionPersistenceAdapter;
import ids.messaging.handler.message.SupportedMessageType;
import ids.messaging.response.MessageResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
@Component
@SupportedMessageType(ConnectorUnavailableMessageImpl.class)
public class ConnectorUnavailableMessageHandler extends AbstractMessageHandler<ConnectorUnavailableMessage> {

    @Autowired
    SelfDescriptionPersistenceAdapter connectorIndexing;

    @Override
    @SneakyThrows
    public MessageResponse handleMessage(ConnectorUnavailableMessage message, String payload) {
        connectorIndexing.unavailable(message.getIssuerConnector()); // TODO shouldn't this be the affectedConnector?
        return messageProcessed(message);
    }
}