package com.acs.waveserver.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class HttpAddressTest {

    @Test
    public void no_params_route_match_root() {
        String routeUri = "/";
        String requestUri = "/";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(0, param.keySet().size());
    }

    @Test
    public void no_params_route_match_other_route() {
        String routeUri = "/api/dummy";
        String requestUri = "/api/dummy";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(0, param.keySet().size());
    }

    @Test
    public void one_params_route_match() {
        String routeUri = "/api/{action}";
        String requestUri = "/api/dummy";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(1, param.keySet().size());
        assertEquals("dummy", param.getMandatory("action", String.class));
    }

    @Test
    public void one_params_route_not_all_match() {
        String routeUri = "/api/{action}";
        String requestUri = "/api/dummy/thing";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertFalse(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(0, param.keySet().size());
    }

    @Test
    public void one_params_route_not_all_match_wild_card() {
        String routeUri = "/api/{action}/*";
        String requestUri = "/api/dummy/thing";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(1, param.keySet().size());
        assertEquals("dummy", param.getMandatory("action", String.class));
    }

    @Test
    public void two_params_route_match() {
        String routeUri = "/api/{action}/{id}";
        String requestUri = "/api/dum+my/1";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(2, param.keySet().size());
        assertEquals("dum my", param.getMandatory("action", String.class));
        assertEquals((Long) 1L, param.getMandatory("id", Long.class));
    }

    @Test
    public void param_and_param_left_route_match() {
        String routeUri = "/api/{action}/{path+}";
        String requestUri = "/api/dummy/etc/host/dummy.txt";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(2, param.keySet().size());
        assertEquals("dummy", param.getMandatory("action", String.class));
        assertEquals("etc/host/dummy.txt", param.getMandatory("path", String.class));
    }

    @Test
    public void web_jars_route() {
        String routeUri = "/webjars/{path+}";
        String requestUri = "/webjars/bootstrap/3.3.7-1/css/bootstrap.css";
        HTTPAddress address = HTTPAddress.build(requestUri).ofRoute(routeUri);
        assertTrue(address.matchUrl(routeUri));

        HTTPParams param = address.pathParams;
        assertEquals(1, param.keySet().size());
        assertEquals("bootstrap/3.3.7-1/css/bootstrap.css", param.getMandatory("path", String.class));
    }
}
