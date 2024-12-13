package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
    private NoticeRepository noticeRepo;

	@Value("${publishing.server.hostname}")
	private String hostname;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	public JsonObject postToNoticeServer(JsonObject notice) {
		String url = hostname + "/notice";

		RequestEntity<String> req = RequestEntity
				.post(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(notice.toString(), String.class);

		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = template.exchange(req, String.class);

		String payload = resp.getBody();
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject result = reader.readObject();

		return result;
	}

	// Task 4
	public void insertNotices(String redisKey, String value) {
		noticeRepo.insertNotices(redisKey, value);
	}

	// for health status check
	public boolean randomKeyObtained(){
		return noticeRepo.getRandomKey();
	}
}
