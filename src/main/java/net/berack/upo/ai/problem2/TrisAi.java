package net.berack.upo.ai.problem2;

import static net.berack.upo.ai.problem2.Tris.Symbol.*;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 
 * @author Berack96
 */
public class TrisAi {

    public static final Function<Tris, Tris.Coordinate[]> ACTIONS = tris -> tris.availablePlays();
    public static final BiFunction<Tris, Tris.Coordinate, Tris> TRANSITION = (tris, coord) -> new Tris(tris, coord);
    public static final Function<Tris, Integer> GAIN = tris -> {
        var symbol = tris.getNextPlaySymbol();
        var count = 0;

        // top left
        count += TrisAi.value(symbol, tris.get(0,0), tris.get(1,0), tris.get(2,0));
        count += TrisAi.value(symbol, tris.get(0,0), tris.get(0,1), tris.get(0,2));

        // bottom right
        count += TrisAi.value(symbol, tris.get(2,2), tris.get(1,2), tris.get(0,2));
        count += TrisAi.value(symbol, tris.get(2,2), tris.get(2,1), tris.get(2,0));

        // center diagonals
        count += TrisAi.value(symbol, tris.get(0,0), tris.get(1,1), tris.get(2,2));
        count += TrisAi.value(symbol, tris.get(0,2), tris.get(1,1), tris.get(2,0));

        // center horizontal & vertical
        count += TrisAi.value(symbol, tris.get(0,1), tris.get(1,1), tris.get(2,1));
        count += TrisAi.value(symbol, tris.get(1,0), tris.get(1,1), tris.get(1,2));

        // all calculation are done on the NEXT so for the current invert the value
        return -count;
    };

    static int value(Tris.Symbol symbol, Tris.Symbol...values) {
        var totE = 0;
        var totO = 0;
        var totX = 0;

        for(var val : values) switch(val) {
            case VALUE_O: totO += 1; break;
            case VALUE_X: totX += 1; break;
            case EMPTY: totE += 1; break;
        }

        var value = 0;
        if(totO == 3 || totX == 3) value = 100;
        else if(totE + totO == 3 || totE + totX == 3) value = switch(totE) {
            case 1 -> 10;
            case 2 -> 1;
            default -> 0;
        };

        if((totO > totX && symbol == VALUE_X) || totX > totO && symbol == VALUE_O)
            return -value;
        return value;
    }


    private Tris tris;
    private MiniMax<Tris, Tris.Coordinate> minimax;

    public TrisAi(Tris tris) {
        this.minimax =  new MiniMax<>(TRANSITION, ACTIONS, GAIN);
        this.tris = Objects.requireNonNull(tris);
    }

    public void playNext() {
        this.playNext(2);
    }

    public void playNext(int lookahead) {
        var action = minimax.next(this.tris, lookahead);
        tris.play(action.x, action.y);
    }
}
