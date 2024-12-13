package vttp.batch5.ssf.noticeboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // Landing page, add empty object (Task 1)
    @GetMapping(path = { "/", "index.html" })
    public String getIndex(Model model) {
        model.addAttribute("notice", new Notice());

        return "notice";
    }

    // User clicks on Post (Task 2)
    // redisplay View 1 with error details OR display View 2/3
    @PostMapping("/notice")
    public String postNotice(
            @Valid @ModelAttribute("notice") Notice notice,
            BindingResult bindings, Model model) {

        if (bindings.hasErrors())
            return "notice";
        
        JsonObject result = noticeService.postToNoticeServer(notice.toJson());

        if (result.containsKey("id")) {
            String id = result.get("id").toString();
            System.out.println(id);
            String timestamp = result.get("timestamp").toString();
            noticeService.insertNotices(id, result.toString());
            model.addAttribute("id", id);
            return "view2";
        } else {
            String message = result.get("message").toString();
            String timestamp = result.get("timestamp").toString();

            if (message != null) {
                model.addAttribute("message", message);

                return "view3";
            }
        }
        return "notice";
    }

    // Spring Boot application health status (Task 6)
    @GetMapping("/status")
    public ResponseEntity<String> getHealthStatus() {
        if (noticeService.randomKeyObtained())
            return ResponseEntity.status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{}");

        return ResponseEntity.status(503)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
    }
}