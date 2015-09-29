import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DockerProcessTests{
    @Test
    public void parsesAHeaderLine() throws Exception {
        final String s1 = "CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS" +
                "              PORTS                                                                                                      NAMES";
        assertThat(new DockerProcess(s1).getName(), is("NAMES"));
    }

    @Test
    public void parsesARealLine() throws Exception {
        final String s2 = "9f12264cbffb        fhuz/docker-storm   \\\"/bin/bash /home/stor\\\"   About an hour ago   Up About an hour" +
                "    0.0.0.0:3772-3773->3772-3773/tcp, 0.0.0.0:6627->6627/tcp, 0.0.0.0:8000->8000/tcp, 0.0.0.0:8080->8080/tcp   nimbus";
        assertThat(new DockerProcess(s2).getName(), is("nimbus"));
    }
}
