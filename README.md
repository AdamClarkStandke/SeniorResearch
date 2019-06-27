**Adam Standke, 
(Advisor/Mentor)Dr. C. Dudley Girard** 

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

Once the counts are known for each non-terminal node, the nodes are scored based on their values for setTextCnt and setLinkCnt[3]. Figure 3 details CoreEx’s scoring function for non-terminal nodes (ie., N). Pagetext is the total amount of text that the webpage contains[3]. Weightratio and weighttext are weights assigned to the function which have been experimentally determined to be 0.99 for weightratio and 0.01 for weighttext[3]. Using this scoring function the node with the highest score is picked as the main content node and the corresponding set of nodes within the set S contains the news article[3].

### 2.3 The C4.5 Machine Learning Algorithm
C4.5 is a machine learning algorithm that constructs classification models[4]. The classification task consists of assigning things to categories or classes as determined by their properties[4]. In doing so, C4.5 constructs classification models through induction by generalizing from specific examples[4]. C4.5 is a descendant of an earlier machine learning algorithm called ID3, however, C4.5 provides additional capabilities[4]. In its simplest form, C4.5 generates a classification model in the form of a decision tree, where each leaf indicates a particular class and each non-leaf represents a decision node that specifies some test to be carried out on an attribute[4]. To construct a decision tree C4.5 relies on partitioning a set of training cases T into subsets of cases until a single-class collection of cases is achieved[4]. 

////

Similar to the ID3 algorithm, C4.5 relies on a criteria called gain to effectively partition the training cases[4]. The information theory that underpins the gain criterion relies on the notion that the information conveyed by a message depends on its probability and can be measured in bits[4]. Figures 4, 5, and 6 detail C4.5’s calculation of gain.

Figure 4 shows the function that is used to compute the (im)purity of an arbitrary collection of training examples[2]. This function when applied to a set of training cases measures the average amount of information needed to identify the class of a training case in T[4]. The quantity freq(Cj,T) calculates the number of training cases in T that belong to class Cj[4]. The other function, shown by figure 5 represents the information gained after partitioning a set of training cases by a certain attribute[4]. Each Ti is a subset of T for which attribute X has a value i[2]. Together, both these functions make up the gain criterion as shown by figure 6[4]. The gain criterion will select attributes that maximize the function[4]. In doing so, the  set of training cases are partitioned into a corresponding decision tree[4].

///

C4.5 improves upon the gain criterion by introducing an additional gain criteria called gain ratio[4]. The gain criterion is inherently biased, since it favors attributes with many different outcomes[4]. The bias inherent in the gain criterion is rectified by C4.5 through a normalization procedure in which the apparent gain attributable to an attribute with many outcomes is adjusted[4]. Figures 7 and 8 show C4.5’s calculation of the gain ratio criterion[4]. Figure 7 calculates the split information for an attribute X. T1 through Tn represents n different subsets of T, after being partitioned by n different values of attribute X[2]. After doing so, figure 8 expresses the proportion of information generated by the attribute that is useful for classification[4]. 

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

![](https://github.com/kingjames24/super-duper-octo-goggles/blob/master/images/results.png)

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

Figure 18's histogram shows the depth of the main content node in the DOM tree across websites. The dotted black line represents the median depth value of the main content node in the DOM tree. For non-articles this value was 10. As the histogram shows,  the main content nodes for non-articles were more dispersed than the main content nodes for articles. Figure 19's histogram shows the scores CoreEx assigned to the main content node across websites. The dotted black line represents the median score of the main content node. For non-articles this value was 0.9945. Most scores were somewhere between 0.9925 and 1.0012, and generally less than 1.05. Figure 20 shows how many websites contained a number/id in their root-link. As the chart shows, 58% of websites did not contain a number/id in their root-link. However,  DW News, BBC News, and the Food Network had a significant proportion of their root-links contain a number/id. Figure 21 shows the main content node’s HTML tag across websites. Unlike articles, where the main content node’s HTML tag was either a <div> or <section> tag, for non-articles, the main content node’s HTML tag was more diverse. For  BBC News and CBS News the main content node’s HTML tag was generally a <ul> tag. For Yahoo News, the Verge, and NBC News the main content node’s HTML tag was generally either a <ul> tag or a <div> tag. For the Food Network the main content node’s HTML tag was generally a  <body> tag. And for all the other websites, the main content node’s HTML tag was generally a <div> tag.
  
### 8.2 Structural Examination of the Decision Trees built from CoreEx




















