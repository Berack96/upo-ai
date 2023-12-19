package net.berack.upo.ai;

import java.util.Scanner;
import java.util.function.Function;

import net.berack.upo.ai.problem1.Puzzle8GUI;
import net.berack.upo.ai.problem2.TrisGUI;

public class Main {

    public static void main(String[] args) {
        var value = read("What do you want to play?\n1. Puzzle8\n2. Tris\n", new Scanner(System.in), num -> num > 0 && num < 2);
        var window = switch (value) {
            case 1 -> new Puzzle8GUI();
            case 2 -> new TrisGUI();
            default -> null;
        };

        if(window != null) {
            window.toFront();
            window.requestFocus();
        }
    }

    private static int read(String out, Scanner in, Function<Integer, Boolean> control) {
        var ret = 0;

        do {
            try {
                System.out.print(out);
                var str = in.nextLine();
                ret = Integer.parseInt(str);
            } catch (NumberFormatException ignore) {}
        } while(!control.apply(ret));

        return ret;
    }
}