package net.berack.upo.ai.problem3;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import smile.Network;

/**
 * Classe che permette l'utilizzo della libreria SMILE di BAYESFUSION.
 * La classe carica staticamente la libreria.dll creando la proprietà di
 * sistema jsmile.native.library includendo la resource path di jsmile.
 * In questo modo per utilizzare SMILE basta chiamare un metodo di questa classe
 * per far si che la chiave di attivazione venga correttamente controllata.
 * 
 * @apiNote Scadenza chiave 2024-06-16
 * @author Berack
 */
public class SmileLib {

    public static final String RESOURCE_PATH;
    static {
        var loader = SmileLib.class.getClassLoader();
        var wrongPath = loader.getResource("").getFile();
        var path = wrongPath.substring(1);
        try {
            RESOURCE_PATH = URLDecoder.decode(path, "ASCII");
        } catch (Exception e) {
            throw new RuntimeException("Decodification of path failed!\n" + e.getMessage());
        }

        System.setProperty("jsmile.native.library", RESOURCE_PATH + "jsmile");
        new smile.License(
            "SMILE LICENSE 02a07eb5 5c5fa64a 2a276459 " +
            "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " +
            "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " +
            "AS DEFINED IN THE BAYESFUSION ACADEMIC " +
            "SOFTWARE LICENSING AGREEMENT. " +
            "Serial #: 976arg4ifdszss2oc8kcmru6t " +
            "Issued for: GIACOMO BERTOLAZZI (20015159@studenti.uniupo.it) " +
            "Academic institution: Universit\u00e0 del Piemonte Orientale " +
            "Valid until: 2024-06-16 " +
            "Issued by BayesFusion activation server",
            new byte[] {
                7,-117,-57,124,-91,120,34,-120,-47,101,49,-49,2,-55,118,92,
                -92,-53,-67,-36,103,17,110,-61,27,116,-99,5,-72,-1,-123,-117,
                48,-28,49,-92,39,-37,22,15,-68,-7,-56,97,-6,-35,-33,57,
                114,-81,-56,-11,52,-32,113,91,-84,-33,105,-9,-25,-58,-16,-52
            }
        );
    }

    /**
     * Crea un Network dal file indicato
     * Il file deve essere una risorsa del jar o un file esterno
     * 
     * @param file il file da cercare
     * @return il network creato
     */
    public static Network getNetworkFrom(String file) {
        var net = new Network();
        try {
            net.readFile(RESOURCE_PATH + file);
        } catch (smile.SMILEException e) {
            net.readFile(file);
        }

        return net;
    }

    /**
     * Crea una lista di nodi dal network indicato.
     * I nodi usati sono un po' più comodi rispetto al network.
     * La lista è ordinata in modo che il nodo 'k' sia un discendente
     * dei nodi '0...k-1' e non di 'k+1...n'
     * 
     * @param net il network da cui prendere i dati
     * @return una lista ordinata di nodi
     */
    public static List<NetworkNode> buildListFrom(Network net) {
            var nodes = new HashMap<Integer, NetworkNode>();
            var list = new ArrayList<NetworkNode>();

            for(var handle : net.getAllNodes()) {
                var node = new NetworkNode(net, handle);
                list.add(node);
                nodes.put(handle, node);
            }

            for(var node : nodes.values()) {
                var parentsHandle = net.getParents(node.handle);
                node.parents = new NetworkNode[parentsHandle.length];

                for(var i = 0; i < parentsHandle.length; i++)
                    node.parents[i] = nodes.get(parentsHandle[i]);
            }

            return list;
        }
}
