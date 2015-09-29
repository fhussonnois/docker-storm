public class Zookeeper {
    private final String ipAddress;
    private final Docker docker;

    public Zookeeper(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void start() {
        docker.readStandardOutput(("docker run --name=\"zookeeper\" -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=" +
                this.ipAddress +
                " --env ADVERTISED_PORT=9092 --env NUM_PARTITIONS=1 -d spotify/kafka"));
    }
}
