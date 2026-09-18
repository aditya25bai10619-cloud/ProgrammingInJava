import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIClient {

    private static final String API_KEY =
            System.getenv("GROQ_API_KEY");

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    public EvaluationResult evaluate(
            String question,
            String studentAnswer,
            String referenceAnswer) {

        try {

            if (API_KEY == null || API_KEY.isBlank()) {

                return new EvaluationResult(
                        "GROQ_API_KEY is not set."
                );
            }

            String prompt =
                    "You are an academic evaluator.\n\n" +

                    "Question:\n" +
                    question + "\n\n" +

                    "Student Answer:\n" +
                    studentAnswer + "\n\n" +

                    "Reference Answer:\n" +
                    referenceAnswer + "\n\n" +

                    "Compare student answer with reference answer.\n\n" +

                    "Return EXACTLY in this format:\n" +

                    "SCORE: number\n" +
                    "CORRECTNESS: text\n" +
                    "STRENGTHS: text\n" +
                    "WEAKNESSES: text\n" +
                    "MISSING: text\n" +
                    "SUGGESTIONS: text";

            prompt = escape(prompt);

            String body =
                    "{"
                    + "\"model\":\"openai/gpt-oss-120b\","
                    + "\"messages\":["
                    + "{"
                    + "\"role\":\"user\","
                    + "\"content\":\"" + prompt + "\""
                    + "}"
                    + "],"
                    + "\"temperature\":0.2"
                    + "}";

            HttpRequest request =
                    HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header(
                            "Authorization",
                            "Bearer " + API_KEY
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(
                            HttpRequest.BodyPublishers
                            .ofString(body)
                    )
                    .build();

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                return new EvaluationResult(
                        "API Error: "
                        + response.statusCode()
                );
            }

            String content =
                    extractContent(response.body());

            return parse(content);

        } catch (Exception e) {

            return new EvaluationResult(
                    "Error: " + e.getMessage()
            );
        }
    }

    private String escape(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    private String extractContent(String response) {

        String key = "\"content\":\"";

        int start = response.indexOf(key);

        if (start == -1) {
            return "";
        }

        start += key.length();

        int end =
                response.indexOf("\"", start);

        if (end == -1) {
            return "";
        }

        return response
                .substring(start, end)
                .replace("\\n", "\n");
    }

    private EvaluationResult parse(String text) {

        int score =
                getNumber(text, "SCORE:");

        String correctness =
                getValue(text, "CORRECTNESS:");

        String strengths =
                getValue(text, "STRENGTHS:");

        String weaknesses =
                getValue(text, "WEAKNESSES:");

        String missing =
                getValue(text, "MISSING:");

        String suggestions =
                getValue(text, "SUGGESTIONS:");

        return new EvaluationResult(
                score,
                correctness,
                strengths,
                weaknesses,
                missing,
                suggestions
        );
    }

    private int getNumber(
            String text,
            String label) {

        try {

            return Integer.parseInt(
                    getValue(text, label)
                            .replaceAll("[^0-9]", "")
            );

        } catch (Exception e) {

            return 0;
        }
    }

    private String getValue(
            String text,
            String label) {

        int start =
                text.indexOf(label);

        if (start == -1) {
            return "Not Found";
        }

        start += label.length();

        int end =
                text.indexOf("\n", start);

        if (end == -1) {
            end = text.length();
        }

        return text.substring(start, end)
                .trim();
    }
}