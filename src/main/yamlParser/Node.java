package yamlParser;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node parent;
    private String key;
    private String value;
    private List<Node> children;

    public Node (Node parent, String key, String value) {
        this.key = key;
        this.value = value;
        this.parent = parent;
        this.children = new ArrayList<Node>();
    }

    public Node addChild(Node childNode) {
        childNode.setParent(this);
        this.children.add(childNode);
        return childNode;
    }

    public Node getParent() {
        return parent;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Node getChildNodeByKey(String key) {
        Node result = null;
        for(Node node : children) {
            if(node.getKey().equals(key)) {
                result = node;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Path not found!");
        }
        return result;
    }

    public String searchForPath(String path) {
        if(path.substring(0,1).equals(".")) {
            path = path.substring(1);
        }
        String[] segments;
        if(path.contains(".")) {
            segments = path.split("\\.");
        } else {
            segments = new String[1];
            segments[0] = path;
        }
        Node parent = this;
        for (String actualSegment : segments) {
            parent = parent.getChildNodeByKey(actualSegment);
        }
        return parent.getValue();
    }
}