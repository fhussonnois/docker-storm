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
    protected Process getProcess(String... commands) {
        ProcessBuilder pb = new ProcessBuilder(commands);
        setEnvironment(pb.environment());
        return startProcess(pb);
    }

    private void setEnvironment(Map<String, String> env) {
        new ProcessWrapper()
                .readStandardOutput("docker-machine env --shell=cmd " + this.machineName)
                .stream()
                .forEach(outputLine -> set(env, outputLine));
    }

    private void set(Map<String, String> env, String outputLine) {
        final String[] keyValuePair = outputLine.replace("set ", "").split("=");
        if (hasKeyValuePair(keyValuePair)) {
            env.put(keyValuePair[0], keyValuePair[1]);
        }
    }

    private boolean hasKeyValuePair(String[] keyValuePair) {
        return keyValuePair.length > 1;
    }
}
