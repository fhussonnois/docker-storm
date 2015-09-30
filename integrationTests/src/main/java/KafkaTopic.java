public class KafkaTopic {
    private final String ipAddress;
    private final Docker docker;

    public KafkaTopic(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void create(String topicName) {
        docker.readStandardOutput("docker run --name=\"AddTopic\"" +
                " --rm -w //opt/kafka_2.11-0.8.2.1/bin" +
                " --entrypoint //bin/bash" +
                " spotify/kafka" +
                " ./kafka-topics.sh --create --zookeeper " +
                ipAddress +
                ":2181 --replication-factor 1 --partitions 1 --topic "+
                topicName)
                .stream()
                .forEach(System.out::println);

        docker.readStandardOutput("docker run --name=\"DescribeTopicsAfter\"" +
                " --rm -w //opt/kafka_2.11-0.8.2.1/bin" +
                " --entrypoint //bin/bash" +
                " spotify/kafka" +
                " ./kafka-topics.sh --describe --zookeeper " +
                ipAddress +
                ":2181")
                .stream()
                .forEach(System.out::println);
    }
}
