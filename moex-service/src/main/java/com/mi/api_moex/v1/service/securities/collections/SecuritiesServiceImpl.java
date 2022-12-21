package com.mi.api_moex.v1.service.securities.collections;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SecuritiesServiceImpl implements SecuritiesService {

    @Override
    public List<String> findFirstLevelShares() {
        log.info("Start searching first level shares...........");
        List<String> tickers;
        try {
            Path path = Path.of("first_level_shares.txt");
            if (!Files.exists(path)) {
                downloadFirstLevelShares();
            }
            tickers = Files.readAllLines(path);
            tickers.removeIf(t -> t.equals("securities") || t.equals("SECID") || t.equals(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("End searching first level shares");
        return tickers;
    }


    private void downloadFirstLevelShares() {
        log.info("Start downloading first level shares...........");
        try (InputStream in = new URL("https://iss.moex.com/iss/securitygroups/stock_shares/collections/stock_shares_one/securities.csv" + "?limit=100&iss.meta=off&iss.only=securities&securities.columns=SECID").openStream();
             ReadableByteChannel channel = Channels.newChannel(in);
             FileOutputStream out = new FileOutputStream("first_level_shares.txt")) {
            out.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("End downloading first level shares");
    }


}
