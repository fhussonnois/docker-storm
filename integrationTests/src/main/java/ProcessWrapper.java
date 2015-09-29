import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessWrapper{
    public List<String> readStandardOutput(String commandsWithArguments) {
        final String[] strings = readStandardOutput(commandsWithArguments.split(" "));

        System.out.println(commandsWithArguments);
        final List<String> result = Arrays.asList(strings);
        System.out.println(result.stream().collect(Collectors.joining("\n")));
        System.out.println("*************");

        return result;
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

    protected Process getProcess(String... commands) {
        return startProcess(new ProcessBuilder(commands));
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

    protected Process startProcess(ProcessBuilder pb)  {
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
