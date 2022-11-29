package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.Connector;
import de.fraunhofer.iais.eis.DescriptionRequestMessage;
import de.fraunhofer.iais.eis.DescriptionRequestMessageImpl;
import de.fraunhofer.iais.eis.DescriptionResponseMessageBuilder;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import ids.messaging.handler.message.SupportedMessageType;
import ids.messaging.response.BodyResponse;
import ids.messaging.response.MessageResponse;
import ids.messaging.util.IdsMessageUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
@Component
@SupportedMessageType(DescriptionRequestMessageImpl.class)
public class DescriptionRequestMessageHandler extends AbstractMessageHandler<DescriptionRequestMessage> {

    @Override
    public MessageResponse handleMessage(DescriptionRequestMessage queryHeader, String payload) {
        var connector = configContainer.getConnector();
        var connectorId = connector.getId();

        var message = new DescriptionResponseMessageBuilder()
                ._securityToken_(getDat())
                ._correlationMessage_(queryHeader.getId())
                ._issued_(IdsMessageUtils.getGregorianNow())
                ._issuerConnector_(connectorId)
                ._modelVersion_(configContainer.getConnector().getOutboundModelVersion())
                ._senderAgent_(connectorId)
                ._recipientConnector_(List.of(queryHeader.getIssuerConnector()))
                .build();

        return BodyResponse.create(message, serialize(connector));
    }

    private String serialize(Connector connector) {
        try {
            return new Serializer().serialize(connector);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
