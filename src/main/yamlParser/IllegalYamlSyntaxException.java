package yamlParser;

public class IllegalYamlSyntaxException extends Exception {
    public IllegalYamlSyntaxException(String errorMessage) {
        super(errorMessage);
    }
}
