package net.berack.upo.ai.problem3;

import java.net.URLDecoder;

import smile.Network;

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

    public static Network getNetworkFrom(String file) {
        var net = new Network();
        net.readFile(RESOURCE_PATH + file);
        return net;
    }

    public static void main(String[] args) throws Exception {
        var net = new Network();

        net.readFile(RESOURCE_PATH + "VentureBN.xdsl");

        var nodes = net.getAllNodes();
        for (var i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i] + " -> " + net.getNodeId(nodes[i]));
        }

        net.setEvidence("Forecast", "Moderate");
        net.updateBeliefs();

        var beliefs = net.getNodeValue("Success");
        for (var i = 0; i < beliefs.length; i++) {
            System.out.println(net.getOutcomeId("Success", i) + " = " + beliefs[i]);
        }

        net.close();
    }
}
