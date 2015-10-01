import java.util.List;
import java.util.stream.Collectors;

public class DockerProcessList {

    private final Docker docker;

    public DockerProcessList(Docker docker) {
        this.docker = docker;
    }

    public List<String> getProcessNames(final String arguments) {
        return docker.readStandardOutput("docker ps" + arguments)
                .stream()
                .map(x -> new DockerProcess(x).getName())
                .skip(1)
                .collect(Collectors.toList());
    }
}
