package de.bentzin.hoever.debug;

/*
 * Exit codes handled by the launcher:
 * -1: unknown error (exit without restart)
 * 0: normal exit (dont restart)
 * 1: restart without update
 * 2: restart with update
 * 3: error (restart with update)
 * 4 +: unrecoverable error (dont restart)
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program was started!");
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        System.out.println(args.length + " arguments: " + builder.toString().trim());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the exit-code: ");
        try {
            int exitCode = Integer.parseInt(reader.readLine());
            System.out.println("Exit code: " + exitCode);
            switch (exitCode) {
                case -1:
                    System.out.println("Unknown error (exit without restart)");
                    break;
                case 0:
                    System.out.println("Normal exit (dont restart)");
                    break;
                case 1:
                    System.out.println("Restart without update");
                    break;
                case 2:
                    System.out.println("Restart with update");
                    break;
                case 3:
                    System.out.println("Error (restart with update)");
                    break;
                default:
                    System.out.println("Unrecoverable error (dont restart)");
                    break;
            }
            System.out.println("Exiting program!");
            System.exit(exitCode);
        } catch (IOException e) {
            System.out.println("Failed to read input! Exiting with error code -1");
            System.out.println("Exiting program!");
            System.exit(-1);
        }


    }
}