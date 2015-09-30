public class App {
    public static void main(String[] args) {
        final String machineName = args[0];

        final DockerMachine dockerMachine = new DockerMachine(machineName, new ProcessWrapper());
        final String ipAddress = dockerMachine.getIpAddress();
        final Docker docker = new Docker(dockerMachine);

        new DockerEnvironment(docker, new DockerProcessList(docker))
                .clear();

        final String dockerStormContainerName = "mweliczko/docker-storm";
//        new BuildStorm(docker).start(dockerStormContainerName);

        new Zookeeper(ipAddress, docker).start();
        new Nimbus(ipAddress, docker).start(dockerStormContainerName);

        new NimbusMonitor(docker).waitToStart();

        new Supervisor(ipAddress, docker).start(dockerStormContainerName);

        new StormSubmission(ipAddress, docker).start();

        new StormTopology(ipAddress, docker).list();
    }
}
