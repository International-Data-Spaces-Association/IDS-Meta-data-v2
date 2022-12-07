# IDS-meta-data-v2
this project needs <u>**IDS-Messaging**</u> infomodel-503 package and a part of the original metadata-broker-core see **Build-Me** 

##Infos
- standard mapping added in ids-messaging - ids.messaging.endpoint.EndpointService constructor use this class to add own or remove them
- message based communication registered by annotations see de.fraunhofer.iais.eis.ids.broker.messagehandler
  - handler gets picked in ids.messaging.handler.request.RequestMessageHandlerService | resolveHandler


## Build-me
the current messaging package runs on an old version of ids-infomodel so we need to package a version that implements that model

1. Build install IDS-MESSAGING (for infomodel 503)
   1. clone the <u>[Repository](https://github.com/International-Data-Spaces-Association/IDS-Messaging-Services.git) </u>
   2. checkout the branch "infomodel-artifacts-503"
   3. run <code>mvn clean install</code>
      1. for revision to be sure check the pom property ids.broker.version
2. Build install current version of the original metadata-broker-core
   1. this is only necessary since index-common is not available in a Maven Repository (as of **2nd dec 22**) </br> might be further developed and may has changes which are not compatible any more (working with <u>**5.0.3**</u>)
   2. clone the <u>[Repository](https://github.com/Mobility-Data-Space/MDS-Broker-Core.git) </u>
   3. run <code>mvn -U clean install -Drevision=5.0.3</code> 
      1. for revision to be sure check the pom property ids.broker.version
3. Package Metadata-Broker-Core
   1. run <code>mvn package</code>
   2. dockerize
      1. in docker/metadata-broker-core run <code>docker build . -t [your docker name]</code>