package unosoft;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group {
    private final String filename;
    private final String resultFilename;
    private final SortByLines fileSortingByLines = new SortByLines();

    public Group(String filename, String resultFilename) {
        this.filename = filename;
        this.resultFilename = resultFilename;
    }

    // number of groups which more of one element
    public String numOfGroupMoreThanOne() {
        int countOfGroup = 0;

        try {
            List<List<String>> linesList = fileSortingByLines
                    .findLineGroups(
                            Files.lines(Paths.get(filename)).toList()
                    );

            for (List<String> strings : linesList) {

                int numOfLines = 0;
                for (String ignored : strings) {
                    numOfLines++;
                }
                if (numOfLines > 1) {
                    countOfGroup++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "number of groups which have more of one element: " + countOfGroup;
    }

    // method for writing groups
    public void groupWriter() throws IOException {
        List<List<String>> linesList = fileSortingByLines.findLineGroups(Files.lines(Paths.get(filename)).toList());
        HashMap<Integer, List<List<List<String>>>> map = new HashMap<>();

        for (List<String> stringList : linesList) {
            int numOfLines = 0;
            for (String ignored : stringList) {
                numOfLines++;
            }
            if (!map.containsKey(numOfLines)) {
                List<List<String>> tmp2 = new ArrayList<>();
                tmp2.add(stringList);

                List<List<List<String>>> tmp3 = new ArrayList<>();
                tmp3.add(tmp2);

                map.put(numOfLines, tmp3);

            } else {
                List<List<String>> tmp2 = new ArrayList<>();
                tmp2.add(stringList);
                map.get(numOfLines).add(tmp2);

            }
        }

        try (FileWriter fileWriter = new FileWriter(resultFilename)) {

            int groupNumber = 1;

            fileWriter.write(numOfGroupMoreThanOne());
            fileWriter.append('\n').append('\n');

            for (int i = map.keySet().size(); i > 0; i--) {
                if(map.get(i) == null) continue;
                for (List<List<String>> listOfListLines : map.get(i)) {
                    fileWriter.write("Group " + groupNumber);
                    fileWriter.append('\n');
                    for (List<String> listOfLines : listOfListLines) {
                        for (String line: listOfLines) {
                            fileWriter.write(line);
                            fileWriter.append('\n');
                        }
                    }
                    groupNumber++;
                    fileWriter.append('\n');
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
