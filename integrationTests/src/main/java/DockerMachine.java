import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerMachine {
    public String getIpAddressOf(String machineName) {
        return readLine(getStandardOutput(createProcess(new String[]{"docker-machine", "ip", machineName})));
    }

    private BufferedReader getStandardOutput(Process proc) {
        return new BufferedReader(new InputStreamReader(proc.getInputStream()));
    }

    private String readLine(BufferedReader reader)  {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Process createProcess(String[] commands)  {
        try {
            return Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
