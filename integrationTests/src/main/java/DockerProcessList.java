import java.util.List;
import java.util.stream.Collectors;

public class DockerProcessList {

    private final Docker docker;

    public DockerProcessList(Docker docker) {
        this.docker = docker;
    }

    public List<String> getProcessNames() {
        return docker.readStandardOutput("docker ps -a")
                .stream()
                .map(x -> new DockerProcess(x).getName())
                .skip(1)
                .collect(Collectors.toList());
    }
}
