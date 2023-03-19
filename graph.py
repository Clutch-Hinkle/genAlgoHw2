import pandas as pd
import matplotlib.pyplot as plt
import re
import numpy as np


'''
Graph
Average best fitness across generations
Average standard deviation over 50 runs
Average Average Fitness

Calculate

'''
#pointsRep2BeefyMut_summary <- gray code rep for 600 xyxy...
#n-points_summary.txt <- binary rep for 600 xx...yy..

def main():
    print("hello world")
    generationsRep1 = getFileContents("TSPSwapPMX_summary.txt")
    generationsRep2 = getFileContents("TSPSwapOXX_summary.txt")
    #generationsRep3 = getFileContents("n-pointsPolar800n=5_summary.txt")
    bestFitValues1 = getListOfValues(generationsRep1, 2)
    bestFitValues2 = getListOfValues(generationsRep2, 2)
    #bestFitValues3 = getListOfValues(generationsRep3, 2)
    graphMultiple(bestFitValues1, bestFitValues2, "Best Avg Fit Across 50 runs")


#0 is generation #
#1 is AvgFit over runs
#2 Best Fit over runs
#3 STD over runs
def getListOfValues(generations, index):
    retList = []
    for i in range(len(generations)):
        #Skip the first 4 lines
        if i <= 3:
            continue
        list = generations[i]
        retList.append(float(getList(list)[index]))

    return retList


def graphMultiple(listOne, listTwo, title):
    y = np.array(listOne)
    y2 = np.array(listTwo)
    #y3 = np.array(listThree)
    x = np.array(range(len(listOne)))

    fig, ax = plt.subplots()
    plt.title(title)
    ax.plot(x, y, label="PMX with Swap")
    ax.plot(x, y2, label = "OXX with Swap")
    #ax.plot(x, y3, label = "Polar RadThetaRadTheta...")

    ax.set_xlabel("Generation")
    ax.set_ylabel("Avg Best Fit")
    ax.legend()
    plt.show()

def graph(listOfValues, list2, x, title):
    #y = np.array(listOfValues)
    #x = np.array(x)
    #print(x)
    fig, ax = plt.subplots()
    plt.title(title)
    width = 0.3
    ax.bar(np.arange(len(listOfValues)),listOfValues, width=width, label = "With Replacement")
    ax.bar(np.arange(len(listOfValues)) + width,list2, width=width, label = "Without Replacement")
    #y_ticks = [0, 2, 4, 10, 20, 22, 30, 40, 48, 50]
    x_ticks = [0, 0.3, 1, 1.3]
    x_labels = [256, 256, 1024, 1024]
    #x_labels = x
    #plt.yticks(ticks = y_ticks)
    plt.xticks(ticks = x_ticks, labels = x_labels)
    #plt.xticks(labels = x_labels)
    #plt.yticks(range(0, 2, 5))
    plt.xlabel("Population Size")
    plt.ylabel("Number of Successes Averaged Over Reproduction Rate")
    ax.legend()
    #ax.set_xlabel("Population Size")
    #ax.set_ylabel("Number of Successes")

    plt.show()


def getFileContents(filename):
    with open(filename, "r") as file:
        contents = [s for s in file.read().split('\n') if s]
        return contents

def getList(string):
    result = re.split(' +', string)
    return result


if __name__ == "__main__":
	main()
