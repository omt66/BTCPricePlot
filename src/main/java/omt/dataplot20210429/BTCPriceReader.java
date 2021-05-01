package omt.dataplot20210429;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author omt
 */
public class BTCPriceReader {
    static class Data {
        float high;
        float low;
        Date date;
        String dateStr;
        String symbol;

        @Override
        public String toString() {
            return "[symbol: " + symbol + " high: " + high + " low: " + low 
                    + " date: " + dateStr
                    + " (date: " + date.toInstant() + ") "
                    + "]";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Read");
        
        List<Data> allData = loadBTCData();
        for (Data d: allData) {
            System.out.println("d-> " + d);
        }
    }
    
    public static List<Data> loadBTCData()  {
        try {
            List<Data> alldata = new ArrayList<>();
            String fileName = "data/gemini_BTCUSD_day.csv";
            Path path = Paths.get(fileName);
            String content;
            content = Files.readString(path);
            String[] lines = content.split("\r\n");
            for (int i=2;i< lines.length;i++) {
                String line = lines[i];
                String[] info = line.split(",");
                long unixTimestamp = Long.parseLong(info[0])/1000;
                Data d = new Data();
                d.symbol = info[2];
                d.dateStr = info[1];
                d.high = Float.parseFloat(info[4]);
                d.low = Float.parseFloat(info[5]);
                d.date = Date.from(Instant.ofEpochSecond(unixTimestamp));
                alldata.add(d);
            }
            return alldata;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
