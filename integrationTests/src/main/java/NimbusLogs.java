import java.util.List;
import java.util.stream.Collectors;

public class NimbusLogs {
    private final Docker docker;

    public NimbusLogs(Docker docker) {
        this.docker = docker;
    }

    public List<ConfigurationSetting> toList() {
        System.out.println("trying to get stuff");
        final String lineWithConfiguration = getLineWithConfiguration();

//        final int indexOfOpenBrace = lineWithConfiguration.indexOf("Starting Nimbus with conf ");
//        final String substring = lineWithConfiguration.substring(indexOfOpenBrace);

        throw new RuntimeException(lineWithConfiguration);
    }

    private String getLineWithConfiguration() {
        final List<String> standardOutput = docker.readStandardOutput("docker exec nimbus cat //home/storm/log/nimbus.log");

        System.out.println("got stuff");

        return standardOutput.stream().collect(Collectors.joining("\n"));
    }

    public static class ConfigurationSetting {

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
