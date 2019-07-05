package com.acs.wave.utils;


public final class ExceptionUtils {
    private ExceptionUtils() {

    }

    public static void throwRuntimeException(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else {
            throw new RuntimeException(throwable);
        }
    }

    public static String stacktraceToHtml(Throwable throwable) {
        String result = "<html>";
        result += "<head>";
        result += "<title>Internal Server Error</title>";
        result += "</head>";
        result += "<body>";
        result += "<p>";
        result += stacktraceToHtmlInternal(throwable, false);

        Throwable cause = throwable.getCause();

        while (cause != null) {
            result += stacktraceToHtmlInternal(cause, true);
            cause = cause.getCause();
        }
        result += "</p>";
        result += "</body>";
        result += "</html>";
        return result;
    }

    private static String stacktraceToHtmlInternal(Throwable throwable, boolean causeThrowable) {
        String result = "";
        result += "<b>";
        if (causeThrowable) {
            result += "Caused by: ";
        }
        result += throwable.getClass().getName() + ":&nbsp;" + "</b>" + throwable.getMessage() + "<br/>\n";
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            result += "&nbsp;&nbsp;&nbsp;&nbsp;" + stackTraceElement + "<br/>\n";
        }
        return result;
    }
}
