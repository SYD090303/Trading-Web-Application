package com.project.TradingWebApp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TradingWebApp.entity.Coin;
import com.project.TradingWebApp.repository.CoinRepository;
import com.project.TradingWebApp.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CoinService interface that provides methods for fetching
 * cryptocurrency data from the CoinGecko API and storing relevant information in the database.
 */
@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Fetches a list of coins with pagination.
     * @param page The page number to fetch.
     * @return A list of Coin objects.
     * @throws Exception If an error occurs while fetching data.
     */
    @Override
    public List<Coin> getCoins(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>() {});
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Fetches market chart data for a specific coin over a given period.
     * @param coinId The ID of the coin.
     * @param days The number of days for historical data.
     * @return The market chart data in JSON format.
     * @throws Exception If an error occurs while fetching data.
     */
    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Fetches details of a specific coin and saves it to the database.
     * @param coinId The ID of the coin.
     * @return The coin details in JSON format.
     * @throws Exception If an error occurs while fetching data.
     */
//    @Override
//    public String getCoinDetails(String coinId) throws Exception {
//        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//            JsonNode node = objectMapper.readTree(response.getBody());
//
//            Coin coin = new Coin();
//            coin.setId(node.get("id").asText());
//            coin.setName(node.get("name").asText());
//            coin.setSymbol(node.get("symbol").asText());
//            coin.setImage(node.get("image").get("large").asText());
//
//            JsonNode marketData = node.get("market_data");
//            coin.setCurrentPrice(marketData.get("current_price").asDouble());
//            coin.setMarketCap(marketData.get("market_cap").asLong());
//            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
//            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
//            coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
//            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
//            coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
//            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
//            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
//            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
//            coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());
//            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
//            coinRepository.save(coin);
//            return response.getBody();
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            System.out.println(e.getMessage());
//            throw new Exception(e.getMessage());
//        }
//    }
    @Override
    public String getCoinDetails(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode node = objectMapper.readTree(response.getBody());

            Coin coin = new Coin();
            coin.setId(Optional.ofNullable(node.get("id")).map(JsonNode::asText).orElse(null));
            coin.setName(Optional.ofNullable(node.get("name")).map(JsonNode::asText).orElse(null));
            coin.setSymbol(Optional.ofNullable(node.get("symbol")).map(JsonNode::asText).orElse(null));

            JsonNode imageNode = node.get("image");
            coin.setImage(Optional.ofNullable(imageNode).flatMap(img -> Optional.ofNullable(img.get("large"))).map(JsonNode::asText).orElse(null));

            JsonNode marketData = node.get("market_data");
            if (marketData != null) {
                JsonNode currentPriceNode = marketData.get("current_price");
                if (currentPriceNode != null && currentPriceNode.get("usd") != null) { // Check for USD price
                    coin.setCurrentPrice(currentPriceNode.get("usd").asDouble());
                }

                coin.setMarketCap(Optional.ofNullable(marketData.get("market_cap")).map(JsonNode::asLong).orElse(0L));
                coin.setMarketCapRank(Optional.ofNullable(marketData.get("market_cap_rank")).map(JsonNode::asInt).orElse(0));

                JsonNode totalVolumeNode = marketData.get("total_volume");
                if (totalVolumeNode != null && totalVolumeNode.get("usd") != null) { // Check for USD volume
                    coin.setTotalVolume(totalVolumeNode.get("usd").asLong());
                }

                JsonNode high24hNode = marketData.get("high_24h");
                if (high24hNode != null && high24hNode.get("usd") != null) {
                    coin.setHigh24h(high24hNode.get("usd").asDouble());
                }

                JsonNode low24hNode = marketData.get("low_24h");
                if (low24hNode != null && low24hNode.get("usd") != null) {
                    coin.setLow24h(low24hNode.get("usd").asDouble());
                }

                coin.setPriceChange24h(Optional.ofNullable(marketData.get("price_change_24h")).map(JsonNode::asDouble).orElse(0.0));
                coin.setPriceChangePercentage24h(Optional.ofNullable(marketData.get("price_change_percentage_24h")).map(JsonNode::asDouble).orElse(0.0));
                coin.setMarketCapChange24h(Optional.ofNullable(marketData.get("market_cap_change_24h")).map(JsonNode::asLong).orElse(0L));
                coin.setMarketCapChangePercentage24h(Optional.ofNullable(marketData.get("market_cap_change_percentage_24h")).map(JsonNode::asDouble).orElse(0.0));

                JsonNode totalSupplyNode = marketData.get("total_supply");
                if (totalSupplyNode != null) {
                    coin.setTotalSupply(totalSupplyNode.asLong());
                }

            }


            coinRepository.save(coin);
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Error parsing JSON for coinId: " + coinId + ".  Check API response.");
            e.printStackTrace(); // Print the full stack trace for debugging
            throw new Exception("Error parsing JSON. Check API response for coinId: " + coinId); // Re-throw with a more informative message
        }
    }

    /**
     * Finds a coin by its ID in the database.
     * @param coinId The ID of the coin.
     * @return The Coin object.
     * @throws Exception If the coin is not found.
     */
    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> coin = coinRepository.findById(coinId);
        if (coin.isEmpty()) throw new Exception("Coin not found");
        return coin.get();
    }

    /**
     * Searches for a coin using a keyword.
     * @param keyword The search keyword.
     * @return The search results in JSON format.
     * @throws Exception If an error occurs while fetching data.
     */
    @Override
    public String searchCoin(String keyword) throws Exception {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Fetches the top 50 coins by market capitalization.
     * @return The top 50 coins data in JSON format.
     * @throws Exception If an error occurs while fetching data.
     */
    @Override
    public String getTop50CoinsByMarketCapRank() throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }
    /**
     * Fetches the currently trending coins.
     *
     * @return A JSON string containing trending coins.
     * @throws Exception If an error occurs during retrieval.
     */
    @Override
    public String getTreadingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trending";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }
}
