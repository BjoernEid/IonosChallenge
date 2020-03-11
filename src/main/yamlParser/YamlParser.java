package yamlParser;

public class YamlParser {
    private Node root;
    private Integer actualIndentation;
    private Node actualParent;
    private Node actualChild;
    private YamlReader yamlReader;

    private void readYaml() throws IllegalYamlSyntaxException {
        String line;
        while (yamlReader.hasNextLine()) {
            line = yamlReader.nextline();
            if (line.equals("yaml")) continue;
            if (line.isEmpty() || line.isBlank()) continue;
            processLine(line);
        }
    }

    private void processLine(String line) throws IllegalYamlSyntaxException {
        Integer lineIndentation = countWhiteSpaces(line);
        if (lineIndentation % 2 != 0) {
            throw new IllegalYamlSyntaxException("Yaml indentation syntax error. Please use 2 spaces for indentation. Failing line: " + line);
        }
        String key = getKeyFromYamlLine(line);
        String value = getValueFromYamlLine(line);
        if (isNotIndented(lineIndentation)) {
            actualChild = actualParent.addChild(new Node(actualParent, key, value));
        } else {
            if (isIndentedRight(lineIndentation)) {
                actualParent = actualChild;
                actualIndentation = lineIndentation;
                actualChild = actualChild.addChild(new Node(actualChild, key, value));
            } else {
                if (isIndentedLeft(lineIndentation)) {
                    Integer indentation = lineIndentation;
                    do {
                        actualParent = actualParent.getParent();
                        indentation = indentation + 2;
                    } while (isIndentedLeft(indentation));
                    actualChild = actualParent.addChild(new Node(actualParent, key, value));
                    actualIndentation = lineIndentation;
                }
            }
        }
    }

    private boolean isNotIndented(Integer lineIndentation) {
        return actualIndentation.equals(lineIndentation);
    }

    private boolean isIndentedLeft(Integer lineIndentation) {
        return lineIndentation < actualIndentation;
    }

    private boolean isIndentedRight(Integer lineIndentation) {
        return lineIndentation > actualIndentation;
    }

    private Integer calculateActualColonIndex(String line) throws IllegalYamlSyntaxException {
        Integer result = line.indexOf(':');
        if (result == -1) {
            throw new IllegalYamlSyntaxException("Missing double colon. Please use key value pairs seperated by a double colon. Failing line: " + line);
        }
        return result;
    }

    private String getKeyFromYamlLine(String line) throws IllegalYamlSyntaxException {
        Integer actualColonIndex = calculateActualColonIndex(line);
        return line.substring(countWhiteSpaces(line), actualColonIndex);
    }

    private String getValueFromYamlLine(String line) throws IllegalYamlSyntaxException {
        Integer actualColonIndex = calculateActualColonIndex(line);
        String result = null;
        if (line.length() > actualColonIndex + 2) {
            result = line.substring(actualColonIndex + 2);
        }
        return result;
    }

    private Integer countWhiteSpaces(String line) {
        Integer whiteSpaceCount = 0;
        for (char actualCharacter : line.toCharArray()) {
            if (actualCharacter == ' ') {
                whiteSpaceCount++;
            } else {
                break;
            }
        }
        return whiteSpaceCount;
    }

    private void parse(String path) throws IllegalYamlSyntaxException {
        readYaml();
        if (!path.isEmpty()) {
            System.out.println("Value for path (" + path + ") : " + root.searchForPath(path));
        } else throw new IllegalArgumentException("Please provide a path as argument!");
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                new YamlParser().parse(args[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("Path not found!");
            } catch (IllegalYamlSyntaxException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Please provide the search path as parameter!");
        }
    }

    public YamlParser() {
        root = new Node(null, "root", "");
        actualIndentation = 0;
        actualParent = root;
        actualChild = null;
        yamlReader = new YamlStringReader();
    }
}
