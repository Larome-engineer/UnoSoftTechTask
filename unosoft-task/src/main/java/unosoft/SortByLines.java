package unosoft;

import java.util.*;

public class SortByLines {

    public List<List<String>> findLineGroups(List<String> lines) {

        class NewLineElement {
            private final String lineElement;
            private final int columnNum;

            private NewLineElement(String lineElement, int columnNum) {
                this.lineElement = lineElement;
                this.columnNum = columnNum;
            }
        }

        if (lines == null)
            return Collections.emptyList();

        List<List<String>> linesGroups = new ArrayList<>(); //список групп, каждый элемент вида "номер группы - список строк группы"
        if (lines.size() < 2) {
            linesGroups.add(lines);
            return linesGroups;
        }

        List<Map<String, Integer>> columns = new ArrayList<>(); // список стобцов, каждый столбец - мапа с парами "элемент строки/столбца-номер группы"
        Map<Integer, Integer> unitedGroups = new HashMap<>(); //мэп с парами "номер некоторой группы - номер группы, с которой надо объединить данную"

        for (String line : lines) {
            String[] lineElements = line.split(";");
            TreeSet<Integer> groupsWithSameElems = new TreeSet<>(); //список групп, имеющих совпадающие элементы
            List<NewLineElement> newElements = new ArrayList<>(); //список элементов, которых нет в мапах столбцов

            for (int elmIndex = 0; elmIndex < lineElements.length; elmIndex++) {
                String currLnElem = lineElements[elmIndex];
                if (columns.size() == elmIndex)
                    columns.add(new HashMap<>());
                if (currLnElem.replaceAll("\"", "").trim().isEmpty())
                    continue;

                Map<String, Integer> currCol = columns.get(elmIndex);
                Integer elemGrNum = currCol.get(currLnElem);
                if (elemGrNum != null) {
                    while (unitedGroups.containsKey(elemGrNum)) // если группа с таким номером объединена с другой,
                        elemGrNum = unitedGroups.get(elemGrNum); //то сохраняем номер группы, с которой была объединена данная
                    groupsWithSameElems.add(elemGrNum);
                } else {
                    newElements.add(new NewLineElement(currLnElem, elmIndex));
                }
            }
            int groupNumber;
            if (groupsWithSameElems.isEmpty()) {
                linesGroups.add(new ArrayList<>());
                groupNumber = linesGroups.size() - 1;
            } else {
                groupNumber = groupsWithSameElems.first();
            }
            for (NewLineElement newLineElement : newElements) {
                columns.get(newLineElement.columnNum).put(newLineElement.lineElement, groupNumber);
            }
            for (int matchedGrNum : groupsWithSameElems) { //перебираем все группы с таким же элементом
                if (matchedGrNum != groupNumber) {
                    unitedGroups.put(matchedGrNum, groupNumber); //сохраняем инф-цию об объединённых группах
                    linesGroups.get(groupNumber).addAll(linesGroups.get(matchedGrNum)); //объединяем группы
                    linesGroups.set(matchedGrNum, null); //помечаем группу с текущим номер, как несуществующую
                }
            }
            linesGroups.get(groupNumber).add(line);
        }
        linesGroups.removeAll(Collections.singleton(null)); //удаляем несуществующие группы
        return linesGroups;
    }
}
