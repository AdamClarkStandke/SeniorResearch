**Adam Standke**

**(Advisor/Mentor) Dr. C. Dudley Girard** 

# DOES A NEWS ARTICLE EXIST WITHIN A WEB PAGE?
### Abstract
This paper and repository focuses on the problem of identifying whether a web page contains a news article. Contained within this paper is background information about how machine learning algorithms combined with information extraction algorithms and heuristics are able to identify online news articles. This paper also contains an experimental design along with a proposed solution. Lastly, this paper contains the results of an experiment that was run to determine whether the proposed solution solved the problem of identifying online news articles. This github repository contains all phases of the project. At the end this paper each directory of the repository will be explained more fully (ie., linking sections of this paper with the github repository).

### 1. Introduction
Currently, many news based websites include content other than news articles. Often times, a news web page contains no articles at all but other forms of content such as videos, images, and/or slides. Consequently, identifying news articles by automatic means is a nontrivial task. Simple heuristics that may have worked to identify news articles in the early days of the web have become outdated in today’s web landscape. In order to effectively solve the problem of identifying news articles a multidisciplinary approach must be taken. This project takes such an approach by combining fields of information retrieval, data mining, and machine learning to solve this classification task. 

In doing so, this project will use the following definition of an article to classify online news articles: an article is a contiguous, coherent work of prose on a single topic or multiple closely related topics that alone comprises the main informational content of the page[6]. For the news domain, in addition to the general requirements for an article, a news article must be a story or report at least two paragraphs and eight total sentences in length[6].    

### 2. Literature Review
Topics that deal with information retrieval, data mining, and machine learning will be subsequently discussed. These topics include the following: link-target identification, analysing the content of web pages, and the C4.5 machine learning algorithm.   

### 2.1 Link-Target Identification
The link analysis field studies the relationship between web pages[1]. An area of research that really flourished after the proposal of the page rank algorithm[1]. The page rank algorithm assesses the importance of a web page according to the number of external links that point to it[1]. However, the page rank algorithm does not classify the content of web pages based on the structure of links[1]. In link target identification several features are extracted from the root link within a web page[1]. The features that are extracted are then used to determine whether the root link points to relevant content (eg., other news web pages that contain articles) or irrelevant content (eg., advertisement web pages)[1].

To create such a classification, six specific features based on the link structure are used[1]. The first feature checks to see if the link contains a number/id in the name of the link[1]. Links related to news usually have an id in their structure[1]. 

For example, a hyperlink such as https://abcnews.go.com/story?id=62025268 would be classified as a news article since its link contains a number and an id variable. The second feature checks to see if the link contains a date[1]. In general it is more likely that a news article contains a date in its link, more so than advertising and/or navigation menus[1]. The third feature  judges whether a link contains a news article by measuring a link’s length; generally, longer link structures usually represent news web pages, since the title of a news article is oftentimes contained within the link[1]. 

The fourth feature leverages the fact that navigation menus usually end with a slash mark[1]. For example, a hyperlink such as https://news.yahoo.com/ would be classified as a navigation type object since the link ends with a forward slash mark.  For the fifth feature a number of reserved words are used to filter out multimedia news[1]. The words gallery, video, image, photo, slideshow, episode, and player are a list of reserved words that the fifth feature takes into account to determine whether the web page pointed to by such a link represents irrelevant content[1]. The last feature counts the number of slashes in the link address which indicates the location of a target web page within a file system[1]. 

### 2.2 Analyzing the Content of Web Pages
This form of analysis consists of examining the content within a web page. By transforming the content of a web page into a document object model tree (ie., DOM tree), the content of a web page can be efficiently traversed and extracted by different information retrieval algorithms. One such algorithm that uses a heuristic based approach to extract online news articles is CoreEx.

### 2.2.1 CoreEx
CoreEx analyzes the amount of text and number of links of every node in the DOM tree and then applies a heuristic to determine the node (or set of nodes) that most likely contains the main content[3]. For every node in the DOM tree, CoreEx maintains a set of nodes S which stores a subset of the node’s children and calculates four counts: textCnt, linkCnt, setTextCnt, and setLinkCnt[3]. TextCnt holds the number of words while linkCnt holds the number of links contained in one node[3]. To keep the score normalized between 0 and 1 a link is counted as one word of text[3]. SetTextCnt and setLinkCnt are the sum of textCnt and linkCnt[3]. CoreEx sets these two values only if the text-to-link ratio of a node’s child is above a certain threshold[3].

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/CoreExalgo.png)

The CoreEx algorithm can be split up into two parts; namely, nodes that are terminal and nodes that are non-terminal[3]. Both parts of the algorithm are shown in figures 1 and 2[3]. Figure 1 details CoreEx’s handling of terminal nodes (ie., T). If T is a text node (ie., only contains text) its textCnt is determined by how many words are contained in T[3]. In all other cases where T is not a text node a value of 1 or 0 is assigned to it[3]. Figure 2 details CoreEx’s handling of non-terminal nodes (ie., N). Initially, N’s textCnt, linkCnt, and the set S are empty[3]. Then for each child of N, N’s textCnt and linkCnt are updated[3]. If a child of N satisfies a pre-set threshold (which has been empirically determined to be 0.9) the child will be added to the set S and setTextCnt and setLinkCnt will be updated for N[3]. This process repeats until all non-terminal nodes have been evaluated[3]. 

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/CoreExscoring.png)

Once the counts are known for each non-terminal node, the nodes are scored based on their values for setTextCnt and setLinkCnt[3]. Figure 3 details CoreEx’s scoring function for non-terminal nodes (ie., N). PageText is the total amount of text that the webpage contains[3]. WeightRatio and weightText are weights assigned to the function which have been experimentally determined to be 0.99 for weightratio and 0.01 for weightText[3]. Using this scoring function the node with the highest score is picked as the main content node and the corresponding set of nodes within the set S contains the news article[3].

### 2.3 The C4.5 Machine Learning Algorithm
C4.5 is a machine learning algorithm that constructs classification models[4]. The classification task consists of assigning things to categories or classes as determined by their properties[4]. In doing so, C4.5 constructs classification models through induction by generalizing from specific examples[4]. C4.5 is a descendant of an earlier machine learning algorithm called ID3, however, C4.5 provides additional capabilities[4]. In its simplest form, C4.5 generates a classification model in the form of a decision tree, where each leaf indicates a particular class and each non-leaf represents a decision node that specifies some test to be carried out on an attribute[4]. To construct a decision tree C4.5 relies on partitioning a set of training cases T into subsets of cases until a single-class collection of cases is achieved[4]. 

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image5.png)

**Figure 4**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image6.png)

**Figure 5**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image7.png)

**Figure 6**

Similar to the ID3 algorithm, C4.5 relies on a criteria called gain to effectively partition the training cases[4]. The information theory that underpins the gain criterion relies on the notion that the information conveyed by a message depends on its probability and can be measured in bits[4]. Figures 4, 5, and 6 detail C4.5’s calculation of gain.

Figure 4 shows the function that is used to compute the (im)purity of an arbitrary collection of training examples[2]. This function when applied to a set of training cases measures the average amount of information needed to identify the class of a training case in T[4]. The quantity ![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image8.png)calculates the number of training cases in T that belong to class ![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image9.png)[4]. The other function, shown by figure 5 represents the information gained after partitioning a set of training cases by a certain attribute[4]. Each ![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image10.png)is a subset of T for which attribute X has a value i[2]. Together, both these functions make up the gain criterion as shown by figure 6[4]. The gain criterion will select attributes that maximize the function[4]. In doing so, the  set of training cases are partitioned into a corresponding decision tree[4].

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image11.png)

**Figure 7**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image12.png)

**Figure 8**

C4.5 improves upon the gain criterion by introducing an additional gain criteria called gain ratio[4]. The gain criterion is inherently biased, since it favors attributes with many different outcomes[4]. The bias inherent in the gain criterion is rectified by C4.5 through a normalization procedure in which the apparent gain attributable to an attribute with many outcomes is adjusted[4]. Figures 7 and 8 show C4.5’s calculation of the gain ratio criterion[4]. Figure 7 calculates the split information for an attribute X. ![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image13.png)through ![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image14.png)represents n different subsets of T, after being partitioned by n different values of attribute X[2]. After doing so, figure 8 expresses the proportion of information generated by the attribute that is useful for classification[4]. 

In addition to C4.5’s gain ratio, three additional options of C4.5 seem to improve its classification performance; namely, retrospective pruning, biased windowing, and discrete attribute-value grouping[4]. For retrospective pruning, C4.5 modifies the recursive partitioning of the training cases by removing, retrospectively, some of the original structure of the original decision tree[4]. Generally, this is done by replacing subtrees with leaves by comparing the total number of predicted errors[4]. Even though C4.5 cannot exactly determine the probability of a predicted error at a given leaf, it is able to bound an upper limit on this probability based on a given confidence level[4]. C4.5 then equates the predicted error rate with this upper limit to then compute the total number of predicted errors[4]. By removing parts of the tree that do not contribute to the accuracy of unseen cases, a pruned tree generally has a lower error rate on unseen cases than an un-pruned tree[4].

Under windowing, C4.5 biases the selection of training cases (called the window) from which the initial decision tree is constructed from[4]. C4.5 biases the selection by making sure the distribution of classes in the initial window is as uniform as possible[4]. This type of windowing not only supports faster construction of decision trees, but also more accurate trees[4]. Windowing allows multiple trees to be grown and the tree with the lowest predicted error rate is chosen[4].

Even though the gain ratio criterion usually leads to good choices when splitting attributes, the criterion is biased when it comes to attributes with many values[4]. To combat this bias, value groups can be created in which a collection of attribute values rather than a single value are associated with a single decision tree branch[4]. To find reasonable groupings of attribute values from the data itself, C4.5 uses a greedy approach of iteratively merging value groups[4]. Initially, value groups are just the individual values of the attribute under consideration, and at each cycle, C4.5 evaluates the consequences of merging every pair of groups[4]. The process continues until just two value groups remain, or until no such merger would produce a better partition of the training cases[4]. The merging of attribute values results in fewer subsets of training cases and a corresponding reduction in the split info criterion[4]. Which may lead to a higher gain ratio value[4]. 

### 3. Primary Objective
The primary objective of this project is to assess the accuracy of identifying news articles within a web page. By constructing decision trees using the C4.5 machine learning algorithm from features extracted from link-target identification and CoreEx a simple binary classifier will be constructed to identify online news articles.

### 4.  Solution Description 
To achieve the primary objective several tools will be used for this project. First, the Python language will be used to construct a Selenium WebDriver to make direct calls to the FireFox web browser running in headless mode. The WebDriver will download fully rendered web pages as they would appear on FireFox’s web browser. After each web page has been downloaded, the lxml toolkit will be used to remove scripts from the file.

Once all of the web pages have been downloaded and cleaned, a DOM tree parser will be constructed using the Java library called JSoup. JSoup is an application programming interface that allows HTML files to be parsed and manipulated using the DOM tree.Through JSoup, the Java language, and the Eclipse Integrated Development Environment (IDE), link-target identification and CoreEx will be implemented and applied to each downloaded web page.

Lastly, the eighth release of the C4.5 machine learning program will be used to construct decision trees from the information gathered after running link-target identification and CoreEx.  Windows Visual Studio Integrated Development Environment (IDE) along with the C programming language, and the C-Shell command line interpreter will be used to interact with the C4.5 program. Furthermore, the command line utility program called shuf will  shuffle the data after each trial.

### 5. Hypothesis with Goal Tree
This project proposes two hypotheses. Hypothesis one consists of the following claim: the decision tree constructed from link-target identification will perform better than CoreEx at identifying news articles. Alternatively, the null hypothesis is that the decision tree constructed from link-target identification will have the same performance at identifying news articles as the decision tree constructed from CoreEx.  

The second hypothesis makes the following claim: the decision tree constructed from features extracted from link-target identification and CoreEx will perform better at identifying news articles than the individual decision trees constructed from only CoreEx or link-target identification. Alternatively, the null hypothesis is that the decision tree constructed from features extracted from link-target identification and CoreEx will have the same performance at identifying news articles as the individual decision trees constructed from only CoreEx or link-target identification. 

![Goal Tree](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/GoalTreefinalProject.png)

### 6. Experimental Design
The dataset will contain a total of 344 fully rendered web pages. Using the technique of cross-validation, 344 web pages will be randomly shuffled and split up into 10 groups. Of the ten groups, nine will be used to train the model and one will be used to test the model for each round. After ten rounds of training and testing, the accuracy score for a given trial will be the average of the ten accuracy scores obtained during cross-validation.

![Table1](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/ExperimentDesign.png)

![Table2](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/experimentBlock.png)

The web pages that will make up the dataset span from 2018-19 and consist of the following popular news and non-news based websites: CNN, Yahoo News, Food Network, Reddit, Fox News, BBC, The Verge, Huffington Post, ABC News, NBC News, DW news, and CBS News. Each web page will be manually labeled, with approximately 49% being labeled as an article and approximately 51% being labeled as not an article. Table 1 shows the experiment factors and Table 2 shows the experiment block design.


![boxplot](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/LinkTuning.png)![boxplot](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/CoreExTuning.png)![boxplot](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/CombinedTuning.png)

**Figure 9: The black solid line represents the median. The red stars represent the lower and upper extremes of each notch.
The blue circles represent the error rates(in percentage form).**

### 7. Results

Before running the twenty-trials on the data, the decision trees were hand tuned beforehand to take advantage of C4.5’s additional options (as detailed in sub-section 2.3 of the literature review). C4.5 has many options that can be used to construct decision trees. However some of C4.5’s options such as weighting and confidence-level adjustment were not further explored since the data for the project did not contain a lot of noise and the actual error rate was lower than the predicted error rate for each pruned decision tree. Figure 9  shows the different notched boxplots that were constructed after one trial of 10 fold cross-validation was run for each option.

For link-target identification, because all of the notches for the boxplots overlapped the median error rate was likely the same for each option. Nevertheless, the boxplot that used the combined options of windowing and discrete value grouping had the smallest upward spread from the median. Furthermore, this boxplot was the only boxplot in which its maximum value was equal to its upper quartile, which meant that every sample data point had an error rate less than 10%. Because of these two facts, pruned decision trees constructed from link-target identification used the options of discrete value grouping and windowing for the twenty trials.

For CoreEx, the boxplot that used the option of discrete value grouping and the boxplot that used the options of discrete value grouping and windowing were the only two boxplots whose notches did not overlap with the notches of the default boxplot.  Accordingly, these two boxplots were examined further. The probability of getting an error rate lower than the median error rate was high for the boxplot that used the the option of discrete value grouping, since it had a much larger spread in the downward direction and its minimum value equaled its lower quartile. Because of these two facts, pruned decision trees constructed from CoreEx used the option of discrete value grouping for the twenty trials.

For the combination of CoreEx and link-target identification, because all of the notches for the boxplots overlapped the median error rate was likely the same for each option. Nevertheless, the probability of getting error rates lower than 6% was high with the boxplot that used the option of windoing, since its maximum value was very close to its upper quartile. Thus, for pruned decision trees constructed from both CoreEx and link-target identification windowing was chosen as the option to use for the twenty trials. 

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/Boxplots_Tuned_Default_DecisionTreeModels/Tuned_Pruned_Accurary.png)

**Figure 10: The legend shows the options used during the twenty trials and the dotted horizontal line represents the median value for the pruned decision trees constructed from CoreEx and link-target identification.**

<table class="c43 c49"><tbody><tr class="c26"><td class="c31" colspan="1" rowspan="1"><p class="c23"><span class="c3 c4 c6">Statistics</span></p></td><td class="c14" colspan="1" rowspan="1"><p class="c18"><span class="c3 c4 c6">LinkTarget </span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c18"><span class="c3 c4 c6">CoreEx</span></p></td><td class="c16" colspan="1" rowspan="1"><p class="c18"><span class="c3 c4 c6">LinkTarget and CoreEx</span></p><p class="c18"><span class="c3 c4 c6">(combined)</span></p></td></tr><tr class="c26"><td class="c31" colspan="1" rowspan="1"><p class="c23"><span class="c3 c1">Median</span></p></td><td class="c14" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">94.15%</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">95.9%</span></p></td><td class="c16" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">97.1%</span></p></td></tr><tr class="c26"><td class="c31" colspan="1" rowspan="1"><p class="c23"><span class="c3 c1">Mean</span></p></td><td class="c14" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">94.065%</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">95.83%</span></p></td><td class="c16" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">96.915%</span></p></td></tr><tr class="c26"><td class="c31" colspan="1" rowspan="1"><p class="c23"><span class="c3 c1">Standard Error of the Mean</span></p></td><td class="c14" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.1024%</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.0965%</span></p></td><td class="c16" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.1473%</span></p></td></tr><tr class="c26"><td class="c31" colspan="1" rowspan="1"><p class="c23"><span class="c3 c1">Standard Deviation</span></p></td><td class="c14" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.4579%</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.4317%</span></p></td><td class="c16" colspan="1" rowspan="1"><p class="c18"><span class="c3 c1">0.6587%</span></p></td></tr></tbody></table>

**Table 3**

Figure 10 shows the overall accuracy of the decision trees over twenty trials and table 3 contains the descriptive statistics for the twenty trials. Figure 10 and table 3 both show that the decision trees constructed from CoreEx and link-target identification had the highest mean accuracy score of 96.92%
(SEM=.15%, SD=.66% ).Furthermore, figure 10 and table 3 also show that decision trees constructed from CoreEx had the second highest mean accuracy score of 95.83%
(SEM=.10%, SD=.43%).In terms of variability, the decision trees constructed from CoreEx and link-target identification had the highest variability with a standard deviation of approximately .66%.

<table class="c49 c50"><tbody><tr class="c39"><td class="c34" colspan="1" rowspan="1"><p class="c15 c11"><span class="c3 c4 c6"></span></p></td><td class="c22" colspan="1" rowspan="1"><p class="c10"><span class="c3 c4 c6">Z Test</span></p></td><td class="c32" colspan="1" rowspan="1"><p class="c10"><span class="c3 c4 c6">Alpha</span></p><p class="c10"><span class="c3 c1">(0.05)</span></p></td><td class="c2" colspan="1" rowspan="1"><p class="c10"><span class="c4 c6">Reject or Fail to </span><span class="c4 c6">reject Null</span><span class="c4 c6">&nbsp;Hypothesis </span><span class="c3 c1">(one-tailed testing)</span></p></td></tr><tr class="c26"><td class="c34" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">CoreEx </span></p><p class="c15"><span class="c3 c1">vs. LinkTarget </span></p></td><td class="c22" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">|-12.13|</span></p></td><td class="c32" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">1.645</span></p></td><td class="c2" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">Reject null hypothesis since,| -12.13| &nbsp;&gt; 1.645</span></p></td></tr><tr class="c26"><td class="c34" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">CoreEx </span></p><p class="c15"><span class="c3 c1">vs. (combined)</span></p></td><td class="c22" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">5.21</span></p></td><td class="c32" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">1.645</span></p></td><td class="c2" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">Reject null hypothesis since, 5.21 &gt; 1.645</span></p></td></tr><tr class="c26"><td class="c34" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">LinkTarget vs. (combined)</span></p></td><td class="c22" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">16.26</span></p></td><td class="c32" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">1.645</span></p></td><td class="c2" colspan="1" rowspan="1"><p class="c15"><span class="c3 c1">Reject null hypothesis since, 16.26 &gt; 1.645</span></p></td></tr></tbody></table>

**Table 4**

In order to determine whether the different mean accuracies of table 3 were statistically significant, hypothesis testing was carried out using the mean folds test[5]. The mean folds test first averages the cells for a single 10 fold cross-validation experiment and considers these averages as samples[5]. These averages are known to be better estimates of accuracy[5]. Table 4 contains the results of conducting mean folds testing. Table 4 shows that the null hypothesis for hypothesis one and two can be rejected. Furthermore, after examining the results of  table 3, table 4, and figure 10, it can be concluded that the alternative hypothesis for hypothesis two can be accepted. Thus, the decision tree constructed from features extracted from link-target identification and CoreEx performed better at identifying news articles than the individual decision trees constructed from only CoreEx or link-target identification. 

### 8. Discussion and Conclusion
The results show that combining features from both link-target identification and CoreEx have the best performance in identifying news articles within web pages. Since approximately over 12,000 decision trees were constructed for this project each individual tree cannot be analyzed in its entirety, due to time constraints. However, in order to analyze the previous results three decision trees were constructed from the entire training set and are shown by figure 11. Furthermore, after reviewing the decision trees for the first ten trials some trends do emerge regarding characteristics of online news articles. 

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/Trees.png)

**Figure 11: An example of three different pruned decision trees constructed from the entire training set. Circles represent attributes, lines represent attribute values, and diamonds represent classes. Furthermore, the two values contained in parentheses represent the number of training cases covered by the leaf and the predicted error rate.**

### 8.1 Preprocessing and Exploratory Data Analysis

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/DOMTreeDepth.png)
**Figure 12**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/ScoreofMainContent.png)
**Figure 13**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/NumberID.png)
**Figure 14**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/Date.png)
**Figure 15**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/length.png)
**Figure 16**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/MainContentNode.png)
**Figure 17**

Before CoreEx and link-target identification were run, each web page that was downloaded was preprocessed beforehand by removing HTML tags that may have been detrimental to the overall experiment. (For example, many CNN web pages contained style tags within the body portion of the HTML document.) In addition to script tags being removed, the following tags and/or objects were also removed: style tags, leftover javascript code, unknown tags not part of standard HTML syntax, noscript tags, form tags, flash and frame objects, and comment tags. By doing so, the core content of a web page could be extracted without also extracting irrelevant content such as advertisements, forms, and navigation menus.

After the downloaded web pages were preprocessed, CoreEx and link-target identification were then subsequently run. Exploratory data analysis was then conducted by constructing histograms and bar charts from the data. Figure 12's histogram shows the depth of the main content node in the DOM tree across websites. The dotted black line represents the median depth value of the main content node 
in the DOM tree. For articles this value was 11. As the histogram shows, most main content nodes had a depth value somewhere between 8 and 12 in the DOM tree. The only outlier was Yahoo News. Yahoo News had a much deeper DOM tree depth for its main content node compared to all of the other websites (Yahoo’s main content node generally was somewhere between 19 and 21 in the DOM tree). Figure 13 shows the scores CoreEx assigned to the main content node across websites. The dotted black line represents the median score of the main content node. For articles this value was 1.036. Generally, most scores were above 1 and less than 1.25; however, the Verge had a significant proportion of scores above 1.25 (ie., articles on the Verge contained more text than links). Figure 14 shows how many websites contained a number/id in their root-link. As the chart shows, 70% of websites did contain a number/id in their root-link. Only CNN, CBS News, and FOX News did not. Figure 15 shows how many websites contained a date in their root-link. As the chart shows, 60% of websites did not contain a date in their root-link. Only CNN, CBS News, FOX News, and the Verge contained a date in their root-link.

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/DOMTreeDepth-narticle.png)
**Figure 18**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/ScoreofMainContent-narticle.png)
**Figure 19**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/NumberID-narticle.png)
**Figure 20**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/MainContentNode-narticle.png)
**Figure 21**

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/length-narticle.png)
**Figure 22**

Figure 18's histogram shows the depth of the main content node in the DOM tree across websites. The dotted black line represents the median depth value of the main content node in the DOM tree. For non-articles this value was 10. As the histogram shows,  the main content nodes for non-articles were more dispersed than the main content nodes for articles. Figure 19's histogram shows the scores CoreEx assigned to the main content node across websites. The dotted black line represents the median score of the main content node. For non-articles this value was 0.9945. Most scores were somewhere between 0.9925 and 1.0012, and generally less than 1.05. Figure 20 shows how many websites contained a number/id in their root-link. As the chart shows, 58% of websites did not contain a number/id in their root-link. However,  DW News, BBC News, and the Food Network had a significant proportion of their root-links contain a number/id. Figure 21 shows the main content node’s HTML tag across websites. Unlike articles, where the main content node’s HTML tag was either a &lt;div&gt; or &lt;section&gt; tag, for non-articles, the main content node’s HTML tag was more diverse. For  BBC News and CBS News the main content node’s HTML tag was generally a &lt;ul&gt; tag. For Yahoo News, the Verge, and NBC News the main content node’s HTML tag was generally either a &lt;ul&gt; tag or a &lt;div&gt; tag. For the Food Network the main content node’s HTML tag was generally a  &lt;body&gt; tag. And for all the other websites, the main content node’s HTML tag was generally a &lt;div&gt; tag.
  
### 8.2 Structural Examination of the Decision Trees built from CoreEx

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image28.png)

**Figure 23: Part of a web page in which CoreEx did not extract the first content chunk (ie., an  article), but did extract the second content chunk (ie., a list of products).** 

When it came to only examining attributes of CoreEx that were most important to the classification task, three attributes that consistently provided a high gain-ratio value were: the HTML tag with the highest frequency count in the set S, the depth of the main content node in the DOM tree, and the count of the most frequent tag in the set S. The attribute that dealt with the score of the main content node was determined to be less relevant to the classification task. Which is surprising since from the histograms of figures 13 and 19 it would seem that the decision threshold of 1 would be a good value to partition the training cases on (ie., a score less than 1 would be a non-article, while a score equal to or greater than 1 would be an article).

As shown by figure 23, the decision tree built from CoreEx would first split the training cases based on whether the HTML tag with the highest frequency count was a
&lt;p&gt; tag or one of the following tags: a &lt;li&gt;,&lt;div&gt;,&lt;ol&gt;,&lt;article&gt;,&lt;section&gt;,&lt;meta&gt;,&lt;a&gt;,or &lt;td&gt; tag. Generally, a large proportion of training cases (eg., 138 in figure 10) were classified as articles if their tag with the highest frequency count was a &lt;p&gt; tag and their main content node’s depth value was either 11,6,8,12,9,20,21,19, or 13 in the DOM tree. Conversely, a large proportion of training cases (eg., 94 in figure 10) were classified as non-articles if their tag with the highest frequency count was either a &lt;li&gt;,&lt;ol&gt;,&lt;article&gt;,&lt;section&gt;,&lt;meta&gt;,&lt;a&gt;,or&lt;td&gt; tag.

After examining the decision trees that were constructed for trial one it seems that the average depth value of the main content node was 13.14 (𝜎 = 5.23) for articles. This value was calculated by averaging together the values contained by the set that classified the largest proportion of training cases as articles when the most frequent HTML tag was a &lt;p&gt; tag (eg., in figure 10, this set would be {19, 9, 6, 11, 12,8, 13, 20, 21} since 138 training cases were classified as articles).

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image23.png)

**Figure 24:  Simple scoring term and formula used for scoring a node’s depth in the DOM tree. The variable weightDepth determines how important the scoring term is in regards to the other two terms found in CoreEx’s scoring function.Furthermore,the variable DOMtreeDepth represents a node’s depth value in the DOM tree.**

While CoreEx is a great content extraction algorithm, it primarily extracts content based on text and does have shortcomings, especially when it comes to a lack of structural separation[8]. Namely, when the HTML code offers no indication that two content chunks are unrelated, CoreEx cannot distinguish between those portions of a page[8]. One way to potentially overcome this problem is to extract both content chunks first and then afterwards, distinguish which portions of the content to keep. By  scoring  nodes’ depth values in the DOM tree, both content chunks could be theoretically extracted by retrieving a main content node whose depth lies within a predetermined confidence interval(s) where main content generally resides.

CoreEx had a hard time extracting both content chunks of the web page shown by figure 23. The web page consists of two content chunks. Namely, the first content chunk, as shown by figure 23’s black border, is an article discussing how the Duchess Meghan Merkle is bringing the boatneck neckline back in fashion. The second content chunk, as shown by figure 23’s red border, deals with the actual dresses that one could buy to look like the Duchess Megan Merkle (ie., a non-article chunk). CoreEx extracted this second content chunk. The most frequent HTML tag in the set S was a &lt;p&gt; tag with a frequency count of 2.

After trying various different thresholds and weight values, I was unable to extract the first content chunk (ie., the article). To test the previous theory out (ie.,  regarding the scoring of nodes’ depth values in the DOM tree) , the following 95% confidence interval of 13.14![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/image17.png)1.22 was constructed from the previously calculated average depth value of 13.14 and standard deviation of 5.23.

Figure 24 shows the scoring term and formula used to score a node’s depth value in the DOM tree. As figure 24 shows, if a node’s depth in the DOM tree was within the constructed confidence interval x would equal 1, otherwise x would equal the absolute value of its depth in the DOM tree minus 13.14. After, using a weightRatio value of 0.75, a weightText value of 0.01, and a weightDepth value of 0.24 (the default threshold value of 0.9 was also used), both content chunks were extracted. Interestingly, the most frequent HTML tag in the set S was now a &lt;div&gt; tag instead of a &lt;p&gt; tag with a frequency count of 5. In addition to both content chunks being extracted, other content not associated with the main content was also extracted (ie.,  closed caption settings associated with a video player were extracted too). Nevertheless, adding an additional scoring term based on a node’s depth in the DOM tree could potentially allow CoreEx to specialize its content extraction for specific sites that lack structural separation of unrelated content.

### 8.3 Structural Examination of the Decision Trees built from Link-Target Identification

When it came to only examining the attributes of link-target identification that were most important to the classification task, three attributes that consistently provided a high gain-ratio value were: did the root-link contain a date, the number of forward slashes in the root-link, and did the root-link contain a reserved word. With an initial window of 61 (or sometimes 62) training cases, it generally took an average of 5.1 cycles for windowing to converge to a final decision tree. Furthermore, the final window would contain an average of 105.3 training cases to build the final decision tree. Meaning that, in general, 31% of the selected training cases in the window contained as much information as training on the entire training set of 344!

Consistently, decision trees built from link-target identification would first split the training cases based on whether the root-link contained a date. Then the second attribute generally picked would be whether the root-link contained a reserved word. If the root-link did not contain a reserved word, the number of forward slashes contained in the root-link would then be picked as the third attribute to split the training cases on. If a training case contained a date in its root-link, the decision tree would classify the training case as an article (eg., 53 in figure 10).Generally, a large proportion of training cases were classified as not containing an article if they did not have a date and a reserved word in their root-link, and contained either 0,3,4,or 5 slash marks in their root-link (eg., 87 in figure 10). Conversely, a good amount of training cases were classified as an article if their root-link contained either a date or their root-link did not contain a date and reserved word but had 1(or 2) forward slash mark(s) and a length greater than 49 (or 62).

### 8.4 Structural Examination of the Decision Trees built from CoreEx and Link-Target Identification

When it came to examining the combined attributes of link-target identification and CoreEx that were most important to the classification task, three attributes that consistently provided a high gain-ratio value were: the length of the root-link, whether the root-link contained a number/id, and the HTML tag of the main content node.With an initial window of 61 (or sometimes 62) training cases, it generally took an average of 7.1 cycles for windowing to converge to a final decision tree. Furthermore, the final window would contain an average of 112.1 training cases to build the final decision tree. Meaning that, in general, 33% of the selected training cases in the window contained as much information as training on the entire training set of 344!

Consistently, decision trees built from link-target identification and CoreEx would first split the training cases based on the length of the root-link. Then the two closest attributes to the root node would be: 1) whether the root-link contained a number/id; and 2) the HTML tag of the main content node. Lastly, the score of  the main content node would be used as the last attribute to classify the training cases. Interestingly, these four attributes out of the original 11 attributes were deemed by C4.5 to fully cover the 309 (or sometimes 310) training cases.  From a feature engineering standpoint, the 11 attributes originally selected were chosen under the belief that each would be relevant to the classification task. However, as it turned out, only 4 to 6 of the original 11 attributes were actually used in the classification task (the others being: did the root-link contain a date and the HTML tag with the highest frequency count in the set S). As figure 10 shows, a large proportion of training cases were classified as non-articles if their root-link was less than or equal to 44 and their root-link did not have a number/id. Conversely, a large proportion of training cases were classified as articles if their root-link was greater than 44, their main content tag was a <div> tag, and the score of their main content node was greater than 0.99519.

The fact that  decision trees built from link-target identification and CoreEx had different top attributes (ie., the root node of a decision tree or being close to the root node of a decision tree) than the top attributes of the individual decision trees was a bit surprising at first. However, discrete value grouping was not used for the decision trees built from link-target identification and CoreEx. To understand the potential effects that discrete value grouping had on the individual decision trees, two decision trees were constructed using the entire training set and C4.5’s default options.

The first default decision tree examined was the decision tree constructed from link-target identification. The top three attributes that provided a high gain-ratio were: the length of the root-link, whether the root-link contained a number/id, and whether the root-link contained a reserved word. Thus, two of the top three attributes were different from the top three attributes found in decision trees that used C4.5’s options of windowing and discrete value grouping.

The second default decision tree that was examined was the decision tree constructed from CoreEx.The top three attributes that provided a high gain-ratio were: the score of the main content node, the HTML tag of the main content node, and the count of the most frequent HTML tag in the set S. Once again, two of the top three attributes were different from the top three attributes found in decision trees that used C4.5’s option of discrete value grouping.

Accordingly, for the decision trees that just used C4.5’s default options the top attributes were: the length of the root-link, whether the root-link contained a number/id, whether the root-link contained a reserved word, the score of the main content node, the HTML tag of the main content node, and the count of the most frequent HTML tag in the set S. Consequently, five of these attributes (ie.,the length of the root-link, whether the root-link contained a number/id, the HTML tag of the main content node, and the count of the most frequent HTML tag in the set S) were part of the six attributes that were previously discussed as the only attributes  used to construct decision trees from link-target identification and CoreEx. Thus, discrete value grouping played a significant role in modifying the top attributes for the individual decision trees constructed from CoreEx or link-target identification.

### 8.5 Did Tuning the Decision Trees Beforehand Make a Difference?

In order to see if tuning the decision trees beforehand actually resulted in better performance, a comparison was done between decision trees built using C4.5’s default options and decision trees that were tuned beforehand. 

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/compare_Accurary_DefaultvsTuned.png)

**Figure 25: Boxplots comparing the accuracy of decision trees using C4.5’s default options vs. decision trees that were tuned beforehand. The green squares represent the average accuracy scores for the 20 trials.**

The boxplots of figure 25 contain the results of  this comparison. As figure 25 shows, only the decision trees built from CoreEx benefited immensely by adding the extra option of discrete value grouping, since by doing so the decision trees were able to increase their median accuracy score by approximately 9%.

To understand why the option of discrete value grouping had such a positive impact on decision trees constructed from CoreEx, the output generated from the default and tuned decision trees was examined. The first thing that stood out after examining both types of decision trees was the discrepancy in size of the unpruned decision trees. The unpruned decision trees using C4.5’s default options had an average size of 87, while the unpruned decision trees using C4.5’s option of discrete value grouping had an average size of 32. Which amounts to a 61% decrease in decision tree size! Since pruned decision trees are constructed from their unpruned counter parts, the pruned decision trees using C4.5’s default options would be larger in size than the pruned decision trees using C4.5’s option of discrete value grouping. This is important,since, in general, smaller decision trees provide a lower generalization error.

The second thing that stood out after examining both types of decision trees was the absence of the attribute that dealt with the depth of the main content node in the DOM tree. It seems that C4.5’s gain-ratio criterion negatively biased this attribute, since this attribute was found almost always near the top of the decision trees that used C4.5’s option of discrete value grouping. By not including this attribute, the pruned decision trees that used C4.5’s default options left out an important attribute to the classification task and ultimately suffered a performance penalty for doing so.

The reason why tuning the decision trees beforehand did not significantly matter for the decision trees constructed from link-target identification and CoreEx or for the decision trees constructed from link-target identification can be explained by examining the boxplot notches of figure 9. Boxplot notches are used not only for their primary purpose of determining whether random samples come from the same population, but they are also used as an informal measure to determine whether medians differ[7]. Generally, if the notches overlap it can be concluded with 95% confidence that the medians do not differ. Consequently, it should not be a surprise that tuning the decision trees beforehand did not significantly matter for the decision trees constructed from link-target identification and CoreEx or for the decision trees constructed from link-target identification. As mentioned previously in section 7, all of the boxplots’ notches overlapped for the decision trees constructed from link-target identification and the decision trees constructed from link-target identification and CoreEx. However, this was not the case for CoreEx’s boxplots, since not all of the notches overlapped with the default option. Accordingly, this would informally indicate that the median error rate for the tuned options differed from C4.5’s  default options (for CoreEx the tuned options were either: discrete value grouping or the combination of discrete value grouping and windowing).

### 8.6 Conclusion

In conclusion, both null hypotheses for hypothesis one and two can be rejected. Furthermore, after examining the results of table 3, table 4, and figure 10,  the alternative hypothesis for hypothesis two can be accepted. However, the alternative hypothesis for hypothesis one cannot be accepted since the results of table 3, table 4 ,and figure 10 show that the decision trees constructed from CoreEx perform better at identifying news articles than the decision trees constructed from link-target identification. Decision trees constructed from link-target identification and CoreEx that used C4.5’s windoing option had the highest mean accuracy score of 96.92%
(SEM=.15%, SD=.66% )at identifying news articles within web pages. Furthermore,  decision trees constructed from CoreEx that used  C4.5’s option of discrete value grouping had the second highest mean accuracy score of  95.83% (SEM=.10%, SD=.43%)at identifying news articles within web pages.

For decision trees constructed from link-target identification the three attributes that consistently provided a high gain-ratio value were: did the root-link contain a date, did the root-link contain a reserved word, and number of forward slashes in the root-link. For decision trees constructed from CoreEx the three attributes that consistently provided  a high gain-ratio value were: the HTML tag with the highest frequency count in the set S, the depth of the main content node in the DOM tree, and the count of the most frequent HTML tag in the set S. And for decision trees constructed from link-target identification and CoreEx the three attributes that consistently provided a high gain ratio value were: the length of the root-link, whether the root-link contained a number/id, and the HTML tag of the main content node.

CoreEx had a hard time distinguishing between different forms of unrelated content on web pages that lacked some form of structural separation. As outlined by sub-section 8.2, one potential solution to this problem is to use a node’s depth value in the DOM tree to see if it is within a given confidence interval. This would allow both content chunks to be extracted for further deconstruction.

Generally, tuning C4.5 beforehand did not significantly matter for decision trees constructed from link-target identification and CoreEx or for decision trees constructed from link-target identification. However, tuning  C4.5 beforehand did matter for CoreEx, since CoreEx increased is median accuracy score by approximately 9% by using C4.5’s option of discrete value grouping.

### 9. Acknowledgments

I would like to thank Dr. Girard for providing the idea and mentoring me throughout all phases of this research project. I would also like to thank Nicholas Rummel for giving me access to his data set and allowing me to expand upon his previous work. 

### 10. Works Cited

[1] Ferreira, Rodolfo, et al., “Appling Link Target Identification and Content Extraction to Improve Web News Summarization.” Proceedings of the 2016 ACM Symposium on Document Engineering - DocEng 16, Sept. 2016, pp. 197–200., doi:10.1145/2960811.2967158.

[2] Mitchell, Tom M. Machine Learning. McGraw Hill, 2017.

[3] Prasad, Jyotika, and Andreas Paepcke. “Coreex.” Proceeding of the 17th ACM Conference on 
Document Engineering - DocEng 16, Sept. 2016, pp. 197–200., doi:10.1145/2960811.2967158.	
			
[4] Quinlan, John R. C4.5: Programs for Machine Learning. Morgan Kaufmann, 2006.

[5] Remco R Bouckaert. “Choosing between two learning algorithms based on calibrated tests.” ICML'03 Proceedings of the Twentieth International Conference on International Conference on Machine Learning, Aug. 2003, pp. 51–58.

[6] Pasternack, Jeff, and Dan Roth. “Extracting Article Text from the Web with Maximum Subsequence Segmentation.” Proceedings of the 18th International Conference on World Wide Web - WWW 09, 2009, doi:10.1145/1526709.1526840. 

[7] Chambers, John M., William S. Cleveland, Beat Kleiner, and Paul A. Tukey. "Comparing Data Distributions." In Graphical Methods for Data Analysis. Wadsworth International Group, Belmont, CA, 1983. 

[8]Prasad, Jyotika, and Andreas Paepcke. CoreEx: Content extraction from online news articles. Technical Report 2008-15, Stanford University, May. 2008.




































