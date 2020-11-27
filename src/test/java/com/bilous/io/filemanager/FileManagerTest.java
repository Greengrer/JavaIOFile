package com.bilous.io.filemanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private String pathToResources = "src/test/resources/";
    private String invalidPath = "NotAPath";

    private String pathToFile = pathToResources + "fileToAnalyze.txt";
    private String pathToEmptyDir = pathToResources + "EmptyDir";
    private String pathToDirCountTest = pathToResources + "DirForTestCount";

    private String sourceDir = pathToResources + "sourceDir";
    private String destDir = pathToResources + "destDir";



    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after() {
        File dirToEmpty = new File(pathToResources + "destDir");
        File filledDirToDelete = new File(dirToEmpty.getPath() + "/sourceDir");
        File dirToDeleteInFilled = new File(filledDirToDelete.getPath() + "/DirToCopy" );
        File fileToDeleteInFilled = new File(filledDirToDelete.getPath() + "/fileToCopy");

        File fileToDelete = new File(dirToEmpty.getPath() + "/fileToAnalyze.txt");
        File emptyDirToDelete = new File(dirToEmpty.getPath() + "/EmptyDir" );

        fileToDeleteInFilled.delete();
        dirToDeleteInFilled.delete();
        filledDirToDelete.delete();

        emptyDirToDelete.delete();
        fileToDelete.delete();
    }

    @Test
    void testCountFilesOnInvalidPath(){
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countFiles(invalidPath);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countFiles(pathToFile);
        });
    }

    @Test
    void testCountDirsOnInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countDirs(invalidPath);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countDirs(pathToFile);
        });
    }

    @Test
    void testCountFilesOnEmptyDir() throws IOException {
        assertEquals(0, FileManager.countFiles(pathToEmptyDir));
    }

    @Test
    void testCountDirsOnEmptyDir() throws IOException {
        assertEquals(0, FileManager.countDirs(pathToEmptyDir));
    }

    @Test
    void testCountFiles() throws IOException{
        assertEquals(2, FileManager.countFiles(pathToDirCountTest));
    }

    @Test
    void testCountDirs() throws IOException{
        assertEquals( 2, FileManager.countDirs(pathToDirCountTest));
    }

    @Test
    void testCopyOnInvalidSource() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.copy(invalidPath, destDir);
        });
    }

    @Test
    void testCopyOnInvalidDestination() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.copy(sourceDir, invalidPath);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.copy(sourceDir, pathToFile);
        });
    }

    @Test
    void testCopyOnEmptySource() throws IOException {
        FileManager.copy(pathToEmptyDir, destDir);
        File destDirectory = new File(destDir);
        assertEquals(1, destDirectory.listFiles().length);
    }

    @Test
    void testCopyOnDestinationWithoutEnoughFreeSpace() throws IOException {

    }

    @Test
    void testCopyDirectory() throws IOException {
        FileManager.copy(sourceDir, destDir);
        File createdFile = new File(destDir + "/sourceDir/fileToCopy");
        File createdDirectory = new File(destDir + "/sourceDir/DirToCopy");
        assertTrue(createdFile.exists());
        assertTrue(createdDirectory.exists());
        assertEquals(0, createdDirectory.listFiles().length);
    }

    @Test
    void testCopyFile() throws IOException {
        FileManager.copy(pathToFile, destDir);
        File createdFile = new File(destDir + "/fileToAnalyze.txt");
        assertTrue(createdFile.exists());
    }

    @Test
    void testCreateDirInDestinationDir() {
        File parentDir = new File(destDir);
        File childDir = new File(destDir + "/EmptyDir");

        assertTrue(!childDir.exists());

        FileManager.createDirInDestinationDir(new File(pathToResources + "EmptyDir"), parentDir);

        assertTrue(childDir.exists());
    }

    @Test
    void testCreateFileInDestinationDir() {
        File parentDir = new File(destDir);
        File childFile = new File(destDir + "/fileToAnalyze.txt");

        assertTrue(!childFile.exists());

        FileManager.createDirInDestinationDir(new File(pathToResources + "fileToAnalyze.txt"), parentDir);

        assertTrue(childFile.exists());
    }

}