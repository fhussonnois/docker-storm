public class StormTopology {
    private final String ipAddress;
    private final Docker docker;

    public StormTopology(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void list() {
        docker.readStandardOutput("docker run --rm" +
                " --entrypoint storm" +
                " mweliczko/mike-storm" +
                " list -c nimbus.host=" +
                ipAddress)
                .stream()
                .forEach(System.out::println);
    }
}
