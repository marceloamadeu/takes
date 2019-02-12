/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rs;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import org.cactoos.text.JoinedText;
import org.junit.Test;
import org.llorllale.cactoos.matchers.TextIs;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link RsWithType}.
 * @since 0.16.9
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class RsWithTypeTest {

    /**
     * Carriage return constant.
     */
    private static final String CRLF = "\r\n";

    /**
     * Content type text/html.
     */
    private static final String TYPE_HTML = "text/html";

    /**
     * Content type text/xml.
     */
    private static final String TYPE_XML = "text/xml";

    /**
     * Content type text/plain.
     */
    private static final String TYPE_TEXT = "text/plain";

    /**
     * Content type application/json.
     */
    private static final String TYPE_JSON = "application/json";

    /**
     * HTTP Status OK.
     */
    private static final String HTTP_OK = "HTTP/1.1 200 OK";

    /**
     * Content-Type format.
     */
    private static final String CONTENT_TYPE = "Content-Type: %s";

    /**
     * Content-Type format with charset.
     */
    private static final String TYPE_WITH_CHARSET =
        "Content-Type: %s; charset=%s";

    /**
     * RsWithType can replace an existing type.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void replaceTypeToResponse() throws Exception {
        final String type = RsWithTypeTest.TYPE_TEXT;
        new Assertion<>(
            new RsPrint(
                new RsWithType(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    type
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(RsWithTypeTest.CONTENT_TYPE, type),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType does not replace response code.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void doesNotReplaceResponseCode() throws Exception {
        final String body = "Error!";
        new Assertion<>(
            new RsPrint(
                new RsWithType(
                    new RsWithBody(
                        new RsWithStatus(HttpURLConnection.HTTP_INTERNAL_ERROR),
                        body
                    ),
                    RsWithTypeTest.TYPE_HTML
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    "HTTP/1.1 500 Internal Error",
                    "Content-Length: 6",
                    "Content-Type: text/html",
                    "",
                    body
                )
            )
        );
    }

    /**
     * RsWithType.HTML can replace an existing type with text/html.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void replacesTypeWithHtml() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType.Html(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML)
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_HTML
                    ),
                    "",
                    ""
                )
            )
        );
        new Assertion<>(
            new RsPrint(
                new RsWithType.Html(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_HTML,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType.JSON can replace an existing type with application/json.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void replacesTypeWithJson() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType.Json(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML)
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_JSON
                    ),
                    "",
                    ""
                )
            )
        );
        new Assertion<>(
            new RsPrint(
                new RsWithType.Json(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_JSON,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType.XML can replace an existing type with text/xml.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void replacesTypeWithXml() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType.Xml(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML)
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE, RsWithTypeTest.TYPE_XML
                    ),
                    "",
                    ""
                )
            )
        );
        new Assertion<>(
            new RsPrint(
                new RsWithType.Xml(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_XML,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType.Text can replace an existing type with text/plain.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void replacesTypeWithText() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType.Text(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML)
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE, RsWithTypeTest.TYPE_TEXT
                    ),
                    "",
                    ""
                )
            )
        );
        new Assertion<>(
            new RsPrint(
                new RsWithType.Text(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_TEXT,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType can add properly the content type to the header.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void addsContentType() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_TEXT)
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_TEXT
                    ),
                    "",
                    ""
                )
            )
        );
    }

    /**
     * RsWithType can add the charset to the content type when it is explicitly
     * specified.
     * @throws Exception If a problem occurs.
     */
    @Test
    public void addsCharsetToContentType() throws Exception {
        new Assertion<>(
            new RsPrint(
                new RsWithType(
                    new RsEmpty(),
                    RsWithTypeTest.TYPE_TEXT,
                    StandardCharsets.ISO_8859_1
                )
            ),
            new TextIs(
                new JoinedText(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_OK,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_TEXT,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }
}
