package be.kpse.ohlc.feature.stock_ticker.service.sec;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SecServiceImpl implements SecService {

	@Value("${app.endpoints.tickers}")
	private String secUrl;

	private final RestTemplate restTemplate;

	@Override
	public void getAllStockTickers(Consumer<InputStream> streamConsumer) {

		RequestCallback requestCallback = request -> {
			HttpHeaders headers = request.getHeaders();
			headers.set(HttpHeaders.USER_AGENT,
					"Sample Company Name AdminContact@<sample company domain>.com");
			//			headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
			headers.set(HttpHeaders.HOST, "www.sec.gov");
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
		};

		final ResponseExtractor<?> responseExtractor = clientHttpResponse -> {
			streamConsumer.accept(clientHttpResponse.getBody());
			return null;
		};

		restTemplate.execute(secUrl, HttpMethod.GET, requestCallback, responseExtractor);

	}

}
