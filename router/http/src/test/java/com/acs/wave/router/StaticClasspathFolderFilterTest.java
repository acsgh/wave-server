package com.acs.wave.router;

import com.acs.wave.router.constants.ProtocolVersion;
import com.acs.wave.router.constants.RequestMethod;
import com.acs.wave.router.files.StaticClasspathFolderFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class StaticClasspathFolderFilterTest {

    @Mock
    private ClassLoader classLoader;

    @Mock
    private HTTPHeader header;

    @Mock
    private HTTPParams pathParams;

    @Mock
    private HTTPParams queryParams;

    @Mock
    private HTTPRouter httpRouter;

    @Mock
    private Supplier<Optional<HTTPResponse>> next;

    private StaticClasspathFolderFilter webJarsHandler;

    @Before
    public void init() {
        webJarsHandler = new StaticClasspathFolderFilter("META-INF/resources/webjars", false, classLoader);
    }

    @Test
    public void no_web_jars_found() {
        HTTPRequest request = getRequest(RequestMethod.GET, "/webjars/not-found.txt", ProtocolVersion.HTTP_1_1);
        HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder(request, httpRouter);
        webJarsHandler.handle(request, responseBuilder, next);
        verify(next).get();
    }

    @Test
    public void web_jars_found() throws Exception {
        String secret = "Hello World!";

        HTTPRequest request = getRequest(RequestMethod.GET, "/found.txt", ProtocolVersion.HTTP_1_1);
        HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder(request, httpRouter);

        when(pathParams.get("path", String.class)).thenReturn(Optional.of("found.txt"));
        when(classLoader.getResource("META-INF/resources/webjars/found.txt")).thenReturn(new URL("http://dummy.com"));
        when(classLoader.getResourceAsStream("META-INF/resources/webjars/found.txt")).thenReturn(getInputStream(secret), getInputStream(secret));
        Optional<HTTPResponse> response =  webJarsHandler.handle(request, responseBuilder, next);
        verify(next, times(0)).get();
        assertTrue(response.isPresent());
        System.out.println(Arrays.toString(secret.getBytes()));
        System.out.println(Arrays.toString(response.get().body));
        assertEquals(secret, new String(response.get().body).trim());
    }

    private InputStream getInputStream(String secret) {
        return new ByteArrayInputStream(secret.getBytes());
    }


    private HTTPRequest getRequest(RequestMethod method, String uri, ProtocolVersion version) {
        return new HTTPRequest(method, uri, version, new HTTPHeaders(), "localhost", new byte[0]);
    }
}
