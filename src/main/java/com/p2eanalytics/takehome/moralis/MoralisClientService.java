package com.p2eanalytics.takehome.moralis;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class MoralisClientService {

    public String moralisApiUrl = "https://deep-index.moralis.io/api/v2/";
    public String queryAvalancheChain = "?chain=avalanche";
    public String queryTransferTopic = "&topic=Transfer(address,address,uint256)";
    public String transferEventAbi = "{ \"anonymous\": false, \"inputs\": [ { \"indexed\": true, \"internalType\": \"address\", \"name\": \"from\", \"type\": \"address\" }, { \"indexed\": true, \"internalType\": \"address\", \"name\": \"to\", \"type\": \"address\" }, { \"indexed\": false, \"internalType\": \"uint256\", \"name\": \"value\", \"type\": \"uint256\" } ], \"name\": \"Transfer\", \"type\": \"event\"}";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public MoralisEventResponse fetchTransferEventPageByDate(String tokenAddress, Date from, Date to, Integer page){
        Integer offset = page * 500;
        String queryOffset = "&offset=" + offset;
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        String dateFrom = dateParser.format(from).replace(" ", "T");
        String dateTo = dateParser.format(to).replace(" ", "T");

        String queryDate = "&from_date="+dateFrom+"&to_date="+dateTo;

        String moralisQueryUrl =    moralisApiUrl +
                                    tokenAddress +
                                    "/events" +
                                    queryAvalancheChain +
                                    queryDate +
                                    queryTransferTopic +
                                    queryOffset;

        HttpPost post = new HttpPost(moralisQueryUrl);
        post.addHeader("accept", "application/json");
        post.addHeader("X-API-Key", "bBx9HjKGrE9YsXXvOPzZve059PXn7zfSkirMo1Q5aI4VixFWqMLLIorynM51gXcZ");
        post.addHeader("Content-Type", "application/json");

        try {
            StringEntity body = new StringEntity(transferEventAbi);
            post.setEntity(body);
        } catch (UnsupportedEncodingException e) {
            log.error("Error: "+e.getCause().getLocalizedMessage());
        }

        MoralisEventResponse eventResponse = new MoralisEventResponse();
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity result = response.getEntity();
            Gson gson = new Gson();
            eventResponse = gson.fromJson(EntityUtils.toString(result),MoralisEventResponse.class);

        } catch (ClientProtocolException e) {
            log.error("Error: "+e.getCause().getLocalizedMessage());
            return new MoralisEventResponse();
        } catch (IOException e) {
            log.error("Error: "+e.getCause().getLocalizedMessage());
            return new MoralisEventResponse();
        }

        return eventResponse;
    }
}
