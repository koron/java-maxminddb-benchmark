package net.kaoriya.maxminddb_benchmark;

import java.io.File;
import java.net.InetAddress;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;

public class Main {
    public static void main(String[] args) throws Exception {
        String ip = args.length == 0 ? "24.24.24.24" : args[0];

        File f = new File("tmp/GeoLite2-City.mmdb");
        Reader r = new Reader(f);
        try {
            InetAddress a = InetAddress.getByName(ip);
            JsonNode n = r.get(a);
            System.out.println(n);
        } finally {
            r.close();
        }
    }
}
