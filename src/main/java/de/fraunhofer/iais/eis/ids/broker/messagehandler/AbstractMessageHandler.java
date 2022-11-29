package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.DynamicAttributeToken;
import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.MessageProcessedNotificationMessageBuilder;
import ids.messaging.core.config.ConfigContainer;
import ids.messaging.core.daps.ConnectorMissingCertExtensionException;
import ids.messaging.core.daps.DapsConnectionException;
import ids.messaging.core.daps.DapsEmptyResponseException;
import ids.messaging.core.daps.DapsTokenProvider;
import ids.messaging.handler.message.MessageHandler;
import ids.messaging.handler.message.MessagePayload;
import ids.messaging.response.BodyResponse;
import ids.messaging.response.MessageResponse;
import ids.messaging.util.IdsMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
/**
 * MessageHandler with implementation for standard massage handler tasks.
 *
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
public abstract class AbstractMessageHandler<T extends Message> implements MessageHandler<T> {

    @Autowired
    DapsTokenProvider dapsTokenProvider;

    @Autowired
    ConfigContainer configContainer;

    @Override
    public MessageResponse handleMessage(T queryHeader, MessagePayload payload) {
        log.debug("Handle message: {}", queryHeader);
        var payloadText = readPayload(payload);
        return handleMessage(queryHeader, payloadText);
    }

    String readPayload(MessagePayload payload) {
        if (payload == null || payload.getUnderlyingInputStream() == null)
            return null;

        try {
            return new String(payload.getUnderlyingInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    MessageResponse messageProcessed(Message message) {
        var connector = configContainer.getConnector();
        var connectorId = connector.getId();

        var response = new MessageProcessedNotificationMessageBuilder()
                ._correlationMessage_(message.getId())
                ._securityToken_(getDat())
                ._issued_(IdsMessageUtils.getGregorianNow())
                ._issuerConnector_(connectorId)
                ._modelVersion_(connector.getOutboundModelVersion())
                ._senderAgent_(connectorId)
                ._recipientConnector_(List.of(message.getIssuerConnector()))
                .build();
        return BodyResponse.create(response, "");
    }

    DynamicAttributeToken getDat() {
        try {
            return dapsTokenProvider.getDAT();
        } catch (DapsConnectionException | DapsEmptyResponseException | ConnectorMissingCertExtensionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Message handling for specific messages.
     *
     * @param message the header part of the incoming IDS Message.
     * @param payload the payload part of the incoming IDS Message.
     * @return the response.
     */
    public abstract MessageResponse handleMessage(T message, String payload);
}
