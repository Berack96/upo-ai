package net.berack.upo.ai.problem3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import smile.Network;

public class NetworkNode {

        public static Set<NetworkNode> buildSetFrom(Network net, int totRounds) {
            var nodes = new HashMap<Integer, NetworkNode>();

            for(var handle : net.getAllNodes()) nodes.put(handle, new NetworkNode(net, handle));

            var retSet = Set.copyOf(nodes.values());
            for(var node : retSet) {
                var parentsHandle = net.getParents(node.handle);
                node.parents = new NetworkNode[parentsHandle.length];

                for(var i = 0; i < parentsHandle.length; i++)
                    node.parents[i] = nodes.get(parentsHandle[i]);

                if(!node.isEvidence()) {
                    node.samples = new int[totRounds];
                    Arrays.fill(node.samples, -1);
                }
            }

            return retSet;
        }

        public static NetworkNode[] topologicalSort(Set<NetworkNode> nodes) {
            throw new UnsupportedOperationException("TODO implement this function");
        }

        final int handle;
        final String[] outcomes;
        final double[] definition;
        final int evidence;

        NetworkNode[] parents;
        int[] samples;

        private NetworkNode(Network net, int handle) {
            this.handle = handle;
            this.definition = net.getNodeDefinition(handle);
            this.outcomes = net.getOutcomeIds(handle);
            this.evidence = net.isEvidence(handle)? net.getEvidence(handle) : -1;
        }

        public boolean isEvidence() {
            return this.evidence > 0 ;
        }

        public double getProbSampleEvidence(int round) {
            if(!this.isEvidence()) throw new IllegalArgumentException("Evidence");

            var init = getStartingIndex(round);
            return this.definition[init + this.evidence];
        }

        public void setSample(double rand, int round) {
            var init = getStartingIndex(round);
            var end = init + this.outcomes.length;
            var prob = 0;

            for(var i = init; i < end; i++) {
                prob += this.definition[i];

                if(rand <= prob) {
                    this.samples[round] = i - init;
                    break;
                }
            }
        }

        private int getStartingIndex(int round) {
            var init = 0;
            var tot = this.definition.length;

            for(var p : this.parents) {
                var pIndex = p.isEvidence()? p.evidence : p.samples[round];
                if(pIndex == -1) throw new IllegalArgumentException("Parent");

                tot /= p.outcomes.length;
                init += tot * pIndex;
            }

            return init;
        }
    }