
/*
 * Copyright (c) 2018. SiriusX Innovations d.o.o.
 */

package com.tm.siriusxi.http.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.lang.String.valueOf;
import static java.net.URI.create;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers;
import static java.net.http.HttpResponse.BodyHandlers.ofFile;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toList;

/**
 * This class contains a number of examples and recipes
 * that can be followed to perform common tasks using
 * the Java HTTP Client.
 *
 * @author mohamed_taman
 * @version 1.0
 * @since Java 11
 */
public final class HttpClientApp {


    private static Logger logger = Logger.getLogger(HttpClientApp.class.getName());
    private static HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {

        //runGeneralExample();

        //logger.info("\n");

        httpGetRequest();

        //logger.info("\n");

        //httpPostRequest();

        //asynchronousRequest();

        //asynchronousMultipleRequests();

    }

    static void runGeneralExample() {

        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "username",
                                "password".toCharArray());
                    }
                })
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .proxy(ProxySelector.getDefault())
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                .build();

        var request = newBuilder()
                .uri(create("https://postman-echo.com/get/"))
                .headers("IBM", "Developer", "Author", "Mohamed Taman")
                .version(HttpClient.Version.HTTP_1_1)
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        client.sendAsync(request, ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(logger::info)
                .join();
    }

    static void httpGetRequest() throws URISyntaxException, IOException, InterruptedException {

        URI httpURI = new URI("https://postman-echo.com/get?foo1=bar1&foo2=bar2");

        HttpRequest request = newBuilder(httpURI).GET()
                .headers("Accept-Enconding", "gzip, deflate").build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        String responseBody = response.body();

        int statusCode = response.statusCode();

        System.out.println(responseBody + ", " + statusCode);
    }

    static void httpPostRequest() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = newBuilder(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofString("This is expected to be sent back as part of response body."))
                .build();

        HttpResponse<String> response
                = client.send(request, BodyHandlers.ofString());

        logger.info(response.body());
    }

    static void asynchronousRequest() throws URISyntaxException {

        URI httpURI = new URI("http://jsonplaceholder.typicode.com/posts/1");

        HttpRequest request = newBuilder(httpURI).GET().build();

        var futureResponse = client.sendAsync(request,
                BodyHandlers.ofString());

        futureResponse.join();
    }

    static void asynchronousMultipleRequests() throws URISyntaxException {
        List<URI> targets = List.of(new URI("http://jsonplaceholder.typicode.com/posts/1"),
                new URI("http://jsonplaceholder.typicode.com/posts/2"));

        targets.stream()
                .map(target -> client
                        .sendAsync(
                                newBuilder(target)
                                        .GET()
                                        .build(), ofFile(Paths.get("base", target.getPath())))
                        .thenApply(response -> response.body())
                        .thenApply(path -> path.toFile()))
                .collect(toList()).forEach(future -> future.join());
    }


    /*
      The following are a number of examples and recipes that can be
      followed to perform common tasks using the Java HTTP Client

      Advanced examples - Synchronous Get
     */

    /**
     * Response body as a String
     * <p>
     * This example uses the ofString BodyHandler to convert the
     * response body bytes into a String.
     * <p>
     * A BodyHandler must be supplied for each HttpRequest sent.
     * The BodyHandler determines how to handle the response body,
     * if any.
     * <p>
     * The BodyHandler is invoked once the response status code and
     * headers are available, but before the response body bytes
     * are received.
     * <p>
     * The BodyHandler is responsible for creating the BodySubscriber
     * which is a reactive-stream subscriber that receives streams of
     * data with non-blocking back pressure.
     * <p>
     * The BodySubscriber is responsible for, possibly, converting the
     * response body bytes into a higher-level Java type.
     * <p>
     * The HttpResponse.BodyHandlers class provides a number of convenience
     * static factory methods for creating a BodyHandler.
     * <p>
     * A number of these accumulate the response bytes in memory until
     * it is completely received, after which it is converted into the
     * higher-level Java type, for example, ofString, and ofByteArray.
     * <p>
     * Others stream the response data as it arrives; ofFile,
     * ofByteArrayConsumer, and ofInputStream. Alternatively,
     * a custom written subscriber implementation can be provided.
     *
     * @param uri the uri to call
     * @throws Exception
     */
    static void get(String uri) throws Exception {

        HttpRequest request = newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, BodyHandlers.ofString());

        logger.info(response.body());
    }

    /**
     * Response body as a File
     *
     * @param uri
     * @throws Exception
     */
    static void getAsFile(String uri) throws Exception {

        HttpRequest request = newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<Path> response =
                client.send(request, BodyHandlers.ofFile(Paths.get("body.txt")));

        logger.info("Response in file:" + response.body());
    }

    // Asynchronous Get

    /**
     * Response body as a String
     * <p>
     * The asynchronous API returns immediately with a CompletableFuture
     * that completes with the HttpResponse when it becomes available.
     * CompletableFuture was added in Java 8 and supports composable
     * asynchronous programming.
     * <p>
     * The CompletableFuture.thenApply(Function) method can be used to map
     * the HttpResponse to its body type, status code, etc.
     *
     * @param uri
     * @return
     */
    static CompletableFuture<String> asyncGet(String uri) {

        HttpRequest request = newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Response body as a File
     *
     * @param uri
     * @return
     */
    static CompletableFuture<Path> asyncGetAsFile(String uri) {

        HttpRequest request = newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, BodyHandlers.ofFile(Paths.get("body.txt")))
                .thenApply(HttpResponse::body);
    }

    // Post

    /**
     * A request body can be supplied by an HttpRequest.BodyPublisher.
     * <p>
     * The above example uses the ofString BodyPublisher to convert the given
     * String into request body bytes.
     * <p>
     * The BodyPublisher is a reactive-stream publisher that publishes streams
     * of request body on-demand. HttpRequest.Builder has a number of methods
     * that allow setting a BodyPublisher; Builder::POST, Builder::PUT, and
     * Builder::method. The HttpRequest.BodyPublishers class has a number of
     * convenience static factory methods that create a BodyPublisher for common
     * types of data; ofString, ofByteArray, ofFile.
     * <p>
     * The discarding BodyHandler can be used to receive and discard the response
     * body when it is not of interest.
     *
     * @param uri
     * @param data
     * @throws Exception
     */
    static void post(String uri, String data) throws Exception {

        HttpRequest request = newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        HttpResponse<?> response = client.send(request, BodyHandlers.discarding());

        logger.info("Status code: ".concat(valueOf(response.statusCode())));
    }

    // Concurrent Requests

    /**
     * It's easy to combine Java Streams and the CompletableFuture API to issue
     * a number of requests and await their responses. The following example sends
     * a GET request for each of the URIs in the list and stores all the responses
     * as Strings.
     *
     * @param uris
     */
    static void getURIs(List<URI> uris) {

        ExecutorService executor = Executors.newFixedThreadPool(6);

        client = HttpClient.newBuilder().executor(executor).build();

        List<HttpRequest> requests = uris.stream()
                .map(HttpRequest::newBuilder)
                .map(reqBuilder -> reqBuilder.build())
                .collect(toList());

        CompletableFuture.allOf(requests.stream()
                .map(request -> client.sendAsync(request, ofString()))
                .toArray(CompletableFuture<?>[]::new))
                .join();

        /* Another implementation
        List<CompletableFuture<String>> futures = uris.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target).build(),
                                ofString())
                        .thenApply(response -> response.body()))
                .collect(Collectors.toList());
        */

    }

    // Get JSON

    /**
     * In many cases the response body will be in some higher-level format.
     * The convenience response body handlers can be used, along with a third-party
     * library to convert the response body into that format.
     * <p>
     * The following example demonstrates how to use the Jackson library,
     * in combination with BodyHandlers::ofString to convert a JSON response
     * into a Map of String key/value pairs.
     * <p>
     * The above example uses ofString which accumulates the response body bytes in memory.
     * Alternatively, a streaming subscriber, like ofInputStream could be used.
     *
     * @param uri
     * @return
     */
    static CompletableFuture<Map<String, String>> JSONBodyAsMap(URI uri) {

        UncheckedObjectMapper objectMapper = new UncheckedObjectMapper();

        HttpRequest request = newBuilder(uri)
                .header("Accept", "application/json")
                .build();

        return client
                .sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(objectMapper::readValue);
    }

    /**
     * In many cases the request body will be in some higher-level format.
     * The convenience request body handlers can be used, along with a third-party
     * library to convert the request body into that format.
     * <p>
     * The following example demonstrates how to use the Jackson library, in combination
     * with the BodyPublishers::ofString to convert a Map of String key/value pairs into JSON.
     *
     * @param uri
     * @param map
     * @return
     * @throws IOException
     */
    static CompletableFuture<Void> postJSON(URI uri, Map<String, String> map) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        HttpRequest request = newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client
                .sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(System.out::println);
    }

    // Post JSON

    /**
     * Response body as a String with a specified proxy
     * <p>
     * A ProxySelector can be configured on the HttpClient through the client's
     * Builder::proxy method. The ProxySelector API returns a specific proxy for
     * a given URI. In many cases a single static proxy is sufficient.
     * <p>
     * <p>
     * The ProxySelector::of static factory method can be used to create such a selector.
     * <p>
     * Alternatively, the system-wide default proxy selector can be used,
     * which is the default on macOS.
     * <p>
     * {@code
     *     HttpClient.newBuilder()
     *          .proxy(ProxySelector.getDefault())
     *          .build();
     * }
     *
     * @param uri
     * @return
     */
    static CompletableFuture<String> getWithProxy(String uri) {
        client = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress("www-proxy.com", 8080)))
                .build();

        var request = newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    static class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
        /**
         * Parses the given JSON string into a Map.
         */
        Map<String, String> readValue(String content) {
            try {
                return this.readValue(content, new TypeReference<>() {
                });
            } catch (IOException ioe) {
                throw new CompletionException(ioe);
            }
        }

    }

}
