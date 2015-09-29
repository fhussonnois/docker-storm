import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Docker {

    private final String machineName;

    public Docker(String machineName) {
        this.machineName = machineName;
    }

    public String[] readStandardOutput(String commandsWithArguments) {
        final String[] strings = readStandardOutput(commandsWithArguments.split(" "));

        System.out.println(commandsWithArguments);
        System.out.println(Arrays.asList(strings).stream().collect(Collectors.joining("\n")));
        System.out.println("*************");

        return strings;
    }

    public String getMachineName() {
        return machineName;
    }

    private String[] readStandardOutput(String[] commands) {
        final Process process = this.getProcess(commands);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> output = new ArrayList<>();

        String s;
        while ((s = getLine(stdInput)) != null) {
            output.add(s);
        }

        return output.toArray(new String[output.size()]);
    }

    private Process getProcess(String... commands) {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Map<String, String> env = pb.environment();
        env.put("DOCKER_TLS_VERIFY", "1");
        env.put("DOCKER_HOST", "tcp://192.168.99.100:2376");
        env.put("DOCKER_CERT_PATH", "C:\\Users\\mweliczko\\.docker\\machine\\machines\\default");
        env.put("DOCKER_MACHINE_NAME", machineName);

        return startProcess(pb);
    }

    private void failIfError(Process proc) {
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        List<String> errors = new ArrayList<>();
        String s;
        while ((s = getLine(stdError)) != null) {
            errors.add(s);
        }
        if(errors.size() > 0){
            throw new RuntimeException(errors.stream().collect(Collectors.joining("\n")));
        }
    }

    private Process startProcess(ProcessBuilder pb)  {
        try {
            final Process start = pb.start();
            this.failIfError(start);
            return start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
