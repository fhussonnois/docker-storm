public class StormSubmission {
    private final String ipAddress;
    private final Docker docker;

    public StormSubmission(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void start() {
        docker.readStandardOutput("docker run --name=\"StormSubmission\"" +
                " --env ZK_ENV_ADVERTISED_HOST=" +
                ipAddress +
                " mweliczko/mike-storm" +
                " -c nimbus.host=" +
                ipAddress +
                " -c nimbus.thrift.port=6627" +
                " -c storm.zookeeper.servers.0=" +
                ipAddress)
                .stream()
                .forEach(System.out::println);
    }
}
