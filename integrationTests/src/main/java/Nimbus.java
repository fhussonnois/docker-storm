public class Nimbus {
    private final String ipAddress;
    private final Docker docker;

    public Nimbus(String ipAddress, Docker docker) {
        this.ipAddress = ipAddress;
        this.docker = docker;
    }

    public void start(String dockerStormContainerName) {
        docker.readStandardOutput(("docker run --name=\"nimbus\" -h nimbus " +
                "-p 6627:6627 -p 8000:8000 -p 8080:8080 -p 3772:3772 -p 3773:3773 " +
                "--expose 6627 --expose 3772 --expose 3773 --expose 8000 --expose 8080  " +
//                "--memory=268435456 " +
                "--env NIMBUS_ADDR=" + this.ipAddress +
                " " +
                "--env ZOOKEEPER_ADDR=" + this.ipAddress +
                " " +
                "-d " + dockerStormContainerName +
                " --daemon nimbus ui logviewer " +
//                "--storm.options nimbus.childopts:\\\"-Xmx1024m\\\" " +
//                "ui.childopts:\\\"-Xmx768m\\\" " +
//                "logviewer.childopts:\\\"-Xmx128m\\\"" +
                ""));
    }
}
