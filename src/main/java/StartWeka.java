package main.java;

import java.io.File;
import java.util.Scanner;

public class StartWeka {

  public static void main(String[] args) throws Exception {
    System.out.println("===== SMS Spam Assassin =====\n");
    System.out.println("Welcome! Type 'h' or 'help' for a list of available commands");
    boolean running = true;
    boolean initialized = false;
    SMSClassifier evaluator = new SMSClassifier();

    while (running) {
      System.out.print("> ");
      Scanner scanner = new Scanner(System.in);
      String input = scanner.nextLine();
      String[] tokenized = input.split("\\s+");
      if (tokenized.length == 1) {
        if (tokenized[0].equalsIgnoreCase("h") || tokenized[0].equalsIgnoreCase("help")) {
          System.out.println("== Available Commands ==");
          System.out.println("- initialize [modelName]");
          System.out.println("    Initialize model name used by spam assassin");
          System.out.println("    # modelName = string");
          System.out.println("- train [trainingDataset]");
          System.out.println("    Start training for spam assassin based on the path of training data");
          System.out.println("    # trainingDataset = string");
          System.out.println("- evaluate [SMS]");
          System.out.println("    Generate a verdict toward the SMS input");
          System.out.println("    # SMS = string");
          System.out.println("- quit");
          System.out.println("    Exit the program\n");
        } else if (tokenized[0].equalsIgnoreCase("q") || tokenized[0].equalsIgnoreCase("quit")) {
          running = false;
        } else {
          System.out.println("ERROR: Invalid command\n");
        }
      } else if (tokenized.length >= 2) {
        if (tokenized[0].equalsIgnoreCase("i") || tokenized[0].equalsIgnoreCase("initialize") && tokenized.length == 2) {
          initialized = true;
          evaluator.setModelName("build/model/" + tokenized[1] + ".dat");
          File model = new File("build/model/" + tokenized[1] + ".dat");
          boolean modelExist = (model.exists() && !model.isDirectory());
          System.out.println("Initialization complete");
          System.out.println("Model is set to build/model/" + tokenized[1] + ".dat\n");
          if (!modelExist) {
            System.out.println("WARNING: New model selected, please use 'train' command before proceeding\n");
          }
        } else if (tokenized[0].equalsIgnoreCase("t") || tokenized[0].equalsIgnoreCase("train")) {
          if (initialized) {
            File trainData = new File("data/" + tokenized[1] + ".arff");
            if (trainData.exists() && !trainData.isDirectory()) {
              System.out.println("Training process using data from data/"+ tokenized[1] + ".arff");
              evaluator.train("data/" + tokenized[1] + ".arff");
            } else {
              System.out.println("ERROR: Training file data/" + tokenized[1] + ".arff not found\n");
            }
          } else {
            System.out.println("ERROR: Please initialize the program before proceeding.\n");
          }
        } else if (tokenized[0].equalsIgnoreCase("e") || tokenized[0].equalsIgnoreCase("evaluate")) {
          if (initialized) {
            StringBuilder SMS = new StringBuilder();
            for (int i=1; i<tokenized.length; i++) {
              SMS.append(tokenized[i]);
              SMS.append(" ");
            }
            System.out.println(SMS.toString());
            evaluator.evaluateSMS(SMS.toString());
          }
          else {
            System.out.println("ERROR: Please initialize the program before proceeding.\n");
          }
        }
        else {
          System.out.println("ERROR: Invalid command\n");
        }
      }
    }
    System.out.println("\nBye!");
  }
}
