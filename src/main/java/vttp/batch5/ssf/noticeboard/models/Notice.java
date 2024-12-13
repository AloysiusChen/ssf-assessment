package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {
    @NotNull(message = "Notice title cannot be empty")
    @NotEmpty(message = "Notice title cannot be empty")
    @Size(min = 3, max = 128, message = "Notice title must be between 3 and 128 characters")
    private String title;

    @NotNull(message = "Email cannot be empty")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Must be a well-formed email address")
    private String poster;

    @NotNull(message = "Post date cannot be empty")
    @Future(message = "Post date must be a date in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;

    @Size(min = 1, message = "Must include at least 1 category")
    private List<String> categories;

    @NotNull(message = "Contents of the notice cannot be empty")
    @NotEmpty(message = "Contents of the notice cannot be empty")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Notice [title=" + title + ", poster=" + poster + ", postDate=" + postDate + ", categories=" + categories
                + ", text=" + text + "]";
    }

    // Notice -> JSON string
    public JsonObject toJson() {
        long millis = this.getPostDate().getTime();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String category : this.categories) {
            arrayBuilder.add(category);
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("title", this.getTitle())
                .add("poster", this.getPoster())
                .add("postDate", millis)
                .add("categories", arrayBuilder.build())
                .add("text", this.getText())
                .build();

        return jsonObject;
    }
}