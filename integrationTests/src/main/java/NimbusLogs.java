import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class NimbusLogs {
    public static final String SEARCH_STRING = "Starting Nimbus with conf ";
    private final Docker docker;

    public NimbusLogs(Docker docker) {
        this.docker = docker;
    }

    public List<ConfigurationSetting> toList() {
        return getConfigurationSettings(convertToValidJson());
    }

    private List<ConfigurationSetting> getConfigurationSettings(String validJson) {
        return new Gson().fromJson(validJson, JsonObject.class).entrySet()
                .stream()
                .map(stringJsonElementEntry -> new ConfigurationSetting(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue().toString()))
                .collect(Collectors.toList());
    }

    private String convertToValidJson() {
        return getLineWithConfiguration(0)
                .replace(SEARCH_STRING, "")
                .replace(", ", ",")
                .replace(" ", ":")
                .replace("6700:6701:6702:6703", "6700,6701,6702,6703");
    }

    private String getLineWithConfiguration(int attemptNumber) {
        final String entireLogFileContents = getEntireLogFileContents(attemptNumber);
        final int indexOfSearchString = entireLogFileContents.indexOf(SEARCH_STRING);
        if(isConfigurationPresentInLogs(indexOfSearchString)) {
            final int endOfLine = entireLogFileContents.indexOf("\n", indexOfSearchString);
            return entireLogFileContents.substring(indexOfSearchString, endOfLine);
        }
        return getLineWithConfiguration(++attemptNumber);
    }

    private boolean isConfigurationPresentInLogs(int indexOfSearchString) {
        return indexOfSearchString >= 0;
    }

    private String getEntireLogFileContents(int attemptNumber) {
        if(attemptNumber > 100){
            throw new RuntimeException("tried " + attemptNumber + " times to get the logs");
        }
        final String logContents = docker.readStandardOutput("docker exec nimbus cat //home/storm/log/nimbus.log")
                .stream()
                .collect(Collectors.joining("\n"));
        if(logContents.isEmpty()){
            verifyDaemonsAreRunning();
            waitForNimbusToStart(2);
            return getEntireLogFileContents(++attemptNumber);
        }
        return logContents;
    }

    private void verifyDaemonsAreRunning() {
        final String logContents = docker.readStandardOutput("docker logs nimbus")
                .stream()
                .collect(Collectors.joining("\n"));
        System.out.println(logContents);

        if(logContents.contains("exit status 1; not expected")){
            throw new RuntimeException("the app is broken");
        }
    }

    private void waitForNimbusToStart(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ConfigurationSetting {

        private String key;
        private String value;

        public ConfigurationSetting(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "{" +
                    "'" + key + '\'' +
                    "=" + value +
                    '}';
        }
    }
}
