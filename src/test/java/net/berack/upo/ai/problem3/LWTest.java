package net.berack.upo.ai.problem3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import smile.Network;

public class LWTest {

    // 3% difference max (it is a lot but is fair)
    public static final float DELTA = 0.05f;

    @Test
    public void testSmile() {
        var net = SmileLib.getNetworkFrom("VentureBN.xdsl");

        net.setEvidence("Forecast", "Moderate");
        net.updateBeliefs();

        var beliefs = net.getNodeValue("Success");
        var arr = new double[] {0.25, 0.75};
        assertArrayEquals(arr, beliefs, 0.001);

        net.close();
    }

    @Test
    public void testSimpleNetwork() {
        checkNodesValues(SmileLib.getNetworkFrom("Malaria.xdsl"));
        checkNodesValues(SmileLib.getNetworkFrom("VentureBN.xdsl"));
    }

    @Test
    public void testEvidence() {
        var net = SmileLib.getNetworkFrom("WetGrass.xdsl");
        checkNodesValues(net);

        net.setEvidence("Sprinkler", "On");
        checkNodesValues(net);

        net.setEvidence("Wet_grass", "Bagnata");
        checkNodesValues(net);
    }

    private void checkNodesValues(Network net) {
        var lw = new LikelyhoodWeighting(net);

        net.updateBeliefs();
        lw.updateNetwork(1000);

        for(var node : net.getAllNodes()) {
            var arr1 = net.getNodeValue(node);
            var arr2 = lw.getNodeValue(node);
            assertArrayEquals(arr1, arr2, DELTA);
        }
    }
}
