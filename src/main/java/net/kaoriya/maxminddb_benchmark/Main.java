package net.kaoriya.maxminddb_benchmark;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;
import java.util.Random;

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

    private static InetAddress nextAddress(Random r) throws IOException {
        byte[] b = new byte[4];
        r.nextBytes(b);
        return InetAddress.getByAddress(b);
    }

    private static void queryMany(Reader reader) {
        long seed = 0;
        long sec = 10;
        try {
            Random rand = new Random(seed);
            long hitCount = 0;
            long queryCount = 0;
            long end = System.currentTimeMillis() + sec * 1000;
            while (System.currentTimeMillis() < end) {
                InetAddress addr = nextAddress(rand);
                JsonNode n = reader.get(addr);
                if (n != null) {
                    ++hitCount;
                }
                ++queryCount;
            }
            System.out.println(String.format(
                        "%1.2f/sec (total:%2$d, hit-rate:%3$.2f)",
                        (float)queryCount / sec,
                        queryCount,
                        (float)hitCount / queryCount));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        //findOne(args.length == 0 ? "24.24.24.24" : args[0]);
        runCity(Main::queryMany);
        runCity(Main::queryMany);
        runCity(Main::queryMany);
    }

}
