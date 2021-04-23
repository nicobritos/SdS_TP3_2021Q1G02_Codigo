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

csvfile = "1ap=0.01/output_"+ str(indexCsv1)+".csv"
ap1_001y = []
ap1_001x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_001y.append(float(line[9]))
        ap1_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.01/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.01/output_"+ str(indexCsv1)+".csv"

ap2_001y = []
ap2_001x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_001y.append(float(line[9]))
        ap2_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.01/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.01/output_"+ str(indexCsv1)+".csv"

ap3_001x = []
ap3_001y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_001y.append(float(line[9]))
        ap3_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.01/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_001x), len(ap2_001x), len(ap3_001x))
ap_001x = []
ap_001y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_001x):
        mean += ap1_001y[i]
        j+=1
    if i < len(ap2_001x):
        mean += ap2_001y[i]
        j+=1
    if i < len(ap3_001x):
        mean += ap3_001y[i]
        j+=1
    mean /=j
    ap_001x.append(ap1_001x[i])
    ap_001y.append(mean)





indexCsv1 =0

csvfile = "1ap=0.02/output_"+ str(indexCsv1)+".csv"
ap1_002y = []
ap1_002x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_002y.append(float(line[9]))
        ap1_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.02/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.02/output_"+ str(indexCsv1)+".csv"

ap2_002y = []
ap2_002x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_002y.append(float(line[9]))
        ap2_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.02/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.02/output_"+ str(indexCsv1)+".csv"

ap3_002x = []
ap3_002y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_002y.append(float(line[9]))
        ap3_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.02/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_002x), len(ap2_002x), len(ap3_002x))
ap_002x = []
ap_002y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_002x):
        mean += ap1_002y[i]
        j+=1
    if i < len(ap2_002x):
        mean += ap2_002y[i]
        j+=1
    if i < len(ap3_002x):
        mean += ap3_002y[i]
        j+=1
    mean /=j
    ap_002x.append(ap3_002x[i])
    ap_002y.append(mean)






indexCsv1 =0
csvfile = "1ap=0.04/output_"+ str(indexCsv1)+".csv"
ap1_004y = []
ap1_004x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_004y.append(float(line[9]))
        ap1_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.04/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.04/output_"+ str(indexCsv1)+".csv"

ap2_004y = []
ap2_004x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_004y.append(float(line[9]))
        ap2_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.04/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.04/output_"+ str(indexCsv1)+".csv"

ap3_004x = []
ap3_004y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_004y.append(float(line[9]))
        ap3_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.04/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_004x), len(ap2_004x), len(ap3_004x))
ap_004x = []
ap_004y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_004x):
        mean += ap1_004y[i]
        j+=1
    if i < len(ap2_004x):
        mean += ap2_004y[i]
        j+=1
    if i < len(ap3_004x):
        mean += ap3_004y[i]
        j+=1
    mean /=j
    ap_004x.append(ap3_004x[i])
    ap_004y.append(mean)





plt.plot(ap_001x,ap_001y,4)
plt.plot(ap_002x,ap_002y,4)
plt.plot(ap_004x,ap_004y,4)

plt.ylim((0.5, 1.5))
blue_patch = mpatches.Patch(color='blue', label='apertura=0.01')
green_patch = mpatches.Patch(color='green', label='apertura=0.02')
purple_patch = mpatches.Patch(color='purple', label='apertura=0.04')
plt.legend(handles=[green_patch, blue_patch, purple_patch])

plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo')
plt.savefig('fp_t.png')
plt.clf()


plt.plot(ap1_001x, ap1_001y,4)
plt.plot(ap2_001x, ap2_001y,4)
plt.plot(ap3_001x, ap3_001y,4)
plt.ylim((0.5, 1.5))


plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo (apertura 0.01m)')
plt.savefig('fp_ap001_t.png')
plt.clf()



plt.plot(ap1_002x, ap1_002y,4)
plt.plot(ap2_002x, ap2_002y,4)
plt.plot(ap3_002x, ap3_002y,4)
plt.ylim((0.5, 1.5))


plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo (apertura 0.02m)')
plt.savefig('fp_ap002_t.png')
plt.clf()




plt.plot(ap1_004x, ap1_004y,4)
plt.plot(ap2_004x, ap2_004y,4)
plt.plot(ap3_004x, ap3_004y,4)
plt.ylim((0.5, 1.5))


plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo (apertura 0.04m)')
plt.savefig('fp_ap004_t.png')
plt.clf()






