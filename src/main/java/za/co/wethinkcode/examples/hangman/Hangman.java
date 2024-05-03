package za.co.wethinkcode.examples.hangman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

// tag::hangman-class[]
public class Hangman {
//    private static String selectedWord;
    private static  Answer wordToGuess;

    public static void main(String[] args) throws IOException {
        // end::hangman-class[]

        Player player = new Player();
        Answer wordToGuess = getWords(player);
        Answer currentAnswer = start(wordToGuess);
        String message = run(player, wordToGuess, currentAnswer);
        System.out.println(message);

    }

    private  static Answer getWords(Player player) throws IOException {
        Random random = new Random();

        System.out.println("Words file? [leave empty to use short_words.txt]");
        String fileName = player.getWordsFile();
        List<String> words = Files.readAllLines(Path.of(fileName));                                     //<3>
        int randomIndex = random.nextInt(words.size());                                                 //<4>
        String randomWord = words.get(randomIndex).trim();                                                   //<5>
        return new Answer(randomWord);

    }
    public static Answer start(Answer wordToGuess) {
        Answer currentAnswer = wordToGuess.generateRandomHint();
        System.out.println("Guess the word: " + currentAnswer);
        return currentAnswer;
    }
    public static String run(Player player, Answer wordToGuess, Answer currentAnswer){//<7>
        String message = new String();
        while (!currentAnswer.equals(wordToGuess)){                                         //<9>
            String guess = player.getGuess();                                                     //<10>
            if (player.wantsToQuit()){
                System.out.println("Bye!");
                break;
            }
//            char guessedLetter = guess.charAt(0);
            if (currentAnswer.isGoodGuess(wordToGuess,guess.charAt(0))){                                    //<12>
                currentAnswer = wordToGuess.getHint(currentAnswer, guess.charAt(0));
                System.out.println(currentAnswer.toString());

            } else {                                                                                    //<13>
                // tag::use-numberGuesses[]
                player.lostChance();
                System.out.println("Wrong! Number of guesses left: " + player.getChances());

                if (player.hasNoChances()) {
                   return  "Sorry, you are out of guesses. The word was: " + wordToGuess;

                }
                // end::use-numberGuesses[]
            }
        }

        if (currentAnswer.equals(wordToGuess)) {                                             //<14>
            message =   "That is correct! You escaped the noose .. this time." ;
        }
     return message;
    }


}

