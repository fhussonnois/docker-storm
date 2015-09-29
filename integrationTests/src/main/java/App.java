public class App {
    public static void main(String[] args) {
        final String machineName = args[0];

        final ProcessWrapper processWrapper = new ProcessWrapper();
        final DockerMachine dockerMachine = new DockerMachine(machineName, processWrapper);
        final String ipAddress = dockerMachine.getIpAddress();
        final Docker docker = new Docker(dockerMachine);

        new DockerEnvironment(docker, new DockerProcessList(docker))
                .clear();

        final String dockerStormContainerName = "mweliczko/docker-storm";
        new BuildStorm(docker).start(dockerStormContainerName);

        new Zookeeper(ipAddress, docker).start();
        new Nimbus(ipAddress, docker).start(dockerStormContainerName);
    }
}
