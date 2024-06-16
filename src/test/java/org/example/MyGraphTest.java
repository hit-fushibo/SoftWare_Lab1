package org.example;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class MyGraphTest extends MyGraph {

    MyGraph G;
    AtomicInteger i ;
    @Before
    public void setUp(){
        fileIO test = new fileIO();
        G = new MyGraph();
        i = new AtomicInteger();
        test.setFilePath("./src/test.txt");
        test.CreateGraph(G);
    }


    @Test
    public void testSearchBridgeWordCase1(){
        assertEquals(0,G.searchBridgeWord("hello","out",i).size());

    }

    @Test
    public void testSearchBridgeWordCase2(){
        assertEquals(0,G.searchBridgeWord("out","hello",i).size());

    }

    @Test
    public void testSearchBridgeWordCase3(){
        assertEquals(0,G.searchBridgeWord("ok","out",i).size());

    }

    @Test
    public void testSearchBridgeWordCase4(){
        assertEquals(0,G.searchBridgeWord("civilizations","out",i).size());

    }

    @Test
    public void testSearchBridgeWordCase5(){
        assertEquals(0,G.searchBridgeWord("to","new",i).size());

    }

    @Test
    public void testSearchBridgeWordCase6(){
        assertEquals("explore",G.searchBridgeWord("to","strange",i).get(0));

    }



}