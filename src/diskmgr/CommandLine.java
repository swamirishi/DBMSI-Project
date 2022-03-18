package diskmgr;
import java.util.*;

class CommandLine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] input = sc.nextLine().split(" ");
        if(input[0].equals(Utils.QUERY)){
            runQuery(Arrays.copyOfRange(input, 1, input.length));
        }else{
            runReport(Arrays.copyOfRange(input, 1, input.length));
        }
    }

    private static void runReport(String[] input) {

    }

    private static void runQuery(String[] input) {

    }
}

class Utils {
    static final String QUERY = "query";
    static final String REPORT = "report";
}

