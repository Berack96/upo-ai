package net.berack.upo.ai.problem3;

import java.util.Arrays;
import smile.Network;

/**
 * Una classe di appoggio per i nodi di un network.
 * Questa classe contiene anche un array di samples in modo da facilitare il
 * calcolo di valori.
 * 
 * @see SmileLib#buildListFrom(NetWork)
 * @author Berack
 */
public class NetworkNode {

        final int handle;
        final String[] outcomes;
        final double[] definition;
        final int evidence;
        final Network net;

        NetworkNode[] parents;
        public int[] samples;

        /**
         * Questo costruttore crea un nodo e gli assegna i valori essenziali.
         * @apiNote Non usare questo costruttore se non si sa che si sa quello che si sta facendo
         * @param net la rete
         * @param handle l'handle del nodo
         */
        NetworkNode(Network net, int handle) {
            this.handle = handle;
            this.net = net;

            this.definition = net.getNodeDefinition(handle);
            this.outcomes = net.getOutcomeIds(handle);
            this.evidence = net.isEvidence(handle)? net.getEvidence(handle) : -1;
        }

        /**
         * Indica se il nodo è evidenza o meno
         * @return vero se lo è
         */
        public boolean isEvidence() {
            return this.evidence > 0 ;
        }

        /**
         * Per utilizzare questo metodo il nodo deve essere una evidenza.
         * Dato un roud di sample permette di ricevere il valore della
         * probabilità che, dati i sample dei genitori, il nodo abbia
         * il valore dell'evidenza impostata.
         * 
         * @param round il numero del round che si stà controllando
         * @return il valore della probabilità della evidenza
         */
        public double getProbSampleEvidence(int round) {
            if(!this.isEvidence()) throw new IllegalArgumentException("Evidence");

            var init = getStartingIndex(round);
            return this.definition[init + this.evidence];
        }

        /**
         * Mette un sample al nodo nel round selezionato.
         * Il valore di rand deve essere un numero casuale tra 0 e 1 ed
         * esso permetterà di impostare un valore in base ai valori dei genitori.
         * 
         * @param rand un valore casuale tra 0 e 1
         * @param round il numero del round
         */
        public void setSample(double rand, int round) {
            var init = getStartingIndex(round);
            var end = init + this.outcomes.length;
            var prob = 0.0d;

            for(var i = init; i < end; i++) {
                prob += this.definition[i];

                if(prob >= rand) {
                    this.samples[round] = i - init;
                    break;
                }
            }
        }

        /**
         * Dato un round permette di ricavare l'indice di partenza della CPT.
         * Questo metodo serve perchè i genitori del nodo nel sample hanno
         * dei valori e io devo generarli in accordo con la CPT di questo nodo.
         * 
         * @param round il roundo corrente
         * @return l'indice iniziale per gli output del nodo in base ai valori dei genitori
         */
        private int getStartingIndex(int round) {
            var init = 0;
            var tot = this.definition.length;

            for(var p : this.parents) {
                var pIndex = p.isEvidence()? p.evidence : p.samples[round];
                if(pIndex < 0) throw new IllegalArgumentException("Parent"); // in theory impossible since Topological sorted

                tot /= p.outcomes.length;
                init += tot * pIndex;
            }

            return init;
        }

        @Override
        public boolean equals(Object obj) {
            if(!obj.getClass().isInstance(this)) return false;

            var other = (NetworkNode) obj;
            if(this.handle != other.handle) return false;
            if(this.evidence != other.evidence) return false;
            if(!Arrays.equals(this.definition, other.definition)) return false;

            return true;
        }
    }