package ru.tinkoff.telegram.bot.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tinkoff.telegram.bot.impl.clients.TinkoffGeoApiClient;
import ru.tinkoff.telegram.bot.impl.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final Logger log = Logger.getLogger(Bot.class.getName());

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

            while (true) {
                try {
                    for (String point : this.getAvailablePoints()) {
                        message.setText(point);
                        execute(message);
                    }

                } catch (IOException | TelegramApiException e) {
                    log.info(e.getMessage());
                    e.printStackTrace();
                }

                try {
                    int timePeriodInMinutes = Integer.parseInt(DOTENV.get("TIME_PERIOD_IN_MINUTES"));
                    Thread.sleep((long) timePeriodInMinutes * 60 * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> getAvailablePoints() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClusterFilter clusterFilter = configureClusterFilters();

        String requestBody = mapper.writeValueAsString(clusterFilter);
        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8"));

        TinkoffGeoApiClient apiClient = new TinkoffGeoApiClient();
        JSONObject jsonBody = apiClient.request(ApiMethodConstants.POST_REQUEST, body);

        JSONObject payload = jsonBody.getJSONObject("payload");
        JSONArray clusters = payload.getJSONArray("clusters");

        return getResult(clusters);
    }

    @NotNull
    private static ArrayList<String> getResult(JSONArray clusters) {
        ArrayList<String> messages = new ArrayList<>();

        for (Object cluster : clusters) {
            JSONObject clusterJson = (JSONObject) cluster;

            JSONArray points = clusterJson.getJSONArray("points");

            for (Object point : points) {
                JSONObject pointJson = (JSONObject) point;
                StringBuilder result = new StringBuilder();

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
                            .append(" \n");
                }
                messages.add(result.toString());
            }

        }

        return messages;
    }

    private ClusterFilter configureClusterFilters() {
        BottomLeft bottomLeft = configureBottomLeft();
        TopRight topRight = configureTopRight();
        Bounds bounds = configureBounds(bottomLeft, topRight);
        Filters filters = configureFilters();

        ClusterFilter clusterFilter = new ClusterFilter();
        clusterFilter.setBounds(bounds);
        clusterFilter.setFilters(filters);
        clusterFilter.setZoom(11);

        return clusterFilter;
    }

    @NotNull
    private static Filters configureFilters() {
        ArrayList<String> banks = new ArrayList<>();
        banks.add("tcs");

        ArrayList<String> currencies = new ArrayList<>();
        currencies.add("USD");

        Filters filters = new Filters();
        filters.setBanks(banks);
        filters.setShowUnavailable(false);
        filters.setCurrencies(currencies);

        return filters;
    }

    @NotNull
    private static Bounds configureBounds(BottomLeft bottomLeft, TopRight topRight) {
        Bounds bounds = new Bounds();
        bounds.setBottomLeft(bottomLeft);
        bounds.setTopRight(topRight);

        return bounds;
    }

    @NotNull
    private static TopRight configureTopRight() {
        TopRight topRight = new TopRight();
        topRight.setLat(45.14221738593136);
        topRight.setLng(39.53533922900391);

        return topRight;
    }

    @NotNull
    private static BottomLeft configureBottomLeft() {
        BottomLeft bottomLeft = new BottomLeft();
        bottomLeft.setLat(44.98991109584412);
        bottomLeft.setLng(38.43601977099608);

        return bottomLeft;
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