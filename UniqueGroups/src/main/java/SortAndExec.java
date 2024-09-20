import java.io.IOException;

public class SortAndExec {

    public static void sortAndExec(String ...args) throws IOException {
        Group group = new Group(args[0], args[1]); // args[0] - input file | args[1] - output file

        long start = System.currentTimeMillis();
        group.groupWriter();
        long end = System.currentTimeMillis();

        System.out.println("Program execution time: " + ((double) end - start)/1000 + " seconds");
    }
}
