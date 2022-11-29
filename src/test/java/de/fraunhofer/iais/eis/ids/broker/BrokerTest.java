package de.fraunhofer.iais.eis.ids.broker;

import de.fraunhofer.iais.eis.DescriptionResponseMessage;
import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.MessageProcessedNotificationMessage;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrokerTest extends AbstractBrokerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    Serializer serializer;

    @Test
    @Order(1)
    void selfDescription() throws Exception {
        var header = new MockPart("header", DESCRIPTION_REQUEST_MESSAGE.getBytes(StandardCharsets.UTF_8));

        var response = mvc.perform(multipart("/api/ids/data")
                        .part(header)
                        .contentType("multipart/form-data")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var responseHeader = getPart(response, "header");
        var message = serializer.deserialize(responseHeader, Message.class);
        assertTrue(message instanceof DescriptionResponseMessage);
    }

    @Disabled //reason this test only works when fuseki is empty after running the tests its not repeatable
    @Test
    @Order(2)
    void noConnectorRegistered() throws Exception {
        try {
            String response = sendBrokerRequest(CONNECTOR_QUERY_HEADER, CONNECTOR_QUERY_PAYLOAD);
            var result = getPart(response, "payload");
            assertEquals(1, result.split("\n").length);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("The index is empty"));
        }
    }

    @Test
    @Order(3)
    void registerConnector() throws Exception {
        String response = sendBrokerRequest(CONNECTOR_REGISTRATION_MESSAGE_HEADER, CONNECTOR_REGISTRATION_MESSAGE_PAYLOAD);

        var responseHeader = getPart(response, "header");
        var message = serializer.deserialize(responseHeader, Message.class);
        assertTrue(message instanceof MessageProcessedNotificationMessage);
    }

    @Test
    @Order(4)
    void connectorRegistered() throws Exception {
        String response = sendBrokerRequest(CONNECTOR_QUERY_HEADER, CONNECTOR_QUERY_PAYLOAD);

        var result = getPart(response, "payload");

        assertTrue(
                Arrays.stream(result.split("\n"))
                .anyMatch(s -> s.equals("<" + CONNECTOR_ID + ">"))
        );
    }

    @Test
    @Order(5)
    void resourceUpdate() throws Exception {
        String response = sendBrokerRequest(RESOURCE_UPDATE_MESSAGE_HEADER, RESOURCE_UPDATE_MESSAGE_PAYLOAD);
        var responseHeader = getPart(response, "header");
        var message = serializer.deserialize(responseHeader, Message.class);
        assertTrue(message instanceof MessageProcessedNotificationMessage);
    }

    @Test
    @Order(6)
    void isResourceAvailable() throws Exception {
        String response = sendBrokerRequest(CONNECTOR_QUERY_HEADER, RESOURCE_QUERY_PAYLOAD);
        System.out.println(RESOURCE_QUERY_PAYLOAD);
        System.out.println(response);
        var result = getPart(response, "payload");
        assertTrue(
                Arrays.stream(result.split("\n"))
                        .anyMatch(s -> s.contains("<" + CONNECTOR_ID + ">") && s.contains("<" + RESOURCE_ID + ">"))
        );
    }

    @Test
    @Order(7)
    void resourceUnavailable() throws Exception {
        String response = sendBrokerRequest(RESOURCE_UNAVAILABLE_MESSAGE_HEADER, "");
        var responseHeader = getPart(response, "header");
        var message = serializer.deserialize(responseHeader, Message.class);
        assertTrue(message instanceof MessageProcessedNotificationMessage);
    }

    @Test
    @Order(8)
    void connectorUnavailable() throws Exception {
        String response = sendBrokerRequest(CONNECTOR_UNAVAILABLE, "");
        var responseHeader = getPart(response, "header");
        var message = serializer.deserialize(responseHeader, Message.class);
        assertTrue(message instanceof MessageProcessedNotificationMessage);
    }

    private String sendBrokerRequest(String CONNECTOR_QUERY_HEADER, String CONNECTOR_QUERY_PAYLOAD) throws Exception {
        var header = new MockPart("header", CONNECTOR_QUERY_HEADER.getBytes(StandardCharsets.UTF_8));
        var payload = new MockPart("payload", CONNECTOR_QUERY_PAYLOAD.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(multipart("/api/ids/data")
                        .part(header, payload)
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    // TODO send empty payload to register connector and expect rejected message
}
