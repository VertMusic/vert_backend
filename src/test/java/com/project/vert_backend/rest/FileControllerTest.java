package com.project.vert_backend.rest;

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
     * Test of upload method, of class FileController.
     * @TODO: Finish test
     */
    @Test
    public void testUpload() {
        FileController instance = new FileController();
        Response result = instance.upload();
        Assert.assertNotNull(result);
    }

    /**
     * Test of stream method, of class FileController.
     * @TODO: Finish test
     */
    @Test
    public void testStream() {
        FileController instance = new FileController();
        Response result = instance.stream();
        Assert.assertNotNull(result);
    }

    /**
     * Test of download method, of class FileController.
     * @TODO: Finish test
     */
    @Test
    public void testDownload() {
        FileController instance = new FileController();
        Response result = instance.download();
        Assert.assertNotNull(result);
    }
}
