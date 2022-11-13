package ru.tinkoff.telegram.bot.impl.clients;

import io.github.cdimascio.dotenv.Dotenv;

public class TinkoffGeoApiClient extends AbstractApiClient {
    private static final Dotenv DOTENV = Dotenv.load();
    public TinkoffGeoApiClient() {
        super(DOTENV.get("TINKOFF_GEO_API_URL"));
    }
}
