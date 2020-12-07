package co.edu.javeriana.jreng;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Command {
    public static class Result {
        private int exitCode;
        private List<String> output;

        public Result(int exitCode, List<String> output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        public int getExitCode() {
            return exitCode;
        }

        public List<String> getOutput() {
            return output;
        }

        @Override
        public String toString() {
            return "Exit code: " + exitCode + "\n" + String.join("\n", output);
        }

    }

    public static Result run(String cmd, File cwd) {
        Process process;
        List<String> output = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(cmd, null, cwd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            return new Result(process.waitFor(), output);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(1, null);

    }
}
