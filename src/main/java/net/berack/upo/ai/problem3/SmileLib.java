package net.berack.upo.ai.problem3;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    static {
        var jsmile = jsmileLibName();
        var loader = SmileLib.class.getClassLoader();
        var resource = loader.getResource(jsmile);

        try {
            var uri = resource.toURI();
            var path = "";

            if(uri.toString().startsWith("jar")) {
                var tmp = System.getProperty("java.io.tmpdir");
                var file = new File(tmp + jsmile);
                if(!file.exists()) {
                    var in = resource.openStream();
                    Files.copy(in, file.toPath());
                }
                path = file.getAbsolutePath();
            }
            else path = Path.of(uri).toString();

            System.setProperty("jsmile.native.library", path);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

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
     * Ritorna il nome della libreria jsmile in base al sistema operativo.
     * @return il node del file
     */
    private static String jsmileLibName() {
        var os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("win") >= 0) return "jsmile.dll";
        if(os.indexOf("mac") >= 0) return "libjsmile.jnilib";
        if(os.indexOf("nix") >= 0
        || os.indexOf("nux") >= 0
        || os.indexOf("aix") >= 0) return "libjsmile.so";

        return null;
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
            var loader = SmileLib.class.getClassLoader();
            var in = loader.getResourceAsStream(file);
            var str = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            net.readString(str);
        } catch (Exception e) {
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
            var node = new NetworkNode(net, handle, nodes);
            list.add(node);
            nodes.put(handle, node);
        }

        return list;
    }
}
