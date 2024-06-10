package org.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MyGraphTest {
    private static final FileIo fileIo = new FileIo();
    private static final MyGraph G = new MyGraph();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        fileIo.setFilePath("src/test.txt");
        fileIo.CreateGraph(G);
        System.out.println("test begin!");


    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("test end!");
    }

    @Test
    public void testOneWorld() {
        String testString = "to";
        String expectedOutput = "to";
        String outPut = G.GetNewText(testString);

        if (!Objects.equals(outPut, expectedOutput)) {
            System.out.println("test1 test one world input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput + ".");
        }
        assertThat(outPut, is(expectedOutput));
    }

    @Test
    public void testNullString() {
        String testString = "";
        String expectedOutput = "";
        String outPut = G.GetNewText(testString);

        if (!Objects.equals(outPut, expectedOutput)) {
            System.out.println("test2 test null string input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput + ".");
        }
        assertThat(outPut, is(expectedOutput));
    }

    @Test
    public void testSignalAndMultiBridgeWorlds() {
        String testString = "to out new and";
        String expectedOutput1 = "to seek out new life and";
        String expectedOutput2 = "to seek out new civilizations and";
        String outPut = G.GetNewText(testString);
        if (!Objects.equals(outPut, expectedOutput1) && !Objects.equals(outPut, expectedOutput2)) {
            System.out.println("test3 test signal and multi bridge worlds input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput1 + "." + expectedOutput2 + ".");
        }
        assertThat(outPut, anyOf(is(expectedOutput1), is(expectedOutput2)));
    }

    @Test
    public void testUppercase() {
        String testString = "to explore new LIFE";
        String expectedOutput = "to explore strange new LIFE";
        String outPut = G.GetNewText(testString);
        if (!Objects.equals(outPut, expectedOutput)) {
            System.out.println("test4 test uppercase world input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput + ".");
        }
        assertThat(outPut, is(expectedOutput));
    }

    @Test
    public void testAllUppercase() {
        String testString = "TO SEEK NEW LIFE";
        String expectedOutput = "TO SEEK out NEW LIFE";
        String outPut = G.GetNewText(testString);
        if (!Objects.equals(outPut, expectedOutput)) {
            System.out.println("test5 test all uppercase world input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput + ".");
        }
        assertThat(outPut, is(expectedOutput));
    }

    @Test
    public void testNonAlphabeticCharacters() {
        String testString = "life n##**(e)*(&w a89/n4896d";
        String expectedOutput1 = "life and new civilizations and";
        String expectedOutput2 = "life and new life and";
        String outPut = G.GetNewText(testString);
        if (!Objects.equals(outPut, expectedOutput1) && !Objects.equals(outPut, expectedOutput2)) {
            System.out.println("test6 test non-alphabetic characters world input fail.input:" + testString
                    + ".output:" + outPut + ".expected output:" + expectedOutput1 + "." + expectedOutput2 + ".");
        }
        assertThat(outPut, anyOf(is(expectedOutput1), is(expectedOutput2)));
    }
}