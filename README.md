# Spam Assassin
A text classification program based on Weka that is able to identify spam messages.


## How It Works
The program uses a Naive Bayes learning algorithm by [**Weka**](http://www.cs.waikato.ac.nz/ml/weka/). Using supervised training, the SpamAssassin will learn which messages are spam and which ones are not. The algorithm would analyze these messages word by word, weighing each of them with a probability value. The value would later be used to determine whether the analyzed message is a spam.

The data set for training is stored in _data/_ with _arff_ format. Currently, only one training set is included in the folder – and it is for Indonesian spam messages. However, the Spam Assassin could classify messages from other languages too, given the appropriate training set. The data set should be in _arff_ format with the same attributes as _data/sms.arff_.

There are two attributes in the _arff_ file, **verdict** and **content**. Verdict contains information whether the content is _PositiveSpam_ or _NegativeSpam_. While the content is the message to be evaluated. In training phase, the Spam Assassin would learn through these two attributes. Individual values will be assigned to each words in the contents; each corresponding to its verdict.

In evaluation phase, the Spam Assassin would use the previously-gained information in training phase. It would then give its own _verdict_ towards the processed message.


## How To Use
Clone the bin folder, and then run **RunWekaSpamAssassin.bat**. It should open a new terminal window with a shell interface. Type 'h' or 'help' for further instructions. Further explanation about the program and its commands could be found in **doc/Bagian_B.pdf**.

A pre-existing model called _zero_ is saved in _model/_. It has been trained with data set _data/sms.arff_, which is in Indonesian. For a quick start, do the following:
* Clone the repo
* Run RunWekaSpamAssassin.bat
* Type _initialize zero_
* Type _evaluate_ followed with an Indonesian message.


### List Of Commands
* **help** – show details about available commands.
* **initialize** – specify which model to use. A warning will be shown if the specified model does not exist.
* **train** – select which data set to use for training and start training process. The model would be saved for future uses.
* **evaluate** – determine the category of the message that follows the keyword.
* **quit** – self explanatory.