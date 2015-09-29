public class DockerMachine {
    private final Docker docker;

    public DockerMachine(Docker docker) {
        this.docker = docker;
    }

    public String getIpAddressOf(String machineName) {
        return docker.readStandardOutput("docker-machine ip "+ machineName)[0];
    }
}
