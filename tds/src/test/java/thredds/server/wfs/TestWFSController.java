package thredds.server.wfs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import static org.junit.Assert.*;

public class TestWFSController {

    private WFSController wfs;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @Before
    public void setUp(){
        wfs = new WFSController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void describeFeatureTypeTest(){
        request.addParameter("SERVICE", "WFS");
        request.addParameter("REQUEST", "DescribeFeatureType");
        request.addParameter("VERSION", "2.0.0");

        wfs.httpHandler(request, response);

    }

}