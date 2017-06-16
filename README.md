# SpamAssassin
A text classification program based on Weka that is able to identify spam messages.

## How To Use
Clone the whole project, and then run **RunWekaSpamAssassin.bat**. It should open a new terminal window with a shell-ish interface. Type 'h' or 'help' for further instructions.

## How It Works
The program uses a Naive Bayes learning algorithm by [**Weka**](http://www.cs.waikato.ac.nz/ml/weka/). Using supervised training, the SpamAssassin will learn which messages are spam and which ones are not. The algorithm would analyze these messages word by word, weighing each of them with a probability value. The value would later be used to determine whether the analyzed message is a spam.