package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class contains the core of Spam Assassin. It includes the initialization of
 */
public class SMSClassifier {

  private Instances trainData;
  private FilteredClassifier classifier;
  private String modelName;

  /**
   * Initializes the filter, along with its tokenizer, and classifier. In this particular
   * Spam Assassin, the classifier uses NaiveBayes algorithm.
   *
   * @see <a href="https://en.wikipedia.org/wiki/Naive_Bayes_classifier">Wikipedia article about
   * naive bayes algorithm</a>
   */
  public SMSClassifier() {
    try {
      StringToWordVector filter = new StringToWordVector();
      filter.setDoNotOperateOnPerClassBasis(true);
      filter.setLowerCaseTokens(true);
      filter.setWordsToKeep(10000);

      WordTokenizer tokenizer = new WordTokenizer();
      tokenizer.setDelimiters(" .,;:\'\"!@#$%^&*()-+=1234567890[]\\/[]{}\r\n\t");
      filter.setTokenizer(tokenizer);

      classifier = new FilteredClassifier();
      classifier.setFilter(filter);
      classifier.setClassifier(new NaiveBayes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets <code>modelName</code> to be used.
   *
   * @param modelName File name for model.
   */
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  /**
   * Saves current <code>classifier</code> to a specific file for future uses.
   *
   * @param classifier Classifier to be saved.
   * @param fileName File name for model.
   */
  private void saveModel(FilteredClassifier classifier, String fileName) {
    try {
      ObjectOutputStream outStream = new ObjectOutputStream(
          new FileOutputStream(new File(fileName)));
      outStream.writeObject(classifier);
      outStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads a saved <code>classifier</code> from a specific file.
   *
   * @param fileName File name for existing model.
   * @return A previously saved classifier.
   */
  private FilteredClassifier loadModel(String fileName) {
    try {
      ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName));
      FilteredClassifier classifier = (FilteredClassifier) inStream.readObject();
      inStream.close();
      return classifier;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Loads training data from a specific data set.
   *
   * @param fileName File name for data set.
   */
  private void loadTrainData(String fileName) {
    try {
      BufferedReader buffRead = new BufferedReader(new FileReader(fileName));
      ArffReader arffRead = new ArffReader(buffRead);
      trainData = arffRead.getData();
      trainData.setClassIndex(0);
      buffRead.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Trains current <code>classifier</code> with a specific data set.
   *
   * @param dataFile File name for data set.
   */
  public void train(String dataFile) {
    try {
      loadTrainData(dataFile);
      classifier.buildClassifier(trainData);
      Evaluation eval = new Evaluation(trainData);
      eval.evaluateModel(classifier, trainData);
      System.out.println(eval.toSummaryString("\nTraining result\n======\n", true));
      System.out.println("Training process complete.");
      saveModel(classifier, modelName);
      System.out.println("Updated model saved to " + modelName + "\n");
    } catch (Exception e) {
      System.out.println("Training process failed.\n");
      e.printStackTrace();
    }
  }

  /**
   * Evaluates a string (<code>SMS</code>) and determines whether it belongs to spam or not.
   *
   * @param SMS A string containing to-be-evaluated message.
   */
  public void evaluateSMS(String SMS) {
    FilteredClassifier classifier = loadModel(modelName);
    ArrayList<String> possVal = new ArrayList<String>();
    possVal.add("PositiveSpam");
    possVal.add("NegativeSpam");

    Attribute verdict = new Attribute("verdict", possVal);
    Attribute content = new Attribute("content", (ArrayList<String>) null);
    ArrayList<Attribute> wekaAttr = new ArrayList<Attribute>();
    wekaAttr.add(verdict);
    wekaAttr.add(content);

    Instances instances = new Instances("Tested SMS", wekaAttr, 1);
    instances.setClassIndex(0);
    DenseInstance dense = new DenseInstance(2);
    dense.setValue(content, SMS);
    instances.add(dense);

    try {
      double result = classifier.classifyInstance(instances.instance(0));
      if (instances.classAttribute().value((int) result).equalsIgnoreCase("PositiveSpam")) {
        System.out.println("The message is detected as SPAM\n");
      } else {
        System.out.println("The message is detected as NOT SPAM\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
      e.printStackTrace();
    }
  }
}
