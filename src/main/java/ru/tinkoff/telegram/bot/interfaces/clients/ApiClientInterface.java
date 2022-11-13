package ru.tinkoff.telegram.bot.interfaces.clients;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public interface ApiClientInterface {
    public String request(String method, RequestBody body) throws IOException;
    public String getResponse(Request request) throws IOException;
}
