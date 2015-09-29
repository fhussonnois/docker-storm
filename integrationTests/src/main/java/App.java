public class App {
    public static void main(String[] args) {
        final String machineName = args[0];

        final DockerMachine dockerMachine = new DockerMachine(machineName, new ProcessWrapper());
        final String ipAddress = dockerMachine.getIpAddress();
        final Docker docker = new Docker(machineName, dockerMachine);

        new DockerEnvironment(docker, new DockerProcessList(docker))
                .clear();

        new Zookeeper(ipAddress, docker).start();
        new Nimbus(ipAddress, docker).start();

        new NimbusLogs(docker).toList()
        .stream()
        .forEach(System.out::println);
    }

}
