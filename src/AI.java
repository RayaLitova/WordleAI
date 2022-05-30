import java.util.*;

public class AI {
    List<String> words; //all possible words
    Map<Character, Integer> contains; //letters contained in the answer
    private boolean gameOver;
    private Integer attempts;

    public AI(List<String> words) {
        this.words = new ArrayList<String>(words);
        contains = new HashMap<Character, Integer>();
        gameOver = false;
        attempts = 0;
    }

    private HashMap<Character, Integer> getMostUsedLetters(){
        HashMap<Character, Integer> res = new HashMap<Character, Integer>(); //letter - count

        for(String word : words){
            for(Character letter : word.toCharArray()){
                if(res.containsKey(letter))
                    res.put(letter, res.get(letter)+1);
                else
                    res.put(letter,1);
            }
        }
        return res;
    }

    private String determineBestWord(List<String> options){ //returns the word with the most coverage
        String res = new String();
        long maxCoverage = 0; //the word with the maximum coverage eliminates the most words
        long currentCoverage = 0;
        HashMap<Character, Integer> letters = getMostUsedLetters();

        for(String word : options){
            currentCoverage = 0;
            for(Character letter : word.toCharArray()){
                if(word.chars().filter(ch -> ch == letter).count() > 1) //divide coverage for repeated letters
                    currentCoverage += letters.get(letter) / word.chars().filter(ch -> ch == letter).count();
                else
                    currentCoverage += letters.get(letter);
            }
            if(currentCoverage>maxCoverage){
                maxCoverage = currentCoverage;
                res = word;
            }
        }
        return res;

    }

    private void removeIfNotContains(Character letter, Integer count){
        List<String> newWords = new ArrayList<>(words); //create temporary list to avoid errors on line 80
        for(String word : words){
            if(word.contains(letter.toString())) continue;
            if(word.chars().filter(ch -> ch == letter).count() >= count) continue;
            newWords.remove(word);
        }
        words = newWords;
    }

    private void removeIfContains(Character letter, Integer count /* needed for cases with repeated letters*/){
        List<String> newWords = new ArrayList<>(words);//create temporary list to avoid errors on line 90
        for(String word : words){
            if(word.chars().filter(ch -> ch == letter).count() <= count) continue; //fix issues with repeated letters
            newWords.remove(word);
        }
        words = newWords;
    }

    private void removeIfOnPosition(Character letter, Integer index){
        List<String> newWords = new ArrayList<>(words); //create temporary list to avoid errors on line 99
        for(String word : words){
            if(word.toCharArray()[index] == letter)
                newWords.remove(word);
        }
        words = newWords;
    }

    private void removeIfNotOnPosition(Character letter, Integer index){
        List<String> newWords = new ArrayList<>(words);//create temporary list to avoid errors on line 108
        for(String word : words){
            if(word.toCharArray()[index] != letter)
                newWords.remove(word);
        }
        words = newWords;
    }

    private void scanInput(String input, String guess) throws Exception {
        attempts++;
        if(input.contains("PPPPP")){ //check for correct guess
            gameOver = true;
            System.out.println("Word guessed!");
            System.out.println("Took " + attempts + " attempts");
            return;
        }
        if(input.length() > guess.length()){
            throw (new Exception("Bad Input!"));
        }
        for(int i=0; i<guess.length(); i++){
            Character currentLetter = guess.toCharArray()[i];
            if(input.toCharArray()[i] == '_'){
                if(contains.containsKey(currentLetter)) //needed for case with repeated letters
                    removeIfContains(currentLetter,contains.get(currentLetter));
                else
                    removeIfContains(currentLetter, 0);

            }else if(input.toCharArray()[i] == 'L'){
                if(contains.containsKey(currentLetter))
                    contains.put(currentLetter, contains.get(currentLetter)+1);
                else
                    contains.put(currentLetter, 1);

                removeIfNotContains(currentLetter, contains.get(currentLetter));
                removeIfOnPosition(currentLetter, i);
            }else if(input.toCharArray()[i] == 'P'){
                if(contains.containsKey(currentLetter))
                    contains.put(currentLetter, contains.get(currentLetter)+1);
                else
                    contains.put(currentLetter, 1);

                removeIfNotOnPosition(currentLetter, i);
            }else{
                throw (new Exception("Incorrect Input!"));
            }
        }
    }

    public void startGame() throws Exception {
        System.out.println("Expected input:");
        System.out.println("Letter not present: _");
        System.out.println("Letter is present but on the wrong place: L");
        System.out.println("Letter is present and on the right position: P");
        System.out.println("On incorrect input the program throws an exception!");
        System.out.println("For random word from the dictionary visit: https://random-word-api.herokuapp.com/word?length=5");
        System.out.println();


        String word;

        while(words.size()>0 && !gameOver){
            word = determineBestWord(words);
            System.out.println(word);
            Scanner sc=new Scanner(System.in);
            String input = sc.nextLine();
            scanInput(input, word);
        }
        if(words.size() == 0 && !gameOver) throw (new Exception("Word not in the list"));
    }
}
