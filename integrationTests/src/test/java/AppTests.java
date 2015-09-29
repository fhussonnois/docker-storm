import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class AppTests{

    public static final String MACHINE_NAME = "default";
    private static List<String> processNames;
    private static Docker docker;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("^^^^^^^^^^^^^^^^^^^");
        App.main(new String[]{MACHINE_NAME});
        docker = new Docker(MACHINE_NAME);
        processNames = new DockerProcessList(docker).getProcessNames();
    }

    @Test
    public void zookeeperWasStarted() throws Exception {
        assertThat(processNames, hasItem("zookeeper"));
    }

    @Test
    public void nimbusWasStarted() throws Exception {
        assertThat(processNames, hasItem("nimbus"));
    }

    public static class NimbusLogsTests{

        private static List<NimbusLogs.ConfigurationSetting> configurationSettings;

        @BeforeClass
        public static void setUp() throws Exception {
            System.out.println("###############");
            configurationSettings = new NimbusLogs(docker).toList();
        }

        @Test
        public void nimbusChildOptsIsSet() throws Exception {
            assertThat(getValue("nimbus.childopts"), is("\"-Xmx1024m\""));
        }

        @Test
        public void uiChildOptsIsSet() throws Exception {
            assertThat(getValue("ui.childopts"), is("\"-Xmx768m\""));
        }

        @Test
        public void logviewerChildOptsIsSet() throws Exception {
            assertThat(getValue("logviewer.childopts"), is("\"-Xmx128m\""));
        }

        private String getValue(String setting) {
            return configurationSettings
                    .stream()
                    .filter(configurationSetting -> configurationSetting.getKey().equals(setting))
                    .findFirst()
                    .get()
                    .getValue();
        }
    }
}

