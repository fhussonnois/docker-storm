public class DockerMachine {
    private final Docker docker;

    public DockerMachine(Docker docker) {
        this.docker = docker;
    }

    public String getIpAddress() {
        return new ProcessWrapper().readStandardOutput("docker-machine ip "+ docker.getMachineName())[0];
    }
}
