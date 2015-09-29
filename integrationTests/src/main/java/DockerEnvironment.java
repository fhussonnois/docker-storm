public class DockerEnvironment {
    private final Docker docker;
    private final DockerProcessList dockerProcessList;

    public DockerEnvironment(Docker docker, DockerProcessList dockerProcessList) {
        this.docker = docker;
        this.dockerProcessList = dockerProcessList;
    }

    public void clear() {
        dockerProcessList.getProcessNames()
                .stream()
                .forEach(processName -> {
                    System.out.println("stopping and removing " + processName);
                    docker.readStandardOutput("docker stop " + processName);
                    docker.readStandardOutput("docker rm "+ processName);
                });
    }
}
