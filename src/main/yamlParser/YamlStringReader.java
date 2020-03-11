package yamlParser;

import java.util.Scanner;

public class YamlStringReader implements YamlReader {
    private final String YAML = "yaml\n" +
            "version: 3.3\n" +
            "\n" +
            "services:\n" +
            "  db:\n" +
            "    image: mysql:5.7\n" +
            "    volumes:\n" +
            "      0: db_data:/var/lib/mysql\n" +
            "    restart: always\n" +
            "    environment:\n" +
            "      MYSQL_ROOT_PASSWORD: somewordpress\n" +
            "      MYSQL_DATABASE: wordpress\n" +
            "      MYSQL_USER: wordpress\n" +
            "      MYSQL_PASSWORD: wordpress\n" +
            "\n" +
            "  wordpress:\n" +
            "    depends_on:\n" +
            "      0: db\n" +
            "    image: wordpress:latest\n" +
            "    ports:\n" +
            "      0: 8000:80\n" +
            "    restart: always\n" +
            "    environment:\n" +
            "      WORDPRESS_DB_HOST: db:3306\n" +
            "      WORDPRESS_DB_USER: wordpress\n" +
            "      WORDPRESS_DB_PASSWORD: wordpress\n" +
            "      WORDPRESS_DB_NAME: wordpress\n" +
            "volumes:\n" +
            "  db_data:";

    private Scanner scanner;
    @Override
    public String nextline() {
        String result = null;
        if (scanner.hasNextLine()) {
            result = scanner.nextLine();
        } else {
            scanner.close();
        }
        return result;
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public YamlStringReader() {
        scanner = new Scanner(YAML);
    }

}
