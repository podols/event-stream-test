# 프로젝트 설명
1. checkout
- 이커머스에서 상품 주문 및 결제에 대한 도메인 간소화
- 상품 주문 및 결제에 대한 정보를 DB에 저장하고, 카프카로 이벤트를 발행 
- 8080 port

2. shipment
- checkout 도메인에서 발생한 이벤트를 소비하여 배송에 대한 도메인 간소화
- 배송에 대한 정보를 DB에 저장
- 8090 port

3. kafkastreams
- checkout 도메인에서 발행한 이벤트를 실시간으로 집계하여 새로운 토픽으로 집계결과를 발행하는 모듈

4. kafka connect
- 집계결과를 소비하여 es 에 저장
- elastic search 9200 port
- kibana 5601 port
- kafka connect (to elastic search) 8083 port
- 커넥터 상태조회 api: http://localhost:8083/connectors?expand=status&expand=info


# 실행 환경
1. m1 mac
2. open jdk 1.8 x86 arch
3. kafka 2.13-2.8.2
4. elastic search 7.17.8
5. kibana 7.17.8
6. confluentinc-kafka-connect-elasticsearch-14.0.6 
7. apache jmeter 5.5


# 사용 방법
1. zookeeper 실행
- ./bin/zookeeper-server-start.sh config/zookeeper.properties

2. kafka cluster 설정 
- config/server.properties
```
broker.id=0
listeners=PLAINTEXT://localhost:9092
log.dirs=/tmp/kafka-logs
```

- config/server1.properties
```
broker.id=1
listeners=PLAINTEXT://localhost:9093
log.dirs=/tmp/kafka-logs1
```

- config/server2.properties
```
broker.id=2
listeners=PLAINTEXT://localhost:9094
log.dirs=/tmp/kafka-logs2
```

3. kafka 실행
- ./bin/kafka-server-start.sh ./config/server.properties
- ./bin/kafka-server-start.sh ./config/server1.properties
- ./bin/kafka-server-start.sh ./config/server2.properties

4. topic 생성
- ./bin/kafka-topics.sh —create —topic checkout.complete.v1  —bootstrap-server localhost:9092 —paritions 3 —replication-factor 2
- ./bin/kafka-topics.sh --create --topic checkout.productId.aggregated.v1 --bootstrap-server localhost:9092 --partitions 3 --replication-factor 2

5. elastic search, kibana 실행
- bin/elasticsearch
- bin/kibana

6. kafkaToEsConnector 실행
- kafkaToEsConnector 에 있는 properties 파일들을 kafka-root/config/kafka-connect 로 이동
- ./bin/connect-standalone.sh ./config/kafka-connect/connect-standalone.properties ./config/kafka-connect/checkout-complete.properties ./config/kafka-connect/checkout-aggregate.properties

7. 도메인 서비스 실행
- checkout, shipment, kafkastreams 소스코드를 jar 로 빌드하여 배포
- java -jar $domain.jar

8. jmeter 폴더에 있는 HTTPRequest.jmx 를 불러와서 테스트 실행

9. 기타
- 토픽 모니터링 
`kafka-root/bin/kafka-console-consumer.sh --topic checkout.complete.v1 --bootstrap-server localhost:9092 --property print.key=true --property key.separator="-"`

`kafka-root/bin/kafka-console-consumer.sh --topic checkout.productId.aggregated.v1 --bootstrap-server localhost:9092 --property print.key=true --property key.separator=“-“`

- kafka connector 정보
`http://localhost:8083/connectors?expand=status&expand=info`

- checkout form url
`http://localhost:8080/checkOutForm`

- h2 ui
`http://localhost:8080/h2-console/`
`http://localhost:8090/h2-console/`

- kibana
`http://localhost:5601` 
