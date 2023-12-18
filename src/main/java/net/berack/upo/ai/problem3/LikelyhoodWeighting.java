package net.berack.upo.ai.problem3;

import java.security.SecureRandom;
import java.util.Objects;
import smile.Network;

public class LikelyhoodWeighting {

    public final Network net;

    public LikelyhoodWeighting(Network net) {
        this.net = Objects.requireNonNull(net);
    }

    public void calculate(int totalRuns) {
        totalRuns = Math.max(1, totalRuns);

        var nodes = NetworkNode.buildSetFrom(net, totalRuns);
        var rand = new SecureRandom();
        var prob = new double[totalRuns];
        var sum = 0.0d;

        for(var run = 0; run < totalRuns; run++) {
            prob[run] = 1;

            for(var node: nodes) {
                if(!node.isEvidence()) node.setSample(rand.nextDouble(), run);
                else prob[run] *= node.getProbSampleEvidence(run);
            }

            sum += prob[run];
        }

        for(var node : nodes) if(!node.isEvidence()) {
            var values = new double[node.outcomes.length];

            for(var run = 0; run < totalRuns; run++)
                values[node.samples[run]] += prob[run];
            for(var i = 0; i < values.length; i++)
                values[i] /= sum;

            net.setPointValues(node.handle, values);
        }
    }
}
