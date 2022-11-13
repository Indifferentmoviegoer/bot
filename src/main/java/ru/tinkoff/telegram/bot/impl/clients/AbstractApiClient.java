package ru.tinkoff.telegram.bot.impl.clients;

import okhttp3.*;
import org.json.JSONObject;
import ru.tinkoff.telegram.bot.interfaces.clients.ApiClientInterface;

import java.io.IOException;
import java.util.Objects;

abstract class AbstractApiClient implements ApiClientInterface {
    protected final OkHttpClient client;
    protected String url;

    public AbstractApiClient(String url) {
        this.url = url;
        this.client = new OkHttpClient();
    }

    public String request(String method, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(this.url)
                .method(method, body)
                .build();

        return this.getResponse(request);
    }

    public String request(String method) throws IOException {
        Request request = new Request.Builder()
                .url(this.url)
                .method(method, null)
                .build();

        return this.getResponse(request);
    }

    public String getResponse(Request request) throws IOException {
        Response response = this.client.newCall(request).execute();

        return Objects.requireNonNull(response.body()).string();
    }
}
