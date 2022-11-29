package de.fraunhofer.iais.eis.ids.broker;

import ids.messaging.protocol.multipart.parser.MultipartParser;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractBrokerTest {

    String CONNECTOR_ID = "https://test.connector.de/";

    String RESOURCE_ID = "https://w3id.org/idsa/autogen/dataResource/afaafe22-fb63-4532-a1e1-15230cb99444";

    String DESCRIPTION_REQUEST_MESSAGE =
            "{" +
                    "  \"@context\" : {" +
                    "    \"ids\" : \"https://w3id.org/idsa/core/\"," +
                    "    \"idsc\" : \"https://w3id.org/idsa/code/\"" +
                    "  }," +
                    "  \"@type\" : \"ids:DescriptionRequestMessage\"," +
                    "  \"@id\" : \"http://industrialdataspace.org/1a421b8c-3407-44a8-aeb9-253f145c869a\"," +
                    "  \"ids:requestedElement\" : {" +
                    "    \"@id\" : \"https://broker.ids.isst.fraunhofer.de/catalog/-1818765891/-1941089326/1870145456\"" +
                    "  }," +
                    "  \"ids:issued\" : {" +
                    "    \"@value\" : \"2021-05-25T15:35:34.589Z\"," +
                    "    \"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "  }," +
                    "  \"ids:securityToken\" : {" +
                    "    \"@type\" : \"ids:DynamicAttributeToken\"," +
                    "    \"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/2bd53efc-5995-d75590476820\"," +
                    "    \"ids:tokenValue\" : \"null\"," +
                    "    \"ids:tokenFormat\" : {" +
                    "      \"@id\" : \"https://w3id.org/idsa/code/JWT\"" +
                    "    }" +
                    "  }," +
                    "  \"ids:senderAgent\" : {" +
                    "    \"@id\" : \"https://localhost/agent\"" +
                    "  }," +
                    "  \"ids:modelVersion\" : \"4.0.0\"," +
                    "  \"ids:issuerConnector\" : {" +
                    "    \"@id\" : \"https://localhost/59a68243\"" +
                    "  }" +
                    "}";

    String CONNECTOR_REGISTRATION_MESSAGE_HEADER =
            "{" +
                    "  \"@context\" : {" +
                    "    \"ids\" : \"https://w3id.org/idsa/core/\"," +
                    "    \"idsc\" : \"https://w3id.org/idsa/code/\"" +
                    "  }," +
                    "  \"@type\" : \"ids:ConnectorUpdateMessage\"," +
                    "  \"@id\" : \"https://w3id.org/idsa/autogen/connectorUpdateMessage/6d875403-cfea-4aad-979c-3515c2e71967\"," +
                    "  \"ids:securityToken\" : {" +
                    "    \"@type\" : \"ids:DynamicAttributeToken\"," +
                    "    \"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/7bbbd2c1-2d75-4e3d-bd10-c52d0381cab0\"," +
                    "    \"ids:tokenValue\" : \"INVALID\"," +
                    "    \"ids:tokenFormat\" : {" +
                    "      \"@id\" : \"idsc:JWT\"" +
                    "    }" +
                    "  }," +
                    "  \"ids:senderAgent\" : {" +
                    "    \"@id\" : \"http://example.org\"" +
                    "  }," +
                    "  \"ids:modelVersion\" : \"4.0.0\"," +
                    "  \"ids:issuerConnector\" : {" +
                    "    \"@id\" : \"https://test.connector.de/\"" +
                    "  }," +
                    "  \"ids:issued\" : {" +
                    "    \"@value\" : \"2021-06-23T17:27:23.566+02:00\"," +
                    "    \"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "  }," +
                    "  \"ids:affectedConnector\" : {" +
                    "    \"@id\" : \"https://test.connector.de/\"" +
                    "  }" +
                    "}";

    String CONNECTOR_REGISTRATION_MESSAGE_PAYLOAD =
            "{" +
                    "  \"@context\" : {" +
                    "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
                    "	\"idsc\" : \"https://w3id.org/idsa/code/\"" +
                    "  }," +
                    "  \"@type\" : \"ids:BaseConnector\"," +
                    "  \"@id\" : \"https://test.connector.de/\"," +
                    "  \"ids:outboundModelVersion\" : \"3.0.0\"," +
                    "  \"ids:description\" : [ {" +
                    "	\"@value\" : \"This is a dummy description from the Interaction Library\"," +
                    "	\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "  } ]," +
                    "  \"ids:maintainer\" : {" +
                    "	\"@id\" : \"https://example.org/\"" +
                    "  }," +
                    "  \"ids:securityProfile\" : {" +
                    "	\"@id\" : \"idsc:BASE_SECURITY_PROFILE\"" +
                    "  }," +
                    "  \"ids:curator\" : {" +
                    "	\"@id\" : \"https://example.org/\"" +
                    "  }," +
                    "  \"ids:resourceCatalog\" : {" +
                    "	\"@type\" : \"ids:ResourceCatalog\"," +
                    "	\"@id\" : \"https://w3id.org/idsa/autogen/catalog/fa535e30-8cb0-4ede-b5a9-def664d69556\"," +
                    "	\"ids:offer\" : [ {" +
                    "	  \"@type\" : \"ids:Resource\"," +
                    "	  \"@id\" : \"https://w3id.org/idsa/autogen/resource/f559500f-b668-4351-a1b1-50f712a6d368\"," +
                    "	  \"ids:description\" : [ {" +
                    "		\"@value\" : \"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quos blanditiis tenetur \\n       unde suscipit, quam beatae rerum inventore consectetur, neque doloribus, cupiditate numquam  \\n      dignissimos laborum fugiat deleniti? Eum quasi quidem quibusdam.\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  } ]," +
                    "	  \"ids:created\" : {" +
                    "		\"@value\" : \"2003-01-10T00:00Z\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "	  }," +
                    "	  \"ids:contentType\" : {" +
                    "		\"@id\" : \"idsc:INTERFACE_DEFINITION\"" +
                    "	  }," +
                    "	  \"ids:author\" : [ {" +
                    "		\"@type\" : \"ids:Person\"," +
                    "		\"@id\" : \"https://w3id.org/idsa/autogen/person/8b51a76a-7316-4285-b96a-722bf7a944f8\"," +
                    "		\"ids:phoneNumber\" : [ \"012345678891\" ]," +
                    "		\"ids:givenName\" : \"Max\"," +
                    "		\"ids:emailAddress\" : [ \"info@example.org\" ]," +
                    "		\"ids:familyName\" : \"Mustermensch\"," +
                    "		\"ids:homepage\" : \"http://example.org\"" +
                    "	  } ]," +
                    "	  \"ids:dataCreator\" : {" +
                    "		\"@value\" : \"Data creator/Owner\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  }," +
                    "	  \"ids:contentStandard\" : {" +
                    "		\"@id\" : \"http://contentstandard.org\"" +
                    "	  }," +
                    "	  \"ids:rawData\" : false," +
                    "	  \"ids:standardLicense\" : {" +
                    "		\"@id\" : \"idsc:OTHERFREEWARELICENSE\"" +
                    "	  }," +
                    "	  \"ids:mainTitle\" : [ {" +
                    "		\"@value\" : \"Energy Conservation with Open Source Ad Blockers\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  } ]," +
                    "	  \"ids:customLicense\" : {" +
                    "		\"@id\" : \"http://customlicense.org\"" +
                    "	  }," +
                    "	  \"ids:keyword\" : [ {" +
                    "		\"@value\" : \"energy\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  }, {" +
                    "		\"@value\" : \"energy\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  }, {" +
                    "		\"@value\" : \"conservation\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  }, {" +
                    "		\"@value\" : \"ad\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  }, {" +
                    "		\"@value\" : \"open\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  } ]," +
                    "	  \"ids:representation\" : [ {" +
                    "		\"@type\" : \"ids:TextRepresentation\"," +
                    "		\"@id\" : \"https://w3id.org/idsa/autogen/textRepresentation/8a23edb5-be86-4722-afd4-fd2276c9bc18\"," +
                    "		\"ids:created\" : {" +
                    "		  \"@value\" : \"2013-11-14T00:00Z\"," +
                    "		  \"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "		}," +
                    "		\"ids:modified\" : {" +
                    "		  \"@value\" : \"2020-01-15T00:00Z\"," +
                    "		  \"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "		}," +
                    "		\"ids:domainVocabulary\" : {" +
                    "		  \"@type\" : \"ids:VocabularyData\"," +
                    "		  \"@id\" : \"https://w3id.org/idsa/autogen/vocabularyData/bbe50ee4-5033-4c13-bf43-35c733a7b781\"," +
                    "		  \"ids:vocabulary\" : {" +
                    "			\"@id\" : \"http://textVovabulary.com\"" +
                    "		  }" +
                    "		}," +
                    "		\"ids:representationStandard\" : {" +
                    "		  \"@id\" : \"http://textRepresentation.org\"" +
                    "		}," +
                    "		\"ids:mediaType\" : {" +
                    "		  \"@type\" : \"ids:IANAMediaType\"," +
                    "		  \"@id\" : \"idsc:APPLICATION_PDF\"" +
                    "		}," +
                    "		\"ids:instance\" : [ {" +
                    "		  \"@type\" : \"ids:Artifact\"," +
                    "		  \"@id\" : \"https://w3id.org/idsa/autogen/artifact/aa1cf3d9-8aa1-42ef-9f85-08ce0f48005b\"," +
                    "		  \"ids:creationDate\" : {" +
                    "			\"@value\" : \"2020-06-23T17:27:18.599+02:00\"," +
                    "			\"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "		  }," +
                    "		  \"ids:byteSize\" : 2678," +
                    "		  \"ids:fileName\" : \"data.pdf\"" +
                    "		} ]" +
                    "	  } ]," +
                    "	  \"ids:modified\" : {" +
                    "		\"@value\" : \"2020-01-29T00:00Z\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
                    "	  }," +
                    "	  \"ids:title\" : [ {" +
                    "		\"@value\" : \"Energy Conservation with Open Source Ad Blockers\"," +
                    "		\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "	  } ]," +
                    "	  \"ids:version\" : \"version 3.0.0\"," +
                    "	  \"ids:researchDataType\" : {" +
                    "		\"@id\" : \"idsc:OBSERVATIONAL_DATA\"" +
                    "	  }," +
                    "	  \"ids:domainVocabulary\" : {" +
                    "		\"@type\" : \"ids:VocabularyData\"," +
                    "		\"@id\" : \"https://w3id.org/idsa/autogen/vocabularyData/2d98d466-52ce-48b8-a29e-936f7f4efa9e\"," +
                    "		\"ids:vocabulary\" : {" +
                    "		  \"@id\" : \"http://vocabulary.org\"" +
                    "		}" +
                    "	  }," +
                    "	  \"ids:language\" : [ {" +
                    "		\"@id\" : \"idsc:EN\"" +
                    "	  }, {" +
                    "		\"@id\" : \"idsc:ES\"" +
                    "	  } ]" +
                    "	} ]" +
                    "  }," +
                    "  \"ids:mainTitle\" : [ {" +
                    "	\"@value\" : \"dummy\"," +
                    "	\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "  } ]," +
                    "  \"ids:title\" : [ {" +
                    "	\"@value\" : \"This is a dummy title from the Interaction Library\"," +
                    "	\"@type\" : \"http://www.w3.org/2001/XMLSchema#string\"" +
                    "  } ]," +
                    "  \"ids:hasDefaultEndpoint\": {" +
                    "	\"@id\":\"https://test.connector.de/data/\"," +
                    "	\"@type\":\"ids:ConnectorEndpoint\"," +
                    "	\"ids:path\" : \"/infrastructure\"," +
                    "	\"ids:endpointInformation\" : [ {" +
                    "	  \"@value\" : \"This endpoint provides IDS Connector information and IDS Resources.\"," +
                    "	  \"@language\" : \"en\"" +
                    "	} ]," +
                    "	\"ids:endpointDocumentation\" : [ {" +
                    "	  \"@id\" : \"https://app.swaggerhub.com/apis/idsa/IDS-Broker/1.3.1#/Multipart%20Interactions/post_infrastructure\"" +
                    "	} ]," +
                    "	\"ids:accessURL\" : {" +
                    "	  \"@id\" : \"https://exmple.org/infrastructure\"" +
                    "	}" +
                    "  }," +
                    "  \"ids:maintainer\": {\"@id\":\"https://connector.de/agent/\"}," +
                    "  \"ids:curator\": {\"@id\":\"https://connector.de/agent/\"}," +
                    "  \"ids:inboundModelVersion\" : [ \"4.0.0\" ]," +
                    "  \"ids:outboundModelVersion\" : \"4.0.0\"," +
                    "  \"ids:securityProfile\" : {" +
                    "	\"@id\" : \"https://w3id.org/idsa/code/BASE_SECURITY_PROFILE\"" +
                    "  }" +
                    "}";

    String CONNECTOR_QUERY_HEADER =
            "{" +
                    "  \"@context\" : {" +
                    "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
                    "	\"idsc\" : \"https://w3id.org/idsa/code/\"," +
                    "	\"xsd\":\"http://www.w3.org/2001/XMLSchema#\"" +
                    "  }," +
                    "  \"@type\" : \"ids:QueryMessage\"," +
                    "  \"@id\" : \"http://industrialdataspace.org/1a421b8c-3407-44a8-aeb9-253f145c869a\"," +
                    "  \"ids:issued\" : {\"@value\":\"2021-05-25T15:35:34.589Z\",\"@type\":\"xsd:dateTimeStamp\"}," +
                    "  \"ids:modelVersion\" : \"4.0.0\"," +
                    "  \"ids:senderAgent\":{\"@id\":\"https://localhost/agent\"}," +
                    "  \"ids:issuerConnector\":{\"@id\":\"https://localhost/59a68243\"}," +
                    "  \"ids:securityToken\" : {" +
                    "	\"@type\" : \"ids:DynamicAttributeToken\"," +
                    "	\"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/2bd53efc-5995-d75590476820\"," +
                    "	\"ids:tokenFormat\" : {" +
                    "	  \"@id\" : \"https://w3id.org/idsa/code/JWT\"" +
                    "	}," +
                    "	\"ids:tokenValue\" : \"{{dat}}\"" +
                    "  }," +
                    "  \"ids:queryLanguage\" : {" +
                    "	\"@id\" : \"idsc:SPARQL\"" +
                    "  }," +
                    "  \"ids:queryScope\" : {" +
                    "	\"@id\" : \"idsc:ALL\"" +
                    "  }" +
                    "}";

    String CONNECTOR_QUERY_PAYLOAD = "PREFIX ids: <https://w3id.org/idsa/core/>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "SELECT ?connectorid " +
            "WHERE { GRAPH ?g {" +
            "	?connector owl:sameAs ?connectorid ." +
            "}}";

    String RESOURCE_UPDATE_MESSAGE_HEADER = "{" +
            "  \"@context\" : {" +
            "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
            "	\"idsc\" : \"https://w3id.org/idsa/code/\"" +
            "  }," +
            "  \"@type\" : \"ids:ResourceUpdateMessage\"," +
            "  \"@id\" : \"https://w3id.org/idsa/autogen/connectorUpdateMessage/6d875403-cfea-4aad-979c-3515c2e71967\"," +
            "  \"ids:securityToken\" : {" +
            "	\"@type\" : \"ids:DynamicAttributeToken\"," +
            "	\"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/7bbbd2c1-2d75-4e3d-bd10-c52d0381cab0\"," +
            "	\"ids:tokenValue\" : \"INVALID\"," +
            "	\"ids:tokenFormat\" : {" +
            "	  \"@id\" : \"idsc:JWT\"" +
            "	}" +
            "  }," +
            "  \"ids:senderAgent\" : {" +
            "	\"@id\" : \"http://example.org\"" +
            "  }," +
            "  \"ids:modelVersion\" : \"4.0.0\"," +
            "  \"ids:issuerConnector\" : {" +
            "	\"@id\" : \"https://test.connector.de/\"" +
            "  }," +
            "  \"ids:issued\" : {" +
            "	\"@value\" : \"2021-06-23T17:27:23.566+02:00\"," +
            "	\"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
            "  }," +
            "  \"ids:affectedResource\" : {" +
            "	\"@id\": \"https://w3id.org/idsa/autogen/dataResource/afaafe22-fb63-4532-a1e1-15230cb99444\"" +
            "  }" +
            "}";

    String RESOURCE_UPDATE_MESSAGE_PAYLOAD = "{" +
            "  \"@context\" : {" +
            "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
            "	\"idsc\" : \"https://w3id.org/idsa/code/\"" +
            "  }," +
            "	  \"@type\" : \"ids:DataResource\"," +
            "	  \"@id\" : \"https://w3id.org/idsa/autogen/dataResource/afaafe22-fb63-4532-a1e1-15230cb99444\"," +
            "	  \"ids:representation\" : [ {" +
            "		\"@type\" : \"ids:DataRepresentation\"," +
            "		\"@id\" : \"https://w3id.org/idsa/autogen/dataRepresentation/d5b72746-2302-4685-99b8-fc2be06fd23f\"," +
            "		\"ids:instance\" : [ {" +
            "		  \"@type\" : \"ids:Artifact\"," +
            "		  \"@id\" : \"https://broker.ids.isst.fraunhofer.de/connectors/\"" +
            "		} ]" +
            "	  } ]" +
            "}";

    String RESOURCE_QUERY_PAYLOAD = "PREFIX ids: <https://w3id.org/idsa/core/> " +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
            "SELECT ?connectorid ?resource ?resourceid " +
            "WHERE { GRAPH ?g { " +
            "?connector owl:sameAs ?connectorid . " +
            "?connector ids:resourceCatalog ?catalog . " +
            "?catalog ids:offeredResource ?resource . " +
            "?resource owl:sameAs ?resourceid . " +
            "}}";

    String RESOURCE_UNAVAILABLE_MESSAGE_HEADER = "{" +
            "  \"@context\" : {" +
            "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
            "	\"idsc\" : \"https://w3id.org/idsa/code/\"" +
            "  }," +
            "  \"@type\" : \"ids:ResourceUnavailableMessage\"," +
            "  \"@id\" : \"https://w3id.org/idsa/autogen/connectorUpdateMessage/6d875403-cfea-4aad-979c-3515c2e71967\"," +
            "  \"ids:securityToken\" : {" +
            "	\"@type\" : \"ids:DynamicAttributeToken\"," +
            "	\"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/7bbbd2c1-2d75-4e3d-bd10-c52d0381cab0\"," +
            "	\"ids:tokenValue\" : \"INVALID\"," +
            "	\"ids:tokenFormat\" : {" +
            "	  \"@id\" : \"idsc:JWT\"" +
            "	}" +
            "  }," +
            "  \"ids:senderAgent\" : {" +
            "	\"@id\" : \"https://test.connector.de/\"" +
            "  }," +
            "  \"ids:modelVersion\" : \"4.0.0\"," +
            "  \"ids:issuerConnector\" : {" +
            "	\"@id\" : \"https://test.connector.de/\"" +
            "  }," +
            "  \"ids:issued\" : {" +
            "	\"@value\" : \"2021-06-23T17:27:23.566+02:00\"," +
            "	\"@type\" : \"http://www.w3.org/2001/XMLSchema#dateTimeStamp\"" +
            "  }," +
            "  \"ids:affectedResource\" : {" +
            "	\"@id\" : \"https://w3id.org/idsa/autogen/dataResource/afaafe22-fb63-4532-a1e1-15230cb99444\"" +
            "  }" +
            "}";

    String CONNECTOR_UNAVAILABLE = "{" +
            "  \"@context\" : {" +
            "	\"ids\" : \"https://w3id.org/idsa/core/\"," +
            "	\"idsc\" : \"https://w3id.org/idsa/code/\"" +
            "  }," +
            "  \"@type\" : \"ConnectorUnavailableMessage\"," +
            "  \"@id\" : \"https://w3id.org/idsa/autogen/cfea-4aad-979c-3515c2e71967\"," +
            "  \"ids:affectedConnector\" : {" +
            "	\"@id\":\"https://test.connector.de/\"," +
            "	\"@type\":\"ids:BaseConnector\"" +
            "  }," +
            "  \"ids:securityToken\" : {" +
            "	\"@type\" : \"ids:DynamicAttributeToken\"," +
            "	\"@id\" : \"https://w3id.org/idsa/autogen/dynamicAttributeToken/7bbbd2c1-2d75-4e3d-bd10-c52d0381cab0\"," +
            "	\"ids:tokenValue\" : \"INVALID\"," +
            "	\"ids:tokenFormat\" : {" +
            "	  \"@id\" : \"idsc:JWT\"" +
            "	}" +
            "  }," +
            "  \"ids:senderAgent\" : {" +
            "	\"@id\" : \"https://test.connector.de/\"" +
            "  }," +
            "  \"ids:modelVersion\" : \"4.0.0\"," +
            "  \"ids:issuerConnector\" : {" +
            "	\"@id\" : \"https://test.connector.de/\"" +
            "  }," +
            "  \"ids:issued\" : {" +
            "	\"@value\" : \"2021-06-05T17:27:23.566+02:00\"," +
            "	\"@type\" : \"http://www.w3.org/2001/XMLSchema%23dateTimeStamp\"" +
            "  }" +
            "}";

    String getPart(String response, String part) {
        assertNotNull(response, "Response is null");
        var parts = assertDoesNotThrow(
                () -> MultipartParser.stringToMultipart(response),
                "Response cannot be parsed."
        );
        assertTrue(parts.containsKey(part), "Response does not contain a part named " + part);
        return parts.get(part);
    }
}
