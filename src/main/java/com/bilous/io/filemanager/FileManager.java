package com.bilous.io.filemanager;

import java.io.*;

public class FileManager {

    // public static int countFiles(String path) - принимает путь к папке,
    // возвращает количество файлов в папке и всех подпапках по пути
    public static int countFiles(String path) {

        File directory = new File(path);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("This isn't a directory.");
        }
        int count = 0;
        File[] listOfElements = directory.listFiles();
        if (listOfElements != null) {
            for (File element : listOfElements) {
                if (element.isFile()) {
                    count++;
                } else {
                    count += countFiles(element.getAbsolutePath());
                }
            }
        }

        return count;
    }

    // public static int countDirs(String path) - принимает путь к папке,
    // возвращает количество папок в папке и всех подпапках по пути
    public static int countDirs(String path) {
        File directory = new File(path);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("This isn't a directory.");
        }
        int count = 0;
        File[] listOfElements = directory.listFiles();
        if (listOfElements != null) {
            for (File element : listOfElements) {
                if (element.isDirectory()) {
                    count++;
                    count += countDirs(element.getAbsolutePath());
                }
            }
        }

        return count;
    }

    public static void copy(String from, String to) throws IOException {

        File copyFrom = new File(from);
        File copyTo = new File(to);
        validatePaths(copyFrom, copyTo);

        if (copyFrom.isFile()) {
            copyFileToDestination(copyFrom, copyTo);
        } else {
            File newDestinationDir = createDirInDestinationDir(copyFrom, copyTo);
            File[] arrayOfElements = copyFrom.listFiles();
            if (arrayOfElements.length != 0) {
                for (File element : arrayOfElements) {

                    if (element.isFile()) {
                        copyFileToDestination(element, newDestinationDir);
                    } else {
                        copy(element.getPath(), newDestinationDir.getPath());
                    }
                }
            }
        }
    }

    static File createDirInDestinationDir(File copyFrom, File copyTo) {
        File newDestinationDir = new File(copyTo.getPath(), copyFrom.getName());
        newDestinationDir.mkdir();
        return newDestinationDir;
    }

    private static void copyFileToDestination(File copyFrom, File copyTo) throws IOException {

        checkEnoughSpaceInDestination(copyFrom, copyTo);
        FileInputStream inputStreamFrom = new FileInputStream(copyFrom);
        byte[] buff = new byte[(int) copyFrom.length()];
        inputStreamFrom.read(buff, 0, (int) copyFrom.length());

        File destinationChild = new File(copyTo.getPath(), copyFrom.getName());
        destinationChild.createNewFile();
        FileOutputStream outputStreamTo = new FileOutputStream(destinationChild);
        outputStreamTo.write(buff, 0, (int) copyFrom.length());

        inputStreamFrom.close();
        outputStreamTo.close();
    }


    private static void checkEnoughSpaceInDestination(File copyFrom, File copyTo) {
        if (copyTo.getFreeSpace() < copyFrom.length()) {
            throw new IllegalStateException("There isn't enough space in destination directory to copy " + copyFrom.getAbsolutePath() + " into it.");
        }
    }

    static void validatePaths(File copyFrom, File copyTo) {
        if (!copyTo.isDirectory()) {
            throw new IllegalArgumentException("This isn't a directory you are copying to.");
        }

        if (!copyFrom.isDirectory() && !copyFrom.isFile()) {
            throw new IllegalArgumentException("This isn't a file nor a directory you're copying.");
        }
    }
}
