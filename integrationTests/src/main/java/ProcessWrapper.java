import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessWrapper{
    public List<String> readStandardOutput(String commandsWithArguments) {
        System.out.println(commandsWithArguments);

        final List<String> result = readStandardOutput(commandsWithArguments.split(" "));

//        System.out.println(result.stream().collect(Collectors.joining("\n")));
        System.out.println("*************");

        return result;
    }

    private List<String> readStandardOutput(String[] commands) {
        final Process process = this.getProcess(commands);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> output = new ArrayList<>();

        String s;
        while ((s = getLine(stdInput)) != null) {
            output.add(s);
        }

        return output;
    }

    protected Process getProcess(String... commands) {
        return startProcess(new ProcessBuilder(commands));
    }

    private void failIfError(Process proc) {
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        List<String> errors = new ArrayList<>();

        if(isReady(stdError)) {
            String s;
            while ((s = getLine(stdError)) != null) {
                errors.add(s);
            }
            if (errors.size() > 0) {
                throw new RuntimeException(errors.stream().collect(Collectors.joining("\n")));
            }
        }
    }

    private boolean isReady(BufferedReader stdError){
        try {
            return stdError.ready();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
