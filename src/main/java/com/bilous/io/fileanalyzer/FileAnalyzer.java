package com.bilous.io.fileanalyzer;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileAnalyzer {
    //checking if it is a file - done.
    //checking if it isn't null - done.
    //?? Ctrl +Alt +L
    // Send over to git
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("There is more or less than two arguments.");
        }
        File fileToAnalyze = new File(args[0]);
        if (!fileToAnalyze.isFile()) {
            throw new IllegalArgumentException(fileToAnalyze.getAbsolutePath() + " isn't a file.");
        }
        String sample = args[1];

        String fileContent;
        fileContent = readFileIntoString(fileToAnalyze);

        printAmountOfInstances(getAmountOfInstances(fileContent, sample));
        printEverySentenceWithInstance(getSentencesWithInstances(fileContent, sample));

    }

    static String readFileIntoString(File fileToRead) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileToRead);
        int fileLength = (int) fileToRead.length();
        byte[] fileContent = new byte[fileLength];
        fileInputStream.read(fileContent, 0, fileLength);
        return new String(fileContent);
    }

    static List<String> getSentencesWithInstances(String fileContent, String sample) {

        if (fileContent.isEmpty()) {
            return null;
        } else {
            List<String> listOfSentences = new ArrayList<>();
            int beginningOfSentence = 0;
            for (int i = 0; i < fileContent.length(); i++) {
                if ((fileContent.charAt(i) == '!') || (fileContent.charAt(i) == '?') || (fileContent.charAt(i) == '.')) {
                    if (fileContent.substring(beginningOfSentence, i + 1).contains(sample)) {
                        listOfSentences.add(fileContent.substring(beginningOfSentence, i + 1));
                    }
                    beginningOfSentence = i + 1;
                }
            }

            return listOfSentences;
        }
    }

    static int getAmountOfInstances(String fileContent, String sample) {
        int amountOfInstances = 0;
        int indexOfInstance = fileContent.indexOf(sample);
        while (indexOfInstance != -1) {
            amountOfInstances++;
            indexOfInstance = fileContent.indexOf(sample, indexOfInstance + sample.length());
        }
        return amountOfInstances;
    }

    static void printAmountOfInstances(int amount) {
        System.out.println(amount);
    }

    static void printEverySentenceWithInstance(List<String> sentencesWithInstances) {
        if (sentencesWithInstances != null) {
            Iterator iterator = sentencesWithInstances.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
}
