package de.fraunhofer.iais.eis.ids.broker.messagehandler;

import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.QueryMessage;
import de.fraunhofer.iais.eis.QueryMessageImpl;
import de.fraunhofer.iais.eis.ResultMessageBuilder;
import de.fraunhofer.iais.eis.ids.connector.commons.broker.QueryResultsProvider;
import ids.messaging.handler.message.SupportedMessageType;
import ids.messaging.response.BodyResponse;
import ids.messaging.response.MessageResponse;
import ids.messaging.util.IdsMessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sebastian Lorenz (sebastian.lorenz(at)ivi.fraunhofer.de
 */
@Component
@SupportedMessageType(QueryMessageImpl.class)
public class QueryHandler extends AbstractMessageHandler<QueryMessage> {

    @Autowired
    @Qualifier("ConnectorIndexer")
    QueryResultsProvider queryResultsProvider;

    @Override
    @SneakyThrows
    public MessageResponse handleMessage(QueryMessage message, String payload) {
        var result = queryResultsProvider.getResults(payload);
        return queryResultMessage(message, result);
    }

    MessageResponse queryResultMessage(Message requestMessage, String result) {
        var connector = configContainer.getConnector();
        var connectorId = connector.getId();

        var response = new ResultMessageBuilder()
                ._correlationMessage_(requestMessage.getId())
                ._securityToken_(getDat())
                ._issued_(IdsMessageUtils.getGregorianNow())
                ._issuerConnector_(connectorId)
                ._modelVersion_(connector.getOutboundModelVersion())
                ._senderAgent_(connectorId)
                ._recipientConnector_(List.of(requestMessage.getIssuerConnector()))
                .build();
        return BodyResponse.create(response, result);
    }
}
