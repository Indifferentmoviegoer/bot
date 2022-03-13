package com.tt.bot;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger log = Logger.getLogger(Bot.class.getName());

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

//            while (true) {
                try {
                    message.setText(this.getAvailablePoints());
                } catch (IOException e) {
                    e.printStackTrace();

                    log.info(e.getMessage());
                }

                try {

                    execute(message);

                } catch (TelegramApiException e) {
                    e.printStackTrace();

                    log.info(e.getMessage());
                }

//                try {
//                    Thread.sleep(600000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public String getAvailablePoints() throws IOException {
        String pointsInfo = "Ничего не найдено :(";

        OkHttpClient client = new OkHttpClient();

        String json = "{\"bounds\":{\"bottomLeft\":{\"lat\":44.98991109584412,\"lng\":38.43601977099608},\"topRight\":{\"lat\":45.14221738593136,\"lng\":39.53533922900391}},\"filters\":{\"banks\":[\"tcs\"],\"showUnavailable\":true,\"currencies\":[\"USD\"]},\"zoom\":11}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url("https://api.tinkoff.ru/geo/withdraw/clusters")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String textResponse = Objects.requireNonNull(response.body()).string();

        JSONObject jsonBody = new JSONObject(textResponse);
        JSONObject payload = jsonBody.getJSONObject("payload");
        JSONArray clusters = payload.getJSONArray("clusters");

        StringBuilder result = new StringBuilder();
        for (Object cluster : clusters) {
            JSONObject clusterJson = (JSONObject) cluster;

            JSONArray points = clusterJson.getJSONArray("points");

            for (Object point : points) {
                JSONObject pointJson = (JSONObject) point;

                result
                        .append("Банк: ")
                        .append(pointJson.getJSONObject("brand").getString("name"))
                        .append(" \n")
                        .append("Адрес: ")
                        .append(pointJson.getString("address"))
                        .append(" \n");

                JSONArray limits = pointJson.getJSONArray("limits");

                for (Object limit : limits) {
                    JSONObject limitJson = (JSONObject) limit;

                    result
                            .append("Валюта: ")
                            .append(limitJson.getString("currency"))
                            .append(" \n")
                            .append("Лимиты: ")
                            .append(limitJson.getBigInteger("max"))
                            .append(" \n")
                            .append("Номиналы: ")
                            .append(limitJson.getJSONArray("denominations"))
                            .append(" \n")
                            .append("Доступная сумма: ")
                            .append(limitJson.getBigInteger("amount"))
                            .append(" \n\n");
                }
            }
        }

        if (!result.toString().isEmpty()) {
            pointsInfo = result.toString();
        }

        return pointsInfo;
    }

    @Override
    public String getBotUsername() {
        return DOTENV.get("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return DOTENV.get("BOT_TOKEN");
    }
}