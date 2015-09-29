import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class AppTests{

    public static final String MACHINE_NAME = "default";
    private static List<String> processNames;

    @BeforeClass
    public static void setUp() throws Exception {
        App.main(new String[]{MACHINE_NAME});
        processNames = new DockerProcessList(new Docker(MACHINE_NAME)).getProcessNames();
    }

    @Test
    public void zookeeperWasStarted() throws Exception {
        assertThat(processNames, hasItem("zookeeper"));
    }

    @Test
    public void nimbusWasStarted() throws Exception {
        assertThat(processNames, hasItem("nimbus"));
    }
}

