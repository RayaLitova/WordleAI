import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) throws Exception {
        List<String> words = getWordsFromURL("https://random-word-api.herokuapp.com/all");
        AI ai = new AI(words);
        ai.startGame();
    }

    public static List<String> getWordsFromURL(String url) throws IOException {
        List<String> words = new ArrayList<String>();
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader( new InputStreamReader( oracle.openStream() ) );

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            List<String> tmp_words = Arrays.stream(inputLine.split("[,\"]")).toList();//remove ("[],)
            for(String w:tmp_words) {
                if (w.length() == 5) //filter only 5-letter words
                    words.add(w);
            }
        }
        in.close();
        return words;
    }
}
