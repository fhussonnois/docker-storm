import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class AppTests{

    public static final String MACHINE_NAME = "default";

    @BeforeClass
    public static void setUp() throws Exception {
        App.main(new String[]{MACHINE_NAME});
    }

    @Test
    public void zookeeperWasStarted() throws Exception {
        assertThat(new DockerProcessList(new Docker(MACHINE_NAME)).getProcessNames(), hasItem("zookeeper"));
    }
}

