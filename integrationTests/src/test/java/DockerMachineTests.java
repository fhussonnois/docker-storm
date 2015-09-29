import org.junit.Test;

import static java.util.regex.Pattern.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DockerMachineTests {

    @Test
    public void ipAddressMatchesExpectedRegEx() throws Exception {
        final String ipAddress = new DockerMachine().getIpAddressOf("default");

        final boolean isAnIpAddress = matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", ipAddress);

        assertThat(isAnIpAddress, is(true));
    }
}
