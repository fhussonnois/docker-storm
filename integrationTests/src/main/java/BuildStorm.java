public class BuildStorm {
    private final Docker docker;

    public BuildStorm(Docker docker) {
        this.docker = docker;
    }

    public void start(final String dockerStormContainerName) {
        docker.readStandardOutput("docker rmi " + dockerStormContainerName)
                .stream()
                .forEach(System.out::println);

        docker.readStandardOutput("docker build -t " + dockerStormContainerName + " C:\\_git\\openSource\\docker-storm")
                .stream()
                .forEach(System.out::println);

        docker.readStandardOutput("docker push " + dockerStormContainerName)
                .stream()
                .forEach(System.out::println);
    }
}
