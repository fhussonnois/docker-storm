import org.junit.Test;

import static java.util.regex.Pattern.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DockerMachineTests {

    public static final String MACHINE_NAME = "default";

    @Test
    public void ipAddressMatchesExpectedRegEx() throws Exception {
        final String ipAddress = new DockerMachine(MACHINE_NAME, new ProcessWrapper()).getIpAddress();

        final boolean isAnIpAddress = matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", ipAddress);

        assertThat(isAnIpAddress, is(true));
    }
}

