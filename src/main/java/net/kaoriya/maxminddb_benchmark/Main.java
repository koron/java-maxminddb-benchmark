package net.kaoriya.maxminddb_benchmark;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;

public class Main {

    public static final String MMDB_CITY = "tmp/GeoLite2-City.mmdb";
    public static final String MMDB_COUNTRY = "tmp/GeoLite2-Country.mmdb";

    public static void findOne(String ip) throws IOException {
        runCity((r) -> {
            try {
                InetAddress a = InetAddress.getByName(ip);
                JsonNode n = r.get(a);
                System.out.println(n);
            } catch (IOException e) {
            }
        });
    }

    private static void runCity(Consumer<Reader> c) throws IOException {
        run(MMDB_CITY, c);
    }

    private static void run(String path, Consumer<Reader> c)
        throws IOException
    {
        File f = new File(path);
        Reader r = new Reader(f);
        try {
            c.accept(r);
        } finally {
            r.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //findOne(args.length == 0 ? "24.24.24.24" : args[0]);
    }

}
