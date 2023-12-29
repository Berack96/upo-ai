package net.berack.upo.ai.problem3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class LWTest {

    @Test
    public void testSmile() {
        var net = SmileLib.getNetworkFrom("VentureBN.xdsl");

        var nodes = net.getAllNodes();
        for (var i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i] + " -> " + net.getNodeId(nodes[i]));
        }

        net.setEvidence("Forecast", "Moderate");
        net.updateBeliefs();

        var beliefs = net.getNodeValue("Success");
        var arr = new double[] {0.25, 0.75};
        assertArrayEquals(arr, beliefs, 0.001);

        net.close();
    }

    @Test
    public void testSimpleNetwork() {
        var net = SmileLib.getNetworkFrom("VentureBN.xdsl");
        net.updateBeliefs();

        var lw = new LikelyhoodWeighting(net);
        lw.updateNetwork(1000);

        for(var node : net.getAllNodes()) {
            var arr1 = net.getNodeValue(node);
            var arr2 = lw.getNodeValue(node);

            System.out.println(Arrays.toString(arr1) + " " + Arrays.toString(arr2));
            assertArrayEquals(arr1, arr2, 0.05); // 5% difference max (it is a lot but is fair)
        }
    }
}
