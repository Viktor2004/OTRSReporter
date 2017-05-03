package input;

import calculate.Calc;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Виктор on 25.02.2017.
 */
public class Cmd {
    private static final Logger log = Logger.getLogger(Cmd.class);
    public static void executeCommand(String command) throws Exception {
        log.info("=====>Начинаем выполнять cmd команду "+command);
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        log.info("<=====Закончили выполнять cmd команду "+command);
        //BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //String line;
        //while (true) {
          //  line = r.readLine();
           // if (line == null) { break; }
          //  System.out.println(line);
         //}
    }
}
