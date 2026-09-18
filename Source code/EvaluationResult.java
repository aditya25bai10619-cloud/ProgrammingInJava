public class EvaluationResult {

    private int score;
    private String correctness;
    private String strengths;
    private String weaknesses;
    private String missingConcepts;
    private String suggestions;

    private boolean success;
    private String error;

    public EvaluationResult(
            int score,
            String correctness,
            String strengths,
            String weaknesses,
            String missingConcepts,
            String suggestions) {

        this.score = score;
        this.correctness = correctness;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.missingConcepts = missingConcepts;
        this.suggestions = suggestions;

        this.success = true;
    }

    public EvaluationResult(String error) {

        this.error = error;
        this.success = false;
    }

    public int getScore() {
        return score;
    }

    public String getCorrectness() {
        return correctness;
    }

    public String getStrengths() {
        return strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public String getMissingConcepts() {
        return missingConcepts;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public boolean isSuccessful() {
        return success;
    }

    public String getError() {
        return error;
    }
}