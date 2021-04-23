import os
import sys
import csv
import os.path
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# Get the total number of args passed
total = len(sys.argv)
if total != 1:
    quit()
indexCsv1 = 0
indexCsv2 = 0
indexCsv3 = 0
csvfile1 = "ap=0.01/ap=0.01/output_"+ str(indexCsv1)+".csv"
csvfile2 = "ap=0.05/output copia/output_"+ str(indexCsv2)+".csv"
csvfile3 = "ap=0.005/output copia/output_"+ str(indexCsv2)+".csv"
fp = 1.1

f1x = []
f1y = []
x= 0.0
y= 1.0
while os.path.isfile(csvfile1):
    f = open(csvfile1) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[9]), 3) == fp:
        x =  float(line[8])
        y = float(line[9])
    else:
        f1y.append(y)
        f1x.append(x)
        fp = round(float(line[9]), 3)
        f1y.append(float(line[9]))
        f1x.append(float(line[8]))
    indexCsv1 +=1
    csvfile1 = "ap=0.01/ap=0.01/output_"+ str(indexCsv1)+".csv"


fp = 1.1
x= 0.0
y= 1.0
f2x = []
f2y = []
time = 0
while os.path.isfile(csvfile2):
    f = open(csvfile2) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[9]), 3) == fp:
        x =  float(line[8])
        y = float(line[9])
        if float(line[8]) <time:
            print(str(time))
            print("forom up to down")
            print(line[8])
            print(indexCsv2)
        time = float(line[8])
    else:
        if float(line[8]) <time:
            print(str(time))
            print("forom up to down")
            print(line[8])
            print(indexCsv2)
        time = float(line[8])
        f2y.append(y)
        f2x.append(x)
        fp = round(float(line[9]), 3)
        f2y.append(float(line[9]))
        f2x.append(float(line[8]))
    indexCsv2 +=1
    csvfile2 = "ap=0.05/output copia/output_"+ str(indexCsv2)+".csv"

print("f3")
fp = 1.1
x= 0.0
y= 1.0
f3x = []
f3y = []

while os.path.isfile(csvfile3):
    f = open(csvfile3) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[9]), 3) == fp:
        x =  float(line[8])
        y = float(line[9])
    else:
        f3y.append(y)
        f3x.append(x)
        fp = round(float(line[9]), 3)
        f3y.append(float(line[9]))
        f3x.append(float(line[8]))
    indexCsv3 +=1
    csvfile3 = "ap=0.005/output copia/output_"+ str(indexCsv3)+".csv"


plt.ylim((0, 1.5))

plt.plot(f1x, f1y,4)
plt.plot(f2x, f2y,4)
# plt.plot(f3x, f3y,4)


blue_patch = mpatches.Patch(color='blue', label='ap=0.01')
green_patch = mpatches.Patch(color='green', label='ap=0.05')
purple_patch = mpatches.Patch(color='purple', label='ap=0.005')
plt.legend(handles=[red_patch, blue_patch, purple_patch])

plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo')
plt.savefig('fp_t.png')
plt.clf()
     
    

