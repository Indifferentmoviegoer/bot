package ru.tinkoff.telegram.bot.interfaces.clients;

import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

public interface ApiClientInterface {
    public JSONObject request(String method, RequestBody body) throws IOException;
    public JSONObject getResponse(Request request) throws IOException;
}
