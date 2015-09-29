public class App {
    public static void main(String[] args) {
        final String machineName = args[0];
        final Docker docker = new Docker(machineName);
        
        final String ipAddress = new DockerMachine(docker).getIpAddress();

        new DockerEnvironment(docker, new DockerProcessList(docker))
                .clear();

        new Zookeeper(ipAddress, docker).start();
        new Nimbus(ipAddress, docker).start();

        new NimbusLogs(docker).toList()
        .stream()
        .forEach(System.out::println);
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
