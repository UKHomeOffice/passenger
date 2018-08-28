package org.gov.uk.homeoffice.digital.permissions.passenger.audit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLUtil.class);

    public static String encodeUtf8(final String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.warn("Unable to encode string " + string);
            return string;
        }
    }

    public static String decodeUtf8(final String string) {
        try {
            return string != null ? URLDecoder.decode(string, "UTF-8") : null;
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.warn("Unable to decode string " + string);
            return string;
        }
    }

}
