package com.project.vert_backend.rest;

import com.project.vert_backend.controller.DataController;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Selwyn Lehmann
 *
 * @TODO: Finish units tests and include Mockito once FileController is fully implemented.
 */
public class FileControllerTest {

    private Response okResponse;

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {
        /**
         * @TODO: Finish test by creating Mockito instance for FileController
         */
    }

    @After
    public void tearDown() {}

    /**
     * Test of playlist method, of class FileController.
     * @TODO: Finish test
     */
    @Test
    public void testPlaylists() {
        DataController instance = new DataController();
        Map result = instance.getPlaylists(null);
        Assert.assertNotNull(result);
    }
}
