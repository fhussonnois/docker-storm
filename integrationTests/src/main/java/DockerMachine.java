import java.util.List;

public class DockerMachine {
    private final String machineName;
    private final ProcessWrapper processWrapper;

    public DockerMachine(String machineName, ProcessWrapper processWrapper) {
        this.machineName = machineName;
        this.processWrapper = processWrapper;
    }

    public String getIpAddress() {
        return processWrapper.readStandardOutput("docker-machine ip " + machineName).get(0);
    }

    public List<String> getEnvironmentSettings() {
        return processWrapper.readStandardOutput("docker-machine env --shell=cmd " + this.machineName);
    }
}
