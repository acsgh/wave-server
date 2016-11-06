package com.acs.waveserver.provider.jetty;

import static spark.Spark.*;

public class SparkJavaServer {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}