public class DockerProcess {
    private final String lineOfOutput;

    public DockerProcess(String lineOfOutput) {
        this.lineOfOutput = lineOfOutput;
    }

    public String getName() {
        int lastIndexOfSpace = this.lineOfOutput.lastIndexOf(" ");

        return this.lineOfOutput.substring(lastIndexOfSpace).trim();
    }
}
