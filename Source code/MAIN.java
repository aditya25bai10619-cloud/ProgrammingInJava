import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("      AI ANSWER EVALUATOR");
        System.out.println("=================================");

        // Module 1: Input & Validation

        System.out.print("Enter Question: ");
        String question = scanner.nextLine();

        System.out.print("Enter Student Answer: ");
        String studentAnswer = scanner.nextLine();

        System.out.print("Enter Reference Answer: ");
        String referenceAnswer = scanner.nextLine();

        if (question.isBlank() ||
            studentAnswer.isBlank() ||
            referenceAnswer.isBlank()) {

            System.out.println("Error: All inputs are required.");
            return;
        }

        // Module 2: AI Evaluation

        AIClient client = new AIClient();

        EvaluationResult result =
                client.evaluate(
                        question,
                        studentAnswer,
                        referenceAnswer
                );

        // Module 3: Report Generation

        System.out.println("\n=================================");
        System.out.println("       EVALUATION REPORT");
        System.out.println("=================================");

        if (!result.isSuccessful()) {

            System.out.println(result.getError());

        } else {

            System.out.println("Score: "
                    + result.getScore() + "/100");

            System.out.println("\nCorrectness:");
            System.out.println(result.getCorrectness());

            System.out.println("\nStrengths:");
            System.out.println(result.getStrengths());

            System.out.println("\nWeaknesses:");
            System.out.println(result.getWeaknesses());

            System.out.println("\nMissing Concepts:");
            System.out.println(result.getMissingConcepts());

            System.out.println("\nSuggestions:");
            System.out.println(result.getSuggestions());
        }

        scanner.close();
    }
}