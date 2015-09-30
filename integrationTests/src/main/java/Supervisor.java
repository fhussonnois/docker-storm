public class Supervisor {
    private final String ipAddress;
    private final Docker docker;

    public Supervisor(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void start(String dockerStormContainerName) {
        docker.readStandardOutput("docker run --name=\"supervisor1\" -h supervisor1" +
                " --env NIMBUS_ADDR=" +
                ipAddress +
                " --env ZOOKEEPER_ADDR=" +
                ipAddress +
                " -d " +
                dockerStormContainerName+
                " " +
                "--daemon supervisor")
                .stream()
                .forEach(System.out::println);
    }
}
