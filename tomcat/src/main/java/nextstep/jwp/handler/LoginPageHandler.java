package nextstep.jwp.handler;

import org.apache.catalina.session.SessionManager;
import org.apache.catalina.util.ResourceFileReader;
import org.apache.coyote.http.HttpHeader;
import org.apache.coyote.http.HttpMethod;
import org.apache.coyote.http.HttpStatus;
import org.apache.coyote.http.SupportFile;
import org.apache.coyote.http.vo.HttpHeaders;
import org.apache.coyote.http.vo.HttpRequest;
import org.apache.coyote.http.vo.HttpResponse;
import org.apache.coyote.http.vo.Url;

public class LoginPageHandler implements Handler {

    @Override
    public HttpResponse handle(final HttpRequest request) {
        if (AuthenticationUser(request)) {
            final HttpHeaders headers = HttpHeaders.getEmptyHeaders();
            headers.put(HttpHeader.LOCATION, "/index.html");

            return new HttpResponse.Builder()
                    .status(HttpStatus.REDIRECT)
                    .headers(headers)
                    .build();
        }

        final HttpHeaders headers = HttpHeaders.getEmptyHeaders();
        headers.put(HttpHeader.CONTENT_TYPE, SupportFile.HTML.getContentType());
        final String body = ResourceFileReader.readFile("/login.html");

        return new HttpResponse.Builder()
                .status(HttpStatus.OK)
                .headers(headers)
                .body(body)
                .build();
    }

    private boolean AuthenticationUser(final HttpRequest request) {
        return request.hasCookie("JSESSIONID") && SessionManager.findSession(request.getCookie("JSESSIONID")) != null;
    }

    @Override
    public boolean isSupported(final HttpRequest request) {
        return request.isRequestMethodOf(HttpMethod.GET) &&
                request.isUrl(Url.from("/login"));
    }
}
