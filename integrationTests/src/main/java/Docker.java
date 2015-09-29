import java.util.Map;

public class Docker extends ProcessWrapper {

    private final String machineName;

    public Docker(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineName() {
        return machineName;
    }

    @Override
    protected Process getProcess(String... commands){
        ProcessBuilder pb = new ProcessBuilder(commands);
        Map<String, String> env = pb.environment();
        env.put("DOCKER_TLS_VERIFY", "1");
        env.put("DOCKER_HOST", "tcp://192.168.99.100:2376");
        env.put("DOCKER_CERT_PATH", "C:\\Users\\mweliczko\\.docker\\machine\\machines\\default");
        env.put("DOCKER_MACHINE_NAME", machineName);

        return startProcess(pb);
    }
}
