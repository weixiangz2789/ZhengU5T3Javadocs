/**
 * This class represents a hangman object
 *
 * @author Wei Xiang Zheng
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Hangman {
    /** dictionary in Hangman */
    private ArrayList dictionary;
    /** Difficulty level in Hangman */
    private int difficulty;
    /** Number of guesses the user gets */
    private int lives;

    /** Sets difficulty level
     *
     * @param diff the difficulty
     * */
    public Hangman(int diff) {
        this.difficulty = diff;
    }

    /** Sets the difficulty level to random */
    public Hangman() {
        difficulty = (int) (Math.random() * 3) + 1;
    }
    /** Runs the hangman game where the user has to guess the random word
     * each part of the word they correctly guess replaced an underscore
     * The user gets 6 guesses and each time they guess wrong, 1 live is deducted */
    public void play() {
        Scanner scan = new Scanner(System.in);
        String word = getRandomWord();
        String wordCopy = word;
        System.out.println(word); //Testing
        String temp = "";
        String used = "";
        lives = 6;
        System.out.println();
        int index = 0;
        for (int i = 0; i < word.length(); i++) { // Prints the underscores
            temp += "_";
        }
        System.out.println(temp + "\n");
        while (lives > 0 && !isWin(temp, word)) { // Runs while # of guesses > 0 and user hasn't guessed the word
            System.out.println(determineHangman());
            System.out.println("Number of lives left: " + lives);
            System.out.println("\nPlease choose a letter or guess the word: ");
            String letter = scan.nextLine();
            System.out.println("");
            if (letter.equals(word)) { // If user guesses the word, break out of loop
                temp = letter;
                break;
            }
            if (temp.contains(letter)) {
                System.out.println("You've guessed " + letter + " before. Try again!");
            }

            if (wordCopy.contains(letter)) { // Replaces the underscores with correctly guessed letters
                while (wordCopy.contains(letter)) {
                    index = wordCopy.indexOf(letter);
                    temp = temp.substring(0, index) + letter + temp.substring(index + letter.length());
                    if (letter.length() == 1) {
                        wordCopy = wordCopy.replaceFirst(letter, " ");
                    } else {
                        for (int i = 0; i < letter.length(); i++) {
                            wordCopy = wordCopy.replaceFirst(letter.substring(i, i + 1), " ");
                        }
                    }
                }
                System.out.println(temp);
            } else {
                if (used.contains(letter)) {
                    lives++;
                }
                lives--;
                System.out.println(temp);
            }
            if (letter.length() == 1) {
                if (used.contains(letter)) { // Creates a string that contains all the letter the user has entered
                    System.out.println("Used letters: " + used);
                } else {
                    used += letter + ", ";
                    System.out.println("Used letters: " + used);
                }
            }
        }
        if (temp.equals(word)) { // win condition
            if (lives > 0) {
                if (lives == 6) {
                    System.out.println("You win! Now you may kiss your bride.");
                } else {
                    System.out.println("You win! You now must take responsibility for his lost limbs :P");
                }
            }
        } else {
            System.out.println(determineHangman());
            System.out.println("Louis XVI has died. Game over.");
            System.out.println("The word was: " + word);
        }
    }

    /** Returns a randomly generated word
     *
     * @return a randomly generated word
     * */
    public String getRandomWord() {
        int temp = 0;
        if (difficulty == 1) {
            temp = (int) (Math.random() * 999);
        } else if (difficulty == 2) {
            temp = (int) (Math.random() * 10000);
        } else if (difficulty == 3) {
            temp = (int) (Math.random() * 58110);
        }
        importDictionary();
        Object word = dictionary.get(temp);
        return word.toString();
    }

    /** Returns a randomly generated word based on difficulty
     *
     * @param level changes difficulty level
     * @return a randomly generated word
     * */
    public String getRandomWord(int level) {
        int temp = 0;
        if (level == 1) {
            temp = (int) (Math.random() * 999);
        } else if (difficulty == 2) {
            level = (int) (Math.random() * 10000);
        } else if (difficulty == 3) {
            level = (int) (Math.random() * 58110);
        }
        importDictionary();
        Object word = dictionary.get(temp);
        return word.toString();
    }
    /** Create an array list based on difficulty and stores it into dictionary*/
    private void importDictionary() {
        String[] tmp = null;
        try {
            FileReader fileReader = new FileReader("blank.txt");
            if (difficulty == 1) {
                fileReader = new FileReader("EasyWords.txt");
            } else if (difficulty == 2) {
                fileReader = new FileReader("Medium.txt");
            } else if (difficulty == 3) {
                fileReader = new FileReader("Hard.txt");
            }
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            bufferedReader.close();
            tmp = lines.toArray(new String[lines.size()]);
        } catch (IOException e) {
            System.out.println("Error importing file; unable to access " + e.getMessage());
        }

        dictionary = new ArrayList<String>(Arrays.asList(tmp));
    }
    /** Prints out the state of the hangman based on how many guesses the user has left*/
    private String determineHangman() { // helper method to print the state of our hangman based on the # of guesses the
        // user has gotten wrong
        if (lives == 6) {
            return "------|\n|\n|\n|\n|\n|_____";
        } else if (lives == 5) {
            return "------|\n|   (o.o)\n|\n|\n|\n|_____";
        } else if (lives == 4) {
            return "------|\n|   (o.o)\n|     |\n|     |\n|\n|_____";
        } else if (lives == 3) {
            return "------|\n|   (o.o)\n|    \\|\n|     |\n|\n|_____";
        } else if (lives == 2) {
            return "------|\n|   (o.o)\n|    \\|/\n|     |\n|\n|_____";
        } else if (lives == 1) {
            return "------|\n|   (o.o)\n|    \\|/\n|     |\n|    /\n|_____";
        }
        return "------|\n|   (x.x)\n|    \\|/\n|     |\n|    / \\\n|_____";
    }
    /** Returns true if the user guesses the word correctly
     *
     * @param past temp string which represents the user's guess so far
     * @param present the current word
     * @return true if the user guessed the word correctly, false otherwise
     * */
    private boolean isWin(String past, String present) {
        return past.equals(present);

    }
}